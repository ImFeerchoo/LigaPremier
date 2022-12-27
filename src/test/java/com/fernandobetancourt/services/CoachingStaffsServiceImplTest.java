package com.fernandobetancourt.services;

import static com.fernandobetancourt.Data.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.fernandobetancourt.exceptions.AddingCoachingStaffException;
import com.fernandobetancourt.exceptions.ClubNotFoundException;
import com.fernandobetancourt.exceptions.CoachingStaffNotFoundException;
import com.fernandobetancourt.model.dao.ICoachingStaffsDao;
import com.fernandobetancourt.model.entity.Club;
import com.fernandobetancourt.model.entity.CoachingStaff;
import com.fernandobetancourt.validators.CoachingStaffValidator;

@SpringBootTest(classes = {CoachingStaffsServiceImpl.class, CoachingStaffValidator.class})
class CoachingStaffsServiceImplTest {

	@MockBean
	private ICoachingStaffsDao coachingStaffsDao;

	@MockBean
	private IClubesService clubesService;

	@Autowired
	private ICoachingStaffsService coachingStaffsService;

	// GET

	@Test
	void testGetCoachingStaffsSuccessed() {
		// Given
		when(coachingStaffsDao.findAll()).thenReturn(COACHING_STAFFS_WITH_ID);

		// When
		List<CoachingStaff> coachingStaffs = coachingStaffsService.getCoachingStaffs();

		// Then
		assertEquals(COACHING_STAFFS_WITH_ID, coachingStaffs);

		verify(coachingStaffsDao).findAll();
	}

//	@Test
//	void testGetCoachingStaffsEmptyList() {
//		// Given
//		when(coachingStaffsDao.findAll()).thenReturn(Collections.emptyList());
//
//		// When
//		List<CoachingStaff> coachingStaffs = coachingStaffsService.getCoachingStaffs();
//
//		// Then
//		assertTrue(coachingStaffs.isEmpty());
//		verify(coachingStaffsDao).findAll();
//	}
	
	@Test
	void testGetCoachingStaffsEmptyList() {
		// Given
		when(coachingStaffsDao.findAll()).thenReturn(Collections.emptyList());
		
		// Then
		assertThrows(CoachingStaffNotFoundException.class, () -> {
			// When
			coachingStaffsService.getCoachingStaffs();
		});
		
		verify(coachingStaffsDao).findAll();
	}

	@Test
	void testGetCoachingStaffsByClubSuccessed() {
		// Given
		when(clubesService.getClubById(anyLong())).thenReturn(getClubByIdBreakingReference(1L));
		when(coachingStaffsDao.findByClub(any(Club.class))).thenReturn(getCoachingStaffsByClub(1L));

		// When
		List<CoachingStaff> coachingStaffs = coachingStaffsService.getCoachingStaffsByClub(1L);

		// Then
		assertFalse(coachingStaffs.isEmpty());
		assertEquals(2, coachingStaffs.size());
		assertTrue(coachingStaffs.contains(getCoachingStaffByIdBreakingReference(1L)));
		assertTrue(coachingStaffs.contains(getCoachingStaffByIdBreakingReference(2L)));

		verify(clubesService).getClubById(1L);
		verify(coachingStaffsDao).findByClub(any(Club.class));
	}

	@Test
	void testGetCoachingStaffsByClubEmptyList() {
		// Given
		when(clubesService.getClubById(anyLong())).thenReturn(getClubByIdBreakingReference(1L));
		when(coachingStaffsDao.findByClub(any(Club.class))).thenReturn(Collections.emptyList());

		// Then
		assertThrows(CoachingStaffNotFoundException.class, () -> {
			// When
			coachingStaffsService.getCoachingStaffsByClub(1L);
		});

		verify(clubesService).getClubById(1L);
		verify(coachingStaffsDao).findByClub(any(Club.class));
	}
	
	//Lanzo una lista vacía pero no una excepción
//	@Test
//	void testGetCoachingStaffsByClubEmptyList() {
//		// Given
//		when(clubesService.getClubById(anyLong())).thenReturn(getClubByIdBreakingReference(1L));
//		when(coachingStaffsDao.findByClub(any(Club.class))).thenReturn(Collections.emptyList());
//		
//		// When
//		List<CoachingStaff> coachingStaffs = coachingStaffsService.getCoachingStaffsByClub(1L);
//		
//		// Then
//		assertTrue(coachingStaffs.isEmpty());
//		
//		verify(clubesService).getClubById(1L);
//		verify(coachingStaffsDao).findByClub(any(Club.class));
//	}

