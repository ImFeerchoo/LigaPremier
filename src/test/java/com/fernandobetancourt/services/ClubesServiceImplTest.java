package com.fernandobetancourt.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.fernandobetancourt.Data.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.fernandobetancourt.exceptions.AddingClubException;
import com.fernandobetancourt.exceptions.ClubNotFoundException;
import com.fernandobetancourt.exceptions.GroupNotFoundException;
import com.fernandobetancourt.model.dao.IClubesDao;
import com.fernandobetancourt.model.entity.Club;
import com.fernandobetancourt.model.entity.Group;
import com.fernandobetancourt.validators.ClubValidator;

@SpringBootTest(classes = {ClubesServiceImpl.class, ClubValidator.class})
class ClubesServiceImplTest {

	@MockBean
	private IClubesDao clubesDao;

	@MockBean
	private IGroupsService groupsService;

	@Autowired
	private IClubesService clubesService;

	// GET

	@Test
	void testGetClubesSuccessed() {
		// Given
		when(clubesDao.findAll()).thenReturn(CLUBES_WITH_ID);

		// When
		List<Club> clubes = clubesService.getClubes();

		// Then
		assertFalse(clubes.isEmpty());
		assertEquals(10, clubes.size());
		assertEquals("Cruz Azul", clubes.get(0).getName());

		verify(clubesDao).findAll();

	}

//	@Test
//	void testGetClubesEmptyList() {
//		// Given
//		when(clubesDao.findAll()).thenReturn(Collections.emptyList());
//
//		// When
//		List<Club> clubes = clubesService.getClubes();
//
//		// Then
//		assertTrue(clubes.isEmpty());
//		assertEquals(0, clubes.size());
//
//		verify(clubesDao).findAll();
//
//	}
	
	@Test
	void testGetClubesEmptyList() {
		// Given
		when(clubesDao.findAll()).thenReturn(Collections.emptyList());
		
		// Then
		assertThrows(ClubNotFoundException.class, () -> {
			// When
			clubesService.getClubes();
		});
		
		verify(clubesDao).findAll();
	}

	@Test
	void testGetClubesByGroupSuccessed() {
		// Given
		when(groupsService.getGroup(anyLong())).thenReturn(GROUPS_WITH_ID.get(0));
		when(clubesDao.findByGroup(GROUPS_WITH_ID.get(0)))
				.thenReturn(findClubsByGroup(GROUPS_WITH_ID.get(0).getGroupId()));

		// When
		List<Club> clubes = clubesService.getClubesByGroup(1000L);

		// Then
		assertFalse(clubes.isEmpty());
		assertEquals(2, clubes.size());
		assertTrue(clubes.contains(CLUBES_WITH_ID.get(0)));
		assertTrue(clubes.contains(CLUBES_WITH_ID.get(1)));
	}

	@Test
	void testGetClubesByGroupFailed() {
		// Given
		when(groupsService.getGroup(anyLong())).thenThrow(GroupNotFoundException.class);

		// Then
		assertThrows(GroupNotFoundException.class, () -> {

			// When
			clubesService.getClubesByGroup(1L);

		});

		verify(groupsService).getGroup(1L);

	}
	
	@Test
	void testGetClubesByGroupEmptyList() {
		// Given
		when(groupsService.getGroup(anyLong())).thenReturn(getGroupByIdBreakingReference(1L));
		when(clubesDao.findByGroup(any(Group.class))).thenReturn(Collections.emptyList());
		
		// Then
		assertThrows(ClubNotFoundException.class, () -> {
			// When
			clubesService.getClubesByGroup(1L);
		});
		
		verify(groupsService).getGroup(1L);
		verify(clubesDao).findByGroup(any(Group.class));
	}

	@Test
	void testGetClubByIdSuccessed() {
		// Given
		when(clubesDao.findById(anyLong())).thenReturn(Optional.of(CLUBES_WITH_ID.get(0)));

		// When
		Club club = clubesService.getClubById(1L);

		// Then
		assertEquals("Cruz Azul", club.getName());
		assertEquals("Estadio Azteca", club.getStadium());
		assertEquals("crz", club.getPhoto());

		verify(clubesDao).findById(1L);

	}

	@Test
	void testGetClubByIdFailed() {
		// Given
		when(clubesDao.findById(anyLong())).thenReturn(Optional.ofNullable(null));

		// Then
		assertThrows(ClubNotFoundException.class, () -> {
			// When
			clubesService.getClubById(1L);
		});

		verify(clubesDao).findById(1L);

	}

	// POST

	@Test
	void testAddClubSuccessed() {

		// Given
		when(clubesDao.save(any(Club.class))).then(invocation -> {
			Club club = invocation.getArgument(0);
			club.setClubId(1L);
			return club;
		});

		// When
		Club club = clubesService.addClub(getClubWithoutIdByPositionBreakingReference(0));

		// Then
		assertEquals(1L, club.getClubId());
		assertEquals("Cruz Azul", club.getName());
		assertEquals("Estadio Azteca", club.getStadium());
		assertEquals("crz", club.getPhoto());

		verify(groupsService).getGroup(anyLong());
		verify(clubesDao).save(any(Club.class));

	}

