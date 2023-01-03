package com.fernandobetancourt.services;

import java.util.List;

import com.fernandobetancourt.dto.MatchDto;
import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.entity.Match;

public interface IMatchesService {
	
	//GET	
	public abstract List<Match> getAllMatches();
	public abstract List<Match> getMatchesByJourney(Long journeyId) throws InformationNotFoundException;
	public abstract List<MatchDto> getMatchesByJourneyWithClubes(Long journeyId) throws InformationNotFoundException;
	public abstract Match getMatch(Long id) throws InformationNotFoundException;

	//POST
	public abstract Match addMatch(Match match, Long localClubId, Long visitorClubId) throws InformationNotFoundException, WritingInformationException;
	public abstract MatchDto addMatch(MatchDto matchDto) throws InformationNotFoundException, WritingInformationException;
	
	//PUT
	public abstract Match updateMatch(Match match, Long localClubId, Long visitorClubId) throws InformationNotFoundException, WritingInformationException;
	
	//DELETE
	public abstract Match deleteMatch(Long id) throws InformationNotFoundException;
}
