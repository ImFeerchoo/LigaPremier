package com.fernandobetancourt.services;

import static com.fernandobetancourt.Data.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.fernandobetancourt.exceptions.AddingMatchException;
import com.fernandobetancourt.exceptions.ClubMatchNotFoundException;
import com.fernandobetancourt.exceptions.ClubNotFoundException;
import com.fernandobetancourt.exceptions.JourneyNotFoundException;
import com.fernandobetancourt.exceptions.LineupMatchNotFoundException;
import com.fernandobetancourt.exceptions.MatchNotFoundException;
import com.fernandobetancourt.exceptions.ScoreboardNotFoundException;
import com.fernandobetancourt.model.dao.IMatchesDao;
import com.fernandobetancourt.model.entity.ClubMatch;
import com.fernandobetancourt.model.entity.ClubScoreboard;
import com.fernandobetancourt.model.entity.LineupMatch;
import com.fernandobetancourt.model.entity.Match;

@SpringBootTest
class MatchesServiceImplTest {

	@MockBean
	private IMatchesDao matchesDao;

	@MockBean
	private IScoreboardsService scoreboardsService;

	@MockBean
	private IJourneysService journeysService;

	@MockBean
	private IClubesScoreboardsService clubesScoreboardsService;

	@MockBean
	private IClubesService clubesService;

	@MockBean
	private ILineupsMatchesService lineupsMatchesService;

	@MockBean
	private IClubesMatchesService clubesMatchesService;

	@Autowired
	private IMatchesService matchesService;

	// GET

	@Test
	void testGetAllMatchesSuccessed() {
		// Given
		when(matchesDao.findAll()).thenReturn(MATCHES_WITH_ID);

		// When
		List<Match> matches = matchesService.getAllMatches();

		// Then
		assertEquals(MATCHES_WITH_ID, matches);
		verify(matchesDao).findAll();
	}

	@Test
	void testGetAllMatchesEmptyList() {
		// Given
		when(matchesDao.findAll()).thenReturn(Collections.emptyList());

		// Then
		assertThrows(MatchNotFoundException.class, () -> {
			// When
			matchesService.getAllMatches();
		});

		verify(matchesDao).findAll();
	}

	@Test
	void testGetMatchSuccessed() {
		// Given
		when(matchesDao.findById(anyLong())).then(invocation -> {
			return Optional.of(getMatchByIdBreakingReference(invocation.getArgument(0)));
		});

		// When
		Match match = matchesService.getMatch(1L);

		// Then
		assertEquals(getMatchByIdBreakingReference(1L), match);
		verify(matchesDao).findById(1L);
	}

	@Test
	void testGetMatchFailed() {
		// Given
		when(matchesDao.findById(anyLong())).thenReturn(Optional.empty());

		// Then
		assertThrows(MatchNotFoundException.class, () -> {
			// When
			matchesService.getMatch(1L);
		});

		verify(matchesDao).findById(1L);
	}

	// POST

	// Agregar el caso de fallo cuando el id de los 2 equipos es el mismo

	@Test
	void testAddMatchSuccessed() {
		// Given
		when(matchesDao.save(any(Match.class))).then(invocation -> {
			Match match = invocation.getArgument(0);
			match.getScoreboard().setScoreboardId(1L);
			match.setMatchId(1L);
			return match;
		});

		when(clubesService.getClubById(anyLong())).then(invocation -> {
			return getClubByIdBreakingReference(invocation.getArgument(0));
		});

		Match match = new Match("Estadio Azteca", "El Chiqui Drácula", LocalDateTime.now(),
				getJourneyByIdBreakingReference(1L), getScoreboadWithId());
		Match matchExpected = getMatchByIdBreakingReference(1L);

		// When
		Match matchSaved = matchesService.addMatch(match, 1L, 2L);

		// Then
		assertEquals(matchExpected.getMatchId(), matchSaved.getMatchId());
		assertEquals(matchExpected.getStadium(), matchSaved.getStadium());
		assertEquals(matchExpected.getReferee(), matchSaved.getReferee());
		assertEquals(matchExpected.getJourney(), matchSaved.getJourney());
		assertEquals(matchExpected.getScoreboard(), matchSaved.getScoreboard());

		assertEquals(matchExpected.getDate().getDayOfMonth(), matchSaved.getDate().getDayOfMonth());
		assertEquals(matchExpected.getDate().getHour(), matchSaved.getDate().getHour());
		assertEquals(matchExpected.getDate().getMinute(), matchSaved.getDate().getMinute());

		verify(journeysService).getJourney(anyLong());
		verify(clubesService, atLeast(2)).getClubById(anyLong()); // Cambiar el atLeast por times cuando regrese los
																	// clubes desde el validador
		verify(matchesDao).save(any(Match.class));
		verify(clubesScoreboardsService, times(2)).addClubScoreboard(any(ClubScoreboard.class));
		verify(lineupsMatchesService, times(2)).addLineupMatch(any(LineupMatch.class));
		verify(clubesMatchesService).addClubMatch(any(ClubMatch.class));
	}

	@Test
	void testAddMatchFailedMatchNull() {
		// Then
		assertThrows(AddingMatchException.class, () -> {
			// When
			matchesService.addMatch(null, 1L, 2L);
		});

		verify(journeysService, never()).getJourney(anyLong());
		verify(clubesService, never()).getClubById(anyLong());
		verify(matchesDao, never()).save(any(Match.class));
		verify(clubesScoreboardsService, never()).addClubScoreboard(any(ClubScoreboard.class));
		verify(lineupsMatchesService, never()).addLineupMatch(any(LineupMatch.class));
		verify(clubesMatchesService, never()).addClubMatch(any(ClubMatch.class));
	}

	@Test
	void testAddMatchFailedDateNull() {
		// Given
		Match matchDateNull = getMatchWithoutIdByPositionBreakingReference(0);
		matchDateNull.setDate(null);

		// Then
		assertThrows(AddingMatchException.class, () -> {
			// When
			matchesService.addMatch(matchDateNull, 1L, 2L);
		});

		verify(journeysService, never()).getJourney(anyLong());
		verify(clubesService, never()).getClubById(anyLong());
		verify(matchesDao, never()).save(any(Match.class));
		verify(clubesScoreboardsService, never()).addClubScoreboard(any(ClubScoreboard.class));
		verify(lineupsMatchesService, never()).addLineupMatch(any(LineupMatch.class));
		verify(clubesMatchesService, never()).addClubMatch(any(ClubMatch.class));
	}