	@Test
	void testGetCoachingStaffsByClubClubDoesNotExists() {
		// Given
		when(clubesService.getClubById(anyLong())).thenThrow(ClubNotFoundException.class);

		// Then
		assertThrows(ClubNotFoundException.class, () -> {
			// When
			coachingStaffsService.getCoachingStaffsByClub(1L);
		});

		verify(clubesService).getClubById(1L);
	}

	@Test
	void testGetCoachingStaffSuccessed() {
		// Given
		when(coachingStaffsDao.findById(anyLong())).thenReturn(Optional.of(getCoachingStaffByIdBreakingReference(1L)));

		// When
		CoachingStaff coachingStaff = coachingStaffsService.getCoachingStaff(1L);

		// Then
		assertEquals(getCoachingStaffByIdBreakingReference(1L), coachingStaff);
		verify(coachingStaffsDao).findById(1L);
	}

	@Test
	void testGetCoachingStaffFailed() {
		// Given
		when(coachingStaffsDao.findById(anyLong())).thenReturn(Optional.ofNullable(null));

		// Then
		assertThrows(CoachingStaffNotFoundException.class, () -> {
			// When
			coachingStaffsService.getCoachingStaff(1L);
		});

		verify(coachingStaffsDao).findById(1L);
	}

	// POST

	@Test
	void testAddCoachingStaffSuccessed() {
		// Given
		when(coachingStaffsDao.save(any(CoachingStaff.class))).then(invocation -> {
			CoachingStaff coachingStaff = invocation.getArgument(0);
			coachingStaff.setCoachingStaffId(1L);
			return coachingStaff;
		});

		// When
		CoachingStaff coachingStaff = coachingStaffsService
				.addCoachingStaff(getCoachingStaffWithoutIdByPositionBreakingReference(0));

		// Then
		assertEquals(1L, coachingStaff.getCoachingStaffId());
		assertEquals("Jorge Humberto", coachingStaff.getNames());
		assertEquals("Torres Mata", coachingStaff.getLastNames());
		assertEquals("Director Técnico", coachingStaff.getPosition());
		assertEquals(59, coachingStaff.getAge());
		assertEquals(85.0, coachingStaff.getWeight());
		assertEquals(1.76, coachingStaff.getHeight());
		assertEquals("Mexicano", coachingStaff.getNationality());
		assertEquals("JTorres", coachingStaff.getPhoto());
		assertEquals(getClubByIdBreakingReference(1L), coachingStaff.getClub());

		verify(clubesService).getClubById(anyLong());
		verify(coachingStaffsDao).save(any(CoachingStaff.class));
	}

	@Test
	void testAddCoachingStaffFailedCoachingStaffNull() {
		// Then
		assertThrows(AddingCoachingStaffException.class, () -> {
			// When
			coachingStaffsService.addCoachingStaff(null);
		});
	}

	@Test
	void testAddCoachingStaffFailedNamesNull() {
		// Given
		CoachingStaff coachingStaffNamesNull = getCoachingStaffWithoutIdByPositionBreakingReference(0);
		coachingStaffNamesNull.setNames(null);

		// Then
		assertThrows(AddingCoachingStaffException.class, () -> {
			// When
			coachingStaffsService.addCoachingStaff(coachingStaffNamesNull);
		});
	}

	@Test
	void testAddCoachingStaffFailedNamesEmptyString() {
		// Given
		CoachingStaff coachingStaffNamesEmptyString = getCoachingStaffWithoutIdByPositionBreakingReference(0);
		coachingStaffNamesEmptyString.setNames("");

		// Then
		assertThrows(AddingCoachingStaffException.class, () -> {
			// When
			coachingStaffsService.addCoachingStaff(coachingStaffNamesEmptyString);
		});
	}

	@Test
	void testAddCoachingStaffFailedLastNamesNull() {
		// Given
		CoachingStaff coachingStaffLastNamesNull = getCoachingStaffWithoutIdByPositionBreakingReference(0);
		coachingStaffLastNamesNull.setLastNames(null);

		// Then
		assertThrows(AddingCoachingStaffException.class, () -> {
			// When
			coachingStaffsService.addCoachingStaff(coachingStaffLastNamesNull);
		});
	}

