package com.fernandobetancourt.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fernandobetancourt.exceptions.CoachingStaffNotFoundException;
import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.dao.ICoachingStaffsDao;
import com.fernandobetancourt.model.entity.Club;
import com.fernandobetancourt.model.entity.CoachingStaff;
import com.fernandobetancourt.validators.CoachingStaffValidator;

@Service
public class CoachingStaffsServiceImpl implements ICoachingStaffsService {
	
	@Autowired
	private ICoachingStaffsDao coachingStaffsDao;
	
	@Autowired
	private IClubesService clubesService;
	
	@Autowired
	private CoachingStaffValidator coachingStaffValidator;

	//GET
	
	public List<CoachingStaff> getCoachingStaffs(){
		List<CoachingStaff> coachingStaffs = coachingStaffsDao.findAll();
		if(coachingStaffs.isEmpty()) throw new CoachingStaffNotFoundException("There are not coaching staffs available");
		return coachingStaffs;
	}
	
//	@Override
//	public List<CoachingStaff> getCoachingStaffsByClub(Long clubId) throws InformationNotFoundException {
//		Club clubRecovered = this.clubesService.getClubById(clubId);
//		List<CoachingStaff> coachingStaffs = coachingStaffsDao.findByClub(clubRecovered);
//		if(coachingStaffs.isEmpty()) throw new CoachingStaffNotFoundException("There are not coaching staffs available");
//		return coachingStaffs;
//	}
	
	@Override
	public List<CoachingStaff> getCoachingStaffsByClub(Long clubId) throws InformationNotFoundException {
		Club clubRecovered = this.clubesService.getClubById(clubId);
		List<CoachingStaff> coachingStaffs = coachingStaffsDao.findByClub(clubRecovered);
		if(coachingStaffs.isEmpty()) {
			var sb = new StringBuilder();
			sb.append("Coaching Staffs of club ").append(clubRecovered.getName()).append(" has not been found");
			throw new CoachingStaffNotFoundException(sb.toString());
		}
		return coachingStaffs;
	}
	
	@Override
	public CoachingStaff getCoachingStaff(Long id) throws InformationNotFoundException {
		return coachingStaffValidator.coachingStaffExists(id);
	}
	
	//POST

	@Override
	public CoachingStaff addCoachingStaff(CoachingStaff coachingStaff) throws InformationNotFoundException, WritingInformationException {
		coachingStaffValidator.isCoachingStaffValidToSave(coachingStaff);
		return this.coachingStaffsDao.save(coachingStaff);
	}
	
	//PUT

	@Override
	public CoachingStaff updateCoachingStaff(CoachingStaff coachingStaff) throws InformationNotFoundException, WritingInformationException {
		coachingStaffValidator.isCoachingStaffValidToUpdate(coachingStaff);
		return this.coachingStaffsDao.save(coachingStaff);
	}
	
	//DELETE

	@Override
	public CoachingStaff deleteCoachingStaff(Long id) throws InformationNotFoundException {
		CoachingStaff coachingStaffDeleted = coachingStaffValidator.coachingStaffExists(id);
		this.coachingStaffsDao.deleteById(id);
		return coachingStaffDeleted;
	}
}
