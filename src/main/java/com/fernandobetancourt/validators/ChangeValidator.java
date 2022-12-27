package com.fernandobetancourt.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fernandobetancourt.exceptions.AddingChangeException;
import com.fernandobetancourt.exceptions.ChangeNotFoundException;
import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.dao.IChangesDao;
import com.fernandobetancourt.model.entity.Change;
import com.fernandobetancourt.model.entity.Club;
import com.fernandobetancourt.model.entity.Match;
import com.fernandobetancourt.model.entity.Player;
import com.fernandobetancourt.services.IClubesMatchesService;
import com.fernandobetancourt.services.IMatchesService;
import com.fernandobetancourt.services.IPlayersService;

@Component
public class ChangeValidator {
	
	@Autowired
	private IPlayersService playersService;
	
	@Autowired
	private IMatchesService matchesService;
	
	@Autowired
	private IChangesDao changesDao;
	
	@Autowired
	private IClubesMatchesService clubesMatchesService;

	//Verificar que los players pertenezcan al club antes de setearselos al change
	public boolean isChangeValidToSave(Change change, Long matchId) throws InformationNotFoundException, WritingInformationException {
		
		if(		change == null ||
				change.getMinute() == null || change.getMinute() < 0 ||
				change.getPlayerIn() == null || change.getPlayerIn().getPlayerId() == null || change.getPlayerIn().getPlayerId() < 1 ||
				change.getPlayerOut() == null || change.getPlayerOut().getPlayerId() == null || change.getPlayerOut().getPlayerId() < 1 ||
				change.getPlayerIn().getPlayerId() == change.getPlayerOut().getPlayerId() ||
				matchId == null || matchId < 1
				) {
			
			throw new AddingChangeException("Change is not valid to save");
			
		}
		change.setPlayerIn(this.playersService.getPlayerById(change.getPlayerIn().getPlayerId()));
		change.setPlayerOut(this.playersService.getPlayerById(change.getPlayerOut().getPlayerId()));
		
		change.setMatch(this.matchesService.getMatch(matchId));
		
		return true;
	}

//	public boolean isChangeValidToUpdate(Change change, Long matchId) throws InformationNotFoundException, WritingInformationException {
//		
//		if(		change == null ||
//				change.getChangeId() == null || change.getChangeId() < 1 ||
//				change.getMinute() == null || change.getMinute() < 0 ||
//				change.getPlayerIn() == null || change.getPlayerIn().getPlayerId() == null || change.getPlayerIn().getPlayerId() < 1 ||
//				change.getPlayerOut() == null || change.getPlayerOut().getPlayerId() == null || change.getPlayerOut().getPlayerId() < 1 ||
//				change.getPlayerIn().getPlayerId() == change.getPlayerOut().getPlayerId() ||
//				matchId == null || matchId < 1
//				) {
//			
//			throw new AddingChangeException("Change is not valid to save");
//			
//		}
//		
//		this.changeExists(change.getChangeId());
//		//Player exists se remplaza con getPlayerById
////		change.setPlayerIn(this.playersService.playerExists(change.getPlayerIn().getPlayerId()));
//		change.setPlayerIn(this.playersService.getPlayerById(change.getPlayerIn().getPlayerId()));
//		//Player exists se remplaza con getPlayerById
////		change.setPlayerOut(this.playersService.playerExists(change.getPlayerOut().getPlayerId()));
//		change.setPlayerOut(this.playersService.getPlayerById(change.getPlayerOut().getPlayerId()));
//		
//		//Cambie el matchExists por getMatch
////		change.setMatch(this.matchesService.matchExists(change.getMatch().getMatchId()));
//		change.setMatch(this.matchesService.getMatch(change.getMatch().getMatchId()));
//		
//		return true;
//	}

	public Change changeExists(Long id) throws InformationNotFoundException {
		return this.changesDao.findById(id).orElseThrow(() -> {
			StringBuilder sb = new StringBuilder().append("Change with id ").append(id).append(" has not been found");
			throw new ChangeNotFoundException(sb.toString());
		});
	}
	
	public boolean playerBelongToClub(Player player, Match match, String clubStatus) {
		
		Club clubRecovered;
		if(clubStatus.equalsIgnoreCase("Local")) {
			clubRecovered = this.clubesMatchesService.getClubMatchByMatch(match).getLocalClub();
		}else if(clubStatus.equalsIgnoreCase("Visitor")) {
			clubRecovered = this.clubesMatchesService.getClubMatchByMatch(match).getVisitorClub();
		}else {
			throw new WritingInformationException("ClubStatus is not valid");
		}
		
		if(player.getClub().getClubId() != clubRecovered.getClubId()) {
			StringBuilder sb = new StringBuilder().append("Player with id ").append(player.getPlayerId())
					.append(" doesn't belong to club with id ").append(clubRecovered.getClubId());
			throw new AddingChangeException(sb.toString());
		}
		
		return true;
	}
}