	@Test
	void testAddCoachingStaffFailedLastNamesEmptyString() {
		// Given
		CoachingStaff coachingStaffLastNamesEmptyString = getCoachingStaffWithoutIdByPositionBreakingReference(0);
		coachingStaffLastNamesEmptyString.setLastNames("");

		// Then
		assertThrows(AddingCoachingStaffException.class, () -> {
			// When
			coachingStaffsService.addCoachingStaff(coachingStaffLastNamesEmptyString);
		});
	}

	@Test
	void testAddCoachingStaffFailedPositionNull() {
		// Given
		CoachingStaff coachingStaffPositionNull = getCoachingStaffWithoutIdByPositionBreakingReference(0);
		coachingStaffPositionNull.setPosition(null);

		// Then
		assertThrows(AddingCoachingStaffException.class, () -> {
			// When
			coachingStaffsService.addCoachingStaff(coachingStaffPositionNull);
		});
	}

	@Test
	void testAddCoachingStaffFailedPositionEmptyString() {
		// Given
		CoachingStaff coachingStaffPositionEmptyString = getCoachingStaffWithoutIdByPositionBreakingReference(0);
		coachingStaffPositionEmptyString.setPosition("");

		// Then
		assertThrows(AddingCoachingStaffException.class, () -> {
			// When
			coachingStaffsService.addCoachingStaff(coachingStaffPositionEmptyString);
		});
	}

	@Test
	void testAddCoachingStaffFailedAgeNull() {
		// Given
		CoachingStaff coachingStaffAgeNull = getCoachingStaffWithoutIdByPositionBreakingReference(0);
		coachingStaffAgeNull.setAge(null);

		// Then
		assertThrows(AddingCoachingStaffException.class, () -> {
			// When
			coachingStaffsService.addCoachingStaff(coachingStaffAgeNull);
		});
	}

	@Test
	void testAddCoachingStaffFailedAgeLessThan1() {
		// Given
		CoachingStaff coachingStaffAgeLessThan1 = getCoachingStaffWithoutIdByPositionBreakingReference(0);
		coachingStaffAgeLessThan1.setAge(0);

		// Then
		assertThrows(AddingCoachingStaffException.class, () -> {
			// When
			coachingStaffsService.addCoachingStaff(coachingStaffAgeLessThan1);
		});
	}

	@Test
	void testAddCoachingStaffFailedWeightNull() {
		// Given
		CoachingStaff coachingStaffWeightNull = getCoachingStaffWithoutIdByPositionBreakingReference(0);
		coachingStaffWeightNull.setWeight(null);

		// Then
		assertThrows(AddingCoachingStaffException.class, () -> {
			// When
			coachingStaffsService.addCoachingStaff(coachingStaffWeightNull);
		});
	}

	@Test
	void testAddCoachingStaffFailedWeightLessThan1() {
		// Given
		CoachingStaff coachingStaffWeightLessThan1 = getCoachingStaffWithoutIdByPositionBreakingReference(0);
		coachingStaffWeightLessThan1.setWeight(0.0);

		// Then
		assertThrows(AddingCoachingStaffException.class, () -> {
			// When
			coachingStaffsService.addCoachingStaff(coachingStaffWeightLessThan1);
		});
	}

	@Test
	void testAddCoachingStaffFailedHeightNull() {
		// Given
		CoachingStaff coachingStaffHeightNull = getCoachingStaffWithoutIdByPositionBreakingReference(0);
		coachingStaffHeightNull.setHeight(null);

		// Then
		assertThrows(AddingCoachingStaffException.class, () -> {
			// When
			coachingStaffsService.addCoachingStaff(coachingStaffHeightNull);
		});
	}

	@Test
	void testAddCoachingStaffFailedHeightLessThan1() {
		// Given
		CoachingStaff coachingStaffHeightLessThan1 = getCoachingStaffWithoutIdByPositionBreakingReference(0);
		coachingStaffHeightLessThan1.setHeight(0.0);

		// Then
		assertThrows(AddingCoachingStaffException.class, () -> {
			// When
			coachingStaffsService.addCoachingStaff(coachingStaffHeightLessThan1);
		});
	}

	@Test
	void testAddCoachingStaffFailedClubNull() {
		// Given
		CoachingStaff coachingStaffClubNull = getCoachingStaffWithoutIdByPositionBreakingReference(0);
		coachingStaffClubNull.setClub(null);

		// Then
		assertThrows(AddingCoachingStaffException.class, () -> {
			// When
			coachingStaffsService.addCoachingStaff(coachingStaffClubNull);
		});
	}

