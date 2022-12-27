package com.fernandobetancourt.services;

import static com.fernandobetancourt.Data.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.fernandobetancourt.exceptions.AddingClubMatchException;
import com.fernandobetancourt.exceptions.ClubMatchNotFoundException;
import com.fernandobetancourt.exceptions.ClubNotFoundException;
import com.fernandobetancourt.model.dao.IClubesMatchesDao;
import com.fernandobetancourt.model.entity.ClubMatch;
import com.fernandobetancourt.model.entity.Match;
import com.fernandobetancourt.validators.ClubMatchValidator;

@SpringBootTest(classes = {ClubesMatchesServiceImpl.class, ClubMatchValidator.class})
class ClubesMatchesServiceImplTest {

	@MockBean
	private IClubesMatchesDao clubesMatchesDao;

	@MockBean
	private IClubesService clubesService;

	@Autowired
	private IClubesMatchesService clubesMatchesService;

	// GET

	@Test
	void testGetClubMatchSuccessed() {
		// Given
		when(clubesMatchesDao.findById(anyLong())).thenReturn(Optional.of(getClubMatch()));

		// When
		ClubMatch clubMatch = clubesMatchesService.getClubMatch(1L);

		// Then
		assertEquals(getClubMatch(), clubMatch);
		verify(clubesMatchesDao).findById(1L);
	}

	@Test
	void testGetClubMatchFailed() {
		// Given
		when(clubesMatchesDao.findById(anyLong())).thenReturn(Optional.empty());

		// Then
		assertThrows(ClubMatchNotFoundException.class, () -> {
			// When
			clubesMatchesService.getClubMatch(1L);
		});

		verify(clubesMatchesDao).findById(1L);
	}

	@Test
	void testGetClubMatchByMatchSuccessed() {
		// Given
		when(clubesMatchesDao.findByMatch(any(Match.class))).thenReturn(Optional.of(getClubMatch()));
		Match match = getMatchByIdBreakingReference(1L);

		// When
		ClubMatch clubMatch = clubesMatchesService.getClubMatchByMatch(match);

		// Then
		assertEquals(getClubMatch(), clubMatch);
		verify(clubesMatchesDao).findByMatch(any(Match.class));
	}

	@Test
	void testGetClubMatchByMatchFailed() {
		// Given
		when(clubesMatchesDao.findByMatch(any(Match.class))).thenReturn(Optional.empty());
		Match match = getMatchByIdBreakingReference(1L);

		// Then
		assertThrows(ClubMatchNotFoundException.class, () -> {
			// When
			clubesMatchesService.getClubMatchByMatch(match);
		});

		verify(clubesMatchesDao).findByMatch(any(Match.class));
	}

	// POST

	@Test
	void testAddClubMatchSuccessed() {
		// Given
		when(clubesMatchesDao.save(any(ClubMatch.class))).then(invocation -> {
			ClubMatch clubMatch = invocation.getArgument(0);
			clubMatch.setClubMatchId(1L);
			return clubMatch;
		});

		// When
		ClubMatch clubMatch = clubesMatchesService.addClubMatch(getClubMatchWithoutId());

		// Then
		assertEquals(getClubMatch(), clubMatch);
		verify(clubesService, times(2)).getClubById(anyLong());
		verify(clubesMatchesDao).save(any(ClubMatch.class));
	}

	@Test
	void testAddClubMatchFailedClubMatchNull() {
		// Then
		assertThrows(AddingClubMatchException.class, () -> {
			// When
			clubesMatchesService.addClubMatch(null);

		});

		verify(clubesService, never()).getClubById(anyLong());
		verify(clubesMatchesDao, never()).save(any(ClubMatch.class));
	}

