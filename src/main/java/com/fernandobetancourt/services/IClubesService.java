package com.fernandobetancourt.services;

import java.util.List;

import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.entity.Club;

public interface IClubesService {

	//GET
	public abstract List<Club> getClubes();
	public abstract List<Club> getClubesByGroup(Long groupId) throws InformationNotFoundException;
	public abstract Club getClubById(Long id) throws InformationNotFoundException;
	
	//POST
	public abstract Club addClub(Club club) throws InformationNotFoundException, WritingInformationException;
	
	//PUT
	public abstract Club updateClub(Club club) throws InformationNotFoundException, WritingInformationException;
	
	//DELETE
	public abstract Club deleteClub(Long id) throws InformationNotFoundException;
	
}