	@Test
	void testAddMatchFailedStadiumNull() {
		// Given
		Match matchStadiumNull = getMatchWithoutIdByPositionBreakingReference(0);
		matchStadiumNull.setStadium(null);

		// Then
		assertThrows(AddingMatchException.class, () -> {
			// When
			matchesService.addMatch(matchStadiumNull, 1L, 2L);
		});

		verify(journeysService, never()).getJourney(anyLong());
		verify(clubesService, never()).getClubById(anyLong());
		verify(matchesDao, never()).save(any(Match.class));
		verify(clubesScoreboardsService, never()).addClubScoreboard(any(ClubScoreboard.class));
		verify(lineupsMatchesService, never()).addLineupMatch(any(LineupMatch.class));
		verify(clubesMatchesService, never()).addClubMatch(any(ClubMatch.class));
	}

	@Test
	void testAddMatchFailedStadiumEmptyString() {
		// Given
		Match matchStadiumEmptyString = getMatchWithoutIdByPositionBreakingReference(0);
		matchStadiumEmptyString.setStadium("");

		// Then
		assertThrows(AddingMatchException.class, () -> {
			// When
			matchesService.addMatch(matchStadiumEmptyString, 1L, 2L);
		});

		verify(journeysService, never()).getJourney(anyLong());
		verify(clubesService, never()).getClubById(anyLong());
		verify(matchesDao, never()).save(any(Match.class));
		verify(clubesScoreboardsService, never()).addClubScoreboard(any(ClubScoreboard.class));
		verify(lineupsMatchesService, never()).addLineupMatch(any(LineupMatch.class));
		verify(clubesMatchesService, never()).addClubMatch(any(ClubMatch.class));
	}

	@Test
	void testAddMatchFailedRefereeNull() {
		// Given
		Match matchRefereeNull = getMatchWithoutIdByPositionBreakingReference(0);
		matchRefereeNull.setReferee(null);

		// Then
		assertThrows(AddingMatchException.class, () -> {
			// When
			matchesService.addMatch(matchRefereeNull, 1L, 2L);
		});

		verify(journeysService, never()).getJourney(anyLong());
		verify(clubesService, never()).getClubById(anyLong());
		verify(matchesDao, never()).save(any(Match.class));
		verify(clubesScoreboardsService, never()).addClubScoreboard(any(ClubScoreboard.class));
		verify(lineupsMatchesService, never()).addLineupMatch(any(LineupMatch.class));
		verify(clubesMatchesService, never()).addClubMatch(any(ClubMatch.class));
	}

	@Test
	void testAddMatchFailedRefereeEmptyString() {
		// Given
		Match matchRefereeEmptyString = getMatchWithoutIdByPositionBreakingReference(0);
		matchRefereeEmptyString.setReferee("");

		// Then
		assertThrows(AddingMatchException.class, () -> {
			// When
			matchesService.addMatch(matchRefereeEmptyString, 1L, 2L);
		});

		verify(journeysService, never()).getJourney(anyLong());
		verify(clubesService, never()).getClubById(anyLong());
		verify(matchesDao, never()).save(any(Match.class));
		verify(clubesScoreboardsService, never()).addClubScoreboard(any(ClubScoreboard.class));
		verify(lineupsMatchesService, never()).addLineupMatch(any(LineupMatch.class));
		verify(clubesMatchesService, never()).addClubMatch(any(ClubMatch.class));
	}

	@Test
	void testAddMatchFailedJourneyNull() {
		// Given
		Match matchJourneyNull = getMatchWithoutIdByPositionBreakingReference(0);
		matchJourneyNull.setJourney(null);

		// Then
		assertThrows(AddingMatchException.class, () -> {
			// When
			matchesService.addMatch(matchJourneyNull, 1L, 2L);
		});

		verify(journeysService, never()).getJourney(anyLong());
		verify(clubesService, never()).getClubById(anyLong());
		verify(matchesDao, never()).save(any(Match.class));
		verify(clubesScoreboardsService, never()).addClubScoreboard(any(ClubScoreboard.class));
		verify(lineupsMatchesService, never()).addLineupMatch(any(LineupMatch.class));
		verify(clubesMatchesService, never()).addClubMatch(any(ClubMatch.class));
	}

	@Test
	void testAddMatchFailedJourneyIdNull() {
		// Given
		Match matchJourneyIdNull = getMatchWithoutIdByPositionBreakingReference(0);
		matchJourneyIdNull.setJourney(getJourneyWithoutIdByPositionBreakingReference(0));

		// Then
		assertThrows(AddingMatchException.class, () -> {
			// When
			matchesService.addMatch(matchJourneyIdNull, 1L, 2L);
		});

		verify(journeysService, never()).getJourney(anyLong());
		verify(clubesService, never()).getClubById(anyLong());
		verify(matchesDao, never()).save(any(Match.class));
		verify(clubesScoreboardsService, never()).addClubScoreboard(any(ClubScoreboard.class));
		verify(lineupsMatchesService, never()).addLineupMatch(any(LineupMatch.class));
		verify(clubesMatchesService, never()).addClubMatch(any(ClubMatch.class));
	}

	@Test
	void testAddMatchFailedJourneyIdLessThan1() {
		// Given
		Match matchJourneyIdLessThan1 = getMatchWithoutIdByPositionBreakingReference(0);
		matchJourneyIdLessThan1.setJourney(JOURNEY_WITH_ID_LESS_THAN_1);

		// Then
		assertThrows(AddingMatchException.class, () -> {
			// When
			matchesService.addMatch(matchJourneyIdLessThan1, 1L, 2L);
		});

		verify(journeysService, never()).getJourney(anyLong());
		verify(clubesService, never()).getClubById(anyLong());
		verify(matchesDao, never()).save(any(Match.class));
		verify(clubesScoreboardsService, never()).addClubScoreboard(any(ClubScoreboard.class));
		verify(lineupsMatchesService, never()).addLineupMatch(any(LineupMatch.class));
		verify(clubesMatchesService, never()).addClubMatch(any(ClubMatch.class));
	}

	@Test
	void testAddMatchFailedLocalClubIdNull() {
		// Given
		Match match = getMatchWithoutIdByPositionBreakingReference(0);

		// Then
		assertThrows(AddingMatchException.class, () -> {
			// When
			matchesService.addMatch(match, null, 2L);
		});

		verify(journeysService, never()).getJourney(anyLong());
		verify(clubesService, never()).getClubById(anyLong());
		verify(matchesDao, never()).save(any(Match.class));
		verify(clubesScoreboardsService, never()).addClubScoreboard(any(ClubScoreboard.class));
		verify(lineupsMatchesService, never()).addLineupMatch(any(LineupMatch.class));
		verify(clubesMatchesService, never()).addClubMatch(any(ClubMatch.class));
	}

	@Test
	void testAddMatchFailedLocalClubIdLessThan1() {
		// Given
		Match match = getMatchWithoutIdByPositionBreakingReference(0);

		// Then
		assertThrows(AddingMatchException.class, () -> {
			// When
			matchesService.addMatch(match, 0L, 2L);
		});

		verify(journeysService, never()).getJourney(anyLong());
		verify(clubesService, never()).getClubById(anyLong());
		verify(matchesDao, never()).save(any(Match.class));
		verify(clubesScoreboardsService, never()).addClubScoreboard(any(ClubScoreboard.class));
		verify(lineupsMatchesService, never()).addLineupMatch(any(LineupMatch.class));
		verify(clubesMatchesService, never()).addClubMatch(any(ClubMatch.class));
	}

