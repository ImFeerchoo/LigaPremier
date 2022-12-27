package com.fernandobetancourt.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fernandobetancourt.exceptions.AddingGoalException;
import com.fernandobetancourt.exceptions.GoalNotFoundException;
import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.dao.IGoalsDao;
import com.fernandobetancourt.model.entity.ClubMatch;
import com.fernandobetancourt.model.entity.Goal;
import com.fernandobetancourt.model.entity.Match;
import com.fernandobetancourt.model.entity.Player;
import com.fernandobetancourt.services.IClubesMatchesService;
import com.fernandobetancourt.services.IPlayersService;

@Component
public class GoalValidator {

	@Autowired
	private IPlayersService playersService;
	
	@Autowired
	private IClubesMatchesService clubesMatchesService;
	
	@Autowired
	private IGoalsDao goalsDao;

	public boolean isGoalValidToSave(Goal goal) throws InformationNotFoundException, WritingInformationException {
		//No tiene validacion del scoreboard
		if(		goal == null ||
				goal.getMinute() == null || goal.getMinute() < 0 ||
				goal.getPlayer() == null || goal.getPlayer().getPlayerId() == null || goal.getPlayer().getPlayerId() < 1
				) {
					
			throw new AddingGoalException("Goal is not valid to save");
					
		}
		goal.setPlayer(this.playersService.getPlayerById(goal.getPlayer().getPlayerId()));
				
		return true;
	}

//	@Override
//	public boolean isGoalValidToUpdate(Goal goal) throws InformationNotFoundException, WritingInformationException {
//		//No tiene validacion del scoreboard
//		if(		goal == null ||
//				goal.getGoalId() == null || goal.getGoalId() < 1 ||
//				goal.getMinute() == null || goal.getMinute() < 0 ||
//				goal.getPlayer() == null || goal.getPlayer().getPlayerId() == null || goal.getPlayer().getPlayerId() < 1
//				) {
//			
//			throw new AddingGoalException("Goal is not valid to save");
//			
//		}
//		
//		this.goalExists(goal.getGoalId());
//		//Player exists se remplaza con getPlayerById
////		goal.setPlayer(this.playersService.playerExists(goal.getPlayer().getPlayerId()));
//		goal.setPlayer(this.playersService.getPlayerById(goal.getPlayer().getPlayerId()));
//		
//		return true;
//	}

	public Goal goalExists(Long id) throws InformationNotFoundException {
		return this.goalsDao.findById(id).orElseThrow(() -> {
			StringBuilder sb = new StringBuilder().append("Goal with id ").append(id).append(" has not been found");
			throw new GoalNotFoundException(sb.toString());
		});
	}
	
	public boolean playerBelongToAnyClub(Player player, Match match) {
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
