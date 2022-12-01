package com.fernandobetancourt.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fernandobetancourt.exceptions.AddingGoalException;
import com.fernandobetancourt.exceptions.GoalNotFoundException;
import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.dao.IGoalsDao;
import com.fernandobetancourt.model.entity.ClubMatch;
import com.fernandobetancourt.model.entity.Goal;
import com.fernandobetancourt.model.entity.Match;
import com.fernandobetancourt.model.entity.Player;

@Service
public class GoalsServiceImpl implements IGoalsService {
	
	@Autowired
	private IGoalsDao goalsDao;
	
	@Autowired
	private IMatchesService matchesService;
	
	@Autowired
	private IPlayersService playersService;
	
	@Autowired
	private IClubesMatchesService clubesMatchesService;

	//GET
	
	@Override
	public List<Goal> getGoalsByMatch(Long matchId) throws InformationNotFoundException {
		
		Match matchRecovered = this.matchesService.getMatch(matchId);
		
		return this.goalsDao.findByScoreboard(matchRecovered.getScoreboard()).orElseThrow(() -> {
			throw new GoalNotFoundException("Goals not found");
		});
	}
	
	//POST

	@Override
	public Goal addGoal(Goal goal, Long matchId) throws InformationNotFoundException, WritingInformationException {
		this.isGoalValidToSave(goal);
		Match matchRecovered = this.matchesService.getMatch(matchId);
		goal.setScoreboard(matchRecovered.getScoreboard());
		this.playerBelongToAnyClub(goal.getPlayer(), matchRecovered);
		return this.goalsDao.save(goal);
	}
	
	//PUT

	@Override
	public Goal updateGoal(Goal goal, Long matchId) throws InformationNotFoundException, WritingInformationException {
		this.isGoalValidToUpdate(goal);
		Match matchRecovered = this.matchesService.getMatch(matchId);
		goal.setScoreboard(matchRecovered.getScoreboard());
		this.playerBelongToAnyClub(goal.getPlayer(), matchRecovered);
		return this.goalsDao.save(goal);
	}
	
	//DELETE

	@Override
	public Goal deleteGoal(Long id) throws InformationNotFoundException {
		Goal goalDeleted = this.goalExists(id);
		this.goalsDao.deleteById(id);
		return goalDeleted;
	}
	
	//VALIDATIONS

	@Override
	public boolean isGoalValidToSave(Goal goal) throws InformationNotFoundException, WritingInformationException {
		//No tiene validacion del scoreboard
		if(		goal == null ||
				goal.getMinute() == null || goal.getMinute() < 0 ||
				goal.getPlayer() == null || goal.getPlayer().getPlayerId() == null || goal.getPlayer().getPlayerId() < 1
				) {
					
			throw new AddingGoalException("Goal is not valid to save");
					
		}
		//Player exists se remplaza con getPlayerById
//		goal.setPlayer(this.playersService.playerExists(goal.getPlayer().getPlayerId()));
		goal.setPlayer(this.playersService.getPlayerById(goal.getPlayer().getPlayerId()));
				
		return true;
	}

	@Override
	public boolean isGoalValidToUpdate(Goal goal) throws InformationNotFoundException, WritingInformationException {
		//No tiene validacion del scoreboard
		if(		goal == null ||
				goal.getGoalId() == null || goal.getGoalId() < 1 ||
				goal.getMinute() == null || goal.getMinute() < 0 ||
				goal.getPlayer() == null || goal.getPlayer().getPlayerId() == null || goal.getPlayer().getPlayerId() < 1
				) {
			
			throw new AddingGoalException("Goal is not valid to save");
			
		}
		
		this.goalExists(goal.getGoalId());
		//Player exists se remplaza con getPlayerById
//		goal.setPlayer(this.playersService.playerExists(goal.getPlayer().getPlayerId()));
		goal.setPlayer(this.playersService.getPlayerById(goal.getPlayer().getPlayerId()));
		
		return true;
	}

	@Override
	public Goal goalExists(Long id) throws InformationNotFoundException {
		return this.goalsDao.findById(id).orElseThrow(() -> {
			StringBuilder sb = new StringBuilder().append("Goal with id ").append(id).append(" has not been found");
			throw new GoalNotFoundException(sb.toString());
		});
	}
	
	private boolean playerBelongToAnyClub(Player player, Match match) {
		ClubMatch clubMatchRecovered = this.clubesMatchesService.getClubMatchByMatch(match);
		
		if(		clubMatchRecovered.getLocalClub().getClubId() != player.getClub().getClubId() &&
				clubMatchRecovered.getVisitorClub().getClubId() != player.getClub().getClubId()) {
			StringBuilder sb = new StringBuilder().append("Player with id ").append(player.getPlayerId())
					.append(" doedn't belong to neither club");
			throw new AddingGoalException(sb.toString());
		}
		
		return true;
	}

}
