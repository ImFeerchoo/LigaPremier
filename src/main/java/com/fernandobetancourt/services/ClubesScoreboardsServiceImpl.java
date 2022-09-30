package com.fernandobetancourt.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fernandobetancourt.exceptions.AddingClubScoreboardException;
import com.fernandobetancourt.exceptions.ClubScoreboardNotFoundException;
import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.dao.IClubesScoreboardsDao;
import com.fernandobetancourt.model.entity.ClubScoreboard;
import com.fernandobetancourt.model.entity.Scoreboard;

@Service
public class ClubesScoreboardsServiceImpl implements IClubesScoreboardsService {
	
	@Autowired
	private IClubesScoreboardsDao clubesScoreboardsDao;
	
	@Autowired
	private IClubesService clubesService;
	
	@Autowired
	private IScoreboardsService scoreboardsService;

	//GET
	
	@Override
	public ClubScoreboard getClubScoreboard(Long id) throws InformationNotFoundException {
		return this.clubScoreboardExists(id);
	}
	
	public ClubScoreboard getClubScoreboardByScoreboardAndClubStatus(Long scoreboardId, String ClubStatus) throws InformationNotFoundException {
		
		Scoreboard scoreboardRecovered = this.scoreboardsService.scoreboardExists(scoreboardId);
		
		return this.clubesScoreboardsDao.findByScoreboardAndClubStatus(scoreboardRecovered, ClubStatus).orElseThrow(() -> {
			throw new ClubScoreboardNotFoundException("ClubScoreboard has not been found");
		});
	}

	//POST
	
	@Override
	public ClubScoreboard addClubScoreboard(ClubScoreboard clubScoreboard) throws InformationNotFoundException, WritingInformationException {
		
		this.isClubScoreboardValidToSave(clubScoreboard);
		
		return this.clubesScoreboardsDao.save(clubScoreboard);
		
	}
	
	//PUT

	@Override
	public ClubScoreboard updateClubScoreboard(ClubScoreboard clubScoreboard) throws InformationNotFoundException, WritingInformationException {
		
		this.isClubScoreboardValidToUpdate(clubScoreboard);
		
		return this.clubesScoreboardsDao.save(clubScoreboard);
	}
	
	//DELETE

	@Override
	public ClubScoreboard deleteClubScoreboard(Long id) throws InformationNotFoundException {
		
		ClubScoreboard clubScoreboardDeleted = this.clubScoreboardExists(id);
		this.clubesScoreboardsDao.deleteById(id);
		
		return clubScoreboardDeleted;
	}
	
	//VALIDATIONS
	//Voy a lanzar las excepciones desde las validaciones
	
	@Override
	public boolean isClubScoreboardValidToSave(ClubScoreboard clubScoreboard) throws InformationNotFoundException, WritingInformationException {
		
		if(		clubScoreboard == null ||
				clubScoreboard.getClubStatus() == null || clubScoreboard.getClubStatus().trim().equals("") ||
				clubScoreboard.getScoreboard() == null || clubScoreboard.getScoreboard().getScoreboardId() == null || clubScoreboard.getScoreboard().getScoreboardId() < 1 ||
				clubScoreboard.getClub() == null || clubScoreboard.getClub().getClubId() == null || clubScoreboard.getClub().getClubId() < 1 
				) {
			
			throw new AddingClubScoreboardException("ClubScoreboard is not valid to save");
			
		}
		
		this.scoreboardsService.scoreboardExists(clubScoreboard.getScoreboard().getScoreboardId());
		this.clubesService.getClubById(clubScoreboard.getClub().getClubId());
		
		return true;
	}

	@Override
	public boolean isClubScoreboardValidToUpdate(ClubScoreboard clubScoreboard) throws InformationNotFoundException, WritingInformationException {
		
		if(		clubScoreboard == null ||
				clubScoreboard.getClubScoreboardId() == null || clubScoreboard.getClubScoreboardId() < 1 ||
				clubScoreboard.getClubStatus() == null || clubScoreboard.getClubStatus().trim().equals("") ||
				clubScoreboard.getScoreboard() == null || clubScoreboard.getScoreboard().getScoreboardId() == null || clubScoreboard.getScoreboard().getScoreboardId() < 1 ||
				clubScoreboard.getClub() == null || clubScoreboard.getClub().getClubId() == null || clubScoreboard.getClub().getClubId() < 1 
				) {
			
			throw new AddingClubScoreboardException("ClubScoreboard is not valid to save");
			
		}
		
		this.clubScoreboardExists(clubScoreboard.getClubScoreboardId());
		this.scoreboardsService.scoreboardExists(clubScoreboard.getScoreboard().getScoreboardId());
		this.clubesService.getClubById(clubScoreboard.getClub().getClubId());
		
		return true;
	}

	@Override
	public ClubScoreboard clubScoreboardExists(Long id) throws InformationNotFoundException {
		return this.clubesScoreboardsDao.findById(id).orElseThrow(() -> {
			StringBuilder sb = new StringBuilder().append("ClubScoreboard with id ").append(id).append(" has not been found");
			throw new ClubScoreboardNotFoundException(sb.toString());
		});
	}


}
