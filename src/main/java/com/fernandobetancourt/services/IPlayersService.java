package com.fernandobetancourt.services;

import java.util.List;

import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.entity.Club;
import com.fernandobetancourt.model.entity.Player;

public abstract interface IPlayersService {

	//GET
	public abstract Player getPlayerById(Long id) throws InformationNotFoundException;
	public abstract Player getPlayerByName(String names) throws InformationNotFoundException;
	public abstract List<Player> getAllPlayers();
	public abstract List<Player> getPlayersByClub(Club club) throws InformationNotFoundException;
	
	//POST
	public abstract Player addPlayer(Player player) throws InformationNotFoundException, WritingInformationException;
	
	//PUT
	public abstract Player updatePlayer(Player player) throws InformationNotFoundException, WritingInformationException;
	
	//DELETE
	public abstract Player deletePlayer(Long id) throws InformationNotFoundException;
	
	//VALIDATIONS
	public abstract boolean isPlayerValidToUpdate(Player player) throws InformationNotFoundException, WritingInformationException;
	public abstract boolean isPlayerValidToSave(Player player) throws InformationNotFoundException, WritingInformationException;
	public abstract Player playerExists(Long id) throws InformationNotFoundException;
	
}
