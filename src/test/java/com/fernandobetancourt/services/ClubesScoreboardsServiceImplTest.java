package com.fernandobetancourt.services;

import static com.fernandobetancourt.Data.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.fernandobetancourt.exceptions.AddingClubScoreboardException;
import com.fernandobetancourt.exceptions.ClubNotFoundException;
import com.fernandobetancourt.exceptions.ClubScoreboardNotFoundException;
import com.fernandobetancourt.exceptions.ScoreboardNotFoundException;
import com.fernandobetancourt.model.dao.IClubesScoreboardsDao;
import com.fernandobetancourt.model.entity.ClubScoreboard;
import com.fernandobetancourt.model.entity.Scoreboard;
import com.fernandobetancourt.validators.ClubScoreboardValidator;

@SpringBootTest(classes = {ClubesScoreboardsServiceImpl.class, ClubScoreboardValidator.class})
class ClubesScoreboardsServiceImplTest {

	@MockBean
	private IClubesScoreboardsDao clubesScoreboardsDao;

	@MockBean
	private IClubesService clubesService;

	@MockBean
	private IScoreboardsService scoreboardsService;

	@Autowired
	private IClubesScoreboardsService clubesScoreboardsService;

	// GET

	@Test
	void testGetClubScoreboardSuccessed() {
		// Given
		when(clubesScoreboardsDao.findById(anyLong())).thenReturn(Optional.of(getClubScoreboardWithIdLocal()));

		// When
		ClubScoreboard clubScoreboard = clubesScoreboardsService.getClubScoreboard(1L);

		// Then
		assertEquals(getClubScoreboardWithIdLocal(), clubScoreboard);
		verify(clubesScoreboardsDao).findById(1L);

	}

	@Test
	void testGetClubScoreboardFailed() {
		// Given
		when(clubesScoreboardsDao.findById(anyLong())).thenReturn(Optional.empty());

		// Then
		assertThrows(ClubScoreboardNotFoundException.class, () -> {
			// When
			clubesScoreboardsService.getClubScoreboard(1L);
		});

		verify(clubesScoreboardsDao).findById(1L);
	}

	@Test
	void testGetClubScoreboardByScoreboardAndClubStatusSuccessed() {
		// Given
		when(scoreboardsService.getScoreboard(anyLong())).thenReturn(getScoreboadWithId());

		when(clubesScoreboardsDao.findByScoreboardAndClubStatus(any(Scoreboard.class), anyString()))
				.thenReturn(Optional.of(getClubScoreboardWithIdLocal()));

		// WHen
		ClubScoreboard clubScoreboard = clubesScoreboardsService.getClubScoreboardByScoreboardAndClubStatus(1L,
				"Local");

		// Then
		assertEquals(getClubScoreboardWithIdLocal(), clubScoreboard);
		verify(scoreboardsService).getScoreboard(1L);
		verify(clubesScoreboardsDao).findByScoreboardAndClubStatus(getScoreboadWithId(), "Local");
	}

	@Test
	void testGetClubScoreboardByScoreboardAndClubStatusFailed() {
		// Given
		when(scoreboardsService.getScoreboard(anyLong())).thenReturn(getScoreboadWithId());

		when(clubesScoreboardsDao.findByScoreboardAndClubStatus(any(Scoreboard.class), anyString()))
				.thenReturn(Optional.empty());

		// Then
		assertThrows(ClubScoreboardNotFoundException.class, () -> {
			// When
			clubesScoreboardsService.getClubScoreboardByScoreboardAndClubStatus(1L, "Local");
		});

		verify(scoreboardsService).getScoreboard(1L);
		verify(clubesScoreboardsDao).findByScoreboardAndClubStatus(getScoreboadWithId(), "Local");
	}

	// POST

