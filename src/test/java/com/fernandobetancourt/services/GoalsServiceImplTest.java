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

import com.fernandobetancourt.exceptions.AddingGoalException;
import com.fernandobetancourt.exceptions.GoalNotFoundException;
import com.fernandobetancourt.exceptions.MatchNotFoundException;
import com.fernandobetancourt.exceptions.PlayerNotFoundException;
import com.fernandobetancourt.model.dao.IGoalsDao;
import com.fernandobetancourt.model.entity.Goal;
import com.fernandobetancourt.model.entity.Match;
import com.fernandobetancourt.model.entity.Scoreboard;
import com.fernandobetancourt.validators.GoalValidator;

@SpringBootTest(classes = {GoalsServiceImpl.class, GoalValidator.class})
class GoalsServiceImplTest {

	@MockBean
	private IGoalsDao goalsDao;

	@MockBean
	private IMatchesService matchesService;

	@MockBean
	private IPlayersService playersService;

	@MockBean
	private IClubesMatchesService clubesMatchesService;

	@Autowired
	private IGoalsService goalsService;

	// GET

	@Test
	void testGetGoalsByMatchSuccessed() {
		// Given
		when(matchesService.getMatch(anyLong()))
				.then(invocation -> getMatchByIdBreakingReference(invocation.getArgument(0)));
		when(goalsDao.findByScoreboard(any(Scoreboard.class))).thenReturn(GOALS_WITH_ID_MATCH_1);

		// When
		List<Goal> goals = goalsService.getGoalsByMatch(1L);

		// Then
		assertEquals(GOALS_WITH_ID_MATCH_1, goals);
		assertEquals(7, goals.size());
		verify(matchesService).getMatch(1L);
		verify(goalsDao).findByScoreboard(any(Scoreboard.class));
	}

	@Test
	void testGetGoalsByMatchEmptyList() {
		// Given
		when(matchesService.getMatch(anyLong()))
				.then(invocation -> getMatchByIdBreakingReference(invocation.getArgument(0)));
		when(goalsDao.findByScoreboard(any(Scoreboard.class))).thenReturn(Collections.emptyList());

		// Then
		assertThrows(GoalNotFoundException.class, () -> {
			// When
			goalsService.getGoalsByMatch(1L);
		});

		verify(matchesService).getMatch(1L);
		verify(goalsDao).findByScoreboard(any(Scoreboard.class));
	}

	// POST

	@Test
	void testAddGoalSuccessed() {
		//Given
		when(playersService.getPlayerById(anyLong())).then(invocation -> getPlayerByIdBreakingReference(invocation.getArgument(0)));
		when(matchesService.getMatch(anyLong())).then(invocation -> getMatchByIdBreakingReference(invocation.getArgument(0)));
		when(clubesMatchesService.getClubMatchByMatch(any(Match.class))).thenReturn(getClubMatch());
		
		when(goalsDao.save(any(Goal.class))).then(invocation -> {
			Goal goal = invocation.getArgument(0);
			goal.setGoalId(1L);
			return goal;
		});
		
		//When
		Goal goalSaved = goalsService.addGoal(getGoalWithoutIdByPositionBreakingReference(0), 1L);
		
		//Then
		assertEquals(getGoalByIdBreakingReference(1L), goalSaved);
		verify(playersService).getPlayerById(anyLong());
		verify(matchesService).getMatch(anyLong());
		verify(clubesMatchesService).getClubMatchByMatch(any(Match.class));
		verify(goalsDao).save(any(Goal.class));
	}

	@Test
	void testAddGoalFailedGoalNull() {
		//Then
		assertThrows(AddingGoalException.class, () -> {
			//When
			goalsService.addGoal(null, 1L);
		});
		
		verify(playersService, never()).getPlayerById(anyLong());
		verify(matchesService, never()).getMatch(anyLong());
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(goalsDao, never()).save(any(Goal.class));
	}

	@Test
	void testAddGoalFailedMinuteNull() {
		//Given
		Goal goalMinuteNull = getGoalWithoutIdByPositionBreakingReference(0);
		goalMinuteNull.setMinute(null);
		
		//Then
		assertThrows(AddingGoalException.class, () -> {
			//When
			goalsService.addGoal(goalMinuteNull, 1L);
		});
		
		verify(playersService, never()).getPlayerById(anyLong());
		verify(matchesService, never()).getMatch(anyLong());
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(goalsDao, never()).save(any(Goal.class));
	}

	@Test
	void testAddGoalFailedMinuteLessThan0() {
		//Given
		Goal goalMinuteLessThan0 = getGoalWithoutIdByPositionBreakingReference(0);
		goalMinuteLessThan0.setMinute(-1);
		
		//Then
		assertThrows(AddingGoalException.class, () -> {
			//When
			goalsService.addGoal(goalMinuteLessThan0, 1L);
		});
		
		verify(playersService, never()).getPlayerById(anyLong());
		verify(matchesService, never()).getMatch(anyLong());
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(goalsDao, never()).save(any(Goal.class));
	}