	@Test
	void testAddCoachingStaffFailedClubIdNull() {
		// Given
		CoachingStaff coachingStaffClubIdNull = getCoachingStaffWithoutIdByPositionBreakingReference(0);
		coachingStaffClubIdNull.setClub(getClubWithoutIdByPositionBreakingReference(0));

		// Then
		assertThrows(AddingCoachingStaffException.class, () -> {
			// When
			coachingStaffsService.addCoachingStaff(coachingStaffClubIdNull);
		});
	}

	@Test
	void testAddCoachingStaffFailedClubIdLessThan1() {
		// Given
		CoachingStaff coachingStaffClubIdLessThan1 = getCoachingStaffWithoutIdByPositionBreakingReference(0);
		coachingStaffClubIdLessThan1.setClub(CLUB_WITH_ID_LESS_THAN_1);

		// Then
		assertThrows(AddingCoachingStaffException.class, () -> {
			// When
			coachingStaffsService.addCoachingStaff(coachingStaffClubIdLessThan1);
		});
	}

	@Test
	void testAddCoachingStaffFailedClubDoesNotExists() {
		// Given
		when(clubesService.getClubById(anyLong())).thenThrow(ClubNotFoundException.class);

		// Then
		assertThrows(ClubNotFoundException.class, () -> {
			// When
			coachingStaffsService.addCoachingStaff(getCoachingStaffWithoutIdByPositionBreakingReference(0));
		});

		verify(clubesService).getClubById(anyLong());
	}

	// PUT

	@Test
	void testUpdateCoachingStaffSuccessed() {
		// Given
		when(coachingStaffsDao.findById(anyLong())).thenReturn(Optional.of(getCoachingStaffByIdBreakingReference(1L)));

		when(coachingStaffsDao.save(any(CoachingStaff.class))).then(invocation -> {
			return invocation.getArgument(0);
		});

		CoachingStaff coachingStaff = getCoachingStaffByIdBreakingReference(1L);
		coachingStaff.setNames("Juan Raúl");
		coachingStaff.setLastNames("Gómez Chávez");
		coachingStaff.setPosition("Recogedor");
		coachingStaff.setAge(20);
		coachingStaff.setWeight(64.0);
		coachingStaff.setHeight(1.77);
		coachingStaff.setNationality("Canadiense");
		coachingStaff.setPhoto("JGómez");
		coachingStaff.setClub(getClubByIdBreakingReference(5L));

		// When
		coachingStaff = coachingStaffsService.updateCoachingStaff(coachingStaff);

		// Then
		assertEquals(1L, coachingStaff.getCoachingStaffId());
		assertEquals("Juan Raúl", coachingStaff.getNames());
		assertEquals("Gómez Chávez", coachingStaff.getLastNames());
		assertEquals("Recogedor", coachingStaff.getPosition());
		assertEquals(20, coachingStaff.getAge());
		assertEquals(64.0, coachingStaff.getWeight());
		assertEquals(1.77, coachingStaff.getHeight());
		assertEquals("Canadiense", coachingStaff.getNationality());
		assertEquals("JGómez", coachingStaff.getPhoto());
		assertEquals(getClubByIdBreakingReference(5L), coachingStaff.getClub());

		verify(coachingStaffsDao).findById(1L);
		verify(clubesService).getClubById(5L);
		verify(coachingStaffsDao).save(any(CoachingStaff.class));
	}

	@Test
	void testUpdateCoachingStaffFailedCoachingStaffNull() {
		// Then
		assertThrows(AddingCoachingStaffException.class, () -> {
			// When
			coachingStaffsService.updateCoachingStaff(null);
		});
	}

	@Test
	void testUpdateCoachingStaffFailedCoachingStaffIdNull() {
		// Then
		assertThrows(AddingCoachingStaffException.class, () -> {
			// When
			coachingStaffsService.updateCoachingStaff(getCoachingStaffWithoutIdByPositionBreakingReference(0));
		});
	}

	@Test
	void testUpdateCoachingStaffFailedCoachingStaffIdLessThan1() {
		// Then
		assertThrows(AddingCoachingStaffException.class, () -> {
			// When
			coachingStaffsService.updateCoachingStaff(COACHING_STAFF_WITH_ID_LESS_THAN_1);
		});
	}

