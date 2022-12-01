package com.fernandobetancourt.services;

import static com.fernandobetancourt.Data.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.fernandobetancourt.exceptions.AddingLineupPlayerException;
import com.fernandobetancourt.exceptions.LineupMatchNotFoundException;
import com.fernandobetancourt.exceptions.LineupPlayerNotFoundException;
import com.fernandobetancourt.exceptions.MatchNotFoundException;
import com.fernandobetancourt.exceptions.PlayerNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.dao.ILineupsPlayersDao;
import com.fernandobetancourt.model.entity.Lineup;
import com.fernandobetancourt.model.entity.LineupPlayer;
import com.fernandobetancourt.model.entity.Match;

@SpringBootTest
class LineupsPlayersServiceImplTest {

	@MockBean
	private ILineupsPlayersDao lineupsPlayersDao;

	@MockBean
	private IPlayersService playersService;

	@MockBean
	private ILineupsService lineupsService;

	@MockBean
	private ILineupsMatchesService lineupsMatchesService;

	@MockBean
	private IMatchesService matchesService;

	@MockBean
	private IClubesMatchesService clubesMatchesService;

	@Autowired
	private ILineupsPlayersService lineupsPlayersService;

	// GET

	@Test
	void testGetLineupPlayerSuccessed() {
		// Given
		when(lineupsPlayersDao.findById(anyLong()))
				.then(invocation -> Optional.of(getLineupPlayerByIdBreakingReference(invocation.getArgument(0))));

		// When
		LineupPlayer lineupPlayer = lineupsPlayersService.getLineupPlayer(1L);

		// Then
		assertEquals(getLineupPlayerByIdBreakingReference(1L), lineupPlayer);
		verify(lineupsPlayersDao).findById(1L);
	}

	@Test
	void testGetLineupPlayerFailed() {
		// Given
		when(lineupsPlayersDao.findById(anyLong())).thenReturn(Optional.empty());

		// Then
		assertThrows(LineupPlayerNotFoundException.class, () -> {
			// When
			lineupsPlayersService.getLineupPlayer(1L);
		});

		verify(lineupsPlayersDao).findById(1L);
	}

	@Test
	void testGetLineupsPlayersByMatchSuccessed() {
		// Given
		when(matchesService.getMatch(anyLong()))
				.then(invocation -> getMatchByIdBreakingReference(invocation.getArgument(0)));

		when(lineupsMatchesService.getLineupMatchesByMatch(any(Match.class)))
				.thenReturn(Arrays.asList(getLineupMatchLocal(), getLineupMatchVisitor()));

		when(lineupsPlayersDao.findByLineup(getLineupWithId())).thenReturn(LINEUP_PLAYERS_WITH_ID_CLUB_1);
		when(lineupsPlayersDao.findByLineup(getLineupWithId2())).thenReturn(LINEUP_PLAYERS_WITH_ID_CLUB_2);

		// When
		List<LineupPlayer> lineupPlayers = lineupsPlayersService.getLineupsPlayersByMatch(1L);

		// Then
		assertTrue(LINEUP_PLAYERS_MATCH().equals(lineupPlayers));

		verify(matchesService).getMatch(1L);
		verify(lineupsMatchesService).getLineupMatchesByMatch(any(Match.class));
		verify(lineupsPlayersDao, times(2)).findByLineup(any(Lineup.class));
	}

	@Test
	void testGetLineupsPlayersByMatchFailed() {
		// Given
		when(matchesService.getMatch(anyLong()))
				.then(invocation -> getMatchByIdBreakingReference(invocation.getArgument(0)));

		when(lineupsMatchesService.getLineupMatchesByMatch(any(Match.class)))
				.thenReturn(Arrays.asList(getLineupMatchLocal(), getLineupMatchVisitor()));

		when(lineupsPlayersDao.findByLineup(getLineupWithId())).thenReturn(Collections.emptyList());
		when(lineupsPlayersDao.findByLineup(getLineupWithId2())).thenReturn(Collections.emptyList());

		// Then
		assertThrows(LineupPlayerNotFoundException.class, () -> {
			// When
			lineupsPlayersService.getLineupsPlayersByMatch(1L);
		});

		verify(matchesService).getMatch(1L);
		verify(lineupsMatchesService).getLineupMatchesByMatch(any(Match.class));
		verify(lineupsPlayersDao, times(2)).findByLineup(any(Lineup.class));
	}

	// POST

