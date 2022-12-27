package com.fernandobetancourt.services;

import static com.fernandobetancourt.Data.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fernandobetancourt.exceptions.AddingLineupMatchException;
import com.fernandobetancourt.exceptions.LineupMatchNotFoundException;
import com.fernandobetancourt.model.dao.ILineupsMatchesDao;
import com.fernandobetancourt.model.entity.LineupMatch;
import com.fernandobetancourt.model.entity.Match;
import com.fernandobetancourt.validators.LineupMatchValidator;

//@SpringBootTest(properties = "spring.main.lazy-initialization=true")
@SpringBootTest(classes = {LineupsMatchesServiceImpl.class, LineupMatchValidator.class})
@ExtendWith(SpringExtension.class)
class LineupsMatchesServiceImplTest {

	@MockBean
	private ILineupsMatchesDao lineupsMatchesDao;

	@Autowired
	private ILineupsMatchesService lineupsMatchesService;

	// GET

	@Test
	void testGetLineupMatchesByMatchSuccessed() {
		// Given
		when(lineupsMatchesDao.findByMatch(any(Match.class))).thenReturn(LINEUPMATCHES);

		// When
		List<LineupMatch> lineupMatches = lineupsMatchesService
				.getLineupMatchesByMatch(getMatchByIdBreakingReference(1L));

		// Then
		assertEquals(LINEUPMATCHES, lineupMatches);
		verify(lineupsMatchesDao).findByMatch(any(Match.class));
	}

	@Test
	void testGetLineupMatchesByMatchFailed() {
		// Given
		when(lineupsMatchesDao.findByMatch(any(Match.class))).thenReturn(Collections.emptyList());

		// Then
		assertThrows(LineupMatchNotFoundException.class, () -> {
			// When
			lineupsMatchesService.getLineupMatchesByMatch(getMatchByIdBreakingReference(1L));

		});

		verify(lineupsMatchesDao).findByMatch(any(Match.class));
	}

	// POST

	@Test
	void testAddLineupMatchSuccessed() {
		// Given
		when(lineupsMatchesDao.save(any(LineupMatch.class))).then(invocation -> {
			LineupMatch lineupMatch = invocation.getArgument(0);
			lineupMatch.setLineupMatchId(1L);
			return lineupMatch;
		});

		// When
		LineupMatch lineupMatchSaved = lineupsMatchesService.addLineupMatch(getLineupMatchWithoutId());

		// Then
		assertEquals(getLineupMatchLocal(), lineupMatchSaved);
		verify(lineupsMatchesDao).save(any(LineupMatch.class));
	}

	@Test
	void testAddLineupMatchFailedLineupMatchNull() {
		// Then
		assertThrows(AddingLineupMatchException.class, () -> {
			// When
			lineupsMatchesService.addLineupMatch(null);
		});

		verify(lineupsMatchesDao, never()).save(any(LineupMatch.class));
	}

	@Test
	void testAddLineupMatchFailedMatchNull() {
		// Given
		LineupMatch lineupMatchMatchNull = getLineupMatchWithoutId();
		lineupMatchMatchNull.setMatch(null);
		
		// Then
		assertThrows(AddingLineupMatchException.class, () -> {
			// When
			lineupsMatchesService.addLineupMatch(lineupMatchMatchNull);
		});

		verify(lineupsMatchesDao, never()).save(any(LineupMatch.class));
	}

	@Test
	void testAddLineupMatchFailedMatchIdNull() {
		// Given
		LineupMatch lineupMatchMatchIdNull = getLineupMatchWithoutId();
		lineupMatchMatchIdNull.setMatch(getMatchWithoutIdByPositionBreakingReference(0));
		
		// Then
		assertThrows(AddingLineupMatchException.class, () -> {
			// When
			lineupsMatchesService.addLineupMatch(lineupMatchMatchIdNull);
		});

		verify(lineupsMatchesDao, never()).save(any(LineupMatch.class));
	}

	@Test
	void testAddLineupMatchFailedMatchIdLessThan1() {
		// Given
		LineupMatch lineupMatchMatchIdLessThan1 = getLineupMatchWithoutId();
		lineupMatchMatchIdLessThan1.setMatch(MATCH_WITH_ID_LESS_THAN_1);
		
		// Then
		assertThrows(AddingLineupMatchException.class, () -> {
			// When
			lineupsMatchesService.addLineupMatch(lineupMatchMatchIdLessThan1);
		});

		verify(lineupsMatchesDao, never()).save(any(LineupMatch.class));
	}

	@Test
	void testAddLineupMatchFailedLineupNull() {
		// Given
		LineupMatch lineupMatchLineupNull = getLineupMatchWithoutId();
		lineupMatchLineupNull.setLineup(null);
		
		// Then
		assertThrows(AddingLineupMatchException.class, () -> {
			// When
			lineupsMatchesService.addLineupMatch(lineupMatchLineupNull);
		});

		verify(lineupsMatchesDao, never()).save(any(LineupMatch.class));
	}

	@Test
	void testAddLineupMatchFailedClubStatusNull() {
		// Given
		LineupMatch lineupMatchClubStatusNull = getLineupMatchWithoutId();
		lineupMatchClubStatusNull.setClubStatus(null);
		
		// Then
		assertThrows(AddingLineupMatchException.class, () -> {
			// When
			lineupsMatchesService.addLineupMatch(lineupMatchClubStatusNull);
		});

		verify(lineupsMatchesDao, never()).save(any(LineupMatch.class));
	}

	@Test
	void testAddLineupMatchFailedClubStatusEmptyString() {
		// Given
		LineupMatch lineupMatchClubStatusEmptyString = getLineupMatchWithoutId();
		lineupMatchClubStatusEmptyString.setClubStatus("");
		
		// Then
		assertThrows(AddingLineupMatchException.class, () -> {
			// When
			lineupsMatchesService.addLineupMatch(lineupMatchClubStatusEmptyString);
		});

		verify(lineupsMatchesDao, never()).save(any(LineupMatch.class));
	}

	// DELETE

	@Test
	void testDeleteLineupMatchSuccessed() {
		//Given
		when(lineupsMatchesDao.findById(anyLong())).thenReturn(Optional.of(getLineupMatchLocal()));
		
		//When
		LineupMatch lineupMatchDeleted = lineupsMatchesService.deleteLineupMatch(1L);
		
		//Then
		assertEquals(getLineupMatchLocal(), lineupMatchDeleted);
		verify(lineupsMatchesDao).findById(1L);
		verify(lineupsMatchesDao).deleteById(1L);
	}

	@Test
	void testDeleteLineupMatchFailed() {
		//Given
		when(lineupsMatchesDao.findById(anyLong())).thenReturn(Optional.empty());
		
		//Then
		assertThrows(LineupMatchNotFoundException.class, () -> {
			//When
			lineupsMatchesService.deleteLineupMatch(1L);
		});
		
		verify(lineupsMatchesDao).findById(anyLong());
		verify(lineupsMatchesDao, never()).deleteById(anyLong());
	}

}
