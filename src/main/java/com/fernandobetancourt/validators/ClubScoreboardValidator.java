package com.fernandobetancourt.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fernandobetancourt.exceptions.AddingClubScoreboardException;
import com.fernandobetancourt.exceptions.ClubScoreboardNotFoundException;
import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.dao.IClubesScoreboardsDao;
import com.fernandobetancourt.model.entity.ClubScoreboard;
import com.fernandobetancourt.services.IClubesService;
import com.fernandobetancourt.services.IScoreboardsService;

@Component
public class ClubScoreboardValidator {
	
	@Autowired
	private IScoreboardsService scoreboardsService;
	
	@Autowired
	private IClubesService clubesService;
	
	@Autowired
	private IClubesScoreboardsDao clubesScoreboardsDao;
	
	public boolean isClubScoreboardValidToSave(ClubScoreboard clubScoreboard) throws InformationNotFoundException, WritingInformationException {
		
		if(		clubScoreboard == null ||
				clubScoreboard.getClubStatus() == null || clubScoreboard.getClubStatus().trim().equals("") ||
				clubScoreboard.getScoreboard() == null || clubScoreboard.getScoreboard().getScoreboardId() == null || clubScoreboard.getScoreboard().getScoreboardId() < 1 ||
				clubScoreboard.getClub() == null || clubScoreboard.getClub().getClubId() == null || clubScoreboard.getClub().getClubId() < 1 
				) {
			
			throw new AddingClubScoreboardException("ClubScoreboard is not valid to save");
			
		}
		
		this.scoreboardsService.getScoreboard(clubScoreboard.getScoreboard().getScoreboardId());
		this.clubesService.getClubById(clubScoreboard.getClub().getClubId());
		
		return true;
	}

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
		this.scoreboardsService.getScoreboard(clubScoreboard.getScoreboard().getScoreboardId());
		this.clubesService.getClubById(clubScoreboard.getClub().getClubId());
		
		return true;
	}

	public ClubScoreboard clubScoreboardExists(Long id) throws InformationNotFoundException {
		return this.clubesScoreboardsDao.findById(id).orElseThrow(() -> {
			StringBuilder sb = new StringBuilder().append("ClubScoreboard with id ").append(id).append(" has not been found");
			throw new ClubScoreboardNotFoundException(sb.toString());
		});
	}
}