	@Test
	void testAddLineupsPlayersSuccessed() {
		// Match - Local (clubId: 1) - Visitor (clubId: 2)
		// Given
		when(matchesService.getMatch(anyLong()))
				.then(invocation -> getMatchByIdBreakingReference(invocation.getArgument(0)));
		when(clubesMatchesService.getClubMatchByMatch(any(Match.class))).thenReturn(getClubMatch());
		when(lineupsMatchesService.getLineupMatchesByMatch(any(Match.class))).thenReturn(LINEUPMATCHES);
		when(lineupsPlayersDao.findByLineup(getLineupWithId())).thenReturn(LINEUP_PLAYERS_WITH_ID_CLUB_1);
		when(lineupsPlayersDao.findByLineup(getLineupWithId2())).thenReturn(LINEUP_PLAYERS_WITH_ID_CLUB_2);
		when(playersService.getPlayerById(anyLong()))
				.then(invocation -> getPlayerByIdBreakingReference(invocation.getArgument(0)));

		when(lineupsPlayersDao.save(any(LineupPlayer.class))).then(new Answer<LineupPlayer>() {

			long counterIds = 1;

			@Override
			public LineupPlayer answer(InvocationOnMock invocation) throws Throwable {
				LineupPlayer lineupPlayer = invocation.getArgument(0);
				lineupPlayer.setLineupPlayerId(counterIds++);
				return lineupPlayer;
			}
		});

		// When
		List<LineupPlayer> lineupPlayers = lineupsPlayersService.addLineupsPlayers(LINEUP_PLAYERS_WITHOUT_ID_CLUB_1, 1L,
				"Local");

		// Then
		assertTrue(LINEUP_PLAYERS_WITH_ID_CLUB_1.equals(lineupPlayers));

		verify(matchesService).getMatch(1L);
		verify(clubesMatchesService).getClubMatchByMatch(any(Match.class));
		verify(lineupsMatchesService).getLineupMatchesByMatch(any(Match.class));
		verify(lineupsPlayersDao).findByLineup(any(Lineup.class));
		verify(lineupsPlayersDao, atLeast(11)).deleteById(anyLong());
		verify(playersService, atLeast(11)).getPlayerById(anyLong());
		verify(lineupsService, atLeast(11)).getLineup(anyLong());
		verify(lineupsPlayersDao, atLeast(11)).save(any(LineupPlayer.class));
	}
	
	@Test
	void testAddLineupsPlayersSuccessed2() {
		// Match - Local (clubId: 1) - Visitor (clubId: 2)
		// Given
		when(matchesService.getMatch(anyLong()))
		.then(invocation -> getMatchByIdBreakingReference(invocation.getArgument(0)));
		when(clubesMatchesService.getClubMatchByMatch(any(Match.class))).thenReturn(getClubMatch());
		when(lineupsMatchesService.getLineupMatchesByMatch(any(Match.class))).thenReturn(LINEUPMATCHES);
		when(lineupsPlayersDao.findByLineup(getLineupWithId())).thenReturn(LINEUP_PLAYERS_WITH_ID_CLUB_1);
		when(lineupsPlayersDao.findByLineup(getLineupWithId2())).thenReturn(LINEUP_PLAYERS_WITH_ID_CLUB_2);
		when(playersService.getPlayerById(anyLong()))
		.then(invocation -> getPlayerByIdBreakingReference(invocation.getArgument(0)));
		
		when(lineupsPlayersDao.save(any(LineupPlayer.class))).then(new Answer<LineupPlayer>() {
			
			long counterIds = 12;
			
			@Override
			public LineupPlayer answer(InvocationOnMock invocation) throws Throwable {
				LineupPlayer lineupPlayer = invocation.getArgument(0);
				lineupPlayer.setLineupPlayerId(counterIds++);
				return lineupPlayer;
			}
		});
		
		// When
		List<LineupPlayer> lineupPlayers = lineupsPlayersService.addLineupsPlayers(LINEUP_PLAYERS_WITHOUT_ID_CLUB_2, 1L,
				"Visitor");
		
		// Then
		assertTrue(LINEUP_PLAYERS_WITH_ID_CLUB_2.equals(lineupPlayers));
		
		verify(matchesService).getMatch(1L);
		verify(clubesMatchesService).getClubMatchByMatch(any(Match.class));
		verify(lineupsMatchesService).getLineupMatchesByMatch(any(Match.class));
		verify(lineupsPlayersDao).findByLineup(any(Lineup.class));
		verify(lineupsPlayersDao, atLeast(11)).deleteById(anyLong());
		verify(playersService, atLeast(11)).getPlayerById(anyLong());
		verify(lineupsService, atLeast(11)).getLineup(anyLong());
		verify(lineupsPlayersDao, atLeast(11)).save(any(LineupPlayer.class));
	}

	@Test
	void testAddLineupsPlayersFailedMatchDoesNotExists() {
		// Match - Local (clubId: 1) - Visitor (clubId: 2)
		// Given
		when(matchesService.getMatch(anyLong())).thenThrow(MatchNotFoundException.class);

		// Then
		assertThrows(MatchNotFoundException.class, () -> {
			// When
			lineupsPlayersService.addLineupsPlayers(LINEUP_PLAYERS_WITHOUT_ID_CLUB_1, 1L, "Local");
		});


		verify(matchesService).getMatch(1L);
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(lineupsMatchesService, never()).getLineupMatchesByMatch(any(Match.class));
		verify(lineupsPlayersDao, never()).findByLineup(any(Lineup.class));
		verify(lineupsPlayersDao, never()).deleteById(anyLong());
		verify(playersService, never()).getPlayerById(anyLong());
		verify(lineupsService, never()).getLineup(anyLong());
		verify(lineupsPlayersDao, never()).save(any(LineupPlayer.class));
	}

	@Test
	void testAddLineupsPlayersFailedClubStatusIsNotValid() {
		//Match -   Local (clubId: 1) - Visitor (clubId: 2)
		//Given
		when(matchesService.getMatch(anyLong())).then(invocation -> getMatchByIdBreakingReference(invocation.getArgument(0)));

		//Then
		assertThrows(WritingInformationException.class, () -> {
			//When
			lineupsPlayersService.addLineupsPlayers(LINEUP_PLAYERS_WITHOUT_ID_CLUB_1, 1L, "ClubStatusNotValid");
		});
		
		verify(matchesService).getMatch(1L);
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(lineupsMatchesService, never()).getLineupMatchesByMatch(any(Match.class));
		verify(lineupsPlayersDao, never()).findByLineup(any(Lineup.class));
		verify(lineupsPlayersDao, never()).deleteById(anyLong());
		verify(playersService, never()).getPlayerById(anyLong());
		verify(lineupsService, never()).getLineup(anyLong());
		verify(lineupsPlayersDao, never()).save(any(LineupPlayer.class));
	}