	@Test
	void testAddClubScoreboardSuccessed() {
		// Given
		when(clubesScoreboardsDao.save(any(ClubScoreboard.class))).then(invocation -> {
			ClubScoreboard clubScoreboard = invocation.getArgument(0);
			clubScoreboard.setClubScoreboardId(1L);
			return clubScoreboard;
		});

		// When
		ClubScoreboard clubScoreboard = clubesScoreboardsService.addClubScoreboard(getClubScoreboardWithoutId());

		// Then
		assertEquals(getClubScoreboardWithIdLocal(), clubScoreboard);
		verify(scoreboardsService).getScoreboard(1L);
		verify(clubesService).getClubById(1L);
		verify(clubesScoreboardsDao).save(any(ClubScoreboard.class));

	}

	@Test
	void testAddClubScoreboardFailedClubScoreboardNull() {
		// Then
		assertThrows(AddingClubScoreboardException.class, () -> {
			// When
			clubesScoreboardsService.addClubScoreboard(null);
		});

		verify(scoreboardsService, never()).getScoreboard(anyLong());
		verify(clubesService, never()).getClubById(anyLong());
		verify(clubesScoreboardsDao, never()).save(any(ClubScoreboard.class));
	}

	@Test
	void testAddClubScoreboardFailedClubStatusNull() {
		// Given
		ClubScoreboard clubScoreboardClubStatusNull = getClubScoreboardWithoutId();
		clubScoreboardClubStatusNull.setClubStatus(null);

		// Then
		assertThrows(AddingClubScoreboardException.class, () -> {
			// When
			clubesScoreboardsService.addClubScoreboard(clubScoreboardClubStatusNull);
		});

		verify(scoreboardsService, never()).getScoreboard(anyLong());
		verify(clubesService, never()).getClubById(anyLong());
		verify(clubesScoreboardsDao, never()).save(any(ClubScoreboard.class));
	}

	@Test
	void testAddClubScoreboardFailedClubStatusEmptyString() {
		// Given
		ClubScoreboard clubScoreboardClubStatusEmptyString = getClubScoreboardWithoutId();
		clubScoreboardClubStatusEmptyString.setClubStatus("");

		// Then
		assertThrows(AddingClubScoreboardException.class, () -> {
			// When
			clubesScoreboardsService.addClubScoreboard(clubScoreboardClubStatusEmptyString);
		});

		verify(scoreboardsService, never()).getScoreboard(anyLong());
		verify(clubesService, never()).getClubById(anyLong());
		verify(clubesScoreboardsDao, never()).save(any(ClubScoreboard.class));
	}

	@Test
	void testAddClubScoreboardFailedScoreboardNull() {
		// Given
		ClubScoreboard clubScoreboardScoreboardNull = getClubScoreboardWithoutId();
		clubScoreboardScoreboardNull.setScoreboard(null);

		// Then
		assertThrows(AddingClubScoreboardException.class, () -> {
			// When
			clubesScoreboardsService.addClubScoreboard(clubScoreboardScoreboardNull);
		});

		verify(scoreboardsService, never()).getScoreboard(anyLong());
		verify(clubesService, never()).getClubById(anyLong());
		verify(clubesScoreboardsDao, never()).save(any(ClubScoreboard.class));
	}

	@Test
	void testAddClubScoreboardFailedScoreboardIdNull() {
		// Given
		ClubScoreboard clubScoreboardScoreboardIdNull = getClubScoreboardWithoutId();
		clubScoreboardScoreboardIdNull.setScoreboard(getScoreboadWithoutId());

		// Then
		assertThrows(AddingClubScoreboardException.class, () -> {
			// When
			clubesScoreboardsService.addClubScoreboard(clubScoreboardScoreboardIdNull);
		});

		verify(scoreboardsService, never()).getScoreboard(anyLong());
		verify(clubesService, never()).getClubById(anyLong());
		verify(clubesScoreboardsDao, never()).save(any(ClubScoreboard.class));
	}

	@Test
	void testAddClubScoreboardFailedScoreboardIdLessThan1() {
		// Given
		ClubScoreboard clubScoreboardScoreboardIdLessThan1 = getClubScoreboardWithoutId();
		clubScoreboardScoreboardIdLessThan1.setScoreboard(getScoreboadWithIdLessThan1());

		// Then
		assertThrows(AddingClubScoreboardException.class, () -> {
			// When
			clubesScoreboardsService.addClubScoreboard(clubScoreboardScoreboardIdLessThan1);
		});

		verify(scoreboardsService, never()).getScoreboard(anyLong());
		verify(clubesService, never()).getClubById(anyLong());
		verify(clubesScoreboardsDao, never()).save(any(ClubScoreboard.class));
	}

