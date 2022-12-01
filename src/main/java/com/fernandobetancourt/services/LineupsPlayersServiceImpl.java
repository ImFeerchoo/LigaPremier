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
import com.fernandobetancourt.validators.LineupPlayerValidator;

@Service
public class LineupsPlayersServiceImpl implements ILineupsPlayersService {
	
	@Autowired
	private ILineupsPlayersDao lineupsPlayersDao;
	
	@Autowired
	private ILineupsMatchesService lineupsMatchesService;
	
	@Autowired
	private IMatchesService matchesService;
	
	@Autowired
	private IClubesMatchesService clubesMatchesService;
	
	@Autowired
	private LineupPlayerValidator lineupPlayerValidator;
	

	//GET
	
	@Override
	public LineupPlayer getLineupPlayer(Long id) throws InformationNotFoundException {
		return lineupPlayerValidator.lineupPlayerExists(id);
	}
	
	@Override
	public List<LineupPlayer> getLineupsPlayersByMatch(Long matchId) throws InformationNotFoundException{
		
		Match matchRecovered = this.matchesService.getMatch(matchId);
		List<LineupMatch> lineupsMatches = this.lineupsMatchesService.getLineupMatchesByMatch(matchRecovered);
		
		List<LineupPlayer> lineupsPlayers = new ArrayList<>();
		
		lineupsMatches.forEach(lineupMatch -> {
			lineupsPlayers.addAll(this.lineupsPlayersDao.findByLineup(lineupMatch.getLineup()));
		});
		
		if(lineupsPlayers.isEmpty()) throw new LineupPlayerNotFoundException("Has not been found LineupPlayers to this match");
		
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
		
		List<LineupPlayer> lineupPlayersResponse = new ArrayList<>();
		
		lineupsPlayers.forEach(lineupPlayer -> {
			
			if(lineupPlayer == null) throw new AddingLineupPlayerException("LineupPlayer is not valid to save");
			lineupPlayer.setLineup(lineupRecovered);
			Player player = lineupPlayerValidator.isLineupPlayerValidToSave(lineupPlayer);
			this.playerBelongToClub(player, clubRecovered);
			lineupPlayersResponse.add(this.lineupsPlayersDao.save(lineupPlayer));
			
		});
		
		return lineupPlayersResponse;
	}

	//PUT

	//En vez de actualizar creo que es mejor solo eliminar el LineupPlayer y crer uno nuevo
//	@Override
//	public LineupPlayer updateLineupPlayer(LineupPlayer lineupPlayer) throws InformationNotFoundException, WritingInformationException {
//		this.isLineupPlayerValidToUpdate(lineupPlayer);
//		return this.lineupsPlayersDao.save(lineupPlayer);
//	}
	
	//DELETE

//	@Override
//	public LineupPlayer deleteLineupPlayer(Long id) throws InformationNotFoundException {
//		LineupPlayer lineupPlayerDeleted = this.lineupPlayerExists(id);
//		this.lineupsPlayersDao.deleteById(id);
//		return lineupPlayerDeleted;
//	}
	
	@Override
	public List<LineupPlayer> deleteLineupsPlayers(List<Long> lineupPlayersIds) throws InformationNotFoundException{
		//En caso de que uno no exista, los demás igualmente se deben de borrar
		List<LineupPlayer> response = new ArrayList<>();
		lineupPlayersIds.forEach(lpId -> {
			try {
				response.add(lineupPlayerValidator.lineupPlayerExists(lpId));
				lineupsPlayersDao.deleteById(lpId);
			}catch(InformationNotFoundException e) {
				System.out.println(e.getMessage()); //Cambiarlo por un logger
			}
		});
		return response;
	}
	
	//UTILERY

	private boolean playerBelongToClub(Player player, Club club) {
		
		if(player.getClub().getClubId() != club.getClubId()) {
			StringBuilder sb = new StringBuilder();
			sb.append("Player with id ").append(player.getPlayerId()).append(" doesn't belong to the")
			.append(club.getName()).append(" club");
			throw new AddingLineupPlayerException(sb.toString());
		}
		
		return true;
	}
	
	private Club getClub(String clubStatus, Match match) throws InformationNotFoundException, WritingInformationException{

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
	
	private void removeIfHasLineupsPlayers(Lineup lineup) {
		List<LineupPlayer> lineupsPlayers = lineupsPlayersDao.findByLineup(lineup);
		lineupsPlayers.forEach(lineupPlayer -> this.lineupsPlayersDao.deleteById(lineupPlayer.getLineupPlayerId()));
	}

}