	@Test
	void testAddLineupsPlayersFailedLineupMatchDoesNotExists() {
		//Match -   Local (clubId: 1) - Visitor (clubId: 2)
		//Given
		when(matchesService.getMatch(anyLong())).then(invocation -> getMatchByIdBreakingReference(invocation.getArgument(0)));
		when(clubesMatchesService.getClubMatchByMatch(any(Match.class))).thenReturn(getClubMatch());
		when(lineupsMatchesService.getLineupMatchesByMatch(any(Match.class))).thenReturn(Collections.emptyList());
		
		//Then
		assertThrows(LineupMatchNotFoundException.class, () -> {
			//When
			lineupsPlayersService.addLineupsPlayers(LINEUP_PLAYERS_WITHOUT_ID_CLUB_1, 1L, "Local");
		});
		
		verify(matchesService).getMatch(1L);
		verify(clubesMatchesService).getClubMatchByMatch(any(Match.class));
		verify(lineupsMatchesService).getLineupMatchesByMatch(any(Match.class));
		verify(lineupsPlayersDao, never()).findByLineup(any(Lineup.class));
		verify(lineupsPlayersDao, never()).deleteById(anyLong());
		verify(playersService, never()).getPlayerById(anyLong());
		verify(lineupsService, never()).getLineup(anyLong());
		verify(lineupsPlayersDao, never()).save(any(LineupPlayer.class));
	}

	@Test
	void testAddLineupsPlayersFailedLineupPlayerNull() {
		//Match -   Local (clubId: 1) - Visitor (clubId: 2)
		//Given
		when(matchesService.getMatch(anyLong())).then(invocation -> getMatchByIdBreakingReference(invocation.getArgument(0)));
		when(clubesMatchesService.getClubMatchByMatch(any(Match.class))).thenReturn(getClubMatch());
		when(lineupsMatchesService.getLineupMatchesByMatch(any(Match.class))).thenReturn(LINEUPMATCHES);
		when(lineupsPlayersDao.findByLineup(getLineupWithId())).thenReturn(LINEUP_PLAYERS_WITH_ID_CLUB_1);
		when(lineupsPlayersDao.findByLineup(getLineupWithId2())).thenReturn(LINEUP_PLAYERS_WITH_ID_CLUB_2);
		when(playersService.getPlayerById(anyLong())).then(invocation -> getPlayerByIdBreakingReference(invocation.getArgument(0)));
		
		when(lineupsPlayersDao.save(any(LineupPlayer.class))).then(new Answer<LineupPlayer>() {
			
			long counterIds = 1;

			@Override
			public LineupPlayer answer(InvocationOnMock invocation) throws Throwable {
				LineupPlayer lineupPlayer = invocation.getArgument(0);
				lineupPlayer.setLineupPlayerId(counterIds++);
				return lineupPlayer;
			}
		});
		
		List<LineupPlayer> lineupPlayers = new ArrayList<>(LINEUP_PLAYERS_WITHOUT_ID_CLUB_1);
		lineupPlayers.add(null);

		//Then
		assertThrows(AddingLineupPlayerException.class, () -> {
			//When
			lineupsPlayersService.addLineupsPlayers(lineupPlayers, 1L, "Local");
		});
		
		verify(matchesService).getMatch(1L);
		verify(clubesMatchesService).getClubMatchByMatch(any(Match.class));
		verify(lineupsMatchesService).getLineupMatchesByMatch(any(Match.class));
		verify(lineupsPlayersDao).findByLineup(any(Lineup.class));
		verify(lineupsPlayersDao, atLeast(11)).deleteById(anyLong());
		verify(playersService, atMost(11)).getPlayerById(anyLong());
		verify(lineupsService, atMost(11)).getLineup(anyLong());
		verify(lineupsPlayersDao, atMost(11)).save(any(LineupPlayer.class));
	}

	@Test
	void testAddLineupsPlayersFailedPlayerStatusNull() {
		//Match -   Local (clubId: 1) - Visitor (clubId: 2)
		//Given
		when(matchesService.getMatch(anyLong())).then(invocation -> getMatchByIdBreakingReference(invocation.getArgument(0)));
		when(clubesMatchesService.getClubMatchByMatch(any(Match.class))).thenReturn(getClubMatch());
		when(lineupsMatchesService.getLineupMatchesByMatch(any(Match.class))).thenReturn(LINEUPMATCHES);
		when(lineupsPlayersDao.findByLineup(getLineupWithId())).thenReturn(LINEUP_PLAYERS_WITH_ID_CLUB_1);
		when(lineupsPlayersDao.findByLineup(getLineupWithId2())).thenReturn(LINEUP_PLAYERS_WITH_ID_CLUB_2);
		when(playersService.getPlayerById(anyLong())).then(invocation -> getPlayerByIdBreakingReference(invocation.getArgument(0)));
		
		when(lineupsPlayersDao.save(any(LineupPlayer.class))).then(new Answer<LineupPlayer>() {
			
			long counterIds = 1;

			@Override
			public LineupPlayer answer(InvocationOnMock invocation) throws Throwable {
				LineupPlayer lineupPlayer = invocation.getArgument(0);
				lineupPlayer.setLineupPlayerId(counterIds++);
				return lineupPlayer;
			}
		});
		
		List<LineupPlayer> lineupPlayers = new ArrayList<>(LINEUP_PLAYERS_WITHOUT_ID_CLUB_1);
		lineupPlayers.add(new LineupPlayer(null, null, getPlayerByIdBreakingReference(29L), getLineupWithId()));

		//Then
		assertThrows(AddingLineupPlayerException.class, () -> {
			//When
			lineupsPlayersService.addLineupsPlayers(lineupPlayers, 1L, "Local");
		});
		
		verify(matchesService).getMatch(1L);
		verify(clubesMatchesService).getClubMatchByMatch(any(Match.class));
		verify(lineupsMatchesService).getLineupMatchesByMatch(any(Match.class));
		verify(lineupsPlayersDao).findByLineup(any(Lineup.class));
		verify(lineupsPlayersDao, atLeast(11)).deleteById(anyLong());
		verify(playersService, atMost(11)).getPlayerById(anyLong());
		verify(lineupsService, atMost(11)).getLineup(anyLong());
		verify(lineupsPlayersDao, atMost(11)).save(any(LineupPlayer.class));
	}