	@Test
	void testAddClubScoreboardFailedClubNull() {
		// Given
		ClubScoreboard clubScoreboardClubNull = getClubScoreboardWithoutId();
		clubScoreboardClubNull.setClub(null);

		// Then
		assertThrows(AddingClubScoreboardException.class, () -> {
			// When
			clubesScoreboardsService.addClubScoreboard(clubScoreboardClubNull);
		});

		verify(scoreboardsService, never()).getScoreboard(anyLong());
		verify(clubesService, never()).getClubById(anyLong());
		verify(clubesScoreboardsDao, never()).save(any(ClubScoreboard.class));
	}

	@Test
	void testAddClubScoreboardFailedClubIdNull() {
		// Given
		ClubScoreboard clubScoreboardClubIdNull = getClubScoreboardWithoutId();
		clubScoreboardClubIdNull.setClub(getClubWithoutIdByPositionBreakingReference(0));

		// Then
		assertThrows(AddingClubScoreboardException.class, () -> {
			// When
			clubesScoreboardsService.addClubScoreboard(clubScoreboardClubIdNull);
		});

		verify(scoreboardsService, never()).getScoreboard(anyLong());
		verify(clubesService, never()).getClubById(anyLong());
		verify(clubesScoreboardsDao, never()).save(any(ClubScoreboard.class));
	}

	@Test
	void testAddClubScoreboardFailedClubIdLessThan1() {
		// Given
		ClubScoreboard clubScoreboardClubIdLessThan1 = getClubScoreboardWithoutId();
		clubScoreboardClubIdLessThan1.setClub(CLUB_WITH_ID_LESS_THAN_1);

		// Then
		assertThrows(AddingClubScoreboardException.class, () -> {
			// When
			clubesScoreboardsService.addClubScoreboard(clubScoreboardClubIdLessThan1);
		});

		verify(scoreboardsService, never()).getScoreboard(anyLong());
		verify(clubesService, never()).getClubById(anyLong());
		verify(clubesScoreboardsDao, never()).save(any(ClubScoreboard.class));
	}

	@Test
	void testAddClubScoreboardFailedScoreboardDoesNotExists() {
		// Given
		when(scoreboardsService.getScoreboard(anyLong())).thenThrow(ScoreboardNotFoundException.class);

		// Then
		assertThrows(ScoreboardNotFoundException.class, () -> {
			// When
			clubesScoreboardsService.addClubScoreboard(getClubScoreboardWithoutId());
		});

		verify(scoreboardsService).getScoreboard(anyLong());
		verify(clubesService, never()).getClubById(anyLong());
		verify(clubesScoreboardsDao, never()).save(any(ClubScoreboard.class));
	}

	@Test
	void testAddClubScoreboardFailedClubDoesNotExists() {
		// Given
		when(clubesService.getClubById(anyLong())).thenThrow(ClubNotFoundException.class);

		// Then
		assertThrows(ClubNotFoundException.class, () -> {
			// When
			clubesScoreboardsService.addClubScoreboard(getClubScoreboardWithoutId());
		});

		verify(scoreboardsService).getScoreboard(anyLong());
		verify(clubesService).getClubById(anyLong());
		verify(clubesScoreboardsDao, never()).save(any(ClubScoreboard.class));
	}

	// PUT