	@Test
	void testUpdateCoachingStaffFailedNamesNull() {
		// Given
		CoachingStaff coachingStaffNamesNull = getCoachingStaffByIdBreakingReference(1L);
		coachingStaffNamesNull.setNames(null);

		// Then
		assertThrows(AddingCoachingStaffException.class, () -> {
			// When
			coachingStaffsService.updateCoachingStaff(coachingStaffNamesNull);
		});
	}

	@Test
	void testUpdateCoachingStaffFailedNamesEmptyString() {
		// Given
		CoachingStaff coachingStaffNamesEmptyString = getCoachingStaffByIdBreakingReference(1L);
		coachingStaffNamesEmptyString.setNames("");

		// Then
		assertThrows(AddingCoachingStaffException.class, () -> {
			// When
			coachingStaffsService.updateCoachingStaff(coachingStaffNamesEmptyString);
		});
	}

	@Test
	void testUpdateCoachingStaffFailedLastNamesNull() {
		// Given
		CoachingStaff coachingStaffLastNamesNull = getCoachingStaffByIdBreakingReference(1L);
		coachingStaffLastNamesNull.setLastNames(null);

		// Then
		assertThrows(AddingCoachingStaffException.class, () -> {
			// When
			coachingStaffsService.updateCoachingStaff(coachingStaffLastNamesNull);
		});
	}

	@Test
	void testUpdateCoachingStaffFailedLastNamesEmptyString() {
		// Given
		CoachingStaff coachingStaffLastNamesEmptyString = getCoachingStaffByIdBreakingReference(1L);
		coachingStaffLastNamesEmptyString.setLastNames("");

		// Then
		assertThrows(AddingCoachingStaffException.class, () -> {
			// When
			coachingStaffsService.updateCoachingStaff(coachingStaffLastNamesEmptyString);
		});
	}

	@Test
	void testUpdateCoachingStaffFailedPositionNull() {
		// Given
		CoachingStaff coachingStaffPositionNull = getCoachingStaffByIdBreakingReference(1L);
		coachingStaffPositionNull.setPosition(null);

		// Then
		assertThrows(AddingCoachingStaffException.class, () -> {
			// When
			coachingStaffsService.updateCoachingStaff(coachingStaffPositionNull);
		});
	}

	@Test
	void testUpdateCoachingStaffFailedPositionEmptyString() {
		// Given
		CoachingStaff coachingStaffPositionNull = getCoachingStaffByIdBreakingReference(1L);
		coachingStaffPositionNull.setPosition("");

		// Then
		assertThrows(AddingCoachingStaffException.class, () -> {
			// When
			coachingStaffsService.updateCoachingStaff(coachingStaffPositionNull);
		});
	}

	@Test
	void testUpdateCoachingStaffFailedAgeNull() {
		// Given
		CoachingStaff coachingStaffAgeNull = getCoachingStaffByIdBreakingReference(1L);
		coachingStaffAgeNull.setAge(null);

		// Then
		assertThrows(AddingCoachingStaffException.class, () -> {
			// When
			coachingStaffsService.updateCoachingStaff(coachingStaffAgeNull);
		});
	}

	@Test
	void testUpdateCoachingStaffFailedAgeLessThan1() {
		// Given
		CoachingStaff coachingStaffAgeLessThan1 = getCoachingStaffByIdBreakingReference(1L);
		coachingStaffAgeLessThan1.setAge(0);

		// Then
		assertThrows(AddingCoachingStaffException.class, () -> {
			// When
			coachingStaffsService.updateCoachingStaff(coachingStaffAgeLessThan1);
		});
	}

	@Test
	void testUpdateCoachingStaffFailedWeightNull() {
		// Given
		CoachingStaff coachingStaffWeightNull = getCoachingStaffByIdBreakingReference(1L);
		coachingStaffWeightNull.setWeight(null);

		// Then
		assertThrows(AddingCoachingStaffException.class, () -> {
			// When
			coachingStaffsService.updateCoachingStaff(coachingStaffWeightNull);
		});
	}

	@Test
	void testUpdateCoachingStaffFailedWeightLessThan1() {
		// Given
		CoachingStaff coachingStaffWeightLessThan1 = getCoachingStaffByIdBreakingReference(1L);
		coachingStaffWeightLessThan1.setWeight(0.0);

		// Then
		assertThrows(AddingCoachingStaffException.class, () -> {
			// When
			coachingStaffsService.updateCoachingStaff(coachingStaffWeightLessThan1);
		});
	}

