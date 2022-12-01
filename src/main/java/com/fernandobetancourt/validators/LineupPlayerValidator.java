package com.fernandobetancourt.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fernandobetancourt.exceptions.AddingLineupPlayerException;
import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.LineupPlayerNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.dao.ILineupsPlayersDao;
import com.fernandobetancourt.model.entity.LineupPlayer;
import com.fernandobetancourt.model.entity.Player;
import com.fernandobetancourt.services.ILineupsService;
import com.fernandobetancourt.services.IPlayersService;

@Component
public class LineupPlayerValidator {
	
	@Autowired
	private ILineupsService lineupsService;
	
	@Autowired
	private IPlayersService playersService;
	
	@Autowired
	private ILineupsPlayersDao lineupsPlayersDao;
	
	public Player isLineupPlayerValidToSave(LineupPlayer lineupPlayer) throws InformationNotFoundException, WritingInformationException {
		//Las validaciones de Lineup no son necesarias porque estos se optienen a través de un LineupMatch, por lo que si este existe, entonces
		//el Lineup debe de existir
		if(		
//				lineupPlayer == null ||
				lineupPlayer.getPlayerStatus() == null || lineupPlayer.getPlayerStatus().trim().equals("") ||
				lineupPlayer.getPlayer() == null || lineupPlayer.getPlayer().getPlayerId() == null || lineupPlayer.getPlayer().getPlayerId() < 1 
//				|| lineupPlayer.getLineup() == null || lineupPlayer.getLineup().getLineupId() == null || lineupPlayer.getLineup().getLineupId() < 1
				
				) {
			
			throw new AddingLineupPlayerException("LineupPlayer is not valid to save");
			
		}

		this.lineupsService.getLineup(lineupPlayer.getLineup().getLineupId());
		
		
		return this.playersService.getPlayerById(lineupPlayer.getPlayer().getPlayerId());
	}

//	public boolean isLineupPlayerValidToUpdate(LineupPlayer lineupPlayer) throws InformationNotFoundException, WritingInformationException {
//		
//		if(		lineupPlayer == null ||
//				lineupPlayer.getLineupPlayerId() == null || lineupPlayer.getLineupPlayerId() < 1 ||
//				lineupPlayer.getPlayerStatus() == null || lineupPlayer.getPlayerStatus().trim().equals("") ||
//				lineupPlayer.getPlayer() == null || lineupPlayer.getPlayer().getPlayerId() == null || lineupPlayer.getPlayer().getPlayerId() < 1 ||
//				lineupPlayer.getLineup() == null || lineupPlayer.getLineup().getLineupId() == null || lineupPlayer.getLineup().getLineupId() < 1
//				) {
//			
//			throw new AddingLineupPlayerException("LineupPlayer is not valid to save");
//			
//		}
//		
//		this.lineupPlayerExists(lineupPlayer.getLineupPlayerId());
//		this.playersService.getPlayerById(lineupPlayer.getPlayer().getPlayerId());
//		//lineupExists se remplaza con getLineup
////		this.lineupsService.lineupExists(lineupPlayer.getLineup().getLineupId());
//		this.lineupsService.getLineup(lineupPlayer.getLineup().getLineupId());
//		
//		return true;
//	}

	public LineupPlayer lineupPlayerExists(Long id) throws InformationNotFoundException {
		return this.lineupsPlayersDao.findById(id).orElseThrow(() -> {
			StringBuilder sb = new StringBuilder().append("LineupPlayer with id ").append(id).append(" has not been found");
			throw new LineupPlayerNotFoundException(sb.toString());
		});
	}
}