	@Test
	void testUpdateClubScoreboardSuccessed() {
		// Given
		when(clubesScoreboardsDao.findById(anyLong())).thenReturn(Optional.of(getClubScoreboardWithIdLocal()));
		when(clubesScoreboardsDao.save(any(ClubScoreboard.class))).then(invocation -> invocation.getArgument(0));
		ClubScoreboard clubScoreboard = getClubScoreboardWithIdLocal();
		clubScoreboard.setClubStatus("Visitante");
		clubScoreboard.setScoreboard(getScoreboadWithId2());
		clubScoreboard.setClub(getClubByIdBreakingReference(7L));

		// When
		ClubScoreboard clubScoreboardUpdated = clubesScoreboardsService.updateClubScoreboard(clubScoreboard);

		// Then
		assertEquals(1L, clubScoreboardUpdated.getClubScoreboardId());
		assertEquals("Visitante", clubScoreboardUpdated.getClubStatus());
		assertEquals(getScoreboadWithId2(), clubScoreboardUpdated.getScoreboard());
		assertEquals(getClubByIdBreakingReference(7L), clubScoreboardUpdated.getClub());

		verify(clubesScoreboardsDao).findById(1L);
		verify(scoreboardsService).getScoreboard(2L);
		verify(clubesService).getClubById(7L);
		verify(clubesScoreboardsDao).save(any(ClubScoreboard.class));

	}

	@Test
	void testUpdateClubScoreboardFailedClubScoreboardNull() {
		// Then
		assertThrows(AddingClubScoreboardException.class, () -> {
			// When
			clubesScoreboardsService.updateClubScoreboard(null);
		});

		verify(clubesScoreboardsDao, never()).findById(anyLong());
		verify(scoreboardsService, never()).getScoreboard(anyLong());
		verify(clubesService, never()).getClubById(anyLong());
		verify(clubesScoreboardsDao, never()).save(any(ClubScoreboard.class));
	}

	@Test
	void testUpdateClubScoreboardFailedClubScoreboardIdNull() {
		// Then
		assertThrows(AddingClubScoreboardException.class, () -> {
			// When
			clubesScoreboardsService.updateClubScoreboard(getClubScoreboardWithoutId());
		});

		verify(clubesScoreboardsDao, never()).findById(anyLong());
		verify(scoreboardsService, never()).getScoreboard(anyLong());
		verify(clubesService, never()).getClubById(anyLong());
		verify(clubesScoreboardsDao, never()).save(any(ClubScoreboard.class));
	}

	@Test
	void testUpdateClubScoreboardFailedClubScoreboardIdLessThan1() {
		// Then
		assertThrows(AddingClubScoreboardException.class, () -> {
			// When
			clubesScoreboardsService.updateClubScoreboard(getClubScoreboardWithIdLessThan1());
		});

		verify(clubesScoreboardsDao, never()).findById(anyLong());
		verify(scoreboardsService, never()).getScoreboard(anyLong());
		verify(clubesService, never()).getClubById(anyLong());
		verify(clubesScoreboardsDao, never()).save(any(ClubScoreboard.class));
	}

	@Test
	void testUpdateClubScoreboardFailedClubStatusNull() {
		// Given
		ClubScoreboard clubScoreboardClubStatusNull = getClubScoreboardWithIdLocal();
		clubScoreboardClubStatusNull.setClubStatus(null);

		// Then
		assertThrows(AddingClubScoreboardException.class, () -> {
			// When
			clubesScoreboardsService.updateClubScoreboard(clubScoreboardClubStatusNull);
		});

		verify(clubesScoreboardsDao, never()).findById(anyLong());
		verify(scoreboardsService, never()).getScoreboard(anyLong());
		verify(clubesService, never()).getClubById(anyLong());
		verify(clubesScoreboardsDao, never()).save(any(ClubScoreboard.class));
	}

	@Test
	void testUpdateClubScoreboardFailedClubStatusEmptyString() {
		// Given
		ClubScoreboard clubScoreboardClubStatusEmptyString = getClubScoreboardWithIdLocal();
		clubScoreboardClubStatusEmptyString.setClubStatus("");

		// Then
		assertThrows(AddingClubScoreboardException.class, () -> {
			// When
			clubesScoreboardsService.updateClubScoreboard(clubScoreboardClubStatusEmptyString);
		});

		verify(clubesScoreboardsDao, never()).findById(anyLong());
		verify(scoreboardsService, never()).getScoreboard(anyLong());
		verify(clubesService, never()).getClubById(anyLong());
		verify(clubesScoreboardsDao, never()).save(any(ClubScoreboard.class));
	}

