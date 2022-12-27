package com.fernandobetancourt.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fernandobetancourt.exceptions.GoalNotFoundException;
import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.dao.IGoalsDao;
import com.fernandobetancourt.model.entity.Goal;
import com.fernandobetancourt.model.entity.Match;
import com.fernandobetancourt.validators.GoalValidator;

@Service
public class GoalsServiceImpl implements IGoalsService {
	
	@Autowired
	private IGoalsDao goalsDao;
	
	@Autowired
	private IMatchesService matchesService;
	
	@Autowired
	private GoalValidator goalValidator;

	//GET
	
//	@Override
//	public List<Goal> getGoalsByMatch(Long matchId) throws InformationNotFoundException {
//		
//		Match matchRecovered = this.matchesService.getMatch(matchId);
//		
//		return this.goalsDao.findByScoreboard(matchRecovered.getScoreboard()).orElseThrow(() -> {
//			throw new GoalNotFoundException("Goals not found");
//		});
//	}
	
	@Override
	public List<Goal> getGoalsByMatch(Long matchId) throws InformationNotFoundException {
		
		Match matchRecovered = this.matchesService.getMatch(matchId);
		
		List<Goal> goals = goalsDao.findByScoreboard(matchRecovered.getScoreboard());
		if(goals.isEmpty()) throw new GoalNotFoundException("Has not been found goals to this match");
		return goals;
	}
	
	//POST

	@Override
	public Goal addGoal(Goal goal, Long matchId) throws InformationNotFoundException, WritingInformationException {
		goalValidator.isGoalValidToSave(goal);
		Match matchRecovered = this.matchesService.getMatch(matchId);
		goal.setScoreboard(matchRecovered.getScoreboard());
		goalValidator.playerBelongToAnyClub(goal.getPlayer(), matchRecovered);
		return this.goalsDao.save(goal);
	}
	
	//PUT

//	@Override
//	public Goal updateGoal(Goal goal, Long matchId) throws InformationNotFoundException, WritingInformationException {
//		this.isGoalValidToUpdate(goal);
//		Match matchRecovered = this.matchesService.getMatch(matchId);
//		goal.setScoreboard(matchRecovered.getScoreboard());
//		this.playerBelongToAnyClub(goal.getPlayer(), matchRecovered);
//		return this.goalsDao.save(goal);
//	}
	
	//DELETE

	@Override
	public Goal deleteGoal(Long id) throws InformationNotFoundException {
		Goal goalDeleted = goalValidator.goalExists(id);
		this.goalsDao.deleteById(id);
		return goalDeleted;
	}
	
}