	@Test
	void testAddLineupsPlayersFailedPlayerStatusEmptyString() {
		//Match -   Local (clubId: 1) - Visitor (clubId: 2)
		//Given
		when(matchesService.getMatch(anyLong())).then(invocation -> getMatchByIdBreakingReference(invocation.getArgument(0)));
		when(clubesMatchesService.getClubMatchByMatch(any(Match.class))).thenReturn(getClubMatch());
		when(lineupsMatchesService.getLineupMatchesByMatch(any(Match.class))).thenReturn(LINEUPMATCHES);
		when(lineupsPlayersDao.findByLineup(getLineupWithId())).thenReturn(LINEUP_PLAYERS_WITH_ID_CLUB_1);
		when(lineupsPlayersDao.findByLineup(getLineupWithId2())).thenReturn(LINEUP_PLAYERS_WITH_ID_CLUB_2);
		when(playersService.getPlayerById(anyLong())).then(invocation -> getPlayerByIdBreakingReference(invocation.getArgument(0)));
		
		when(lineupsPlayersDao.save(any(LineupPlayer.class))).then(new Answer<LineupPlayer>() {
			
			long counterIds = 1;

			@Override
			public LineupPlayer answer(InvocationOnMock invocation) throws Throwable {
				LineupPlayer lineupPlayer = invocation.getArgument(0);
				lineupPlayer.setLineupPlayerId(counterIds++);
				return lineupPlayer;
			}
		});
		
		List<LineupPlayer> lineupPlayers = new ArrayList<>(LINEUP_PLAYERS_WITHOUT_ID_CLUB_1);
		lineupPlayers.add(new LineupPlayer(null, "", getPlayerByIdBreakingReference(29L), getLineupWithId()));

		//Then
		assertThrows(AddingLineupPlayerException.class, () -> {
			//When
			lineupsPlayersService.addLineupsPlayers(lineupPlayers, 1L, "Local");
		});
		
		verify(matchesService).getMatch(1L);
		verify(clubesMatchesService).getClubMatchByMatch(any(Match.class));
		verify(lineupsMatchesService).getLineupMatchesByMatch(any(Match.class));
		verify(lineupsPlayersDao).findByLineup(any(Lineup.class));
		verify(lineupsPlayersDao, atLeast(11)).deleteById(anyLong());
		verify(playersService, atMost(11)).getPlayerById(anyLong());
		verify(lineupsService, atMost(11)).getLineup(anyLong());
		verify(lineupsPlayersDao, atMost(11)).save(any(LineupPlayer.class));
	}

	@Test
	void testAddLineupsPlayersFailedPlayerNull() {
		//Match -   Local (clubId: 1) - Visitor (clubId: 2)
		//Given
		when(matchesService.getMatch(anyLong())).then(invocation -> getMatchByIdBreakingReference(invocation.getArgument(0)));
		when(clubesMatchesService.getClubMatchByMatch(any(Match.class))).thenReturn(getClubMatch());
		when(lineupsMatchesService.getLineupMatchesByMatch(any(Match.class))).thenReturn(LINEUPMATCHES);
		when(lineupsPlayersDao.findByLineup(getLineupWithId())).thenReturn(LINEUP_PLAYERS_WITH_ID_CLUB_1);
		when(lineupsPlayersDao.findByLineup(getLineupWithId2())).thenReturn(LINEUP_PLAYERS_WITH_ID_CLUB_2);
		when(playersService.getPlayerById(anyLong())).then(invocation -> getPlayerByIdBreakingReference(invocation.getArgument(0)));
		
		when(lineupsPlayersDao.save(any(LineupPlayer.class))).then(new Answer<LineupPlayer>() {
			
			long counterIds = 1;

			@Override
			public LineupPlayer answer(InvocationOnMock invocation) throws Throwable {
				LineupPlayer lineupPlayer = invocation.getArgument(0);
				lineupPlayer.setLineupPlayerId(counterIds++);
				return lineupPlayer;
			}
		});
		
		List<LineupPlayer> lineupPlayers = new ArrayList<>(LINEUP_PLAYERS_WITHOUT_ID_CLUB_1);
		lineupPlayers.add(new LineupPlayer(null, "Banca", null, getLineupWithId()));

		//Then
		assertThrows(AddingLineupPlayerException.class, () -> {
			//When
			lineupsPlayersService.addLineupsPlayers(lineupPlayers, 1L, "Local");
		});
		
		verify(matchesService).getMatch(1L);
		verify(clubesMatchesService).getClubMatchByMatch(any(Match.class));
		verify(lineupsMatchesService).getLineupMatchesByMatch(any(Match.class));
		verify(lineupsPlayersDao).findByLineup(any(Lineup.class));
		verify(lineupsPlayersDao, atLeast(11)).deleteById(anyLong());
		verify(playersService, atMost(11)).getPlayerById(anyLong());
		verify(lineupsService, atMost(11)).getLineup(anyLong());
		verify(lineupsPlayersDao, atMost(11)).save(any(LineupPlayer.class));
	}

