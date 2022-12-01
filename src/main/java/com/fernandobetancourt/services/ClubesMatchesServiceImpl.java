package com.fernandobetancourt.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fernandobetancourt.exceptions.AddingClubMatchException;
import com.fernandobetancourt.exceptions.ClubMatchNotFoundException;
import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.dao.IClubesMatchesDao;
import com.fernandobetancourt.model.entity.ClubMatch;
import com.fernandobetancourt.model.entity.Match;

@Service
public class ClubesMatchesServiceImpl implements IClubesMatchesService {
	
	@Autowired
	private IClubesMatchesDao clubesMatchesDao;
	
	@Autowired
	private IClubesService clubesService;
	
//	@Autowired
//	private IMatchesService matchesService;
	
	//GET

	@Override
	public ClubMatch getClubMatch(Long id) throws InformationNotFoundException{
		return this.clubMatchExists(id);
	}
	
	@Override
	public ClubMatch getClubMatchByMatch(Match match) throws InformationNotFoundException{
		//05/11/2022 Mejor solo pedir el id del match y aquí en el método recuperarlo con el servicio
		//Al momento en que pedimos es clubMatch por el Match ya estamos seguros de que el Match existe
		return this.clubesMatchesDao.findByMatch(match).orElseThrow(() -> {
			throw new ClubMatchNotFoundException("ClubMatch not found");
		});
	}
	
	//POST

	@Override
	public ClubMatch addClubMatch(ClubMatch clubMatch) throws WritingInformationException {
		this.isClubMatchValidToSave(clubMatch);
		return this.clubesMatchesDao.save(clubMatch);
	}
	
	//PUT

	@Override
	public ClubMatch updateClubMatch(ClubMatch clubMatch) throws InformationNotFoundException, WritingInformationException {
		this.isClubMatchValidToUpdate(clubMatch);
		return this.clubesMatchesDao.save(clubMatch);
	}
	
	//DELETE

	@Override
	public ClubMatch deleteClubMatch(Long id) throws InformationNotFoundException {
		ClubMatch clubMatchDeleted = this.clubMatchExists(id);
		this.clubesMatchesDao.deleteById(id);
		return clubMatchDeleted;
	}
	
	//VALIDATIONS

	@Override
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

	@Override
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

	@Override
	public ClubMatch clubMatchExists(Long id) throws InformationNotFoundException {
		return this.clubesMatchesDao.findById(id).orElseThrow(() -> {
			StringBuilder sb = new StringBuilder().append("ClubMatch with id ").append(id).append(" has not been found");
			throw new ClubMatchNotFoundException(sb.toString());
		});
	}

}
