package com.fernandobetancourt.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fernandobetancourt.exceptions.AddingCoachingStaffException;
import com.fernandobetancourt.exceptions.CoachingStaffNotFoundException;
import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.dao.ICoachingStaffsDao;
import com.fernandobetancourt.model.entity.Club;
import com.fernandobetancourt.model.entity.CoachingStaff;

@Service
public class CoachingStaffsServiceImpl implements ICoachingStaffsService {
	
	@Autowired
	private ICoachingStaffsDao coachingStaffsDao;
	
	@Autowired
	private IClubesService clubesService;

	//GET
	
	public List<CoachingStaff> getCoachingStaffs(){
		return this.coachingStaffsDao.findAll();
	}
	
	@Override
	public List<CoachingStaff> getCoachingStaffsByClub(Long clubId) throws InformationNotFoundException {
		Club clubRecovered = this.clubesService.getClubById(clubId);
		return this.coachingStaffsDao.findByClub(clubRecovered);
	}
	
	@Override
	public CoachingStaff getCoachingStaff(Long id) throws InformationNotFoundException {
		return this.coachingStaffExists(id);
	}
	
	//POST

	@Override
	public CoachingStaff addCoachingStaff(CoachingStaff coachingStaff) throws InformationNotFoundException, WritingInformationException {
		this.isCoachingStaffValidToSave(coachingStaff);
		return this.coachingStaffsDao.save(coachingStaff);
	}
	
	//PUT

	@Override
	public CoachingStaff updateCoachingStaff(CoachingStaff coachingStaff)
			throws InformationNotFoundException, WritingInformationException {
		this.isCoachingStaffValidToUpdate(coachingStaff);
		return this.coachingStaffsDao.save(coachingStaff);
	}
	
	//DELETE

	@Override
	public CoachingStaff deleteCoachingStaff(Long id) throws InformationNotFoundException {
		CoachingStaff coachingStaffDeleted = this.coachingStaffExists(id);
		this.coachingStaffsDao.deleteById(id);
		return coachingStaffDeleted;
	}
	
	//VALIDATIONS

	@Override
	public boolean isCoachingStaffValidToSave(CoachingStaff coachingStaff)
			throws InformationNotFoundException, WritingInformationException {
		
		if(		coachingStaff == null ||
				coachingStaff.getNames() == null || coachingStaff.getNames().trim().equals("") ||
				coachingStaff.getLastNames() == null || coachingStaff.getLastNames().trim().equals("") ||
				coachingStaff.getPosition() == null || coachingStaff.getPosition().trim().equals("") ||
				coachingStaff.getAge() == null ||
				coachingStaff.getWeight() == null ||
				coachingStaff.getHeight() == null ||
				coachingStaff.getClub() == null || coachingStaff.getClub().getClubId() == null || coachingStaff.getClub().getClubId() < 1
				) {
			
			throw new AddingCoachingStaffException("CoachingStaff is not valid to save");
			
		}
		
		this.clubesService.getClubById(coachingStaff.getClub().getClubId());
		
		return true;
	}

	@Override
	public boolean isCoachingStaffValidToUpdate(CoachingStaff coachingStaff)
			throws InformationNotFoundException, WritingInformationException {
		
		if(		coachingStaff == null ||
				coachingStaff.getCoachingStaffId() == null || coachingStaff.getCoachingStaffId() < 1 ||
				coachingStaff.getNames() == null || coachingStaff.getNames().trim().equals("") ||
				coachingStaff.getLastNames() == null || coachingStaff.getLastNames().trim().equals("") ||
				coachingStaff.getPosition() == null || coachingStaff.getPosition().trim().equals("") ||
				coachingStaff.getAge() == null ||
				coachingStaff.getWeight() == null ||
				coachingStaff.getHeight() == null ||
				coachingStaff.getClub() == null || coachingStaff.getClub().getClubId() == null || coachingStaff.getClub().getClubId() < 1
				) {
			
			throw new AddingCoachingStaffException("CoachingStaff is not valid to save");
			
		}
		
		this.coachingStaffExists(coachingStaff.getCoachingStaffId());
		this.clubesService.getClubById(coachingStaff.getClub().getClubId());
		
		return true;
	}

	@Override
	public CoachingStaff coachingStaffExists(Long id) throws InformationNotFoundException {
		return this.coachingStaffsDao.findById(id).orElseThrow(() -> {
			StringBuilder sb = new StringBuilder();
			sb.append("CoachingStaff with id ").append(id).append(" has not been found");
			throw new CoachingStaffNotFoundException(sb.toString());
		});
	}
}
