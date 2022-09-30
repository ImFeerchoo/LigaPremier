package com.fernandobetancourt.services;

import java.util.List;

import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.entity.Match;

public interface IMatchesService {
	
	//GET	
	public abstract List<Match> getAllMatches(Long id);
	public abstract Match getMatch(Long id) throws InformationNotFoundException;

	//POST
	public abstract Match addMatch(Match match, Long localClubId, Long visitorClubId) throws InformationNotFoundException, WritingInformationException;
	
	//PUT
	public abstract Match updateMatch(Match match, Long localClubId, Long visitorClubId) throws InformationNotFoundException, WritingInformationException;
	
	//DELETE
	public abstract Match deleteMatch(Long id) throws InformationNotFoundException;

	//VALIDATIONS
	public abstract boolean isMatchValidToSave(Match match, Long localClubId, Long visitorClubId) throws InformationNotFoundException, WritingInformationException;
	public abstract boolean isMatchValidToUpdate(Match match, Long localClubId, Long visitorClubId) throws InformationNotFoundException, WritingInformationException;
	public abstract Match matchExists(Long id) throws InformationNotFoundException;
}