	@Test
	void testAddMatchFailedVisitorClubIdNull() {
		// Given
		Match match = getMatchWithoutIdByPositionBreakingReference(0);

		// Then
		assertThrows(AddingMatchException.class, () -> {
			// When
			matchesService.addMatch(match, 1L, null);
		});

		verify(journeysService, never()).getJourney(anyLong());
		verify(clubesService, never()).getClubById(anyLong());
		verify(matchesDao, never()).save(any(Match.class));
		verify(clubesScoreboardsService, never()).addClubScoreboard(any(ClubScoreboard.class));
		verify(lineupsMatchesService, never()).addLineupMatch(any(LineupMatch.class));
		verify(clubesMatchesService, never()).addClubMatch(any(ClubMatch.class));
	}

	@Test
	void testAddMatchFailedVisitorClubIdLessThan1() {
		// Given
		Match match = getMatchWithoutIdByPositionBreakingReference(0);

		// Then
		assertThrows(AddingMatchException.class, () -> {
			// When
			matchesService.addMatch(match, 1L, 0L);
		});

		verify(journeysService, never()).getJourney(anyLong());
		verify(clubesService, never()).getClubById(anyLong());
		verify(matchesDao, never()).save(any(Match.class));
		verify(clubesScoreboardsService, never()).addClubScoreboard(any(ClubScoreboard.class));
		verify(lineupsMatchesService, never()).addLineupMatch(any(LineupMatch.class));
		verify(clubesMatchesService, never()).addClubMatch(any(ClubMatch.class));
	}

	@Test
	void testAddMatchFailedJourneyDoesNotExists() {
		// Given
		when(journeysService.getJourney(anyLong())).thenThrow(JourneyNotFoundException.class);
		Match match = getMatchWithoutIdByPositionBreakingReference(0);

		// Then
		assertThrows(JourneyNotFoundException.class, () -> {
			// When
			matchesService.addMatch(match, 1L, 2L);
		});

		verify(journeysService).getJourney(anyLong());
		verify(clubesService, never()).getClubById(anyLong());
		verify(matchesDao, never()).save(any(Match.class));
		verify(clubesScoreboardsService, never()).addClubScoreboard(any(ClubScoreboard.class));
		verify(lineupsMatchesService, never()).addLineupMatch(any(LineupMatch.class));
		verify(clubesMatchesService, never()).addClubMatch(any(ClubMatch.class));
	}

	@Test
	void testAddMatchFailedAnyClubDoesNotExists() {
		// Given
		when(clubesService.getClubById(anyLong())).thenThrow(ClubNotFoundException.class);
		Match match = getMatchWithoutIdByPositionBreakingReference(0);

		// Then
		assertThrows(ClubNotFoundException.class, () -> {
			// When
			matchesService.addMatch(match, 1L, 2L);
		});

		verify(journeysService).getJourney(anyLong());
		verify(clubesService).getClubById(anyLong());
		verify(matchesDao, never()).save(any(Match.class));
		verify(clubesScoreboardsService, never()).addClubScoreboard(any(ClubScoreboard.class));
		verify(lineupsMatchesService, never()).addLineupMatch(any(LineupMatch.class));
		verify(clubesMatchesService, never()).addClubMatch(any(ClubMatch.class));
	}

	@Test
	void testAddMatchFailedClubsIdsAreEqual() {
		// Given
		Match match = getMatchWithoutIdByPositionBreakingReference(0);

		// Then
		assertThrows(AddingMatchException.class, () -> {
			// When
			matchesService.addMatch(match, 1L, 1L);
		});

		verify(journeysService, never()).getJourney(anyLong());
		verify(clubesService, never()).getClubById(anyLong());
		verify(matchesDao, never()).save(any(Match.class));
		verify(clubesScoreboardsService, never()).addClubScoreboard(any(ClubScoreboard.class));
		verify(lineupsMatchesService, never()).addLineupMatch(any(LineupMatch.class));
		verify(clubesMatchesService, never()).addClubMatch(any(ClubMatch.class));
	}

	// PUT

	@Test
	void testUpdateMatchSuccessedDifferentClubes() {
		//Given
		when(clubesService.getClubById(anyLong())).then(invocation -> {
			return getClubByIdBreakingReference(invocation.getArgument(0));
		});
		
		when(clubesScoreboardsService.getClubScoreboardByScoreboardAndClubStatus(anyLong(), eq("Local"))).thenReturn(getClubScoreboardWithIdLocal()); //Club 1
		when(clubesScoreboardsService.getClubScoreboardByScoreboardAndClubStatus(anyLong(), eq("Visitor"))).thenReturn(getClubScoreboardWithIdVisitor()); //Club 2
		
		when(matchesDao.findById(anyLong())).thenReturn(Optional.of(getMatchByIdBreakingReference(1L)));
		
		when(lineupsMatchesService.getLineupMatchesByMatch(any(Match.class))).thenReturn(List.of(getLineupMatchLocal(), getLineupMatchVisitor()));
		
		when(clubesMatchesService.getClubMatchByMatch(any(Match.class))).thenReturn(getClubMatch());
		
		when(matchesDao.save(any(Match.class))).then(invocation -> invocation.getArgument(0));
		
		Match match = getMatchByIdBreakingReference(1L);
		match.setStadium("New Stadium");
		match.setReferee("Alejandro Fernández");
		match.setDate(LocalDateTime.of(2001, Month.AUGUST, 8, 8, 30, 55));
		match.setJourney(getJourneyByIdBreakingReference(7L));
		match.setScoreboard(getScoreboadWithId2());
		
		//When
		Match matchSaved = matchesService.updateMatch(match, 3L, 4L);
		
		//Then
		assertEquals(match, matchSaved);
		
		verify(clubesMatchesService).getClubMatchByMatch(any(Match.class));
		verify(clubesScoreboardsService).getClubScoreboardByScoreboardAndClubStatus(anyLong(), eq("Local"));
		verify(clubesScoreboardsService).getClubScoreboardByScoreboardAndClubStatus(anyLong(), eq("Visitor"));
		verify(lineupsMatchesService, times(2)).getLineupMatchesByMatch(any(Match.class));
		verify(lineupsMatchesService, times(2)).deleteLineupMatch(anyLong());
		verify(clubesScoreboardsService, times(2)).updateClubScoreboard(any(ClubScoreboard.class));
		verify(lineupsMatchesService, times(2)).addLineupMatch(any(LineupMatch.class));
		verify(clubesMatchesService).updateClubMatch(any(ClubMatch.class));
		verify(matchesDao).save(any(Match.class));
	}
	
