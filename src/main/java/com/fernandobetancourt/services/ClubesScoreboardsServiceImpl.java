package com.fernandobetancourt.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fernandobetancourt.exceptions.ClubScoreboardNotFoundException;
import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.dao.IClubesScoreboardsDao;
import com.fernandobetancourt.model.entity.ClubScoreboard;
import com.fernandobetancourt.model.entity.Scoreboard;
import com.fernandobetancourt.validators.ClubScoreboardValidator;

@Service
public class ClubesScoreboardsServiceImpl implements IClubesScoreboardsService {
	
	@Autowired
	private IClubesScoreboardsDao clubesScoreboardsDao;
	
	@Autowired
	private IScoreboardsService scoreboardsService;
	
	@Autowired
	private ClubScoreboardValidator clubScoreboardValidator;

	//GET
	
	@Override
	public ClubScoreboard getClubScoreboard(Long id) throws InformationNotFoundException {
		return clubScoreboardValidator.clubScoreboardExists(id);
	}
	
	public ClubScoreboard getClubScoreboardByScoreboardAndClubStatus(Long scoreboardId, String ClubStatus) throws InformationNotFoundException {
		
		Scoreboard scoreboardRecovered = this.scoreboardsService.getScoreboard(scoreboardId);
		
		return this.clubesScoreboardsDao.findByScoreboardAndClubStatus(scoreboardRecovered, ClubStatus).orElseThrow(() -> {
			throw new ClubScoreboardNotFoundException("ClubScoreboard has not been found");
		});
	}

	//POST
	
	@Override
	public ClubScoreboard addClubScoreboard(ClubScoreboard clubScoreboard) throws InformationNotFoundException, WritingInformationException {
		
		clubScoreboardValidator.isClubScoreboardValidToSave(clubScoreboard);
		
		return this.clubesScoreboardsDao.save(clubScoreboard);
		
	}
	
	//PUT

	@Override
	public ClubScoreboard updateClubScoreboard(ClubScoreboard clubScoreboard) throws InformationNotFoundException, WritingInformationException {
		
		clubScoreboardValidator.isClubScoreboardValidToUpdate(clubScoreboard);
		
		return this.clubesScoreboardsDao.save(clubScoreboard);
	}
	
	//DELETE

	@Override
	public ClubScoreboard deleteClubScoreboard(Long id) throws InformationNotFoundException {
		
		ClubScoreboard clubScoreboardDeleted = clubScoreboardValidator.clubScoreboardExists(id);
		this.clubesScoreboardsDao.deleteById(id);
		
		return clubScoreboardDeleted;
	}
}
