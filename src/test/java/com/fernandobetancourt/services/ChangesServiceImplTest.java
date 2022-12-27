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

import com.fernandobetancourt.exceptions.AddingChangeException;
import com.fernandobetancourt.exceptions.ChangeNotFoundException;
import com.fernandobetancourt.exceptions.LineupMatchNotFoundException;
import com.fernandobetancourt.exceptions.MatchNotFoundException;
import com.fernandobetancourt.exceptions.PlayerNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.dao.IChangesDao;
import com.fernandobetancourt.model.entity.Change;
import com.fernandobetancourt.model.entity.Match;
import com.fernandobetancourt.validators.ChangeValidator;

@SpringBootTest(classes = {ChangesServiceImpl.class, ChangeValidator.class})
class ChangesServiceImplTest {

	@MockBean
	private IChangesDao changesDao;

	@MockBean
	private IPlayersService playersService;

	@MockBean
	private IMatchesService matchesService;

	@MockBean
	private ILineupsMatchesService lineupsMatchesService;

	@MockBean
	private IClubesMatchesService clubesMatchesService;

	@Autowired
	private IChangesService changesService;

	// GET

	@Test
	void testGetChangesByMatchSuccessed() {
		// Given
		when(matchesService.getMatch(anyLong()))
				.then(invocation -> getMatchByIdBreakingReference(invocation.getArgument(0)));
		when(changesDao.findByMatch(any(Match.class))).thenReturn(CHANGES_WITH_ID_MATCH_1);

		// When
		List<Change> changes = changesService.getChangesByMatch(1L);

		// Then
		assertEquals(CHANGES_WITH_ID_MATCH_1, changes);
		verify(matchesService).getMatch(1L);
		verify(changesDao).findByMatch(any(Match.class));
	}

	@Test
	void testGetChangesByMatchEmptyList() {
		// Given
		when(matchesService.getMatch(anyLong()))
				.then(invocation -> getMatchByIdBreakingReference(invocation.getArgument(0)));
		when(changesDao.findByMatch(any(Match.class))).thenReturn(Collections.emptyList());

		// Then
		assertThrows(ChangeNotFoundException.class, () -> {
			// When
			changesService.getChangesByMatch(1L);
		});

		verify(matchesService).getMatch(1L);
		verify(changesDao).findByMatch(any(Match.class));
	}

	// POST

	@Test
	void testAddChangeSuccessed1() {
		// Given
		when(playersService.getPlayerById(anyLong()))
				.then(invocation -> getPlayerByIdBreakingReference(invocation.getArgument(0)));
		when(matchesService.getMatch(anyLong()))
				.then(invocation -> getMatchByIdBreakingReference(invocation.getArgument(0)));
		when(clubesMatchesService.getClubMatchByMatch(any(Match.class))).thenReturn(getClubMatch());
		when(lineupsMatchesService.getLineupMatchesByMatch(any(Match.class))).thenReturn(LINEUPMATCHES);

		when(changesDao.save(any(Change.class))).then(invocation -> {
			Change change = invocation.getArgument(0);
			change.setChangeId(1L);
			return change;
		});

		// When
		Change changeSaved = changesService.addChange(getChangeWithoutIdByPositionBreakingReference(0), "Local", 1L);

		// Then
		assertEquals(getChangeByIdBreakingReference(1L), changeSaved);
		verify(playersService, times(2)).getPlayerById(anyLong());
		verify(matchesService).getMatch(1L);
		verify(clubesMatchesService, times(2)).getClubMatchByMatch(any(Match.class));
		verify(lineupsMatchesService).getLineupMatchesByMatch(any(Match.class));
		verify(changesDao).save(any(Change.class));
	}
	
	@Test
	void testAddChangeSuccessed2() {
		// Given
		when(playersService.getPlayerById(anyLong()))
		.then(invocation -> getPlayerByIdBreakingReference(invocation.getArgument(0)));
		when(matchesService.getMatch(anyLong()))
		.then(invocation -> getMatchByIdBreakingReference(invocation.getArgument(0)));
		when(clubesMatchesService.getClubMatchByMatch(any(Match.class))).thenReturn(getClubMatch());
		when(lineupsMatchesService.getLineupMatchesByMatch(any(Match.class))).thenReturn(LINEUPMATCHES);
		
		when(changesDao.save(any(Change.class))).then(invocation -> {
			Change change = invocation.getArgument(0);
			change.setChangeId(4L);
			return change;
		});
		
		// When
		Change changeSaved = changesService.addChange(getChangeWithoutIdByPositionBreakingReference(3), "Visitor", 1L);
		
		// Then
		assertEquals(getChangeByIdBreakingReference(4L), changeSaved);
		verify(playersService, times(2)).getPlayerById(anyLong());
		verify(matchesService).getMatch(1L);
		verify(clubesMatchesService, times(2)).getClubMatchByMatch(any(Match.class));
		verify(lineupsMatchesService).getLineupMatchesByMatch(any(Match.class));
		verify(changesDao).save(any(Change.class));
	}