	@Test
	void testAddClubMatchFailedLocalClubNull() {
		// Given
		ClubMatch clubMathLocalClubNull = getClubMatchWithoutId();
		clubMathLocalClubNull.setLocalClub(null);

		// Then
		assertThrows(AddingClubMatchException.class, () -> {
			// When
			clubesMatchesService.addClubMatch(clubMathLocalClubNull);

		});

		verify(clubesService, never()).getClubById(anyLong());
		verify(clubesMatchesDao, never()).save(any(ClubMatch.class));
	}

//
	@Test
	void testAddClubMatchFailedLocalClubIdNull() {
		// Given
		ClubMatch clubMathLocalClubIdNull = getClubMatchWithoutId();
		clubMathLocalClubIdNull.setLocalClub(getClubWithoutIdByPositionBreakingReference(0));

		// Then
		assertThrows(AddingClubMatchException.class, () -> {
			// When
			clubesMatchesService.addClubMatch(clubMathLocalClubIdNull);

		});

		verify(clubesService, never()).getClubById(anyLong());
		verify(clubesMatchesDao, never()).save(any(ClubMatch.class));
	}

	@Test
	void testAddClubMatchFailedLocalClubIdLessThan1() {
		// Given
		ClubMatch clubMathLocalClubIdLessThan1 = getClubMatchWithoutId();
		clubMathLocalClubIdLessThan1.setLocalClub(CLUB_WITH_ID_LESS_THAN_1);

		// Then
		assertThrows(AddingClubMatchException.class, () -> {
			// When
			clubesMatchesService.addClubMatch(clubMathLocalClubIdLessThan1);

		});

		verify(clubesService, never()).getClubById(anyLong());
		verify(clubesMatchesDao, never()).save(any(ClubMatch.class));
	}

	// Parecía que el error estaba en este o en los 2 anteriores
	@Test
	void testAddClubMatchFailedVisitorClubNull() {
		// Given
		ClubMatch clubMathVisitorClubNull = getClubMatchWithoutId();
		clubMathVisitorClubNull.setVisitorClub(null);

		// Then
		assertThrows(AddingClubMatchException.class, () -> {
			// When
			clubesMatchesService.addClubMatch(clubMathVisitorClubNull);

		});

		verify(clubesService, never()).getClubById(anyLong());
		verify(clubesMatchesDao, never()).save(any(ClubMatch.class));
	}

//
	@Test
	void testAddClubMatchFailedVisitorClubIdNull() {
		// Given
		ClubMatch clubMathVisitorClubIdNull = getClubMatchWithoutId();
		clubMathVisitorClubIdNull.setVisitorClub(getClubWithoutIdByPositionBreakingReference(0));

		// Then
		assertThrows(AddingClubMatchException.class, () -> {
			// When
			clubesMatchesService.addClubMatch(clubMathVisitorClubIdNull);

		});

		verify(clubesService, never()).getClubById(anyLong());
		verify(clubesMatchesDao, never()).save(any(ClubMatch.class));
	}

	@Test
	void testAddClubMatchFailedVisitorClubIdLessThan1() {
		// Given
		ClubMatch clubMathVisitorClubIdLessThan1 = getClubMatchWithoutId();
		clubMathVisitorClubIdLessThan1.setVisitorClub(CLUB_WITH_ID_LESS_THAN_1);

		// Then
		assertThrows(AddingClubMatchException.class, () -> {
			// When
			clubesMatchesService.addClubMatch(clubMathVisitorClubIdLessThan1);

		});

		verify(clubesService, never()).getClubById(anyLong());
		verify(clubesMatchesDao, never()).save(any(ClubMatch.class));
	}

	@Test
	void testAddClubMatchFailedMatchNull() {
		// Given
		ClubMatch clubMathMatchNull = getClubMatchWithoutId();
		clubMathMatchNull.setMatch(null);

		// Then
		assertThrows(AddingClubMatchException.class, () -> {
			// When
			clubesMatchesService.addClubMatch(clubMathMatchNull);

		});

		verify(clubesService, never()).getClubById(anyLong());
		verify(clubesMatchesDao, never()).save(any(ClubMatch.class));
	}

//
	@Test
	void testAddClubMatchFailedMatchIdNull() {
		// Given
		ClubMatch clubMathMatchIdNull = getClubMatchWithoutId();
		clubMathMatchIdNull.setMatch(getMatchWithoutIdByPositionBreakingReference(0));

		// Then
		assertThrows(AddingClubMatchException.class, () -> {
			// When
			clubesMatchesService.addClubMatch(clubMathMatchIdNull);

		});

		verify(clubesService, never()).getClubById(anyLong());
		verify(clubesMatchesDao, never()).save(any(ClubMatch.class));
	}

