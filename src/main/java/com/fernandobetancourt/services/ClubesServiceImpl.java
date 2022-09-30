package com.fernandobetancourt.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fernandobetancourt.exceptions.AddingClubException;
import com.fernandobetancourt.exceptions.ClubNotFoundException;
import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.dao.IClubesDao;
import com.fernandobetancourt.model.entity.Club;
import com.fernandobetancourt.model.entity.Group;

@Service
public class ClubesServiceImpl implements IClubesService {

	@Autowired
	private IClubesDao clubesDao;
	
	@Autowired
	private IGroupsService groupsService;

	// GET
	
	@Override
	public List<Club> getClubes() {
		return this.clubesDao.findAll();
	}
	
	@Override
	public List<Club> getClubesByGroup(Long groupId) throws InformationNotFoundException {
		Group groupRecovered = this.groupsService.getGroup(groupId);
		return this.clubesDao.findByGroup(groupRecovered);
	}

	@Override
	public Club getClubById(Long id) throws InformationNotFoundException {
		return this.clubExists(id);
	}

	// POST

	@Override
	public Club addClub(Club club) throws InformationNotFoundException, WritingInformationException {
		this.isClubValidToSave(club);
		return this.clubesDao.save(club);
	}

	// PUT

	@Override
	public Club updateClub(Club club) throws InformationNotFoundException, WritingInformationException {
		this.isClubValidToUpdate(club);
		return this.clubesDao.save(club);
	}

	// DELETE

	@Override
	public Club deleteClub(Long id) throws InformationNotFoundException {
		Club clubDeleted = this.clubExists(id);
		this.clubesDao.deleteById(id);
		return clubDeleted;
	}

	// VALIDATIONS

	@Override
	public boolean isClubValidToSave(Club club) throws InformationNotFoundException, WritingInformationException {

		if(		club == null ||
				club.getName() == null || club.getName().trim().equals("") ||
				club.getStadium() == null || club.getStadium().trim().equals("") ||
				club.getGroup() == null || club.getGroup().getGroupId() == null || club.getGroup().getGroupId() < 1
				) {
			
			throw new AddingClubException("Club is not valid to save");
			
		}
		
		this.groupsService.groupExists(club.getGroup().getGroupId());
		
		return true;
	}

	@Override
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
		this.groupsService.groupExists(club.getGroup().getGroupId());
		
		return true;
	}

	@Override
	public Club clubExists(Long id) throws InformationNotFoundException {
		return this.clubesDao.findById(id).orElseThrow(() -> {
			StringBuilder sb = new StringBuilder().append("Club with id ").append(id).append(" has not been found");
			throw new ClubNotFoundException(sb.toString());
		});
	}
}
