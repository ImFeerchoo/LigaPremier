package com.fernandobetancourt.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fernandobetancourt.exceptions.AddingCoachingStaffException;
import com.fernandobetancourt.exceptions.CoachingStaffNotFoundException;
import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.dao.ICoachingStaffsDao;
import com.fernandobetancourt.model.entity.CoachingStaff;
import com.fernandobetancourt.services.IClubesService;

@Component
public class CoachingStaffValidator {
	
	@Autowired
	private ICoachingStaffsDao coachingStaffsDao;
	
	@Autowired
	private IClubesService clubesService;

	public boolean isCoachingStaffValidToSave(CoachingStaff coachingStaff)
			throws InformationNotFoundException, WritingInformationException {
		if(		coachingStaff == null ||
				coachingStaff.getNames() == null || coachingStaff.getNames().trim().equals("") ||
				coachingStaff.getLastNames() == null || coachingStaff.getLastNames().trim().equals("") ||
				coachingStaff.getPosition() == null || coachingStaff.getPosition().trim().equals("") ||
				coachingStaff.getAge() == null || coachingStaff.getAge() < 1 ||
				coachingStaff.getWeight() == null || coachingStaff.getWeight() < 1 ||
				coachingStaff.getHeight() == null || coachingStaff.getHeight() < 1 ||
				coachingStaff.getClub() == null || coachingStaff.getClub().getClubId() == null || coachingStaff.getClub().getClubId() < 1
				) {
			
			throw new AddingCoachingStaffException("CoachingStaff is not valid to save");
			
		}
		
		this.clubesService.getClubById(coachingStaff.getClub().getClubId());
		
		return true;
	}

	public boolean isCoachingStaffValidToUpdate(CoachingStaff coachingStaff)
			throws InformationNotFoundException, WritingInformationException {
		if(		coachingStaff == null ||
				coachingStaff.getCoachingStaffId() == null || coachingStaff.getCoachingStaffId() < 1 ||
				coachingStaff.getNames() == null || coachingStaff.getNames().trim().equals("") ||
				coachingStaff.getLastNames() == null || coachingStaff.getLastNames().trim().equals("") ||
				coachingStaff.getPosition() == null || coachingStaff.getPosition().trim().equals("") ||
				coachingStaff.getAge() == null || coachingStaff.getAge() < 1 ||
				coachingStaff.getWeight() == null || coachingStaff.getWeight() < 1 ||
				coachingStaff.getHeight() == null || coachingStaff.getHeight() < 1 ||
				coachingStaff.getClub() == null || coachingStaff.getClub().getClubId() == null || coachingStaff.getClub().getClubId() < 1
				) {
			
			throw new AddingCoachingStaffException("CoachingStaff is not valid to save");
			
		}
		
		this.coachingStaffExists(coachingStaff.getCoachingStaffId());
		this.clubesService.getClubById(coachingStaff.getClub().getClubId());
		
		return true;
	}

	public CoachingStaff coachingStaffExists(Long id) throws InformationNotFoundException {
		return this.coachingStaffsDao.findById(id).orElseThrow(() -> {
			StringBuilder sb = new StringBuilder();
			sb.append("CoachingStaff with id ").append(id).append(" has not been found");
			throw new CoachingStaffNotFoundException(sb.toString());
		});
	}
}