	@Test
	void testAddChangeFailedChangeNull() {
		// Then
		assertThrows(AddingChangeException.class, () -> {
			// When
			changesService.addChange(null, "Local", 1L);
		});

		verify(playersService, never()).getPlayerById(anyLong());
		verify(matchesService, never()).getMatch(1L);
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(lineupsMatchesService, never()).getLineupMatchesByMatch(any(Match.class));
		verify(changesDao, never()).save(any(Change.class));
	}

	@Test
	void testAddChangeFailedMinuteNull() {
		//Given
		Change changeMinuteNull = getChangeWithoutIdByPositionBreakingReference(0);
		changeMinuteNull.setMinute(null);
		
		// Then
		assertThrows(AddingChangeException.class, () -> {
			// When
			changesService.addChange(changeMinuteNull, "Local", 1L);
		});

		verify(playersService, never()).getPlayerById(anyLong());
		verify(matchesService, never()).getMatch(1L);
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(lineupsMatchesService, never()).getLineupMatchesByMatch(any(Match.class));
		verify(changesDao, never()).save(any(Change.class));
	}
	
	@Test
	void testAddChangeFailedMinuteLessThan0() {
		//Given
		Change changeMinuteLessThan0 = getChangeWithoutIdByPositionBreakingReference(0);
		changeMinuteLessThan0.setMinute(-1);
		
		// Then
		assertThrows(AddingChangeException.class, () -> {
			// When
			changesService.addChange(changeMinuteLessThan0, "Local", 1L);
		});
		
		verify(playersService, never()).getPlayerById(anyLong());
		verify(matchesService, never()).getMatch(1L);
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(lineupsMatchesService, never()).getLineupMatchesByMatch(any(Match.class));
		verify(changesDao, never()).save(any(Change.class));
	}

	@Test
	void testAddChangeFailedPlayerInNull() {
		//Given
		Change changePlayerInNull = getChangeWithoutIdByPositionBreakingReference(0);
		changePlayerInNull.setPlayerIn(null);
		
		// Then
		assertThrows(AddingChangeException.class, () -> {
			// When
			changesService.addChange(changePlayerInNull, "Local", 1L);
		});

		verify(playersService, never()).getPlayerById(anyLong());
		verify(matchesService, never()).getMatch(1L);
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(lineupsMatchesService, never()).getLineupMatchesByMatch(any(Match.class));
		verify(changesDao, never()).save(any(Change.class));
	}

	@Test
	void testAddChangeFailedPlayerInIdNull() {
		//Given
		Change changePlayerInIdNull = getChangeWithoutIdByPositionBreakingReference(0);
		changePlayerInIdNull.setPlayerIn(getPlayerWithoutIdByPositionBreakingReference(0));
		
		// Then
		assertThrows(AddingChangeException.class, () -> {
			// When
			changesService.addChange(changePlayerInIdNull, "Local", 1L);
		});

		verify(playersService, never()).getPlayerById(anyLong());
		verify(matchesService, never()).getMatch(1L);
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(lineupsMatchesService, never()).getLineupMatchesByMatch(any(Match.class));
		verify(changesDao, never()).save(any(Change.class));
	}

	@Test
	void testAddChangeFailedPlayerInIdLessThan1() {
		//Given
		Change changePlayerInIdLessThan1 = getChangeWithoutIdByPositionBreakingReference(0);
		changePlayerInIdLessThan1.setPlayerIn(PLAYER_WITH_ID_LESS_THAN_1);
		
		// Then
		assertThrows(AddingChangeException.class, () -> {
			// When
			changesService.addChange(changePlayerInIdLessThan1, "Local", 1L);
		});

		verify(playersService, never()).getPlayerById(anyLong());
		verify(matchesService, never()).getMatch(1L);
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(lineupsMatchesService, never()).getLineupMatchesByMatch(any(Match.class));
		verify(changesDao, never()).save(any(Change.class));
	}