	@Test
	void testAddLineupsPlayersFailedPlayerIdNull() {
		//Match -   Local (clubId: 1) - Visitor (clubId: 2)
		//Given
		when(matchesService.getMatch(anyLong())).then(invocation -> getMatchByIdBreakingReference(invocation.getArgument(0)));
		when(clubesMatchesService.getClubMatchByMatch(any(Match.class))).thenReturn(getClubMatch());
		when(lineupsMatchesService.getLineupMatchesByMatch(any(Match.class))).thenReturn(LINEUPMATCHES);
		when(lineupsPlayersDao.findByLineup(getLineupWithId())).thenReturn(LINEUP_PLAYERS_WITH_ID_CLUB_1);
		when(lineupsPlayersDao.findByLineup(getLineupWithId2())).thenReturn(LINEUP_PLAYERS_WITH_ID_CLUB_2);
		when(playersService.getPlayerById(anyLong())).then(invocation -> getPlayerByIdBreakingReference(invocation.getArgument(0)));
		
		when(lineupsPlayersDao.save(any(LineupPlayer.class))).then(new Answer<LineupPlayer>() {
			
			long counterIds = 1;

			@Override
			public LineupPlayer answer(InvocationOnMock invocation) throws Throwable {
				LineupPlayer lineupPlayer = invocation.getArgument(0);
				lineupPlayer.setLineupPlayerId(counterIds++);
				return lineupPlayer;
			}
		});
		
		List<LineupPlayer> lineupPlayers = new ArrayList<>(LINEUP_PLAYERS_WITHOUT_ID_CLUB_1);
		lineupPlayers.add(new LineupPlayer(null, "Banca", getPlayerWithoutIdByPositionBreakingReference(0), getLineupWithId()));

		//Then
		assertThrows(AddingLineupPlayerException.class, () -> {
			//When
			lineupsPlayersService.addLineupsPlayers(lineupPlayers, 1L, "Local");
		});
		
		verify(matchesService).getMatch(1L);
		verify(clubesMatchesService).getClubMatchByMatch(any(Match.class));
		verify(lineupsMatchesService).getLineupMatchesByMatch(any(Match.class));
		verify(lineupsPlayersDao).findByLineup(any(Lineup.class));
		verify(lineupsPlayersDao, atLeast(11)).deleteById(anyLong());
		verify(playersService, atMost(11)).getPlayerById(anyLong());
		verify(lineupsService, atMost(11)).getLineup(anyLong());
		verify(lineupsPlayersDao, atMost(11)).save(any(LineupPlayer.class));
	}

	@Test
	void testAddLineupsPlayersFailedPlayerIdLessThan1() {
		//Match -   Local (clubId: 1) - Visitor (clubId: 2)
		//Given
		when(matchesService.getMatch(anyLong())).then(invocation -> getMatchByIdBreakingReference(invocation.getArgument(0)));
		when(clubesMatchesService.getClubMatchByMatch(any(Match.class))).thenReturn(getClubMatch());
		when(lineupsMatchesService.getLineupMatchesByMatch(any(Match.class))).thenReturn(LINEUPMATCHES);
		when(lineupsPlayersDao.findByLineup(getLineupWithId())).thenReturn(LINEUP_PLAYERS_WITH_ID_CLUB_1);
		when(lineupsPlayersDao.findByLineup(getLineupWithId2())).thenReturn(LINEUP_PLAYERS_WITH_ID_CLUB_2);
		when(playersService.getPlayerById(anyLong())).then(invocation -> getPlayerByIdBreakingReference(invocation.getArgument(0)));
		
		when(lineupsPlayersDao.save(any(LineupPlayer.class))).then(new Answer<LineupPlayer>() {
			
			long counterIds = 1;

			@Override
			public LineupPlayer answer(InvocationOnMock invocation) throws Throwable {
				LineupPlayer lineupPlayer = invocation.getArgument(0);
				lineupPlayer.setLineupPlayerId(counterIds++);
				return lineupPlayer;
			}
		});
		
		List<LineupPlayer> lineupPlayers = new ArrayList<>(LINEUP_PLAYERS_WITHOUT_ID_CLUB_1);
		lineupPlayers.add(new LineupPlayer(null, "Banca", PLAYER_WITH_ID_LESS_THAN_1, getLineupWithId()));

		//Then
		assertThrows(AddingLineupPlayerException.class, () -> {
			//When
			lineupsPlayersService.addLineupsPlayers(lineupPlayers, 1L, "Local");
		});
		
		verify(matchesService).getMatch(1L);
		verify(clubesMatchesService).getClubMatchByMatch(any(Match.class));
		verify(lineupsMatchesService).getLineupMatchesByMatch(any(Match.class));
		verify(lineupsPlayersDao).findByLineup(any(Lineup.class));
		verify(lineupsPlayersDao, atLeast(11)).deleteById(anyLong());
		verify(playersService, atMost(11)).getPlayerById(anyLong());
		verify(lineupsService, atMost(11)).getLineup(anyLong());
		verify(lineupsPlayersDao, atMost(11)).save(any(LineupPlayer.class));
	}