	@Test
	void testUpdateMatchSuccessedSameClubes() {
		//Given
		when(clubesService.getClubById(anyLong())).then(invocation -> {
			return getClubByIdBreakingReference(invocation.getArgument(0));
		});
		
		when(clubesScoreboardsService.getClubScoreboardByScoreboardAndClubStatus(anyLong(), eq("Local"))).thenReturn(getClubScoreboardWithIdLocal()); //Club 1
		when(clubesScoreboardsService.getClubScoreboardByScoreboardAndClubStatus(anyLong(), eq("Visitor"))).thenReturn(getClubScoreboardWithIdVisitor()); //Club 2
		
		when(matchesDao.findById(anyLong())).thenReturn(Optional.of(getMatchByIdBreakingReference(1L)));
		
		when(lineupsMatchesService.getLineupMatchesByMatch(any(Match.class))).thenReturn(List.of(getLineupMatchLocal(), getLineupMatchVisitor()));
		
		when(clubesMatchesService.getClubMatchByMatch(any(Match.class))).thenReturn(getClubMatch());
		
		when(matchesDao.save(any(Match.class))).then(invocation -> invocation.getArgument(0));
		
		Match match = getMatchByIdBreakingReference(1L);
		match.setStadium("New Stadium");
		match.setReferee("Alejandro Fernández");
		match.setDate(LocalDateTime.of(2001, Month.AUGUST, 8, 8, 30, 55));
		match.setJourney(getJourneyByIdBreakingReference(7L));
		match.setScoreboard(getScoreboadWithId2());
		
		//When
		Match matchSaved = matchesService.updateMatch(match, 1L, 2L);
		
		//Then
		assertEquals(match, matchSaved);
		
		verify(clubesMatchesService).getClubMatchByMatch(any(Match.class));
		verify(clubesScoreboardsService).getClubScoreboardByScoreboardAndClubStatus(anyLong(), eq("Local"));
		verify(clubesScoreboardsService).getClubScoreboardByScoreboardAndClubStatus(anyLong(), eq("Visitor"));
		verify(lineupsMatchesService, never()).getLineupMatchesByMatch(any(Match.class));
		verify(lineupsMatchesService, never()).deleteLineupMatch(anyLong());
		verify(clubesScoreboardsService, never()).updateClubScoreboard(any(ClubScoreboard.class));
		verify(lineupsMatchesService, never()).addLineupMatch(any(LineupMatch.class));
		verify(clubesMatchesService, never()).updateClubMatch(any(ClubMatch.class));
		verify(matchesDao).save(any(Match.class));
	}

	@Test
	void testUpdateMatchFailedMatchNull() {
		//Then
		assertThrows(AddingMatchException.class, () -> {
			//When
			matchesService.updateMatch(null, 1L, 2L);
		});
		
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(clubesScoreboardsService, never()).getClubScoreboardByScoreboardAndClubStatus(anyLong(), eq("Local"));
		verify(clubesScoreboardsService, never()).getClubScoreboardByScoreboardAndClubStatus(anyLong(), eq("Visitor"));
		verify(lineupsMatchesService, never()).getLineupMatchesByMatch(any(Match.class));
		verify(lineupsMatchesService, never()).deleteLineupMatch(anyLong());
		verify(clubesScoreboardsService, never()).updateClubScoreboard(any(ClubScoreboard.class));
		verify(lineupsMatchesService, never()).addLineupMatch(any(LineupMatch.class));
		verify(clubesMatchesService, never()).updateClubMatch(any(ClubMatch.class));
		verify(matchesDao, never()).save(any(Match.class));
	}

	@Test
	void testUpdateMatchFailedMatchIdNull() {
		//Then
		assertThrows(AddingMatchException.class, () -> {
			//When
			matchesService.updateMatch(getMatchWithoutIdByPositionBreakingReference(0), 1L, 2L);
		});
		
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(clubesScoreboardsService, never()).getClubScoreboardByScoreboardAndClubStatus(anyLong(), eq("Local"));
		verify(clubesScoreboardsService, never()).getClubScoreboardByScoreboardAndClubStatus(anyLong(), eq("Visitor"));
		verify(lineupsMatchesService, never()).getLineupMatchesByMatch(any(Match.class));
		verify(lineupsMatchesService, never()).deleteLineupMatch(anyLong());
		verify(clubesScoreboardsService, never()).updateClubScoreboard(any(ClubScoreboard.class));
		verify(lineupsMatchesService, never()).addLineupMatch(any(LineupMatch.class));
		verify(clubesMatchesService, never()).updateClubMatch(any(ClubMatch.class));
		verify(matchesDao, never()).save(any(Match.class));
	}

	@Test
	void testUpdateMatchFailedMatchIdLessThan1() {
		//Then
		assertThrows(AddingMatchException.class, () -> {
			//When
			matchesService.updateMatch(MATCH_WITH_ID_LESS_THAN_1, 1L, 2L);
		});
		
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(clubesScoreboardsService, never()).getClubScoreboardByScoreboardAndClubStatus(anyLong(), eq("Local"));
		verify(clubesScoreboardsService, never()).getClubScoreboardByScoreboardAndClubStatus(anyLong(), eq("Visitor"));
		verify(lineupsMatchesService, never()).getLineupMatchesByMatch(any(Match.class));
		verify(lineupsMatchesService, never()).deleteLineupMatch(anyLong());
		verify(clubesScoreboardsService, never()).updateClubScoreboard(any(ClubScoreboard.class));
		verify(lineupsMatchesService, never()).addLineupMatch(any(LineupMatch.class));
		verify(clubesMatchesService, never()).updateClubMatch(any(ClubMatch.class));
		verify(matchesDao, never()).save(any(Match.class));
	}

	@Test
	void testUpdateMatchFailedDateNull() {
		//Given
		Match matchDateNull = getMatchByIdBreakingReference(1L);
		matchDateNull.setDate(null);
		
		//Then
		assertThrows(AddingMatchException.class, () -> {
			//When
			matchesService.updateMatch(matchDateNull, 1L, 2L);
		});
		
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(clubesScoreboardsService, never()).getClubScoreboardByScoreboardAndClubStatus(anyLong(), eq("Local"));
		verify(clubesScoreboardsService, never()).getClubScoreboardByScoreboardAndClubStatus(anyLong(), eq("Visitor"));
		verify(lineupsMatchesService, never()).getLineupMatchesByMatch(any(Match.class));
		verify(lineupsMatchesService, never()).deleteLineupMatch(anyLong());
		verify(clubesScoreboardsService, never()).updateClubScoreboard(any(ClubScoreboard.class));
		verify(lineupsMatchesService, never()).addLineupMatch(any(LineupMatch.class));
		verify(clubesMatchesService, never()).updateClubMatch(any(ClubMatch.class));
		verify(matchesDao, never()).save(any(Match.class));
	}