	@Test
	void testAddGoalFailedPlayerNull() {
		//Given
		Goal goalPlayerNull = getGoalWithoutIdByPositionBreakingReference(0);
		goalPlayerNull.setPlayer(null);
		
		//Then
		assertThrows(AddingGoalException.class, () -> {
			//When
			goalsService.addGoal(goalPlayerNull, 1L);
		});
		
		verify(playersService, never()).getPlayerById(anyLong());
		verify(matchesService, never()).getMatch(anyLong());
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(goalsDao, never()).save(any(Goal.class));
	}

	@Test
	void testAddGoalFailedPlayerIdNull() {
		//Given
		Goal goalPlayerIdNull = getGoalWithoutIdByPositionBreakingReference(0);
		goalPlayerIdNull.setPlayer(getPlayerWithoutIdByPositionBreakingReference(0));
		
		//Then
		assertThrows(AddingGoalException.class, () -> {
			//When
			goalsService.addGoal(goalPlayerIdNull, 1L);
		});
		
		verify(playersService, never()).getPlayerById(anyLong());
		verify(matchesService, never()).getMatch(anyLong());
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(goalsDao, never()).save(any(Goal.class));
	}

	@Test
	void testAddGoalFailedPlayerIdLessThan1() {
		//Given
		Goal goalPlayerIdLessThan1 = getGoalWithoutIdByPositionBreakingReference(0);
		goalPlayerIdLessThan1.setPlayer(PLAYER_WITH_ID_LESS_THAN_1);
		
		//Then
		assertThrows(AddingGoalException.class, () -> {
			//When
			goalsService.addGoal(goalPlayerIdLessThan1, 1L);
		});
		
		verify(playersService, never()).getPlayerById(anyLong());
		verify(matchesService, never()).getMatch(anyLong());
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(goalsDao, never()).save(any(Goal.class));
	}

	@Test
	void testAddGoalFailedPlayerDoesNotExists() {
		//Given
		when(playersService.getPlayerById(anyLong())).thenThrow(PlayerNotFoundException.class);
		Goal goal = getGoalWithoutIdByPositionBreakingReference(0);
		
		//Then
		assertThrows(PlayerNotFoundException.class, () -> {
			//When
			goalsService.addGoal(goal, 1L);
		});
		
		verify(playersService).getPlayerById(anyLong());
		verify(matchesService, never()).getMatch(anyLong());
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(goalsDao, never()).save(any(Goal.class));
	}

	@Test
	void testAddGoalFailedMatchDoesNotExists() {
		//Given
		when(playersService.getPlayerById(anyLong())).then(invocation -> getPlayerByIdBreakingReference(invocation.getArgument(0)));
		when(matchesService.getMatch(anyLong())).thenThrow(MatchNotFoundException.class);
		Goal goal = getGoalWithoutIdByPositionBreakingReference(0);
		
		//Then
		assertThrows(MatchNotFoundException.class, () -> {
			//When
			goalsService.addGoal(goal, 1L);
		});
		
		verify(playersService).getPlayerById(anyLong());
		verify(matchesService).getMatch(anyLong());
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(goalsDao, never()).save(any(Goal.class));
	}

	@Test
	void testAddGoalFailedPlayerDoesNotBelongToAnyClub() {
		//Given
		when(playersService.getPlayerById(anyLong())).then(invocation -> getPlayerByIdBreakingReference(invocation.getArgument(0)));
		when(matchesService.getMatch(anyLong())).then(invocation -> getMatchByIdBreakingReference(invocation.getArgument(0)));
		when(clubesMatchesService.getClubMatchByMatch(any(Match.class))).thenReturn(getClubMatch());
		
		//Then
		assertThrows(AddingGoalException.class, () -> {
			//When
			goalsService.addGoal(GOAL_WITH_PLAYER_OUT_OF_MATCH, 1L);
		});
		
		verify(playersService).getPlayerById(anyLong());
		verify(matchesService).getMatch(anyLong());
		verify(clubesMatchesService).getClubMatchByMatch(any(Match.class));
		verify(goalsDao, never()).save(any(Goal.class));
	}

	// PUT

//	@Test
//	void testUpdateGoal() {
//	}

	// DELETE

	@Test
	void testDeleteGoalSuccessed() {
		//Given
		when(goalsDao.findById(anyLong())).then(invocation -> Optional.of(getGoalByIdBreakingReference(invocation.getArgument(0))));
		
		//When
		Goal goalDeleted = goalsService.deleteGoal(1L);
		
		//Then
		assertEquals(getGoalByIdBreakingReference(1L), goalDeleted);
		verify(goalsDao).findById(1L);
		verify(goalsDao).deleteById(1L);
	}
	
	@Test
	void testDeleteGoalFailed() {
		//Given
		when(goalsDao.findById(anyLong())).thenReturn(Optional.empty());
		
		//Then
		assertThrows(GoalNotFoundException.class, () -> {
			//When
			goalsService.deleteGoal(1L);
		});
		
		verify(goalsDao).findById(1L);
		verify(goalsDao, never()).deleteById(1L);
	}

}