	@Test
	void testAddClubMatchFailedMatchIdLessThan1() {
		// Given
		ClubMatch clubMathMatchIdLessThan1 = getClubMatchWithoutId();
		clubMathMatchIdLessThan1.setMatch(MATCH_WITH_ID_LESS_THAN_1);

		// Then
		assertThrows(AddingClubMatchException.class, () -> {
			// When
			clubesMatchesService.addClubMatch(clubMathMatchIdLessThan1);

		});

		verify(clubesService, never()).getClubById(anyLong());
		verify(clubesMatchesDao, never()).save(any(ClubMatch.class));
	}

	@Test
	void testAddClubMatchFailedLocalClubDoesNotExists() {
		// Given
		when(clubesService.getClubById(1L)).thenThrow(ClubNotFoundException.class);
		ClubMatch clubMath = getClubMatchWithoutId();

		// Then
		assertThrows(ClubNotFoundException.class, () -> {
			// When
			clubesMatchesService.addClubMatch(clubMath);

		});

		verify(clubesService).getClubById(anyLong());
		verify(clubesMatchesDao, never()).save(any(ClubMatch.class));
	}

	@Test
	void testAddClubMatchFailedVisitorClubDoesNotExists() {
		// Given
		when(clubesService.getClubById(2L)).thenThrow(ClubNotFoundException.class);
		ClubMatch clubMath = getClubMatchWithoutId();

		// Then
		assertThrows(ClubNotFoundException.class, () -> {
			// When
			clubesMatchesService.addClubMatch(clubMath);

		});

		verify(clubesService, times(2)).getClubById(anyLong());
		verify(clubesMatchesDao, never()).save(any(ClubMatch.class));
	}

	// PUT

	@Test
	void testUpdateClubMatchSuccessed() {
		// Given
		when(clubesMatchesDao.findById(anyLong())).thenReturn(Optional.of(getClubMatch()));
		when(clubesMatchesDao.save(any(ClubMatch.class))).then(invocation -> invocation.getArgument(0));

		ClubMatch clubMatch = getClubMatch();
		clubMatch.setLocalClub(getClubByIdBreakingReference(5L));
		clubMatch.setVisitorClub(getClubByIdBreakingReference(8L));
		clubMatch.setMatch(getMatchByIdBreakingReference(5L));

		// When
		ClubMatch clubMatchUpdated = clubesMatchesService.updateClubMatch(clubMatch);

		// Then
		assertEquals(clubMatch, clubMatchUpdated);
		verify(clubesService).getClubById(5L);
		verify(clubesService).getClubById(8L);
		verify(clubesMatchesDao).findById(1L);
	}

	@Test
	void testUpdateClubMatchFailedClubMatchNull() {
		// Then
		assertThrows(AddingClubMatchException.class, () -> {
			// When
			clubesMatchesService.updateClubMatch(null);

		});

		verify(clubesService, never()).getClubById(anyLong());
		verify(clubesService, never()).getClubById(anyLong());
		verify(clubesMatchesDao, never()).findById(anyLong());
	}

	@Test
	void testUpdateClubMatchFailedClubMatchIdNull() {
		// Then
		assertThrows(AddingClubMatchException.class, () -> {
			// When
			clubesMatchesService.updateClubMatch(getClubMatchWithoutId());

		});

		verify(clubesService, never()).getClubById(anyLong());
		verify(clubesService, never()).getClubById(anyLong());
		verify(clubesMatchesDao, never()).findById(anyLong());
	}

	@Test
	void testUpdateClubMatchFailedClubMatchIdLessThan1() {
		// Then
		assertThrows(AddingClubMatchException.class, () -> {
			// When
			clubesMatchesService.updateClubMatch(getClubMatchWithIdLessThan1());

		});

		verify(clubesService, never()).getClubById(anyLong());
		verify(clubesService, never()).getClubById(anyLong());
		verify(clubesMatchesDao, never()).findById(anyLong());
	}