	@Test
	void testUpdateMatchFailedStadiumNull() {
		//Given
		Match matchStadiumNull = getMatchByIdBreakingReference(1L);
		matchStadiumNull.setStadium(null);
		
		//Then
		assertThrows(AddingMatchException.class, () -> {
			//When
			matchesService.updateMatch(matchStadiumNull, 1L, 2L);
		});
		
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(clubesScoreboardsService, never()).getClubScoreboardByScoreboardAndClubStatus(anyLong(), eq("Local"));
		verify(clubesScoreboardsService, never()).getClubScoreboardByScoreboardAndClubStatus(anyLong(), eq("Visitor"));
		verify(lineupsMatchesService, never()).getLineupMatchesByMatch(any(Match.class));
		verify(lineupsMatchesService, never()).deleteLineupMatch(anyLong());
		verify(clubesScoreboardsService, never()).updateClubScoreboard(any(ClubScoreboard.class));
		verify(lineupsMatchesService, never()).addLineupMatch(any(LineupMatch.class));
		verify(clubesMatchesService, never()).updateClubMatch(any(ClubMatch.class));
		verify(matchesDao, never()).save(any(Match.class));
	}

	@Test
	void testUpdateMatchFailedStadiumEmptyString() {
		//Given
		Match matchStadiumEmptyuString = getMatchByIdBreakingReference(1L);
		matchStadiumEmptyuString.setStadium("");
		
		//Then
		assertThrows(AddingMatchException.class, () -> {
			//When
			matchesService.updateMatch(matchStadiumEmptyuString, 1L, 2L);
		});
		
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(clubesScoreboardsService, never()).getClubScoreboardByScoreboardAndClubStatus(anyLong(), eq("Local"));
		verify(clubesScoreboardsService, never()).getClubScoreboardByScoreboardAndClubStatus(anyLong(), eq("Visitor"));
		verify(lineupsMatchesService, never()).getLineupMatchesByMatch(any(Match.class));
		verify(lineupsMatchesService, never()).deleteLineupMatch(anyLong());
		verify(clubesScoreboardsService, never()).updateClubScoreboard(any(ClubScoreboard.class));
		verify(lineupsMatchesService, never()).addLineupMatch(any(LineupMatch.class));
		verify(clubesMatchesService, never()).updateClubMatch(any(ClubMatch.class));
		verify(matchesDao, never()).save(any(Match.class));
	}

	@Test
	void testUpdateMatchFailedRefereeNull() {
		//Given
		Match matchRefereeNull = getMatchByIdBreakingReference(1L);
		matchRefereeNull.setReferee(null);
		
		//Then
		assertThrows(AddingMatchException.class, () -> {
			//When
			matchesService.updateMatch(matchRefereeNull, 1L, 2L);
		});
		
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(clubesScoreboardsService, never()).getClubScoreboardByScoreboardAndClubStatus(anyLong(), eq("Local"));
		verify(clubesScoreboardsService, never()).getClubScoreboardByScoreboardAndClubStatus(anyLong(), eq("Visitor"));
		verify(lineupsMatchesService, never()).getLineupMatchesByMatch(any(Match.class));
		verify(lineupsMatchesService, never()).deleteLineupMatch(anyLong());
		verify(clubesScoreboardsService, never()).updateClubScoreboard(any(ClubScoreboard.class));
		verify(lineupsMatchesService, never()).addLineupMatch(any(LineupMatch.class));
		verify(clubesMatchesService, never()).updateClubMatch(any(ClubMatch.class));
		verify(matchesDao, never()).save(any(Match.class));
	}

	@Test
	void testUpdateMatchFailedRefereeEmptyString() {
		//Given
		Match matchRefereeEmptyString = getMatchByIdBreakingReference(1L);
		matchRefereeEmptyString.setReferee("");
		
		//Then
		assertThrows(AddingMatchException.class, () -> {
			//When
			matchesService.updateMatch(matchRefereeEmptyString, 1L, 2L);
		});
		
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(clubesScoreboardsService, never()).getClubScoreboardByScoreboardAndClubStatus(anyLong(), eq("Local"));
		verify(clubesScoreboardsService, never()).getClubScoreboardByScoreboardAndClubStatus(anyLong(), eq("Visitor"));
		verify(lineupsMatchesService, never()).getLineupMatchesByMatch(any(Match.class));
		verify(lineupsMatchesService, never()).deleteLineupMatch(anyLong());
		verify(clubesScoreboardsService, never()).updateClubScoreboard(any(ClubScoreboard.class));
		verify(lineupsMatchesService, never()).addLineupMatch(any(LineupMatch.class));
		verify(clubesMatchesService, never()).updateClubMatch(any(ClubMatch.class));
		verify(matchesDao, never()).save(any(Match.class));
	}

	@Test
	void testUpdateMatchFailedJourneyNull() {
		//Given
		Match matchJourneyNull = getMatchByIdBreakingReference(1L);
		matchJourneyNull.setJourney(null);
		
		//Then
		assertThrows(AddingMatchException.class, () -> {
			//When
			matchesService.updateMatch(matchJourneyNull, 1L, 2L);
		});
		
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(clubesScoreboardsService, never()).getClubScoreboardByScoreboardAndClubStatus(anyLong(), eq("Local"));
		verify(clubesScoreboardsService, never()).getClubScoreboardByScoreboardAndClubStatus(anyLong(), eq("Visitor"));
		verify(lineupsMatchesService, never()).getLineupMatchesByMatch(any(Match.class));
		verify(lineupsMatchesService, never()).deleteLineupMatch(anyLong());
		verify(clubesScoreboardsService, never()).updateClubScoreboard(any(ClubScoreboard.class));
		verify(lineupsMatchesService, never()).addLineupMatch(any(LineupMatch.class));
		verify(clubesMatchesService, never()).updateClubMatch(any(ClubMatch.class));
		verify(matchesDao, never()).save(any(Match.class));
	}

	@Test
	void testUpdateMatchFailedJourneyIdNull() {
		//Given
		Match matchJourneyIdNull = getMatchByIdBreakingReference(1L);
		matchJourneyIdNull.setJourney(getJourneyWithoutIdByPositionBreakingReference(0));
		
		//Then
		assertThrows(AddingMatchException.class, () -> {
			//When
			matchesService.updateMatch(matchJourneyIdNull, 1L, 2L);
		});
		
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(clubesScoreboardsService, never()).getClubScoreboardByScoreboardAndClubStatus(anyLong(), eq("Local"));
		verify(clubesScoreboardsService, never()).getClubScoreboardByScoreboardAndClubStatus(anyLong(), eq("Visitor"));
		verify(lineupsMatchesService, never()).getLineupMatchesByMatch(any(Match.class));
		verify(lineupsMatchesService, never()).deleteLineupMatch(anyLong());
		verify(clubesScoreboardsService, never()).updateClubScoreboard(any(ClubScoreboard.class));
		verify(lineupsMatchesService, never()).addLineupMatch(any(LineupMatch.class));
		verify(clubesMatchesService, never()).updateClubMatch(any(ClubMatch.class));
		verify(matchesDao, never()).save(any(Match.class));
	}