	@Test
	void testAddChangeFailedPlayerOutNull() {
		//Given
		Change changePlayerOutNull = getChangeWithoutIdByPositionBreakingReference(0);
		changePlayerOutNull.setPlayerOut(null);
		
		// Then
		assertThrows(AddingChangeException.class, () -> {
			// When
			changesService.addChange(changePlayerOutNull, "Local", 1L);
		});

		verify(playersService, never()).getPlayerById(anyLong());
		verify(matchesService, never()).getMatch(1L);
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(lineupsMatchesService, never()).getLineupMatchesByMatch(any(Match.class));
		verify(changesDao, never()).save(any(Change.class));
	}

	@Test
	void testAddChangeFailedPlayerOutIdNull() {
		//Given
		Change changePlayerOutIdNull = getChangeWithoutIdByPositionBreakingReference(0);
		changePlayerOutIdNull.setPlayerOut(getPlayerWithoutIdByPositionBreakingReference(0));
		
		// Then
		assertThrows(AddingChangeException.class, () -> {
			// When
			changesService.addChange(changePlayerOutIdNull, "Local", 1L);
		});

		verify(playersService, never()).getPlayerById(anyLong());
		verify(matchesService, never()).getMatch(1L);
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(lineupsMatchesService, never()).getLineupMatchesByMatch(any(Match.class));
		verify(changesDao, never()).save(any(Change.class));
	}

	@Test
	void testAddChangeFailedPlayerOutIdLessThan1() {
		//Given
		Change changePlayerOutIdLessThan1 = getChangeWithoutIdByPositionBreakingReference(0);
		changePlayerOutIdLessThan1.setPlayerOut(PLAYER_WITH_ID_LESS_THAN_1);
		
		// Then
		assertThrows(AddingChangeException.class, () -> {
			// When
			changesService.addChange(changePlayerOutIdLessThan1, "Local", 1L);
		});

		verify(playersService, never()).getPlayerById(anyLong());
		verify(matchesService, never()).getMatch(1L);
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(lineupsMatchesService, never()).getLineupMatchesByMatch(any(Match.class));
		verify(changesDao, never()).save(any(Change.class));
	}

	@Test
	void testAddChangeFailedPlayersAreEqual() {
		// Then
		assertThrows(AddingChangeException.class, () -> {
			// When
			changesService.addChange(CHANGE_WITH_SAME_PLAYERS_MATCH, "Local", 1L);
		});

		verify(playersService, never()).getPlayerById(anyLong());
		verify(matchesService, never()).getMatch(1L);
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(lineupsMatchesService, never()).getLineupMatchesByMatch(any(Match.class));
		verify(changesDao, never()).save(any(Change.class));
	}
	
	@Test
	void testAddChangeFailedMatchIdNull() {
		// Then
		assertThrows(AddingChangeException.class, () -> {
			// When
			changesService.addChange(getChangeWithoutIdByPositionBreakingReference(0), "Local", null);
		});
		
		verify(playersService, never()).getPlayerById(anyLong());
		verify(matchesService, never()).getMatch(1L);
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(lineupsMatchesService, never()).getLineupMatchesByMatch(any(Match.class));
		verify(changesDao, never()).save(any(Change.class));
	}
	
	@Test
	void testAddChangeFailedMatchIdlessThan1() {
		// Then
		assertThrows(AddingChangeException.class, () -> {
			// When
			changesService.addChange(getChangeWithoutIdByPositionBreakingReference(0), "Local", 0L);
		});
		
		verify(playersService, never()).getPlayerById(anyLong());
		verify(matchesService, never()).getMatch(1L);
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(lineupsMatchesService, never()).getLineupMatchesByMatch(any(Match.class));
		verify(changesDao, never()).save(any(Change.class));
	}

	@Test
	void testAddChangeFailedAnyPlayerDoesNotBelongToClub() {
		// Given
		when(playersService.getPlayerById(anyLong()))
				.then(invocation -> getPlayerByIdBreakingReference(invocation.getArgument(0)));
		when(matchesService.getMatch(anyLong()))
				.then(invocation -> getMatchByIdBreakingReference(invocation.getArgument(0)));
		when(clubesMatchesService.getClubMatchByMatch(any(Match.class))).thenReturn(getClubMatch());
		
		// Then
		assertThrows(AddingChangeException.class, () -> {
			// When
			changesService.addChange(CHANGE_WITH_PLAYER_IN_OUT_OF_MATCH, "Local", 1L);
		});

		verify(playersService, times(2)).getPlayerById(anyLong());
		verify(matchesService).getMatch(1L);
		verify(clubesMatchesService, times(1)).getClubMatchByMatch(any(Match.class));
		verify(lineupsMatchesService, never()).getLineupMatchesByMatch(any(Match.class));
		verify(changesDao, never()).save(any(Change.class));
	}

