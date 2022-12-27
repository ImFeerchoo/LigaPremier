package com.fernandobetancourt.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fernandobetancourt.exceptions.AddingClubMatchException;
import com.fernandobetancourt.exceptions.ClubMatchNotFoundException;
import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.dao.IClubesMatchesDao;
import com.fernandobetancourt.model.entity.ClubMatch;
import com.fernandobetancourt.services.IClubesService;

@Component
public class ClubMatchValidator {
	
	@Autowired
	private IClubesService clubesService;
	
	@Autowired
	private IClubesMatchesDao clubesMatchesDao;
	
	
	public boolean isClubMatchValidToSave(ClubMatch clubMatch) throws InformationNotFoundException, WritingInformationException {
		
		if(		clubMatch == null ||
				clubMatch.getLocalClub() == null || clubMatch.getLocalClub().getClubId() == null || clubMatch.getLocalClub().getClubId() < 1 ||
				clubMatch.getVisitorClub() == null || clubMatch.getVisitorClub().getClubId() == null || clubMatch.getVisitorClub().getClubId() < 1 ||
				clubMatch.getMatch() == null || clubMatch.getMatch().getMatchId() == null || clubMatch.getMatch().getMatchId() < 1) {
			
			throw new AddingClubMatchException("ClubMatch is not valid to save");
			
		}
		
		this.clubesService.getClubById(clubMatch.getLocalClub().getClubId());
		this.clubesService.getClubById(clubMatch.getVisitorClub().getClubId());
		
		//Si hago referencia a MatchServiceImpl estoy haciendo una referencia circular, pero en el pundo de que estamos usando este método es seguro
		//que el match existe ya que lo creamos desde su "caller"
//		this.matchesService.matchExists(clubMatch.getMatch().getMatchId());
		
		return true;
	}

	public boolean isClubMatchValidToUpdate(ClubMatch clubMatch) throws InformationNotFoundException, WritingInformationException {
		
		if(		clubMatch == null ||
				clubMatch.getClubMatchId() == null || clubMatch.getClubMatchId() < 1 ||
				clubMatch.getLocalClub() == null || clubMatch.getLocalClub().getClubId() == null || clubMatch.getLocalClub().getClubId() < 1 ||
				clubMatch.getVisitorClub() == null || clubMatch.getVisitorClub().getClubId() == null || clubMatch.getVisitorClub().getClubId() < 1 ||
				clubMatch.getMatch() == null || clubMatch.getMatch().getMatchId() == null || clubMatch.getMatch().getMatchId() < 1) {
			
			throw new AddingClubMatchException("ClubMatch is not valid to save");
			
		}
		
		this.clubesService.getClubById(clubMatch.getLocalClub().getClubId());
		this.clubesService.getClubById(clubMatch.getVisitorClub().getClubId());
		
		//Si hago referencia a MatchServiceImpl estoy haciendo una referencia circular, pero en el pundo de que estamos usando este método es seguro
		//que el match existe ya que lo creamos desde su "caller"
//		this.matchesService.matchExists(clubMatch.getMatch().getMatchId());
		
		this.clubMatchExists(clubMatch.getClubMatchId());
		
		return true;
	}

	public ClubMatch clubMatchExists(Long id) throws InformationNotFoundException {
		return this.clubesMatchesDao.findById(id).orElseThrow(() -> {
			StringBuilder sb = new StringBuilder().append("ClubMatch with id ").append(id).append(" has not been found");
			throw new ClubMatchNotFoundException(sb.toString());
		});
	}
	
}