	@Test
	void testUpdateMatchFailedJourneyIdLessThan1() {
		//Given
		Match matchJourneyIdLessThan1 = getMatchByIdBreakingReference(1L);
		matchJourneyIdLessThan1.setJourney(JOURNEY_WITH_ID_LESS_THAN_1);
		
		//Then
		assertThrows(AddingMatchException.class, () -> {
			//When
			matchesService.updateMatch(matchJourneyIdLessThan1, 1L, 2L);
		});
		
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(clubesScoreboardsService, never()).getClubScoreboardByScoreboardAndClubStatus(anyLong(), eq("Local"));
		verify(clubesScoreboardsService, never()).getClubScoreboardByScoreboardAndClubStatus(anyLong(), eq("Visitor"));
		verify(lineupsMatchesService, never()).getLineupMatchesByMatch(any(Match.class));
		verify(lineupsMatchesService, never()).deleteLineupMatch(anyLong());
		verify(clubesScoreboardsService, never()).updateClubScoreboard(any(ClubScoreboard.class));
		verify(lineupsMatchesService, never()).addLineupMatch(any(LineupMatch.class));
		verify(clubesMatchesService, never()).updateClubMatch(any(ClubMatch.class));
		verify(matchesDao, never()).save(any(Match.class));
	}

	@Test
	void testUpdateMatchFailedScoreboardNull() {
		//Given
		Match matchScoreboardNull = getMatchByIdBreakingReference(1L);
		matchScoreboardNull.setScoreboard(null);
		
		//Then
		assertThrows(AddingMatchException.class, () -> {
			//When
			matchesService.updateMatch(matchScoreboardNull, 1L, 2L);
		});
		
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(clubesScoreboardsService, never()).getClubScoreboardByScoreboardAndClubStatus(anyLong(), eq("Local"));
		verify(clubesScoreboardsService, never()).getClubScoreboardByScoreboardAndClubStatus(anyLong(), eq("Visitor"));
		verify(lineupsMatchesService, never()).getLineupMatchesByMatch(any(Match.class));
		verify(lineupsMatchesService, never()).deleteLineupMatch(anyLong());
		verify(clubesScoreboardsService, never()).updateClubScoreboard(any(ClubScoreboard.class));
		verify(lineupsMatchesService, never()).addLineupMatch(any(LineupMatch.class));
		verify(clubesMatchesService, never()).updateClubMatch(any(ClubMatch.class));
		verify(matchesDao, never()).save(any(Match.class));
	}

	@Test
	void testUpdateMatchFailedScoreboardIdNull() {
		//Given
		Match matchScoreboardIdNull = getMatchByIdBreakingReference(1L);
		matchScoreboardIdNull.setScoreboard(getScoreboadWithoutId());
		
		//Then
		assertThrows(AddingMatchException.class, () -> {
			//When
			matchesService.updateMatch(matchScoreboardIdNull, 1L, 2L);
		});
		
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(clubesScoreboardsService, never()).getClubScoreboardByScoreboardAndClubStatus(anyLong(), eq("Local"));
		verify(clubesScoreboardsService, never()).getClubScoreboardByScoreboardAndClubStatus(anyLong(), eq("Visitor"));
		verify(lineupsMatchesService, never()).getLineupMatchesByMatch(any(Match.class));
		verify(lineupsMatchesService, never()).deleteLineupMatch(anyLong());
		verify(clubesScoreboardsService, never()).updateClubScoreboard(any(ClubScoreboard.class));
		verify(lineupsMatchesService, never()).addLineupMatch(any(LineupMatch.class));
		verify(clubesMatchesService, never()).updateClubMatch(any(ClubMatch.class));
		verify(matchesDao, never()).save(any(Match.class));
	}

	@Test
	void testUpdateMatchFailedScoreboardIdLessThan1() {
		//Given
		Match matchScoreboardIdLessThan1 = getMatchByIdBreakingReference(1L);
		matchScoreboardIdLessThan1.setScoreboard(getScoreboadWithIdLessThan1());
		
		//Then
		assertThrows(AddingMatchException.class, () -> {
			//When
			matchesService.updateMatch(matchScoreboardIdLessThan1, 1L, 2L);
		});
		
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(clubesScoreboardsService, never()).getClubScoreboardByScoreboardAndClubStatus(anyLong(), eq("Local"));
		verify(clubesScoreboardsService, never()).getClubScoreboardByScoreboardAndClubStatus(anyLong(), eq("Visitor"));
		verify(lineupsMatchesService, never()).getLineupMatchesByMatch(any(Match.class));
		verify(lineupsMatchesService, never()).deleteLineupMatch(anyLong());
		verify(clubesScoreboardsService, never()).updateClubScoreboard(any(ClubScoreboard.class));
		verify(lineupsMatchesService, never()).addLineupMatch(any(LineupMatch.class));
		verify(clubesMatchesService, never()).updateClubMatch(any(ClubMatch.class));
		verify(matchesDao, never()).save(any(Match.class));
	}

	@Test
	void testUpdateMatchFailedLocalClubIdNull() {
		//Given
		Match match = getMatchByIdBreakingReference(1L);
		
		//Then
		assertThrows(AddingMatchException.class, () -> {
			//When
			matchesService.updateMatch(match, null, 2L);
		});
		
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(clubesScoreboardsService, never()).getClubScoreboardByScoreboardAndClubStatus(anyLong(), eq("Local"));
		verify(clubesScoreboardsService, never()).getClubScoreboardByScoreboardAndClubStatus(anyLong(), eq("Visitor"));
		verify(lineupsMatchesService, never()).getLineupMatchesByMatch(any(Match.class));
		verify(lineupsMatchesService, never()).deleteLineupMatch(anyLong());
		verify(clubesScoreboardsService, never()).updateClubScoreboard(any(ClubScoreboard.class));
		verify(lineupsMatchesService, never()).addLineupMatch(any(LineupMatch.class));
		verify(clubesMatchesService, never()).updateClubMatch(any(ClubMatch.class));
		verify(matchesDao, never()).save(any(Match.class));
	}

	@Test
	void testUpdateMatchFailedLocalClubIdLessThan1() {
		//Given
		Match match = getMatchByIdBreakingReference(1L);
		
		//Then
		assertThrows(AddingMatchException.class, () -> {
			//When
			matchesService.updateMatch(match, 0L, 2L);
		});
		
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(clubesScoreboardsService, never()).getClubScoreboardByScoreboardAndClubStatus(anyLong(), eq("Local"));
		verify(clubesScoreboardsService, never()).getClubScoreboardByScoreboardAndClubStatus(anyLong(), eq("Visitor"));
		verify(lineupsMatchesService, never()).getLineupMatchesByMatch(any(Match.class));
		verify(lineupsMatchesService, never()).deleteLineupMatch(anyLong());
		verify(clubesScoreboardsService, never()).updateClubScoreboard(any(ClubScoreboard.class));
		verify(lineupsMatchesService, never()).addLineupMatch(any(LineupMatch.class));
		verify(clubesMatchesService, never()).updateClubMatch(any(ClubMatch.class));
		verify(matchesDao, never()).save(any(Match.class));
	}

