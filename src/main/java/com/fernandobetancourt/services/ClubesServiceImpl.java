package com.fernandobetancourt.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fernandobetancourt.exceptions.ClubNotFoundException;
import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.dao.IClubesDao;
import com.fernandobetancourt.model.entity.Club;
import com.fernandobetancourt.model.entity.Group;
import com.fernandobetancourt.validators.ClubValidator;

@Service
public class ClubesServiceImpl implements IClubesService {

	@Autowired
	private IClubesDao clubesDao;
	
	@Autowired
	private IGroupsService groupsService;
	
	@Autowired
	private ClubValidator clubValidator;

	// GET
	
	@Override
	public List<Club> getClubes() {
		List<Club> clubes = clubesDao.findAll();
		if(clubes.isEmpty()) throw new ClubNotFoundException("There are not clubes available");
		return clubes;
	}
	
//	@Override
//	public List<Club> getClubesByGroup(Long groupId) throws InformationNotFoundException {
//		Group groupRecovered = this.groupsService.getGroup(groupId);
//		return this.clubesDao.findByGroup(groupRecovered);
//	}
	
	@Override
	public List<Club> getClubesByGroup(Long groupId) throws InformationNotFoundException {
		Group groupRecovered = this.groupsService.getGroup(groupId);
		List<Club> clubes = clubesDao.findByGroup(groupRecovered);
		if(clubes.isEmpty()) {
			var sb = new StringBuilder();
			sb.append("Clubes of ").append(groupRecovered.getName()).append(" has not been found");
			throw new ClubNotFoundException(sb.toString());
		}
		return clubes;
	}

	@Override
	public Club getClubById(Long id) throws InformationNotFoundException {
		return clubValidator.clubExists(id);
	}

	// POST

	@Override
	public Club addClub(Club club) throws InformationNotFoundException, WritingInformationException {
		clubValidator.isClubValidToSave(club);
		return this.clubesDao.save(club);
	}

	// PUT

	@Override
	public Club updateClub(Club club) throws InformationNotFoundException, WritingInformationException {
		clubValidator.isClubValidToUpdate(club);
		return this.clubesDao.save(club);
	}

	// DELETE

	@Override
	public Club deleteClub(Long id) throws InformationNotFoundException {
		Club clubDeleted = clubValidator.clubExists(id);
		this.clubesDao.deleteById(id);
		return clubDeleted;
	}
}