	@Test
	void testUpdateClubScoreboardFailedScoreboardNull() {
		// Given
		ClubScoreboard clubScoreboardScoreboardNull = getClubScoreboardWithIdLocal();
		clubScoreboardScoreboardNull.setScoreboard(null);

		// Then
		assertThrows(AddingClubScoreboardException.class, () -> {
			// When
			clubesScoreboardsService.updateClubScoreboard(clubScoreboardScoreboardNull);
		});

		verify(clubesScoreboardsDao, never()).findById(anyLong());
		verify(scoreboardsService, never()).getScoreboard(anyLong());
		verify(clubesService, never()).getClubById(anyLong());
		verify(clubesScoreboardsDao, never()).save(any(ClubScoreboard.class));
	}

	@Test
	void testUpdateClubScoreboardFailedScoreboardIdNull() {
		// Given
		ClubScoreboard clubScoreboardScoreboardIdNull = getClubScoreboardWithIdLocal();
		clubScoreboardScoreboardIdNull.setScoreboard(getScoreboadWithoutId());

		// Then
		assertThrows(AddingClubScoreboardException.class, () -> {
			// When
			clubesScoreboardsService.updateClubScoreboard(clubScoreboardScoreboardIdNull);
		});

		verify(clubesScoreboardsDao, never()).findById(anyLong());
		verify(scoreboardsService, never()).getScoreboard(anyLong());
		verify(clubesService, never()).getClubById(anyLong());
		verify(clubesScoreboardsDao, never()).save(any(ClubScoreboard.class));
	}

	@Test
	void testUpdateClubScoreboardFailedScoreboardIdLessThan1() {
		// Given
		ClubScoreboard clubScoreboardScoreboardIdLessThan1 = getClubScoreboardWithIdLocal();
		clubScoreboardScoreboardIdLessThan1.setScoreboard(getScoreboadWithIdLessThan1());

		// Then
		assertThrows(AddingClubScoreboardException.class, () -> {
			// When
			clubesScoreboardsService.updateClubScoreboard(clubScoreboardScoreboardIdLessThan1);
		});

		verify(clubesScoreboardsDao, never()).findById(anyLong());
		verify(scoreboardsService, never()).getScoreboard(anyLong());
		verify(clubesService, never()).getClubById(anyLong());
		verify(clubesScoreboardsDao, never()).save(any(ClubScoreboard.class));
	}

	@Test
	void testUpdateClubScoreboardFailedClubNull() {
		// Given
		ClubScoreboard clubScoreboardClubNull = getClubScoreboardWithIdLocal();
		clubScoreboardClubNull.setClub(null);

		// Then
		assertThrows(AddingClubScoreboardException.class, () -> {
			// When
			clubesScoreboardsService.updateClubScoreboard(clubScoreboardClubNull);
		});

		verify(clubesScoreboardsDao, never()).findById(anyLong());
		verify(scoreboardsService, never()).getScoreboard(anyLong());
		verify(clubesService, never()).getClubById(anyLong());
		verify(clubesScoreboardsDao, never()).save(any(ClubScoreboard.class));
	}

	@Test
	void testUpdateClubScoreboardFailedClubIdNull() {
		// Given
		ClubScoreboard clubScoreboardClubIdNull = getClubScoreboardWithIdLocal();
		clubScoreboardClubIdNull.setClub(getClubWithoutIdByPositionBreakingReference(0));

		// Then
		assertThrows(AddingClubScoreboardException.class, () -> {
			// When
			clubesScoreboardsService.updateClubScoreboard(clubScoreboardClubIdNull);
		});

		verify(clubesScoreboardsDao, never()).findById(anyLong());
		verify(scoreboardsService, never()).getScoreboard(anyLong());
		verify(clubesService, never()).getClubById(anyLong());
		verify(clubesScoreboardsDao, never()).save(any(ClubScoreboard.class));
	}

