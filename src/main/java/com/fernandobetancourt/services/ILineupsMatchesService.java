package com.fernandobetancourt.services;

import java.util.List;

import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.entity.LineupMatch;
import com.fernandobetancourt.model.entity.Match;

public interface ILineupsMatchesService {

	//GET
	public abstract List<LineupMatch> getLineupMatchesByMatch(Match match) throws InformationNotFoundException;
	
	//POST
	public abstract LineupMatch addLineupMatch(LineupMatch lineupMatch) throws InformationNotFoundException, WritingInformationException;
	
	//PUT
	
	//DELTE
	//Al borrar un Match si algun lineupMatch no se encuentra debo de ignorarlo y seguir con el método de borrar el match 
	public abstract LineupMatch deleteLineupMatch(Long id) throws InformationNotFoundException;
	
	//VALIDATIONS
	public abstract boolean isLineupMatchValidToSave(LineupMatch lineupMatch) throws InformationNotFoundException, WritingInformationException;
	public abstract LineupMatch lineupMatchExists(Long id) throws InformationNotFoundException;
	
}