	@Test
	void testUpdateCoachingStaffFailedHeightNull() {
		// Given
		CoachingStaff coachingStaffHeightNull = getCoachingStaffByIdBreakingReference(1L);
		coachingStaffHeightNull.setHeight(null);

		// Then
		assertThrows(AddingCoachingStaffException.class, () -> {
			// When
			coachingStaffsService.updateCoachingStaff(coachingStaffHeightNull);
		});
	}

	@Test
	void testUpdateCoachingStaffFailedHeightLessThan1() {
		// Given
		CoachingStaff coachingStaffHeightLessThan1 = getCoachingStaffByIdBreakingReference(1L);
		coachingStaffHeightLessThan1.setHeight(0.0);

		// Then
		assertThrows(AddingCoachingStaffException.class, () -> {
			// When
			coachingStaffsService.updateCoachingStaff(coachingStaffHeightLessThan1);
		});
	}

	@Test
	void testUpdateCoachingStaffFailedClubNull() {
		// Given
		CoachingStaff coachingStaffClubNull = getCoachingStaffByIdBreakingReference(1L);
		coachingStaffClubNull.setClub(null);

		// Then
		assertThrows(AddingCoachingStaffException.class, () -> {
			// When
			coachingStaffsService.updateCoachingStaff(coachingStaffClubNull);
		});
	}

	@Test
	void testUpdateCoachingStaffFailedClubIdNull() {
		// Given
		CoachingStaff coachingStaffClubIdNull = getCoachingStaffByIdBreakingReference(1L);
		coachingStaffClubIdNull.setClub(getClubWithoutIdByPositionBreakingReference(0));

		// Then
		assertThrows(AddingCoachingStaffException.class, () -> {
			// When
			coachingStaffsService.updateCoachingStaff(coachingStaffClubIdNull);
		});
	}

	@Test
	void testUpdateCoachingStaffFailedClubIdLessThan1() {
		// Given
		CoachingStaff coachingStaffClubIdLessThan1 = getCoachingStaffByIdBreakingReference(1L);
		coachingStaffClubIdLessThan1.setClub(CLUB_WITH_ID_LESS_THAN_1);

		// Then
		assertThrows(AddingCoachingStaffException.class, () -> {
			// When
			coachingStaffsService.updateCoachingStaff(coachingStaffClubIdLessThan1);
		});
	}

	@Test
	void testUpdateCoachingStaffFailedClubDoesNotExists() {
		// Given
		when(coachingStaffsDao.findById(anyLong())).thenReturn(Optional.of(getCoachingStaffByIdBreakingReference(1L)));
		when(clubesService.getClubById(anyLong())).thenThrow(ClubNotFoundException.class);

		// Then
		assertThrows(ClubNotFoundException.class, () -> {
			// When
			coachingStaffsService.updateCoachingStaff(getCoachingStaffByIdBreakingReference(1L));
		});

		verify(coachingStaffsDao).findById(1L);
		verify(clubesService).getClubById(anyLong());
	}

	@Test
	void testUpdateCoachingStaffFailedCoachingStaffDoesNotExists() {
		// Given
		when(coachingStaffsDao.findById(anyLong())).thenReturn(Optional.ofNullable(null));

		// Then
		assertThrows(CoachingStaffNotFoundException.class, () -> {
			// When
			coachingStaffsService.updateCoachingStaff(getCoachingStaffByIdBreakingReference(1L));
		});

		verify(coachingStaffsDao).findById(1L);
	}

	// DELETE

	@Test
	void testDeleteCoachingStaffSuccessed() {
		// Given
		when(coachingStaffsDao.findById(anyLong())).thenReturn(Optional.of(getCoachingStaffByIdBreakingReference(1L)));

		// When
		CoachingStaff coachingStaff = coachingStaffsService.deleteCoachingStaff(1L);

		// Then
		assertEquals(getCoachingStaffByIdBreakingReference(1L), coachingStaff);
		verify(coachingStaffsDao).findById(1L);
		verify(coachingStaffsDao).deleteById(1L);

	}

	@Test
	void testDeleteCoachingStaffFailed() {
		// Given
		when(coachingStaffsDao.findById(anyLong())).thenReturn(Optional.ofNullable(null));
		
		// Then
		assertThrows(CoachingStaffNotFoundException.class, () -> {
			// When
			coachingStaffsService.deleteCoachingStaff(1L);
		});

		verify(coachingStaffsDao).findById(1L);
		verify(coachingStaffsDao, never()).deleteById(1L);
	}

}