	@Test
	void testAddClubFailedClubNull() {
		// Given
		Club clubNull = null;

		// Then
		assertThrows(AddingClubException.class, () -> {
			// When
			clubesService.addClub(clubNull);
		});
	}
	
	@Test
	void testAddClubFailedNameNull() {
		// Given
		Club clubNameNull = getClubWithoutIdByPositionBreakingReference(0);
		clubNameNull.setName(null);

		// Then
		assertThrows(AddingClubException.class, () -> {
			// When
			clubesService.addClub(clubNameNull);
		});
	}
	
	@Test
	void testAddClubFailedNameEmptyString() {
		// Given
		Club clubNameEmptyString = getClubWithoutIdByPositionBreakingReference(0);
		clubNameEmptyString.setName("");

		// Then
		assertThrows(AddingClubException.class, () -> {
			// When
			clubesService.addClub(clubNameEmptyString);
		});
	}
	
	@Test
	void testAddClubFailedStadiumNull() {
		// Given
		Club clubStadiumNull = getClubWithoutIdByPositionBreakingReference(0);
		clubStadiumNull.setStadium(null);

		// Then
		assertThrows(AddingClubException.class, () -> {
			// When
			clubesService.addClub(clubStadiumNull);
		});
	}
	
	@Test
	void testAddClubFailedStadiumEmptyString() {
		// Given
		Club clubStadiumEmptyString = getClubWithoutIdByPositionBreakingReference(0);
		clubStadiumEmptyString.setStadium("");

		// Then
		assertThrows(AddingClubException.class, () -> {
			// When
			clubesService.addClub(clubStadiumEmptyString);
		});
	}
	
	@Test
	void testAddClubFailedGroupNull() {
		// Given
		Club clubGroupNull = getClubWithoutIdByPositionBreakingReference(0);
		clubGroupNull.setGroup(null);

		// Then
		assertThrows(AddingClubException.class, () -> {
			// When
			clubesService.addClub(clubGroupNull);
		});
	}
	
	@Test
	void testAddClubFailedGroupIdNull() {
		// Given
		Club clubGroupIdNull = getClubWithoutIdByPositionBreakingReference(0);
		clubGroupIdNull.setGroup(GROUPS_WITHOUT_ID.get(0));

		// Then
		assertThrows(AddingClubException.class, () -> {
			// When
			clubesService.addClub(clubGroupIdNull);
		});
	}
	
	@Test
	void testAddClubFailedGroupIdLessThan1() {
		// Given
		Club clubGroupIdLessThan1 = getClubWithoutIdByPositionBreakingReference(0);
		clubGroupIdLessThan1.setGroup(GROUP_WITH_ID_LESS_THAN_1);

		// Then
		assertThrows(AddingClubException.class, () -> {
			// When
			clubesService.addClub(clubGroupIdLessThan1);
		});
	}
	
	@Test
	void testAddClubFailedGroupDoesNotExists() {
		// Given
//		when(groupsService.groupExists(anyLong())).thenThrow(GroupNotFoundException.class);
		when(groupsService.getGroup(anyLong())).thenThrow(GroupNotFoundException.class);

		// Then
		assertThrows(GroupNotFoundException.class, () -> {
			// When
			clubesService.addClub(getClubWithoutIdByPositionBreakingReference(0));
		});

//		verify(groupsService).groupExists(anyLong());
		verify(groupsService).getGroup(anyLong());
	}
	
	//PUT	

	@Test
	void testUpdateClubSuccessed() {
		// Given

		when(clubesDao.findById(anyLong())).thenReturn(Optional.of(getClubByIdBreakingReference(1L)));

		when(clubesDao.save(any(Club.class))).then(invocation -> {
			Club club = invocation.getArgument(0);
			return club;
		});
		
		Club club = getClubByIdBreakingReference(1L);
		club.setName("New Club");
		club.setStadium("New Stadium");
		club.setPhoto("New Photo");
		club.setGroup(GROUPS_WITH_ID.get(4));

		// When

		club = clubesService.updateClub(club);

		// Then
		assertEquals(1L, club.getClubId());
		assertEquals("New Club", club.getName());
		assertEquals("New Stadium", club.getStadium());
		assertEquals("New Photo", club.getPhoto());
		assertEquals(GROUPS_WITH_ID.get(4), club.getGroup());

		verify(clubesDao).save(club);
	}
	
	@Test
	void testUpdateClubFailedClubNull() {
		// Given
		Club clubNull = null;

		// Then
		assertThrows(AddingClubException.class, () -> {
			// When
			clubesService.updateClub(clubNull);
		});
	}
	
