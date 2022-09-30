package com.fernandobetancourt.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fernandobetancourt.exceptions.AddingLineupPlayerException;
import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.LineupMatchNotFoundException;
import com.fernandobetancourt.exceptions.LineupPlayerNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.dao.ILineupsPlayersDao;
import com.fernandobetancourt.model.entity.Club;
import com.fernandobetancourt.model.entity.Lineup;
import com.fernandobetancourt.model.entity.LineupMatch;
import com.fernandobetancourt.model.entity.LineupPlayer;
import com.fernandobetancourt.model.entity.Match;
import com.fernandobetancourt.model.entity.Player;

@Service
public class LineupsPlayersServiceImpl implements ILineupsPlayersService {
	
	@Autowired
	private ILineupsPlayersDao lineupsPlayersDao;
	
	@Autowired
	private IPlayersService playersService;
	
	@Autowired
	private ILineupsService lineupsService;
	
	@Autowired
	private ILineupsMatchesService lineupsMatchesService;
	
	@Autowired
	private IMatchesService matchesService;
	
	@Autowired
	private IClubesMatchesService clubesMatchesService;
	

	//GET
	
	@Override
	public LineupPlayer getLineupPlayer(Long id) throws InformationNotFoundException {
		return this.lineupPlayerExists(id);
	}
	
	@Override
	public List<LineupPlayer> getLineupsPlayersByMatch(Long matchId) throws InformationNotFoundException{
		
		Match matchRecovered = this.matchesService.getMatch(matchId);
		List<LineupMatch> lineupsMatches = this.lineupsMatchesService.getLineupMatchesByMatch(matchRecovered);
		
		List<LineupPlayer> lineupsPlayers = new ArrayList<>();
		
		lineupsMatches.forEach(lineupMatch -> {
			
			lineupsPlayers.addAll(this.lineupsPlayersDao.findByLineup(lineupMatch.getLineup())
												.orElseThrow(() -> {
													throw new LineupPlayerNotFoundException("LineupsPlayers not found");
												}));
			
		});
		
		return lineupsPlayers;
	}
	
	//POST
	
	@Transactional(rollbackFor = {InformationNotFoundException.class, WritingInformationException.class})
	@Override
	public List<LineupPlayer> addLineupsPlayers(List<LineupPlayer> lineupsPlayers, Long matchId, String clubStatus) 
			throws InformationNotFoundException, WritingInformationException {
		
		Match matchRecovered = this.matchesService.getMatch(matchId);
		
		Club clubRecovered = this.getClub(clubStatus, matchRecovered);
		
		Lineup lineupRecovered = this.lineupsMatchesService.getLineupMatchesByMatch(matchRecovered)
							.stream()
							.filter(lineupMatch -> lineupMatch.getClubStatus().equalsIgnoreCase(clubStatus))
							.map(lineupMatch -> lineupMatch.getLineup())
							.findFirst().orElseThrow(() -> {
								throw new LineupMatchNotFoundException("LineupMatch not found");
							});
		
		this.removeIfHasLineupsPlayers(lineupRecovered);
		
		lineupsPlayers.forEach(lineupPlayer -> {
			
			lineupPlayer.setLineup(lineupRecovered);
			this.isLineupPlayerValidToSave(lineupPlayer);
			this.playerBelongToClub(this.playersService.getPlayerById(lineupPlayer.getPlayer().getPlayerId()), clubRecovered, clubStatus);
			this.lineupsPlayersDao.save(lineupPlayer);
			
		});
		
		return lineupsPlayers;
	}

	//PUT

	//En vez de actualizar creo que es mejor solo eliminar el LineupPlayer y crer uno nuevo
	@Override
	public LineupPlayer updateLineupPlayer(LineupPlayer lineupPlayer) throws InformationNotFoundException, WritingInformationException {
		this.isLineupPlayerValidToUpdate(lineupPlayer);
		return this.lineupsPlayersDao.save(lineupPlayer);
	}
	
	//DELETE

