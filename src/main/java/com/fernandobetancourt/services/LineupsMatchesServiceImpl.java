package com.fernandobetancourt.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fernandobetancourt.exceptions.AddingLineupMatchException;
import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.LineupMatchNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.dao.ILineupsMatchesDao;
import com.fernandobetancourt.model.entity.LineupMatch;
import com.fernandobetancourt.model.entity.Match;

@Service
public class LineupsMatchesServiceImpl implements ILineupsMatchesService {
	
	@Autowired
	private ILineupsMatchesDao lineupsMatchesDao;
	
	//GET
	
	@Override
	public List<LineupMatch> getLineupMatchesByMatch(Match match) throws InformationNotFoundException{
		//Al momento que se manda a llamar este método ya estamos seguros de que el Match existe
		return this.lineupsMatchesDao.findByMatch(match).orElseThrow(() -> {
			throw new LineupMatchNotFoundException("LineupMatches has not been found");
		});
	}
	
	//POST
	
	@Override
	public LineupMatch addLineupMatch(LineupMatch lineupMatch) throws InformationNotFoundException, WritingInformationException{
		this.isLineupMatchValidToSave(lineupMatch);
		return this.lineupsMatchesDao.save(lineupMatch);
	}

	//DELETE
	
	@Override
	public LineupMatch deleteLineupMatch(Long id) throws InformationNotFoundException{
		
		LineupMatch lineupMatchDeleted = this.lineupMatchExists(id);
		
		lineupsMatchesDao.deleteById(id);
		
		return lineupMatchDeleted;
	}
	
	//VALIDATIONS
	
	@Override
	public boolean isLineupMatchValidToSave(LineupMatch lineupMatch) throws InformationNotFoundException, WritingInformationException{
		
		if(		lineupMatch == null ||
				lineupMatch.getMatch() == null || lineupMatch.getMatch().getMatchId() == null || lineupMatch.getMatch().getMatchId() < 1 ||
				lineupMatch.getLineup() == null ||
				lineupMatch.getClubStatus() == null || lineupMatch.getClubStatus().trim().equals("")
				) {
			
			//No se necesita el lineupId ya que este se crea en cascada con el LineupMatch
//			|| lineupMatch.getLineup().getLineupId() == null || lineupMatch.getLineup().getLineupId() < 1
			
			throw new AddingLineupMatchException("LienupMatch is not valid to save");
			
		}
		
		//Si hago referencia a MatchServiceImpl estoy haciendo una referencia circular, pero en el pundo de que estamos usando este método es seguro
		//que el match existe ya que lo creamos desde su "caller"
//		this.matchesService.matchExists(lineupMatch.getMatch().getMatchId());
		
//		this.lineupsService.lineupExists(lineupMatch.getLineup().getLineupId());
		
		return true;
	}

	@Override
	public LineupMatch lineupMatchExists(Long id) throws InformationNotFoundException {
		return this.lineupsMatchesDao.findById(id).orElseThrow(() -> {
			StringBuilder sb = new StringBuilder().append("LineupMatch with id ").append(id).append(" has not been found");
			throw new LineupMatchNotFoundException(sb.toString());
		});
	}

}
