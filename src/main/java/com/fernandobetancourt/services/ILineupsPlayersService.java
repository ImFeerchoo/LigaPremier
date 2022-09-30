package com.fernandobetancourt.services;

import java.util.List;

import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.entity.LineupPlayer;

public interface ILineupsPlayersService {
	
	//GET
	public abstract LineupPlayer getLineupPlayer(Long id) throws InformationNotFoundException;
	public abstract List<LineupPlayer> getLineupsPlayersByMatch(Long matchId) throws InformationNotFoundException;
	
	//POST
	public abstract List<LineupPlayer> addLineupsPlayers(List<LineupPlayer> lineupsPlayers, Long matchId, String clubStatus) 
			throws InformationNotFoundException, WritingInformationException;
	
	//PUT
	public abstract LineupPlayer updateLineupPlayer(LineupPlayer lineupPlayer) throws InformationNotFoundException, WritingInformationException;
	
	//DELETE
	public abstract LineupPlayer deleteLineupPlayer(Long id) throws InformationNotFoundException;
	
	//VALIDATIONS
	public abstract boolean isLineupPlayerValidToSave(LineupPlayer lineupPlayer) throws InformationNotFoundException, WritingInformationException;
	public abstract boolean isLineupPlayerValidToUpdate(LineupPlayer lineupPlayer) throws InformationNotFoundException, WritingInformationException;
	public abstract LineupPlayer lineupPlayerExists(Long id) throws InformationNotFoundException;

}