	@Override
	public LineupPlayer deleteLineupPlayer(Long id) throws InformationNotFoundException {
		LineupPlayer lineupPlayerDeleted = this.lineupPlayerExists(id);
		this.lineupsPlayersDao.deleteById(id);
		return lineupPlayerDeleted;
	}
	
	//VALIDATIONS

	@Override
	public boolean isLineupPlayerValidToSave(LineupPlayer lineupPlayer) throws InformationNotFoundException, WritingInformationException {
		
		if(		lineupPlayer == null ||
				lineupPlayer.getPlayerStatus() == null || lineupPlayer.getPlayerStatus().trim().equals("") ||
				lineupPlayer.getPlayer() == null || lineupPlayer.getPlayer().getPlayerId() == null || lineupPlayer.getPlayer().getPlayerId() < 1 ||
				lineupPlayer.getLineup() == null || lineupPlayer.getLineup().getLineupId() == null || lineupPlayer.getLineup().getLineupId() < 1
				
				) {
			
			throw new AddingLineupPlayerException("LineupPlayer is not valid to save");
			
		}
		
		this.playersService.getPlayerById(lineupPlayer.getPlayer().getPlayerId());
		this.lineupsService.lineupExists(lineupPlayer.getLineup().getLineupId());
		
		return true;
	}

	@Override
	public boolean isLineupPlayerValidToUpdate(LineupPlayer lineupPlayer) throws InformationNotFoundException, WritingInformationException {
		
		if(		lineupPlayer == null ||
				lineupPlayer.getLineupPlayerId() == null || lineupPlayer.getLineupPlayerId() < 1 ||
				lineupPlayer.getPlayerStatus() == null || lineupPlayer.getPlayerStatus().trim().equals("") ||
				lineupPlayer.getPlayer() == null || lineupPlayer.getPlayer().getPlayerId() == null || lineupPlayer.getPlayer().getPlayerId() < 1 ||
				lineupPlayer.getLineup() == null || lineupPlayer.getLineup().getLineupId() == null || lineupPlayer.getLineup().getLineupId() < 1
				) {
			
			throw new AddingLineupPlayerException("LineupPlayer is not valid to save");
			
		}
		
		this.lineupPlayerExists(lineupPlayer.getLineupPlayerId());
		this.playersService.getPlayerById(lineupPlayer.getPlayer().getPlayerId());
		this.lineupsService.lineupExists(lineupPlayer.getLineup().getLineupId());
		
		return true;
	}

	@Override
	public LineupPlayer lineupPlayerExists(Long id) throws InformationNotFoundException {
		return this.lineupsPlayersDao.findById(id).orElseThrow(() -> {
			StringBuilder sb = new StringBuilder().append("LineupPlayer with id ").append(id).append(" has not been found");
			throw new LineupPlayerNotFoundException(sb.toString());
		});
	}
	
	public boolean playerBelongToClub(Player player, Club club, String clubStatus) {
		
		if(player.getClub().getClubId() != club.getClubId()) {
			StringBuilder sb = new StringBuilder();
			sb.append("Player with id ").append(player.getPlayerId()).append(" don't belong to ")
			.append(clubStatus).append(" team");
			throw new AddingLineupPlayerException(sb.toString());
		}
		
		return true;
	}
	
	public Club getClub(String clubStatus, Match match) throws InformationNotFoundException, WritingInformationException{

		Club clubRecovered;
		if(clubStatus.trim().equalsIgnoreCase("Local")) {
			clubRecovered = this.clubesMatchesService.getClubMatchByMatch(match).getLocalClub();
		}else if(clubStatus.trim().equalsIgnoreCase("Visitor")) {
			clubRecovered = this.clubesMatchesService.getClubMatchByMatch(match).getVisitorClub();
		}else {
			throw new WritingInformationException("ClubStatus is not valid");
		}
		
		
		return clubRecovered;
	}
	
	public void removeIfHasLineupsPlayers(Lineup lineup) {
		this.lineupsPlayersDao.findByLineup(lineup)
				.ifPresent(lineupsPlayers -> {
					
					lineupsPlayers.forEach(lineupPlayer -> this.lineupsPlayersDao.deleteById(lineupPlayer.getLineupPlayerId()));
					
				});
	}

}