	//En el método siempre se le establece el Lineup cuando se esta iterando, por lo que nunca se lanzaría ningún error sobre el Lineup
	//Igual voy a probar regresando un Lineup que sea < 1, null, etc 
	
	@Test
	void testAddLineupsPlayersFailedLineupNull() {
		//Match -   Local (clubId: 1) - Visitor (clubId: 2)
		//Given
		when(matchesService.getMatch(anyLong())).then(invocation -> getMatchByIdBreakingReference(invocation.getArgument(0)));
		when(clubesMatchesService.getClubMatchByMatch(any(Match.class))).thenReturn(getClubMatch());
		when(lineupsMatchesService.getLineupMatchesByMatch(any(Match.class))).thenReturn(LINEUPMATCHES);
		when(lineupsPlayersDao.findByLineup(getLineupWithId())).thenReturn(LINEUP_PLAYERS_WITH_ID_CLUB_1);
		when(lineupsPlayersDao.findByLineup(getLineupWithId2())).thenReturn(LINEUP_PLAYERS_WITH_ID_CLUB_2);
		when(playersService.getPlayerById(anyLong())).then(invocation -> getPlayerByIdBreakingReference(invocation.getArgument(0)));
		
		when(lineupsPlayersDao.save(any(LineupPlayer.class))).then(new Answer<LineupPlayer>() {
			
			long counterIds = 1;

			@Override
			public LineupPlayer answer(InvocationOnMock invocation) throws Throwable {
				LineupPlayer lineupPlayer = invocation.getArgument(0);
				lineupPlayer.setLineupPlayerId(counterIds++);
				return lineupPlayer;
			}
		});
		
		List<LineupPlayer> lineupPlayers = new ArrayList<>(LINEUP_PLAYERS_WITHOUT_ID_CLUB_1);
		lineupPlayers.add(new LineupPlayer(null, "Banca", PLAYER_WITH_ID_LESS_THAN_1, getLineupWithId()));

		//Then
		assertThrows(AddingLineupPlayerException.class, () -> {
			//When
			lineupsPlayersService.addLineupsPlayers(lineupPlayers, 1L, "Local");
		});
		
		verify(matchesService).getMatch(1L);
		verify(clubesMatchesService).getClubMatchByMatch(any(Match.class));
		verify(lineupsMatchesService).getLineupMatchesByMatch(any(Match.class));
		verify(lineupsPlayersDao).findByLineup(any(Lineup.class));
		verify(lineupsPlayersDao, atLeast(11)).deleteById(anyLong());
		verify(playersService, atMost(11)).getPlayerById(anyLong());
		verify(lineupsService, atMost(11)).getLineup(anyLong());
		verify(lineupsPlayersDao, atMost(11)).save(any(LineupPlayer.class));
	}

//	@Test
//	void testAddLineupsPlayersFailedLineupIdNull() {
//		//Match -   Local (clubId: 1) - Visitor (clubId: 2)
//		//Given
//		when(matchesService.getMatch(anyLong())).then(invocation -> getMatchByIdBreakingReference(invocation.getArgument(0)));
//		when(clubesMatchesService.getClubMatchByMatch(any(Match.class))).thenReturn(getClubMatch());
//		when(lineupsMatchesService.getLineupMatchesByMatch(any(Match.class))).thenReturn(LINEUPMATCHES);
//		when(lineupsPlayersDao.findByLineup(getLineupWithId())).thenReturn(LINEUP_PLAYERS_WITH_ID_CLUB_1);
//		when(lineupsPlayersDao.findByLineup(getLineupWithId2())).thenReturn(LINEUP_PLAYERS_WITH_ID_CLUB_2);
//		when(playersService.getPlayerById(anyLong())).then(invocation -> getPlayerByIdBreakingReference(invocation.getArgument(0)));
//		
//		when(lineupsPlayersDao.save(any(LineupPlayer.class))).then(new Answer<LineupPlayer>() {
//			
//			long counterIds = 1;
//
//			@Override
//			public LineupPlayer answer(InvocationOnMock invocation) throws Throwable {
//				LineupPlayer lineupPlayer = invocation.getArgument(0);
//				lineupPlayer.setLineupPlayerId(counterIds++);
//				return lineupPlayer;
//			}
//		});
//		
//		//When
//		List<LineupPlayer> lineupPlayers = lineupsPlayersService.addLineupsPlayers(LINEUP_PLAYERS_WITHOUT_ID_CLUB_1, 1L, "Local");
//		
//		//Then
//		assertTrue(LINEUP_PLAYERS_WITH_ID_CLUB_1.equals(lineupPlayers));
//		
//		verify(matchesService).getMatch(1L);
//		verify(clubesMatchesService).getClubMatchByMatch(any(Match.class));
//		verify(lineupsMatchesService).getLineupMatchesByMatch(any(Match.class));
//		verify(lineupsPlayersDao).findByLineup(any(Lineup.class));
//		verify(lineupsPlayersDao, atLeast(11)).deleteById(anyLong());
//		verify(playersService, atLeast(11)).getPlayerById(anyLong());
//		verify(lineupsService, atLeast(11)).getLineup(anyLong());
//		verify(lineupsPlayersDao, atLeast(11)).save(any(LineupPlayer.class));
//	}
//
//	@Test
//	void testAddLineupsPlayersFailedLineupIdLessThan1() {
//		//Match -   Local (clubId: 1) - Visitor (clubId: 2)
//		//Given
//		when(matchesService.getMatch(anyLong())).then(invocation -> getMatchByIdBreakingReference(invocation.getArgument(0)));
//		when(clubesMatchesService.getClubMatchByMatch(any(Match.class))).thenReturn(getClubMatch());
//		when(lineupsMatchesService.getLineupMatchesByMatch(any(Match.class))).thenReturn(LINEUPMATCHES);
//		when(lineupsPlayersDao.findByLineup(getLineupWithId())).thenReturn(LINEUP_PLAYERS_WITH_ID_CLUB_1);
//		when(lineupsPlayersDao.findByLineup(getLineupWithId2())).thenReturn(LINEUP_PLAYERS_WITH_ID_CLUB_2);
//		when(playersService.getPlayerById(anyLong())).then(invocation -> getPlayerByIdBreakingReference(invocation.getArgument(0)));
//		
//		when(lineupsPlayersDao.save(any(LineupPlayer.class))).then(new Answer<LineupPlayer>() {
//			
//			long counterIds = 1;
//
//			@Override
//			public LineupPlayer answer(InvocationOnMock invocation) throws Throwable {
//				LineupPlayer lineupPlayer = invocation.getArgument(0);
//				lineupPlayer.setLineupPlayerId(counterIds++);
//				return lineupPlayer;
//			}
//		});
//		
//		//When
//		List<LineupPlayer> lineupPlayers = lineupsPlayersService.addLineupsPlayers(LINEUP_PLAYERS_WITHOUT_ID_CLUB_1, 1L, "Local");
//		
//		//Then
//		assertTrue(LINEUP_PLAYERS_WITH_ID_CLUB_1.equals(lineupPlayers));
//		
//		verify(matchesService).getMatch(1L);
//		verify(clubesMatchesService).getClubMatchByMatch(any(Match.class));
//		verify(lineupsMatchesService).getLineupMatchesByMatch(any(Match.class));
//		verify(lineupsPlayersDao).findByLineup(any(Lineup.class));
//		verify(lineupsPlayersDao, atLeast(11)).deleteById(anyLong());
//		verify(playersService, atLeast(11)).getPlayerById(anyLong());
//		verify(lineupsService, atLeast(11)).getLineup(anyLong());
//		verify(lineupsPlayersDao, atLeast(11)).save(any(LineupPlayer.class));
//	}
//
	@Test
	void testAddLineupsPlayersFailedPlayerDoesNotExists() {
		//Match -   Local (clubId: 1) - Visitor (clubId: 2)
		//Given
		when(matchesService.getMatch(anyLong())).then(invocation -> getMatchByIdBreakingReference(invocation.getArgument(0)));
		when(clubesMatchesService.getClubMatchByMatch(any(Match.class))).thenReturn(getClubMatch());
		when(lineupsMatchesService.getLineupMatchesByMatch(any(Match.class))).thenReturn(LINEUPMATCHES);
		when(lineupsPlayersDao.findByLineup(getLineupWithId())).thenReturn(LINEUP_PLAYERS_WITH_ID_CLUB_1);
		when(lineupsPlayersDao.findByLineup(getLineupWithId2())).thenReturn(LINEUP_PLAYERS_WITH_ID_CLUB_2);
		when(playersService.getPlayerById(anyLong())).thenThrow(PlayerNotFoundException.class);

		//Then
		assertThrows(PlayerNotFoundException.class, () -> {
			//When
			lineupsPlayersService.addLineupsPlayers(LINEUP_PLAYERS_WITHOUT_ID_CLUB_1, 1L, "Local");
		});
		
		verify(matchesService).getMatch(1L);
		verify(clubesMatchesService).getClubMatchByMatch(any(Match.class));
		verify(lineupsMatchesService).getLineupMatchesByMatch(any(Match.class));
		verify(lineupsPlayersDao).findByLineup(any(Lineup.class));
		verify(lineupsPlayersDao, atLeast(11)).deleteById(anyLong());
		verify(playersService).getPlayerById(anyLong());
		verify(lineupsService).getLineup(anyLong());
		verify(lineupsPlayersDao, never()).save(any(LineupPlayer.class));
	}