	@Test
	void testUpdateClubScoreboardFailedClubIdLessThan1() {
		// Given
		ClubScoreboard clubScoreboardClubIdLessThan1 = getClubScoreboardWithIdLocal();
		clubScoreboardClubIdLessThan1.setClub(CLUB_WITH_ID_LESS_THAN_1);

		// Then
		assertThrows(AddingClubScoreboardException.class, () -> {
			// When
			clubesScoreboardsService.updateClubScoreboard(clubScoreboardClubIdLessThan1);
		});

		verify(clubesScoreboardsDao, never()).findById(anyLong());
		verify(scoreboardsService, never()).getScoreboard(anyLong());
		verify(clubesService, never()).getClubById(anyLong());
		verify(clubesScoreboardsDao, never()).save(any(ClubScoreboard.class));
	}

	@Test
	void testUpdateClubScoreboardFailedClubScoreboardDoesNotExists() {
		// Given
		when(clubesScoreboardsDao.findById(anyLong())).thenThrow(ClubScoreboardNotFoundException.class);

		// Then
		assertThrows(ClubScoreboardNotFoundException.class, () -> {
			// When
			clubesScoreboardsService.updateClubScoreboard(getClubScoreboardWithIdLocal());
		});

		verify(clubesScoreboardsDao).findById(anyLong());
		verify(scoreboardsService, never()).getScoreboard(anyLong());
		verify(clubesService, never()).getClubById(anyLong());
		verify(clubesScoreboardsDao, never()).save(any(ClubScoreboard.class));
	}

	@Test
	void testUpdateClubScoreboardFailedScoreboardDoesNotExists() {
		// Given
		when(clubesScoreboardsDao.findById(anyLong())).thenReturn(Optional.of(getClubScoreboardWithIdLocal()));
		when(scoreboardsService.getScoreboard(anyLong())).thenThrow(ScoreboardNotFoundException.class);

		// Then
		assertThrows(ScoreboardNotFoundException.class, () -> {
			// When
			clubesScoreboardsService.updateClubScoreboard(getClubScoreboardWithIdLocal());
		});

		verify(clubesScoreboardsDao).findById(anyLong());
		verify(scoreboardsService).getScoreboard(anyLong());
		verify(clubesService, never()).getClubById(anyLong());
		verify(clubesScoreboardsDao, never()).save(any(ClubScoreboard.class));
	}

	@Test
	void testUpdateClubScoreboardFailedClubDoesNotExists() {
		// Given
		when(clubesScoreboardsDao.findById(anyLong())).thenReturn(Optional.of(getClubScoreboardWithIdLocal()));
		when(clubesService.getClubById(anyLong())).thenThrow(ClubNotFoundException.class);

		// Then
		assertThrows(ClubNotFoundException.class, () -> {
			// When
			clubesScoreboardsService.updateClubScoreboard(getClubScoreboardWithIdLocal());
		});

		verify(clubesScoreboardsDao).findById(anyLong());
		verify(scoreboardsService).getScoreboard(anyLong());
		verify(clubesService).getClubById(anyLong());
		verify(clubesScoreboardsDao, never()).save(any(ClubScoreboard.class));
	}

	// DELETE

	@Test
	void testDeleteClubScoreboardSuccessed() {
		//Given
		when(clubesScoreboardsDao.findById(anyLong())).thenReturn(Optional.of(getClubScoreboardWithIdLocal()));
		
		//When
		ClubScoreboard clubScoreboard = clubesScoreboardsService.deleteClubScoreboard(1L);
		
		//Then
		assertEquals(getClubScoreboardWithIdLocal(), clubScoreboard);
		verify(clubesScoreboardsDao).findById(1L);
		verify(clubesScoreboardsDao).deleteById(1L);
	}
	
	@Test
	void testDeleteClubScoreboardFailed() {
		//Given
		when(clubesScoreboardsDao.findById(anyLong())).thenReturn(Optional.empty());
		
		//Then
		assertThrows(ClubScoreboardNotFoundException.class, () -> {
			//When
			clubesScoreboardsService.deleteClubScoreboard(1L);
		});
		
		verify(clubesScoreboardsDao).findById(1L);
		verify(clubesScoreboardsDao, never()).deleteById(1L);
	}

}
