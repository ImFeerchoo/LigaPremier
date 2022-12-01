package com.fernandobetancourt.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.PlayerNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.dao.IPlayersDao;
import com.fernandobetancourt.model.entity.Club;
import com.fernandobetancourt.model.entity.Player;
import com.fernandobetancourt.validators.PlayerValidator;

@Service
public class PlayersServiceImpl implements IPlayersService {

	@Autowired
	private IPlayersDao playersDao;

	@Autowired
	private IClubesService clubesService;
	
	@Autowired
	private PlayerValidator playerValidator;

	// GET
	
	@Override
	public Player getPlayerById(Long id) throws InformationNotFoundException {
		return playerValidator.playerExists(id);
	}

	@Override
	public Player getPlayerByName(String names) throws InformationNotFoundException {
		return this.playersDao.findByNames(names).orElseThrow(() -> {
			var sb = new StringBuilder();
			sb.append("Player ").append(names).append(" has not been found");
			throw new PlayerNotFoundException(sb.toString());
		});
	}

	@Override
	public List<Player> getAllPlayers() {
		List<Player> players = playersDao.findAll();
		if(players.isEmpty()) throw new PlayerNotFoundException("There are not players available");
		return players;
	}
	
//	@Override
//	public List<Player> getPlayersByClub(Long clubId) throws InformationNotFoundException {
//		Club club = clubesService.getClubById(clubId);
//		return playersDao.findByClub(club);
//	}
	
	@Override
	public List<Player> getPlayersByClub(Long clubId) throws InformationNotFoundException {
		Club club = clubesService.getClubById(clubId);
		List<Player> players = playersDao.findByClub(club);
		if(players.isEmpty()) {
			var sb = new StringBuilder();
			sb.append("Players of club ").append(club.getName()).append(" has not been found");
			throw new PlayerNotFoundException(sb.toString());
		}
		return players;
	}

	// POST

	@Override
	public Player addPlayer(Player player) throws InformationNotFoundException, WritingInformationException {
		playerValidator.isPlayerValidToSave(player);
		return this.playersDao.save(player);
	}

	// PUT

	@Override
	public Player updatePlayer(Player player) throws InformationNotFoundException, WritingInformationException {
		playerValidator.isPlayerValidToUpdate(player);
		return this.playersDao.save(player);
	}

	// DELETE

	@Override
	public Player deletePlayer(Long id) throws InformationNotFoundException {
		Player playerDeleted = playerValidator.playerExists(id);
		this.playersDao.deleteById(id);
		return playerDeleted;
	}

}
