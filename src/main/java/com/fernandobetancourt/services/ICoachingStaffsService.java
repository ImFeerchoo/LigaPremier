package com.fernandobetancourt.services;

import java.util.List;

import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.entity.CoachingStaff;

public interface ICoachingStaffsService {
	
	//GET	
	public abstract List<CoachingStaff> getCoachingStaffs();
	public abstract List<CoachingStaff> getCoachingStaffsByClub(Long clubId) throws InformationNotFoundException;
	public abstract CoachingStaff getCoachingStaff(Long id) throws InformationNotFoundException;
	
	//POST
	public abstract CoachingStaff addCoachingStaff(CoachingStaff coachingStaff) throws InformationNotFoundException, WritingInformationException;
	
	//PUT
	public abstract CoachingStaff updateCoachingStaff(CoachingStaff coachingStaff) throws InformationNotFoundException, WritingInformationException;
	
	//DELETE
	public abstract CoachingStaff deleteCoachingStaff(Long id) throws InformationNotFoundException;

}