	@Test
	void testUpdateClubMatchFailedLocalClubNull() {
		// Given
		ClubMatch clubMatchLocalClubNull = getClubMatch();
		clubMatchLocalClubNull.setLocalClub(null);

		// Then
		assertThrows(AddingClubMatchException.class, () -> {
			// When
			clubesMatchesService.updateClubMatch(clubMatchLocalClubNull);

		});

		verify(clubesService, never()).getClubById(anyLong());
		verify(clubesService, never()).getClubById(anyLong());
		verify(clubesMatchesDao, never()).findById(anyLong());
	}

	@Test
	void testUpdateClubMatchFailedLocalClubIdNull() {
		// Given
		ClubMatch clubMatchLocalClubIdNull = getClubMatch();
		clubMatchLocalClubIdNull.setLocalClub(getClubWithoutIdByPositionBreakingReference(0));

		// Then
		assertThrows(AddingClubMatchException.class, () -> {
			// When
			clubesMatchesService.updateClubMatch(clubMatchLocalClubIdNull);

		});

		verify(clubesService, never()).getClubById(anyLong());
		verify(clubesService, never()).getClubById(anyLong());
		verify(clubesMatchesDao, never()).findById(anyLong());
	}

	@Test
	void testUpdateClubMatchFailedLocalClubIdLessThan1() {
		// Given
		ClubMatch clubMatchLocalClubIdLessThan1 = getClubMatch();
		clubMatchLocalClubIdLessThan1.setLocalClub(CLUB_WITH_ID_LESS_THAN_1);

		// Then
		assertThrows(AddingClubMatchException.class, () -> {
			// When
			clubesMatchesService.updateClubMatch(clubMatchLocalClubIdLessThan1);

		});

		verify(clubesService, never()).getClubById(anyLong());
		verify(clubesService, never()).getClubById(anyLong());
		verify(clubesMatchesDao, never()).findById(anyLong());
	}

	@Test
	void testUpdateClubMatchFailedVisitorClubNull() {
		// Given
		ClubMatch clubMatchVisitorClubNull = getClubMatch();
		clubMatchVisitorClubNull.setVisitorClub(null);

		// Then
		assertThrows(AddingClubMatchException.class, () -> {
			// When
			clubesMatchesService.updateClubMatch(clubMatchVisitorClubNull);

		});

		verify(clubesService, never()).getClubById(anyLong());
		verify(clubesService, never()).getClubById(anyLong());
		verify(clubesMatchesDao, never()).findById(anyLong());
	}

	@Test
	void testUpdateClubMatchFailedVisitorClubIdNull() {
		// Given
		ClubMatch clubMatchVisitorClubIdNull = getClubMatch();
		clubMatchVisitorClubIdNull.setVisitorClub(getClubWithoutIdByPositionBreakingReference(0));

		// Then
		assertThrows(AddingClubMatchException.class, () -> {
			// When
			clubesMatchesService.updateClubMatch(clubMatchVisitorClubIdNull);

		});

		verify(clubesService, never()).getClubById(anyLong());
		verify(clubesService, never()).getClubById(anyLong());
		verify(clubesMatchesDao, never()).findById(anyLong());
	}

	@Test
	void testUpdateClubMatchFailedVisitorClubIdLessThan1() {
		// Given
		ClubMatch clubMatchVisitorClubIdLessThan1 = getClubMatch();
		clubMatchVisitorClubIdLessThan1.setVisitorClub(CLUB_WITH_ID_LESS_THAN_1);

		// Then
		assertThrows(AddingClubMatchException.class, () -> {
			// When
			clubesMatchesService.updateClubMatch(clubMatchVisitorClubIdLessThan1);

		});

		verify(clubesService, never()).getClubById(anyLong());
		verify(clubesService, never()).getClubById(anyLong());
		verify(clubesMatchesDao, never()).findById(anyLong());
	}

	@Test
	void testUpdateClubMatchFailedMatchNull() {
		// Given
		ClubMatch clubMatchMatchNull = getClubMatch();
		clubMatchMatchNull.setMatch(null);

		// Then
		assertThrows(AddingClubMatchException.class, () -> {
			// When
			clubesMatchesService.updateClubMatch(clubMatchMatchNull);

		});

		verify(clubesService, never()).getClubById(anyLong());
		verify(clubesService, never()).getClubById(anyLong());
		verify(clubesMatchesDao, never()).findById(anyLong());
	}

