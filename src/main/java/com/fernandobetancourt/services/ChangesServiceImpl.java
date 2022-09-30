package com.fernandobetancourt.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fernandobetancourt.exceptions.AddingChangeException;
import com.fernandobetancourt.exceptions.ChangeNotFoundException;
import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.LineupMatchNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.dao.IChangesDao;
import com.fernandobetancourt.model.entity.Change;
import com.fernandobetancourt.model.entity.Club;
import com.fernandobetancourt.model.entity.LineupMatch;
import com.fernandobetancourt.model.entity.Match;
import com.fernandobetancourt.model.entity.Player;

//Los players deben de ser distintos

//No podemos cambiar el club, mejor eliminamos el cambio y se crea una con el otro club

//En LineupsPlayers puedo insertar el estado actual del jugador, para que desde aquí al momento de cambiarlo saber si esta adentro

@Service
public class ChangesServiceImpl implements IChangesService {
	
	@Autowired
	private IChangesDao changesDao;
	
	@Autowired
	private IPlayersService playersService;
	
//	@Autowired
//	private ILineupsService lineupsService;
	
	@Autowired
	private IMatchesService matchesService;
	
	@Autowired
	private ILineupsMatchesService lineupsMatchesService;
	
	@Autowired
	private IClubesMatchesService clubesMatchesService;

	//GET
	
	@Override
	public List<Change> getChangesByMatch(Long matchId) throws InformationNotFoundException {
		Match matchRecovered = this.matchesService.getMatch(matchId);
		return this.changesDao.findByMatch(matchRecovered).orElseThrow(() -> {
			throw new ChangeNotFoundException("Changes not found");
		});
	}
	
	//POST

	@Override
	public Change addChange(Change change, String clubStatus) throws InformationNotFoundException, WritingInformationException {
		this.isChangeValidToSave(change);
		
		this.playerBelongToClub(change.getPlayerIn(), change.getMatch(), clubStatus);
		this.playerBelongToClub(change.getPlayerOut(), change.getMatch(), clubStatus);
		
		LineupMatch lineupMatchRecovered = this.getLineupMatch(change.getMatch(), clubStatus);
		change.setLineup(lineupMatchRecovered.getLineup());
		
		return this.changesDao.save(change);
	}
	
	//PUT

	@Override
	public Change updateChange(Change change, String clubStatus) throws InformationNotFoundException, WritingInformationException {
		this.isChangeValidToUpdate(change);
		
		this.playerBelongToClub(change.getPlayerIn(), change.getMatch(), clubStatus);
		this.playerBelongToClub(change.getPlayerOut(), change.getMatch(), clubStatus);
		
		LineupMatch lineupMatchRecovered = this.getLineupMatch(change.getMatch(), clubStatus);
		change.setLineup(lineupMatchRecovered.getLineup());
		
		return this.changesDao.save(change);
	}
	
	//DELETE

	@Override
	public Change deleteChange(Long id) throws InformationNotFoundException {
		Change changeDeleted = this.changeExists(id);
		this.changesDao.deleteById(id);
		return changeDeleted;
	}
	
	//VALIDATIONS

	@Override
	public boolean isChangeValidToSave(Change change) throws InformationNotFoundException, WritingInformationException {
		
		if(		change == null ||
				change.getMinute() == null || change.getMinute() < 0 ||
				change.getPlayerIn() == null || change.getPlayerIn().getPlayerId() == null || change.getPlayerIn().getPlayerId() < 1 ||
				change.getPlayerOut() == null || change.getPlayerOut().getPlayerId() == null || change.getPlayerOut().getPlayerId() < 1 ||
				change.getPlayerIn().getPlayerId() == change.getPlayerOut().getPlayerId() ||
				change.getMatch() == null || change.getMatch().getMatchId() == null || change.getMatch().getMatchId() < 1
				) {
			
			throw new AddingChangeException("Change is not valid to save");
			
		}
		
		change.setPlayerIn(this.playersService.playerExists(change.getPlayerIn().getPlayerId()));
		change.setPlayerOut(this.playersService.playerExists(change.getPlayerOut().getPlayerId()));
		change.setMatch(this.matchesService.matchExists(change.getMatch().getMatchId()));
		
		return true;
	}

	@Override
	public boolean isChangeValidToUpdate(Change change) throws InformationNotFoundException, WritingInformationException {
		
		if(		change == null ||
				change.getChangeId() == null || change.getChangeId() < 1 ||
				change.getMinute() == null || change.getMinute() < 0 ||
				change.getPlayerIn() == null || change.getPlayerIn().getPlayerId() == null || change.getPlayerIn().getPlayerId() < 1 ||
				change.getPlayerOut() == null || change.getPlayerOut().getPlayerId() == null || change.getPlayerOut().getPlayerId() < 1 ||
				change.getPlayerIn().getPlayerId() == change.getPlayerOut().getPlayerId() ||
				change.getMatch() == null || change.getMatch().getMatchId() == null || change.getMatch().getMatchId() < 1
				) {
			
			throw new AddingChangeException("Change is not valid to save");
			
		}
		
		this.changeExists(change.getChangeId());
		change.setPlayerIn(this.playersService.playerExists(change.getPlayerIn().getPlayerId()));
		change.setPlayerOut(this.playersService.playerExists(change.getPlayerOut().getPlayerId()));
		change.setMatch(this.matchesService.matchExists(change.getMatch().getMatchId()));
		
		return true;
	}

	@Override
	public Change changeExists(Long id) throws InformationNotFoundException {
		return this.changesDao.findById(id).orElseThrow(() -> {
			StringBuilder sb = new StringBuilder().append("Change with id ").append(id).append(" has not been found");
			throw new ChangeNotFoundException(sb.toString());
		});
	}
	
	private LineupMatch getLineupMatch(Match match, String clubStatus) {
		
		if(clubStatus.equalsIgnoreCase("Local") || clubStatus.equalsIgnoreCase("Visitor")) {
			
			List<LineupMatch> lineupsMatchesRecovered = this.lineupsMatchesService.getLineupMatchesByMatch(match);
			
			return lineupsMatchesRecovered
					.stream()
					.filter(lineupMatchArg -> lineupMatchArg.getClubStatus().equalsIgnoreCase(clubStatus))
					.findFirst()
					.orElseThrow(() -> {
						throw new LineupMatchNotFoundException("LineupMatch not found");
					});
			
		}
		
		throw new WritingInformationException("ClubStatus is not valid");

	}
	
	private boolean playerBelongToClub(Player player, Match match, String clubStatus) {
	
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
