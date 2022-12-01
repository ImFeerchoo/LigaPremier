package com.fernandobetancourt.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fernandobetancourt.exceptions.AddingJourneyException;
import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.JourneyNotFoundException;
import com.fernandobetancourt.model.dao.IJourneysDao;
import com.fernandobetancourt.model.entity.Journey;
import com.fernandobetancourt.services.IGroupsService;

@Component
public class JourneyValidator {
	
	@Autowired
	private IJourneysDao journeysDao;
	
	@Autowired
	private IGroupsService groupsService;
	
	public boolean isJourneyValidToSave(Journey journey) throws InformationNotFoundException {
		
		if(		journey == null ||
				journey.getNumber() == null || journey.getNumber() < 1 ||
				journey.getGroup() == null || journey.getGroup().getGroupId() == null || journey.getGroup().getGroupId() < 1
				) {

			throw new AddingJourneyException("Journey is not valid to save");
			
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
			
			throw new AddingJourneyException("Journey is not valid to save");
			
		}
		
		journeyExists(journey.getJourneyId());
		this.groupsService.getGroup(journey.getGroup().getGroupId());
		
		return true;
		
	}

	public Journey journeyExists(Long id) throws InformationNotFoundException{
		
		Journey journey = this.journeysDao.findById(id).orElseThrow(() -> {
			StringBuilder sb = new StringBuilder().append("Journey with id ").append(id).append(" has not been found");
			throw new JourneyNotFoundException(sb.toString());
		});
		
		return journey;
	}

}
