package com.fernandobetancourt.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fernandobetancourt.exceptions.AddingPlayerException;
import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.PlayerNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.dao.IPlayersDao;
import com.fernandobetancourt.model.entity.Player;
import com.fernandobetancourt.services.IClubesService;

@Component
public class PlayerValidator {
	
	@Autowired
	private IPlayersDao playersDao;
	
	@Autowired
	private IClubesService clubesService;
	
	public boolean isPlayerValidToSave(Player player) throws InformationNotFoundException, WritingInformationException {

		if (	player == null || 
				player.getNames() == null || player.getNames().trim().equals("") || 
				player.getLastNames() == null || player.getLastNames().trim().equals("") || 
				player.getNumber() == null || player.getNumber() < 1 || 
				player.getPosition() == null || player.getPosition().trim().equals("") || 
				player.getAge() == null || player.getAge() < 1 ||
				player.getWeight() == null || player.getWeight() < 1 ||
				player.getHeight() == null || player.getHeight() < 1 ||
				player.getNationality() == null || player.getNationality().trim().equals("") ||
				player.getClub() == null || player.getClub().getClubId() == null || player.getClub().getClubId() < 1
				) {
			  
			throw new AddingPlayerException("Player is not valid to save");

		}

		this.clubesService.getClubById(player.getClub().getClubId());
		return true;
	}

	public boolean isPlayerValidToUpdate(Player player) throws InformationNotFoundException, WritingInformationException {
		if (	player == null || 
				player.getPlayerId() == null || player.getPlayerId() < 1 || 
				player.getNames() == null || player.getNames().trim().equals("") || 
				player.getLastNames() == null || player.getLastNames().trim().equals("") || 
				player.getNumber() == null || player.getNumber() < 1 || 
				player.getPosition() == null || player.getPosition().trim().equals("") || 
				player.getAge() == null || player.getAge() < 1 ||
				player.getWeight() == null || player.getWeight() < 1 ||
				player.getHeight() == null || player.getHeight() < 1 ||
				player.getNationality() == null || player.getNationality().trim().equals("") ||
				player.getClub() == null || player.getClub().getClubId() == null || player.getClub().getClubId() < 1
				) {
			  

			throw new AddingPlayerException("Player is not valid to save");

		}

		this.playerExists(player.getPlayerId());
		this.clubesService.getClubById(player.getClub().getClubId());
		
		return true;
	}

	public Player playerExists(Long id) throws InformationNotFoundException{
		return this.playersDao.findById(id).orElseThrow(() -> {
			StringBuilder sb = new StringBuilder().append("Player with id ").append(id).append(" has not been found");
			throw new PlayerNotFoundException(sb.toString());
		});
	}

}