	//El lineup tiene que existir, ya que este lo recupero de una de sus relaciones
	
//	@Test
//	void testAddLineupsPlayersFailedLineupDoesNotExists() {
//		//Match -   Local (clubId: 1) - Visitor (clubId: 2)
//		//Given
//		when(matchesService.getMatch(anyLong())).then(invocation -> getMatchByIdBreakingReference(invocation.getArgument(0)));
//		when(clubesMatchesService.getClubMatchByMatch(any(Match.class))).thenReturn(getClubMatch());
//		when(lineupsMatchesService.getLineupMatchesByMatch(any(Match.class))).thenReturn(LINEUPMATCHES);
//		when(lineupsPlayersDao.findByLineup(getLineupWithId())).thenReturn(LINEUP_PLAYERS_WITH_ID_CLUB_1);
//		when(lineupsPlayersDao.findByLineup(getLineupWithId2())).thenReturn(LINEUP_PLAYERS_WITH_ID_CLUB_2);
//		when(playersService.getPlayerById(anyLong())).then(invocation -> getPlayerByIdBreakingReference(invocation.getArgument(0)));
//		
//		when(lineupsPlayersDao.save(any(LineupPlayer.class))).then(new Answer<LineupPlayer>() {
//			
//			long counterIds = 1;
//
//			@Override
//			public LineupPlayer answer(InvocationOnMock invocation) throws Throwable {
//				LineupPlayer lineupPlayer = invocation.getArgument(0);
//				lineupPlayer.setLineupPlayerId(counterIds++);
//				return lineupPlayer;
//			}
//		});
//		
//		//When
//		List<LineupPlayer> lineupPlayers = lineupsPlayersService.addLineupsPlayers(LINEUP_PLAYERS_WITHOUT_ID_CLUB_1, 1L, "Local");
//		
//		//Then
//		assertTrue(LINEUP_PLAYERS_WITH_ID_CLUB_1.equals(lineupPlayers));
//		
//		verify(matchesService).getMatch(1L);
//		verify(clubesMatchesService).getClubMatchByMatch(any(Match.class));
//		verify(lineupsMatchesService).getLineupMatchesByMatch(any(Match.class));
//		verify(lineupsPlayersDao).findByLineup(any(Lineup.class));
//		verify(lineupsPlayersDao, atLeast(11)).deleteById(anyLong());
//		verify(playersService, atLeast(11)).getPlayerById(anyLong());
//		verify(lineupsService, atLeast(11)).getLineup(anyLong());
//		verify(lineupsPlayersDao, atLeast(11)).save(any(LineupPlayer.class));
//	}

