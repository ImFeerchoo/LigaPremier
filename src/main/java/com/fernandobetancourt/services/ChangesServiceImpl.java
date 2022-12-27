package com.fernandobetancourt.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fernandobetancourt.exceptions.ChangeNotFoundException;
import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.LineupMatchNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.dao.IChangesDao;
import com.fernandobetancourt.model.entity.Change;
import com.fernandobetancourt.model.entity.LineupMatch;
import com.fernandobetancourt.model.entity.Match;
import com.fernandobetancourt.validators.ChangeValidator;

//Los players deben de ser distintos

//No podemos cambiar el club, mejor eliminamos el cambio y se crea una con el otro club

//En LineupsPlayers puedo insertar el estado actual del jugador, para que desde aquí al momento de cambiarlo saber si esta adentro

@Service
public class ChangesServiceImpl implements IChangesService {
	
	@Autowired
	private IChangesDao changesDao;
	
	@Autowired
	private IMatchesService matchesService;
	
	@Autowired
	private ILineupsMatchesService lineupsMatchesService;
	
	@Autowired
	private ChangeValidator changeValidator;

	//GET
	
//	@Override
//	public List<Change> getChangesByMatch(Long matchId) throws InformationNotFoundException {
//		Match matchRecovered = this.matchesService.getMatch(matchId);
//		return this.changesDao.findByMatch(matchRecovered).orElseThrow(() -> {
//			throw new ChangeNotFoundException("Changes not found");
//		});
//	}
	
	@Override
	public List<Change> getChangesByMatch(Long matchId) throws InformationNotFoundException {
		Match matchRecovered = this.matchesService.getMatch(matchId);
		List<Change> changes = changesDao.findByMatch(matchRecovered);
		if(changes.isEmpty()) throw new ChangeNotFoundException("Changes not found");
		return changes;
	}
	
	//POST
	
	//Los jugadores siempre pertenecen al mismo club, esto ya que pasa por el playerBelongToClub los dos jugadores
	//Hacer un método que agregue una lista de changes (Tal vez)
	
	@Override
	public Change addChange(Change change, String clubStatus, Long matchId) throws InformationNotFoundException, WritingInformationException {
		changeValidator.isChangeValidToSave(change, matchId);
		
		changeValidator.playerBelongToClub(change.getPlayerIn(), change.getMatch(), clubStatus);
		changeValidator.playerBelongToClub(change.getPlayerOut(), change.getMatch(), clubStatus);
		
		LineupMatch lineupMatchRecovered = this.getLineupMatch(change.getMatch(), clubStatus);
		change.setLineup(lineupMatchRecovered.getLineup());
		
		return this.changesDao.save(change);
	}
	
	//PUT
	
//	@Override
//	public Change updateChange(Change change, String clubStatus, Long matchId) throws InformationNotFoundException, WritingInformationException {
//		this.isChangeValidToUpdate(change, matchId);
//		
//		this.playerBelongToClub(change.getPlayerIn(), change.getMatch(), clubStatus);
//		this.playerBelongToClub(change.getPlayerOut(), change.getMatch(), clubStatus);
//		
//		LineupMatch lineupMatchRecovered = this.getLineupMatch(change.getMatch(), clubStatus);
//		change.setLineup(lineupMatchRecovered.getLineup());
//		
//		return this.changesDao.save(change);
//	}
	
	//DELETE

	@Override
	public Change deleteChange(Long id) throws InformationNotFoundException {
		Change changeDeleted = changeValidator.changeExists(id);
		this.changesDao.deleteById(id);
		return changeDeleted;
	}
	
	//UTILERY
	
	//Invertir la lógica para tener el proceso fuera de la condicional, dentro de la condicional solo lanzar la excepción
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
		
		//Esta validación se hace después de validar el clubStatus en playerBelongToClub, por lo que no es necesaria
		throw new WritingInformationException("ClubStatus is not valid");

	}
}
