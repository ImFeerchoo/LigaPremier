package com.fernandobetancourt.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.LineupMatchNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.dao.ILineupsMatchesDao;
import com.fernandobetancourt.model.entity.LineupMatch;
import com.fernandobetancourt.model.entity.Match;
import com.fernandobetancourt.validators.LineupMatchValidator;

@Service
public class LineupsMatchesServiceImpl implements ILineupsMatchesService {
	
	@Autowired
	private ILineupsMatchesDao lineupsMatchesDao;
	
	@Autowired
	private LineupMatchValidator lineupMatchValidator;
	
	public LineupsMatchesServiceImpl(ILineupsMatchesDao lineupsMatchesDao) {
		this.lineupsMatchesDao = lineupsMatchesDao;
	}
	
	//GET
	
//	@Override
//	public List<LineupMatch> getLineupMatchesByMatch(Match match) throws InformationNotFoundException{
//		//Al momento que se manda a llamar este método ya estamos seguros de que el Match existe
//		return this.lineupsMatchesDao.findByMatch(match).orElseThrow(() -> {
//			throw new LineupMatchNotFoundException("LineupMatches has not been found");
//		});
//	}
	
	@Override
	public List<LineupMatch> getLineupMatchesByMatch(Match match) throws InformationNotFoundException{
		//Al momento que se manda a llamar este método ya estamos seguros de que el Match existe
		List<LineupMatch> lineupMatches = lineupsMatchesDao.findByMatch(match);
		if(lineupMatches.isEmpty()) throw new LineupMatchNotFoundException("LineupMatches has not been found");
		return lineupMatches;
	}
	
	//POST
	
	@Override
	public LineupMatch addLineupMatch(LineupMatch lineupMatch) throws InformationNotFoundException, WritingInformationException{
		lineupMatchValidator.isLineupMatchValidToSave(lineupMatch);
		return this.lineupsMatchesDao.save(lineupMatch);
	}

	//DELETE
	
	@Override
	public LineupMatch deleteLineupMatch(Long id) throws InformationNotFoundException{
		
		LineupMatch lineupMatchDeleted = lineupMatchValidator.lineupMatchExists(id);
		
		lineupsMatchesDao.deleteById(id);
		
		return lineupMatchDeleted;
	}
	


}
