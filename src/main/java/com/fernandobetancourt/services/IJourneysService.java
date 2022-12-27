package com.fernandobetancourt.services;

import java.util.List;

import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.entity.Journey;

public interface IJourneysService {

	//GET
	public abstract List<Journey> getAllJourneys() throws InformationNotFoundException;
	public abstract Journey getJourney(Long id) throws InformationNotFoundException;
	public abstract List<Journey> getJourneysByGroup(Long groupId) throws InformationNotFoundException;
	
	//POST
	public abstract Journey addJourney(Journey journey) throws InformationNotFoundException, WritingInformationException;
	
	//PUT
	public abstract Journey updateJourney(Journey journey) throws InformationNotFoundException, WritingInformationException;
	
	//DELETE
	public abstract Journey deleteJourney(Long id) throws InformationNotFoundException;
	
}