	@Test
	void testUpdateClubFailedClubIdNull() {
		// Then
		assertThrows(AddingClubException.class, () -> {
			// When
			clubesService.updateClub(CLUBES_WITHOUT_ID.get(0));
		});
	}
	
	@Test
	void testUpdateClubFailedClubIdLessThan1() {
		// Then
		assertThrows(AddingClubException.class, () -> {
			// When
			clubesService.updateClub(CLUB_WITH_ID_LESS_THAN_1);
		});
	}
	
	@Test
	void testUpdateClubFailedNameNull() {
		// Given
		Club clubNameNull = getClubByIdBreakingReference(1L);
		clubNameNull.setName(null);

		// Then
		assertThrows(AddingClubException.class, () -> {
			// When
			clubesService.updateClub(clubNameNull);
		});
	}
	
	@Test
	void testUpdateClubFailedNameEmptyString() {
		// Given
		Club clubNameEmptyString = getClubByIdBreakingReference(1L);
		clubNameEmptyString.setName("");

		// Then
		assertThrows(AddingClubException.class, () -> {
			// When
			clubesService.updateClub(clubNameEmptyString);
		});
	}
	
	@Test
	void testUpdateClubFailedStadiumNull() {
		// Given
		Club clubStadiumNull = getClubByIdBreakingReference(1L);
		clubStadiumNull.setStadium(null);

		// Then
		assertThrows(AddingClubException.class, () -> {
			// When
			clubesService.updateClub(clubStadiumNull);
		});
	}
	
	@Test
	void testUpdateClubFailedStadiumEmptyString() {
		// Given
		Club clubStadiumEmptyString = getClubByIdBreakingReference(1L);
		clubStadiumEmptyString.setStadium("");

		// Then
		assertThrows(AddingClubException.class, () -> {
			// When
			clubesService.updateClub(clubStadiumEmptyString);
		});
	}
	
	@Test
	void testUpdateClubFailedGroupNull() {
		// Given
		Club clubGroupNull = getClubByIdBreakingReference(1L);
		clubGroupNull.setGroup(null);

		// Then
		assertThrows(AddingClubException.class, () -> {
			// When
			clubesService.updateClub(clubGroupNull);
		});
	}
	
	@Test
	void testUpdateClubFailedGroupIdNull() {
		// Given
		Club clubGroupIdNull = getClubByIdBreakingReference(1L);
		clubGroupIdNull.setGroup(GROUPS_WITHOUT_ID.get(0));

		// Then
		assertThrows(AddingClubException.class, () -> {
			// When
			clubesService.updateClub(clubGroupIdNull);
		});
	}
	
	@Test
	void testUpdateClubFailedGroupIdLessThan1() {
		// Given
		Club clubGroupIdLessThan1 = getClubByIdBreakingReference(1L);
		clubGroupIdLessThan1.setGroup(GROUP_WITH_ID_LESS_THAN_1);

		// Then
		assertThrows(AddingClubException.class, () -> {
			// When
			clubesService.updateClub(clubGroupIdLessThan1);
		});
	}
	
	@Test
	void testUpdateClubFailedGroupsDoesNotExists() {
		// Given
		when(clubesDao.findById(anyLong())).thenReturn(Optional.of(getClubByIdBreakingReference(1L)));
//		when(groupsService.groupExists(anyLong())).thenThrow(GroupNotFoundException.class);
		when(groupsService.getGroup(anyLong())).thenThrow(GroupNotFoundException.class);

		// Then
		assertThrows(GroupNotFoundException.class, () -> {
			// When
			clubesService.updateClub(getClubByIdBreakingReference(1L));
		});

//		verify(groupsService).groupExists(anyLong());
		verify(groupsService).getGroup(anyLong());
	}
	
	@Test
	void testUpdateClubFailedClubDoesNotExists() {
		// Given
		when(clubesDao.findById(anyLong())).thenReturn(Optional.ofNullable(null));

		assertThrows(ClubNotFoundException.class, () -> {
			clubesService.updateClub(CLUBES_WITH_ID.get(0));
		});

		verify(clubesDao).findById(1L);
	}
	
	//DELETE
	
	@Test
	void testDeleteClubSuccessed() {
		//Given
		
		when(clubesDao.findById(anyLong())).thenReturn(Optional.of(getClubByIdBreakingReference(1L)));
		//Al borrar el club no va a hacer nada, el deleteById regresa void
		
		//When
		clubesService.deleteClub(getClubByIdBreakingReference(1L).getClubId());
		
		//Then
		
		verify(clubesDao).findById(1L);
		verify(clubesDao).deleteById(1L);
	}
	
	@Test
	void testDeleteClubFailed() {
		//Given
		
		when(clubesDao.findById(anyLong())).thenReturn(Optional.ofNullable(null));
		
		//Then
		assertThrows(ClubNotFoundException.class, () -> {
			//When
			clubesService.deleteClub(getClubByIdBreakingReference(1L).getClubId());
		});
		
		//Then
		verify(clubesDao).findById(1L);
	}

}