	@Test
	void testUpdateMatchFailedVisitorClubIdNull() {
		//Given
		Match match = getMatchByIdBreakingReference(1L);
		
		//Then
		assertThrows(AddingMatchException.class, () -> {
			//When
			matchesService.updateMatch(match, 1L, null);
		});
		
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(clubesScoreboardsService, never()).getClubScoreboardByScoreboardAndClubStatus(anyLong(), eq("Local"));
		verify(clubesScoreboardsService, never()).getClubScoreboardByScoreboardAndClubStatus(anyLong(), eq("Visitor"));
		verify(lineupsMatchesService, never()).getLineupMatchesByMatch(any(Match.class));
		verify(lineupsMatchesService, never()).deleteLineupMatch(anyLong());
		verify(clubesScoreboardsService, never()).updateClubScoreboard(any(ClubScoreboard.class));
		verify(lineupsMatchesService, never()).addLineupMatch(any(LineupMatch.class));
		verify(clubesMatchesService, never()).updateClubMatch(any(ClubMatch.class));
		verify(matchesDao, never()).save(any(Match.class));
	}

	@Test
	void testUpdateMatchFailedVisitorClubIdLessThan1() {
		//Given
		Match match = getMatchByIdBreakingReference(1L);
		
		//Then
		assertThrows(AddingMatchException.class, () -> {
			//When
			matchesService.updateMatch(match, 1L, 0L);
		});
		
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(clubesScoreboardsService, never()).getClubScoreboardByScoreboardAndClubStatus(anyLong(), eq("Local"));
		verify(clubesScoreboardsService, never()).getClubScoreboardByScoreboardAndClubStatus(anyLong(), eq("Visitor"));
		verify(lineupsMatchesService, never()).getLineupMatchesByMatch(any(Match.class));
		verify(lineupsMatchesService, never()).deleteLineupMatch(anyLong());
		verify(clubesScoreboardsService, never()).updateClubScoreboard(any(ClubScoreboard.class));
		verify(lineupsMatchesService, never()).addLineupMatch(any(LineupMatch.class));
		verify(clubesMatchesService, never()).updateClubMatch(any(ClubMatch.class));
		verify(matchesDao, never()).save(any(Match.class));
	}

	@Test
	void testUpdateMatchFailedClubsIdsAreEqual() {
		//Given
		Match match = getMatchByIdBreakingReference(1L);
		
		//Then
		assertThrows(AddingMatchException.class, () -> {
			//When
			matchesService.updateMatch(match, 1L, 1L);
		});
		
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(clubesScoreboardsService, never()).getClubScoreboardByScoreboardAndClubStatus(anyLong(), eq("Local"));
		verify(clubesScoreboardsService, never()).getClubScoreboardByScoreboardAndClubStatus(anyLong(), eq("Visitor"));
		verify(lineupsMatchesService, never()).getLineupMatchesByMatch(any(Match.class));
		verify(lineupsMatchesService, never()).deleteLineupMatch(anyLong());
		verify(clubesScoreboardsService, never()).updateClubScoreboard(any(ClubScoreboard.class));
		verify(lineupsMatchesService, never()).addLineupMatch(any(LineupMatch.class));
		verify(clubesMatchesService, never()).updateClubMatch(any(ClubMatch.class));
		verify(matchesDao, never()).save(any(Match.class));
	}

	@Test
	void testUpdateMatchFailedScoreboardDoesNotExists() {
		//Given
		when(matchesDao.findById(anyLong())).thenReturn(Optional.of(getMatchByIdBreakingReference(1L)));
		when(scoreboardsService.getScoreboard(anyLong())).thenThrow(ScoreboardNotFoundException.class);
		Match match = getMatchByIdBreakingReference(1L);
		
		//Then
		assertThrows(ScoreboardNotFoundException.class, () -> {
			//When
			matchesService.updateMatch(match, 1L, 2L);
		});
		
		verify(matchesDao).findById(anyLong());
		verify(scoreboardsService).getScoreboard(anyLong());
		
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(clubesScoreboardsService, never()).getClubScoreboardByScoreboardAndClubStatus(anyLong(), eq("Local"));
		verify(clubesScoreboardsService, never()).getClubScoreboardByScoreboardAndClubStatus(anyLong(), eq("Visitor"));
		verify(lineupsMatchesService, never()).getLineupMatchesByMatch(any(Match.class));
		verify(lineupsMatchesService, never()).deleteLineupMatch(anyLong());
		verify(clubesScoreboardsService, never()).updateClubScoreboard(any(ClubScoreboard.class));
		verify(lineupsMatchesService, never()).addLineupMatch(any(LineupMatch.class));
		verify(clubesMatchesService, never()).updateClubMatch(any(ClubMatch.class));
		verify(matchesDao, never()).save(any(Match.class));
	}

	@Test
	void testUpdateMatchFailedJorneyDoesNotExists() {
		//Given
		when(matchesDao.findById(anyLong())).thenReturn(Optional.of(getMatchByIdBreakingReference(1L)));
		when(journeysService.getJourney(anyLong())).thenThrow(JourneyNotFoundException.class);
		Match match = getMatchByIdBreakingReference(1L);
		
		//Then
		assertThrows(JourneyNotFoundException.class, () -> {
			//When
			matchesService.updateMatch(match, 1L, 2L);
		});
		
		verify(matchesDao).findById(anyLong());
		verify(scoreboardsService).getScoreboard(anyLong());
		verify(journeysService).getJourney(anyLong());
		
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(clubesScoreboardsService, never()).getClubScoreboardByScoreboardAndClubStatus(anyLong(), eq("Local"));
		verify(clubesScoreboardsService, never()).getClubScoreboardByScoreboardAndClubStatus(anyLong(), eq("Visitor"));
		verify(lineupsMatchesService, never()).getLineupMatchesByMatch(any(Match.class));
		verify(lineupsMatchesService, never()).deleteLineupMatch(anyLong());
		verify(clubesScoreboardsService, never()).updateClubScoreboard(any(ClubScoreboard.class));
		verify(lineupsMatchesService, never()).addLineupMatch(any(LineupMatch.class));
		verify(clubesMatchesService, never()).updateClubMatch(any(ClubMatch.class));
		verify(matchesDao, never()).save(any(Match.class));
	}

