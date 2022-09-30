package com.fernandobetancourt.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fernandobetancourt.exceptions.AddingPlayerException;
import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.PlayerNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.dao.IPlayersDao;
import com.fernandobetancourt.model.entity.Club;
import com.fernandobetancourt.model.entity.Player;

@Service
public class PlayersServiceImpl implements IPlayersService {

	@Autowired
	private IPlayersDao playersDao;

	@Autowired
	private IClubesService clubesService;

	// GET

	@Override
	public Player getPlayerById(Long id) throws InformationNotFoundException {

		return this.playersDao.findById(id).orElseThrow(() -> {
			throw new PlayerNotFoundException("Player with id " + id + " has not been found");
		});

	}

	@Override
	public Player getPlayerByName(String names) throws InformationNotFoundException {
		return this.playersDao.findByNames(names).orElseThrow(() -> {
			throw new PlayerNotFoundException("Player " + names + " has not been found");
		});
	}

	@Override
	public List<Player> getAllPlayers() {
		return this.playersDao.findAll();
	}

	@Override
	public List<Player> getPlayersByClub(Club club) throws InformationNotFoundException {
		return this.playersDao.findByClub(club).orElseThrow(() -> {
			throw new PlayerNotFoundException("Los jugadores del club " + club.getName() + " no fueron encontrados");
		});
	}

	// POST

	@Override
	public Player addPlayer(Player player) throws InformationNotFoundException, WritingInformationException {
		this.isPlayerValidToSave(player);
		return this.playersDao.save(player);
	}

	// PUT

	@Override
	public Player updatePlayer(Player player) throws InformationNotFoundException, WritingInformationException {
		this.isPlayerValidToUpdate(player);
		return this.playersDao.save(player);
	}

	// DELETE

	@Override
	public Player deletePlayer(Long id) throws InformationNotFoundException {
		Player playerDeleted = this.playerExists(id);
		this.playersDao.deleteById(id);
		return playerDeleted;
	}

	// VALIDATIONS

	public boolean isPlayerValidToSave(Player player) throws InformationNotFoundException, WritingInformationException {

		// Poner validacion de que el club tiene un id valido, > 1 y diferente de null
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

		// Verificacion de que el club existe
		this.clubesService.getClubById(player.getClub().getClubId());
		return true;
	}

	@Override
	public boolean isPlayerValidToUpdate(Player player) throws InformationNotFoundException, WritingInformationException {
		// Poner validacion de que el club tiene un id valido, > 1 y diferente de null
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

		// Verificacion de que el club existe
		this.playerExists(player.getPlayerId());
		this.clubesService.getClubById(player.getClub().getClubId());
		
		return true;
	}

	@Override
	public Player playerExists(Long id) throws InformationNotFoundException{
		return this.playersDao.findById(id).orElseThrow(() -> {
			StringBuilder sb = new StringBuilder().append("Player with id ").append(id).append(" has not been found");
			throw new PlayerNotFoundException(sb.toString());
		});
	}

}
