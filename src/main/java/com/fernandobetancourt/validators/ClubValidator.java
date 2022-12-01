package com.fernandobetancourt.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fernandobetancourt.exceptions.AddingClubException;
import com.fernandobetancourt.exceptions.ClubNotFoundException;
import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.dao.IClubesDao;
import com.fernandobetancourt.model.entity.Club;
import com.fernandobetancourt.services.IGroupsService;

@Component
public class ClubValidator {
	
	@Autowired
	private IGroupsService groupsService;
	
	@Autowired
	private IClubesDao clubesDao;

	public boolean isClubValidToSave(Club club) throws InformationNotFoundException, WritingInformationException {

		if(		club == null ||
				club.getName() == null || club.getName().trim().equals("") ||
				club.getStadium() == null || club.getStadium().trim().equals("") ||
				club.getGroup() == null || club.getGroup().getGroupId() == null || club.getGroup().getGroupId() < 1
				) {
			
			throw new AddingClubException("Club is not valid to save");
			
		}
		
		this.groupsService.getGroup(club.getGroup().getGroupId());
		
		return true;
	}

	public boolean isClubValidToUpdate(Club club) throws InformationNotFoundException, WritingInformationException {
		
		if(		club == null ||
				club.getClubId() == null || club.getClubId() < 1 ||
				club.getName() == null || club.getName().trim().equals("") ||
				club.getStadium() == null || club.getStadium().trim().equals("") ||
				club.getGroup() == null || club.getGroup().getGroupId() == null || club.getGroup().getGroupId() < 1
				) {
			
			throw new AddingClubException("Club is not valid to save");
			
		}
		
		this.clubExists(club.getClubId());
		this.groupsService.getGroup(club.getGroup().getGroupId());
		
		return true;
	}

	public Club clubExists(Long id) throws InformationNotFoundException {
		return this.clubesDao.findById(id).orElseThrow(() -> {
			StringBuilder sb = new StringBuilder().append("Club with id ").append(id).append(" has not been found");
			throw new ClubNotFoundException(sb.toString());
		});
	}
}