	@Test
	void testUpdateMatchFailedAnyClubDoesNotExists() {
		//Given
		when(matchesDao.findById(anyLong())).thenReturn(Optional.of(getMatchByIdBreakingReference(1L)));
		when(clubesService.getClubById(anyLong())).thenThrow(ClubNotFoundException.class);
		Match match = getMatchByIdBreakingReference(1L);
		
		//Then
		assertThrows(ClubNotFoundException.class, () -> {
			//When
			matchesService.updateMatch(match, 1L, 2L);
		});
		
		verify(matchesDao).findById(anyLong());
		verify(scoreboardsService).getScoreboard(anyLong());
		verify(journeysService).getJourney(anyLong());
		verify(clubesService).getClubById(anyLong());
		
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(clubesScoreboardsService, never()).getClubScoreboardByScoreboardAndClubStatus(anyLong(), eq("Local"));
		verify(clubesScoreboardsService, never()).getClubScoreboardByScoreboardAndClubStatus(anyLong(), eq("Visitor"));
		verify(lineupsMatchesService, never()).getLineupMatchesByMatch(any(Match.class));
		verify(lineupsMatchesService, never()).deleteLineupMatch(anyLong());
		verify(clubesScoreboardsService, never()).updateClubScoreboard(any(ClubScoreboard.class));
		verify(lineupsMatchesService, never()).addLineupMatch(any(LineupMatch.class));
		verify(clubesMatchesService, never()).updateClubMatch(any(ClubMatch.class));
		verify(matchesDao, never()).save(any(Match.class));
	}

	@Test
	void testUpdateMatchFailedMatchDoesNotExists() {
		//Given
		when(matchesDao.findById(anyLong())).thenReturn(Optional.empty());
		when(clubesService.getClubById(anyLong())).thenThrow(ClubNotFoundException.class);
		Match match = getMatchByIdBreakingReference(1L);
		
		//Then
		assertThrows(MatchNotFoundException.class, () -> {
			//When
			matchesService.updateMatch(match, 1L, 2L);
		});
		
		verify(matchesDao).findById(anyLong());
		verify(scoreboardsService, never()).getScoreboard(anyLong());
		verify(journeysService, never()).getJourney(anyLong());
		verify(clubesService, never()).getClubById(anyLong());
		
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(clubesScoreboardsService, never()).getClubScoreboardByScoreboardAndClubStatus(anyLong(), eq("Local"));
		verify(clubesScoreboardsService, never()).getClubScoreboardByScoreboardAndClubStatus(anyLong(), eq("Visitor"));
		verify(lineupsMatchesService, never()).getLineupMatchesByMatch(any(Match.class));
		verify(lineupsMatchesService, never()).deleteLineupMatch(anyLong());
		verify(clubesScoreboardsService, never()).updateClubScoreboard(any(ClubScoreboard.class));
		verify(lineupsMatchesService, never()).addLineupMatch(any(LineupMatch.class));
		verify(clubesMatchesService, never()).updateClubMatch(any(ClubMatch.class));
		verify(matchesDao, never()).save(any(Match.class));
	}

	// DELETE

	@Test
	void testDeleteMatchSuccessed() {
		//Given
		when(matchesDao.findById(anyLong())).then(invocation -> {
			return Optional.of(getMatchByIdBreakingReference(invocation.getArgument(0)));
		});
		
		when(clubesMatchesService.getClubMatchByMatch(any(Match.class))).thenReturn(getClubMatch());
		
		when(lineupsMatchesService.getLineupMatchesByMatch(any(Match.class))).thenReturn(LINEUPMATCHES);
		
		Match match = getMatchByIdBreakingReference(1L);
		
		//When
		Match matchDeleted = matchesService.deleteMatch(1L);
		
		//Then
		assertEquals(match, matchDeleted);
		verify(matchesDao).findById(1L);
		verify(clubesMatchesService).getClubMatchByMatch(any(Match.class));
		verify(clubesMatchesService).deleteClubMatch(anyLong());
		verify(lineupsMatchesService).getLineupMatchesByMatch(any(Match.class));
		verify(lineupsMatchesService, times(2)).deleteLineupMatch(anyLong());
		verify(matchesDao).deleteById(1L);
	}
	
	@Test
	void testDeleteMatchFailedMatchDoesNotExists() {
		//Given
		when(matchesDao.findById(anyLong())).thenReturn(Optional.empty());
		
		//Then
		assertThrows(MatchNotFoundException.class, () -> {
			//When
			matchesService.deleteMatch(1L);
		});
		
		verify(matchesDao).findById(anyLong());
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(clubesMatchesService, never()).deleteClubMatch(anyLong());
		verify(lineupsMatchesService, never()).getLineupMatchesByMatch(any(Match.class));
		verify(lineupsMatchesService, never()).deleteLineupMatch(anyLong());
		verify(matchesDao, never()).deleteById(anyLong());
	}
	
	@Test
	void testDeleteMatchFailedClubMatchDoesNotExists() {
		//Given
		when(matchesDao.findById(anyLong())).then(invocation -> {
			return Optional.of(getMatchByIdBreakingReference(invocation.getArgument(0)));
		});
		
		when(clubesMatchesService.getClubMatchByMatch(any(Match.class))).thenThrow(ClubMatchNotFoundException.class);
		
		when(lineupsMatchesService.getLineupMatchesByMatch(any(Match.class))).thenReturn(LINEUPMATCHES);
		
		Match match = getMatchByIdBreakingReference(1L);
		
		//When
		Match matchDeleted = matchesService.deleteMatch(1L);
		
		//Then
		assertEquals(match, matchDeleted);
		verify(matchesDao).findById(1L);
		verify(clubesMatchesService).getClubMatchByMatch(any(Match.class));
		verify(clubesMatchesService, never()).deleteClubMatch(anyLong());
		verify(lineupsMatchesService).getLineupMatchesByMatch(any(Match.class));
		verify(lineupsMatchesService, times(2)).deleteLineupMatch(anyLong());
		verify(matchesDao).deleteById(1L);
	}
	
	@Test
	void testDeleteMatchFailedLineupsMatchesDoesNotExists() {
		//Given
		when(matchesDao.findById(anyLong())).then(invocation -> {
			return Optional.of(getMatchByIdBreakingReference(invocation.getArgument(0)));
		});
		
		when(clubesMatchesService.getClubMatchByMatch(any(Match.class))).thenReturn(getClubMatch());
		
		when(lineupsMatchesService.getLineupMatchesByMatch(any(Match.class))).thenThrow(LineupMatchNotFoundException.class);
		
		Match match = getMatchByIdBreakingReference(1L);
		
		//When
		Match matchDeleted = matchesService.deleteMatch(1L);
		
		//Then
		assertEquals(match, matchDeleted);
		verify(matchesDao).findById(1L);
		verify(clubesMatchesService).getClubMatchByMatch(any(Match.class));
		verify(clubesMatchesService).deleteClubMatch(anyLong());
		verify(lineupsMatchesService).getLineupMatchesByMatch(any(Match.class));
		verify(lineupsMatchesService, never()).deleteLineupMatch(anyLong());
		verify(matchesDao).deleteById(1L);
	}

}
