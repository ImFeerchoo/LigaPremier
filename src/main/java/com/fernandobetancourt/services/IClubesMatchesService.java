package com.fernandobetancourt.services;

import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.entity.ClubMatch;
import com.fernandobetancourt.model.entity.Match;

public interface IClubesMatchesService {

	//GET
	public abstract ClubMatch getClubMatch(Long id) throws InformationNotFoundException;
	public abstract ClubMatch getClubMatchByMatch(Match match) throws InformationNotFoundException;
	
	//POST
	public abstract ClubMatch addClubMatch(ClubMatch clubMatch) throws WritingInformationException;
	
	//PUT
	public abstract ClubMatch updateClubMatch(ClubMatch clubMatch) throws InformationNotFoundException, WritingInformationException;
	
	//DELETE
	public abstract ClubMatch deleteClubMatch(Long id) throws InformationNotFoundException;
	
	//VALIDATIONS
	public abstract boolean isClubMatchValidToSave(ClubMatch clubMatch) throws InformationNotFoundException, WritingInformationException;
	public abstract boolean isClubMatchValidToUpdate(ClubMatch clubMatch) throws InformationNotFoundException, WritingInformationException;
	public abstract ClubMatch clubMatchExists(Long id) throws InformationNotFoundException;
	
}
