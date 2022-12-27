package com.fernandobetancourt.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fernandobetancourt.exceptions.ClubMatchNotFoundException;
import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.dao.IClubesMatchesDao;
import com.fernandobetancourt.model.entity.ClubMatch;
import com.fernandobetancourt.model.entity.Match;
import com.fernandobetancourt.validators.ClubMatchValidator;

@Service
public class ClubesMatchesServiceImpl implements IClubesMatchesService {
	
	@Autowired
	private IClubesMatchesDao clubesMatchesDao;
	
	@Autowired
	private ClubMatchValidator clubMatchValidator;
	
	//GET

	@Override
	public ClubMatch getClubMatch(Long id) throws InformationNotFoundException{
		return clubMatchValidator.clubMatchExists(id);
	}
	
	
	//Parece que no puede recuperar el match por el id, ya que MatchService hace uso de ClubesMatchesService y haría una referencia circular
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
		clubMatchValidator.isClubMatchValidToSave(clubMatch);
		return this.clubesMatchesDao.save(clubMatch);
	}
	
	//PUT

	@Override
	public ClubMatch updateClubMatch(ClubMatch clubMatch) throws InformationNotFoundException, WritingInformationException {
		clubMatchValidator.isClubMatchValidToUpdate(clubMatch);
		return this.clubesMatchesDao.save(clubMatch);
	}
	
	//DELETE

	@Override
	public ClubMatch deleteClubMatch(Long id) throws InformationNotFoundException {
		ClubMatch clubMatchDeleted = clubMatchValidator.clubMatchExists(id);
		this.clubesMatchesDao.deleteById(id);
		return clubMatchDeleted;
	}

}