	@Test
	void testAddLineupsPlayersFailedPlayerDoesNotBelongToClub() {
		//Match -   Local (clubId: 1) - Visitor (clubId: 2)
		//Given
		when(matchesService.getMatch(anyLong())).then(invocation -> getMatchByIdBreakingReference(invocation.getArgument(0)));
		when(clubesMatchesService.getClubMatchByMatch(any(Match.class))).thenReturn(getClubMatch());
		when(lineupsMatchesService.getLineupMatchesByMatch(any(Match.class))).thenReturn(LINEUPMATCHES);
		when(lineupsPlayersDao.findByLineup(getLineupWithId())).thenReturn(LINEUP_PLAYERS_WITH_ID_CLUB_1);
		when(lineupsPlayersDao.findByLineup(getLineupWithId2())).thenReturn(LINEUP_PLAYERS_WITH_ID_CLUB_2);
		when(playersService.getPlayerById(anyLong())).then(invocation -> getPlayerByIdBreakingReference(invocation.getArgument(0)));
		
		when(lineupsPlayersDao.save(any(LineupPlayer.class))).then(new Answer<LineupPlayer>() {
			
			long counterIds = 1;

			@Override
			public LineupPlayer answer(InvocationOnMock invocation) throws Throwable {
				LineupPlayer lineupPlayer = invocation.getArgument(0);
				lineupPlayer.setLineupPlayerId(counterIds++);
				return lineupPlayer;
			}
		});
		
		List<LineupPlayer> lineupPlayers = new ArrayList<>(LINEUP_PLAYERS_WITHOUT_ID_CLUB_1);
		lineupPlayers.add(new LineupPlayer(null, "Banca", getPlayerByIdBreakingReference(4L), getLineupWithId()));

		//Then
		assertThrows(AddingLineupPlayerException.class, () -> {
			//When
			lineupsPlayersService.addLineupsPlayers(lineupPlayers, 1L, "Local");
		});
		
		verify(matchesService).getMatch(1L);
		verify(clubesMatchesService).getClubMatchByMatch(any(Match.class));
		verify(lineupsMatchesService).getLineupMatchesByMatch(any(Match.class));
		verify(lineupsPlayersDao).findByLineup(any(Lineup.class));
		verify(lineupsPlayersDao, atLeast(11)).deleteById(anyLong());
		verify(playersService, atMost(12)).getPlayerById(anyLong());
		verify(lineupsService, atMost(12)).getLineup(anyLong());
		verify(lineupsPlayersDao, atMost(11)).save(any(LineupPlayer.class));
	}

	// PUT

//	@Test
//	void testUpdateLineupPlayer() {
//	}

	// DELETE

	@Test
	void testDeleteLineupsPlayersSuccessed() {
		//Given
		when(lineupsPlayersDao.findById(anyLong())).then(invocation -> Optional.of(getLineupPlayerByIdBreakingReference(invocation.getArgument(0))));
		
		List<Long> lpIds = LINEUP_PLAYERS_WITH_ID_CLUB_1
								.stream()
								.map(lp -> lp.getLineupPlayerId())
								.collect(Collectors.toList());
		
		//When
		List<LineupPlayer> lineupPlayers = lineupsPlayersService.deleteLineupsPlayers(lpIds);
		
		//Then
		assertTrue(LINEUP_PLAYERS_WITH_ID_CLUB_1.equals(lineupPlayers));
		verify(lineupsPlayersDao, times(11)).findById(anyLong());
		verify(lineupsPlayersDao, times(11)).deleteById(anyLong());
	}
	
	@Test
	void testDeleteLineupsPlayersFailed() {
		//Given
		when(lineupsPlayersDao.findById(anyLong())).then(invocation -> Optional.of(getLineupPlayerByIdBreakingReference(invocation.getArgument(0))));
		
		List<Long> lpIds = LINEUP_PLAYERS_WITH_ID_CLUB_1
								.stream()
								.map(lp -> lp.getLineupPlayerId())
								.collect(Collectors.toList());
		
		lpIds.add(1000L);
		lpIds.add(2000L);
		
		//When
		List<LineupPlayer> lineupPlayers = lineupsPlayersService.deleteLineupsPlayers(lpIds);
		
		//Then
		assertTrue(LINEUP_PLAYERS_WITH_ID_CLUB_1.equals(lineupPlayers));
		verify(lineupsPlayersDao, times(13)).findById(anyLong());
		verify(lineupsPlayersDao, times(11)).deleteById(anyLong());
	}

}
