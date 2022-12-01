package com.fernandobetancourt.services;

import java.util.List;

import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.entity.Journey;

public interface IJourneysService {

	//GET
	public abstract List<Journey> getAllJourneys();
	public abstract Journey getJourney(Long id) throws InformationNotFoundException;
	
	//POST
	public abstract Journey addJourney(Journey journey) throws InformationNotFoundException, WritingInformationException;
	
	//PUT
	public abstract Journey updateJourney(Journey journey) throws InformationNotFoundException, WritingInformationException;
	
	//DELETE
	public abstract Journey deleteJourney(Long id) throws InformationNotFoundException;
	
}
