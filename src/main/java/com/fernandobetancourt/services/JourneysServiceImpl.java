package com.fernandobetancourt.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.JourneyNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.dao.IJourneysDao;
import com.fernandobetancourt.model.entity.Journey;
import com.fernandobetancourt.validators.JourneyValidator;

@Service
public class JourneysServiceImpl implements IJourneysService {
	
	@Autowired
	private IJourneysDao journeysDao;
	
	@Autowired
	private JourneyValidator journeyValidator;
	
	//GET
	
	@Override
	public List<Journey> getAllJourneys() {
		List<Journey> journeys = journeysDao.findAll();
		if(journeys.isEmpty()) throw new JourneyNotFoundException("There are not journeys available");
		return journeys;
	}
	
	@Override
	public Journey getJourney(Long id) throws InformationNotFoundException {
		return journeyValidator.journeyExists(id);
	}

	//POST
	
	@Override
	public Journey addJourney(Journey journey) throws InformationNotFoundException, WritingInformationException{
		journeyValidator.isJourneyValidToSave(journey);
		return this.journeysDao.save(journey);
	}
	
	//PUT
		
	@Override
	public Journey updateJourney(Journey journey) throws InformationNotFoundException, WritingInformationException {
		journeyValidator.isJourneyValidToUpdate(journey);
		return this.journeysDao.save(journey);
	}
	
	//DELETE
	
	@Override
	public Journey deleteJourney(Long id) throws InformationNotFoundException {		
		Journey journeyDeleted = journeyValidator.journeyExists(id);
		this.journeysDao.deleteById(id);
		return journeyDeleted;
	}
}