	@Test
	void testUpdateClubMatchFailedMatchIdNull() {
		// Given
		ClubMatch clubMatchMatchIdNull = getClubMatch();
		clubMatchMatchIdNull.setMatch(getMatchWithoutIdByPositionBreakingReference(0));

		// Then
		assertThrows(AddingClubMatchException.class, () -> {
			// When
			clubesMatchesService.updateClubMatch(clubMatchMatchIdNull);

		});

		verify(clubesService, never()).getClubById(anyLong());
		verify(clubesService, never()).getClubById(anyLong());
		verify(clubesMatchesDao, never()).findById(anyLong());
	}

	@Test
	void testUpdateClubMatchFailedMatchIdLessThan1() {
		// Given
		ClubMatch clubMatchMatchIdLessThan1 = getClubMatch();
		clubMatchMatchIdLessThan1.setMatch(MATCH_WITH_ID_LESS_THAN_1);

		// Then
		assertThrows(AddingClubMatchException.class, () -> {
			// When
			clubesMatchesService.updateClubMatch(clubMatchMatchIdLessThan1);

		});

		verify(clubesService, never()).getClubById(anyLong());
		verify(clubesService, never()).getClubById(anyLong());
		verify(clubesMatchesDao, never()).findById(anyLong());
	}

	@Test
	void testUpdateClubMatchFailedLocalClubDoesNotExists() {
		// Given
		when(clubesService.getClubById(1L)).thenThrow(ClubNotFoundException.class);
		ClubMatch clubMatch = getClubMatch();

		// Then
		assertThrows(ClubNotFoundException.class, () -> {
			// When
			clubesMatchesService.updateClubMatch(clubMatch);

		});

		verify(clubesService).getClubById(1L);
		verify(clubesService, never()).getClubById(2L);
		verify(clubesMatchesDao, never()).findById(anyLong());
	}

	@Test
	void testUpdateClubMatchFailedVisitorClubDoesNotExists() {
		// Given
		when(clubesService.getClubById(2L)).thenThrow(ClubNotFoundException.class);
		ClubMatch clubMatch = getClubMatch();

		// Then
		assertThrows(ClubNotFoundException.class, () -> {
			// When
			clubesMatchesService.updateClubMatch(clubMatch);

		});

		verify(clubesService).getClubById(1L);
		verify(clubesService).getClubById(2L);
		verify(clubesMatchesDao, never()).findById(anyLong());
	}

	@Test
	void testUpdateClubMatchFailedClubMatchDoesNotExists() {
		// Given
		when(clubesMatchesDao.findById(anyLong())).thenReturn(Optional.empty());
		ClubMatch clubMatch = getClubMatch();

		// Then
		assertThrows(ClubMatchNotFoundException.class, () -> {
			// When
			clubesMatchesService.updateClubMatch(clubMatch);

		});

		verify(clubesService).getClubById(1L);
		verify(clubesService).getClubById(2L);
		verify(clubesMatchesDao).findById(1L);
	}

	// DELETE

	@Test
	void testDeleteClubMatchSuccessed() {
		//Given
		when(clubesMatchesDao.findById(anyLong())).thenReturn(Optional.of(getClubMatch()));
		
		//When
		ClubMatch clubMatchDeleted = clubesMatchesService.deleteClubMatch(1L);
		
		//Then
		assertEquals(getClubMatch(), clubMatchDeleted);
		verify(clubesMatchesDao).findById(1L);
		verify(clubesMatchesDao).deleteById(1L);
	}

	@Test
	void testDeleteClubMatchFailed() {
		//Given
		when(clubesMatchesDao.findById(anyLong())).thenReturn(Optional.empty());
		
		//Then
		assertThrows(ClubMatchNotFoundException.class, () -> {
			//When
			clubesMatchesService.deleteClubMatch(1L);
		});
		
		verify(clubesMatchesDao).findById(1L);
		verify(clubesMatchesDao, never()).deleteById(1L);
	}

}
