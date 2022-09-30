package com.fernandobetancourt.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fernandobetancourt.exceptions.AddingJourneyException;
import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.JourneyNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.dao.IJourneysDao;
import com.fernandobetancourt.model.entity.Journey;

@Service
public class JourneysServiceImpl implements IJourneysService {
	
	@Autowired
	private IJourneysDao journeysDao;
	
	@Autowired
	private IGroupsService groupsService;

	//GET
	
	@Override
	public List<Journey> getAllJourneys() {
		return this.journeysDao.findAll();
	}

	@Override
	public Journey getJourney(Long id) throws InformationNotFoundException {
		return this.journeyExists(id);
	}

	//POST
	
	@Override
	public Journey addJourney(Journey journey) throws InformationNotFoundException, WritingInformationException{
		
		if(!this.isJourneyValidToSave(journey)) {
			throw new AddingJourneyException("Journey is not valid to save");
		}
		
		return this.journeysDao.save(journey);
	}
	
	//PUT

	@Override
	public Journey updateJourney(Journey journey) throws InformationNotFoundException, WritingInformationException {
		
		if(!this.isJourneyValidToUpdate(journey)) {
			throw new AddingJourneyException("Journey is not valid to save");
		}
		
		this.journeyExists(journey.getJourneyId());
		
		return this.journeysDao.save(journey);
	}
	
	//DELETE

	@Override
	public Journey deleteJourney(Long id) throws InformationNotFoundException {
		
		Journey journeyDeleted = this.journeyExists(id);
		
		this.journeysDao.deleteById(id);
		
		return journeyDeleted;
	}

	//VALIDATIONS
	public boolean isJourneyValidToSave(Journey journey) throws InformationNotFoundException {
		
		if(		journey == null ||
				journey.getNumber() == null || journey.getNumber() < 1 ||
				journey.getGroup() == null || journey.getGroup().getGroupId() == null || journey.getGroup().getGroupId() < 1
				) {

			return false;
			
		}
		
		this.groupsService.getGroup(journey.getGroup().getGroupId());
		
		return true;
	}
	
	public boolean isJourneyValidToUpdate(Journey journey) throws InformationNotFoundException {
		
		if(		journey == null ||
				journey.getJourneyId() == null || journey.getJourneyId() < 1 ||
				journey.getNumber() == null || journey.getNumber() < 1 ||
				journey.getGroup() == null || journey.getGroup().getGroupId() == null || journey.getGroup().getGroupId() < 1
				) {
			
			return false;
			
		}
		
		this.groupsService.getGroup(journey.getGroup().getGroupId());
		
		return true;
		
	}

	@Override
	public Journey journeyExists(Long id) throws InformationNotFoundException{
		
		Journey journey = this.journeysDao.findById(id).orElseThrow(() -> {
			StringBuilder sb = new StringBuilder().append("Journey with id ").append(id).append(" has not been found");
			throw new JourneyNotFoundException(sb.toString());
		});
		
		return journey;
	}
}