	@Test
	void testAddChangeFailedAnyPlayerDoesNotExists() {
		// Given
		when(playersService.getPlayerById(anyLong())).thenThrow(PlayerNotFoundException.class);
		
		// Then
		assertThrows(PlayerNotFoundException.class, () -> {
			// When
			changesService.addChange(getChangeWithoutIdByPositionBreakingReference(0), "Local", 1L);
		});

		verify(playersService).getPlayerById(anyLong());
		verify(matchesService, never()).getMatch(1L);
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(lineupsMatchesService, never()).getLineupMatchesByMatch(any(Match.class));
		verify(changesDao, never()).save(any(Change.class));
	}

	@Test
	void testAddChangeFailedMatchDoesNotExists() {
		// Given
		when(playersService.getPlayerById(anyLong()))
				.then(invocation -> getPlayerByIdBreakingReference(invocation.getArgument(0)));
		when(matchesService.getMatch(anyLong())).thenThrow(MatchNotFoundException.class);
		
		// Then
		assertThrows(MatchNotFoundException.class, () -> {
			// When
			changesService.addChange(getChangeWithoutIdByPositionBreakingReference(0), "Local", 1L);
		});

		verify(playersService, times(2)).getPlayerById(anyLong());
		verify(matchesService).getMatch(1L);
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(lineupsMatchesService, never()).getLineupMatchesByMatch(any(Match.class));
		verify(changesDao, never()).save(any(Change.class));
	}
	
	@Test
	void testAddChangeFaliedLineupMatchDoesNotExists() {
		// Given
		when(playersService.getPlayerById(anyLong()))
				.then(invocation -> getPlayerByIdBreakingReference(invocation.getArgument(0)));
		when(matchesService.getMatch(anyLong()))
				.then(invocation -> getMatchByIdBreakingReference(invocation.getArgument(0)));
		when(clubesMatchesService.getClubMatchByMatch(any(Match.class))).thenReturn(getClubMatch());
		when(lineupsMatchesService.getLineupMatchesByMatch(any(Match.class))).thenReturn(Collections.emptyList());

		// Then
		assertThrows(LineupMatchNotFoundException.class, () -> {
			// When
			changesService.addChange(getChangeWithoutIdByPositionBreakingReference(0), "Local", 1L);
		});

		verify(playersService, times(2)).getPlayerById(anyLong());
		verify(matchesService).getMatch(1L);
		verify(clubesMatchesService, times(2)).getClubMatchByMatch(any(Match.class));
		verify(lineupsMatchesService).getLineupMatchesByMatch(any(Match.class));
		verify(changesDao, never()).save(any(Change.class));
	}
	
	@Test
	void testAddChangeFaliedClubStatusNotValid() {
		// Given
		when(playersService.getPlayerById(anyLong()))
				.then(invocation -> getPlayerByIdBreakingReference(invocation.getArgument(0)));
		when(matchesService.getMatch(anyLong()))
				.then(invocation -> getMatchByIdBreakingReference(invocation.getArgument(0)));
		when(clubesMatchesService.getClubMatchByMatch(any(Match.class))).thenReturn(getClubMatch());
		
		assertThrows(WritingInformationException.class, () -> {
			// When
			changesService.addChange(getChangeWithoutIdByPositionBreakingReference(0), "Club Status Not Valid", 1L);
		});

		// Then
		verify(playersService, times(2)).getPlayerById(anyLong());
		verify(matchesService).getMatch(1L);
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(lineupsMatchesService, never()).getLineupMatchesByMatch(any(Match.class));
		verify(changesDao, never()).save(any(Change.class));
	}

	// PUT

//	@Test
//	void testUpdateChange() {
//	}

	// DELETE

	@Test
	void testDeleteChangeSuccessed() {
		//Given
		when(changesDao.findById(anyLong())).then(invocation -> Optional.of(getChangeByIdBreakingReference(invocation.getArgument(0))));
		
		//When
		Change changeDeleted = changesService.deleteChange(1L);
		
		//Then
		assertEquals(getChangeByIdBreakingReference(1L), changeDeleted);
		verify(changesDao).findById(1L);
		verify(changesDao).deleteById(1L);
	}
	
	@Test
	void testDeleteChangeFailed() {
		//Given
		when(changesDao.findById(anyLong())).thenReturn(Optional.empty());
		
		//Then
		assertThrows(ChangeNotFoundException.class, () -> {
			//When
			changesService.deleteChange(1L);
			
		});
		
		verify(changesDao).findById(1L);
		verify(changesDao, never()).deleteById(anyLong());
	}
}
