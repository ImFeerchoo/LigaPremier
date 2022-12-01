package com.fernandobetancourt.services;

import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.entity.ClubScoreboard;

public interface IClubesScoreboardsService {
	
	//GET
	public abstract ClubScoreboard getClubScoreboard(Long id) throws InformationNotFoundException;
	public abstract ClubScoreboard getClubScoreboardByScoreboardAndClubStatus(Long scoreboardId, String ClubStatus) 
			throws InformationNotFoundException;

	//POST
	public abstract ClubScoreboard addClubScoreboard(ClubScoreboard clubScoreboard) throws InformationNotFoundException, WritingInformationException;
	
	//PUT
	public abstract ClubScoreboard updateClubScoreboard(ClubScoreboard clubScoreboard) throws InformationNotFoundException, WritingInformationException;
	
	//DELETE
	public abstract ClubScoreboard deleteClubScoreboard(Long id) throws InformationNotFoundException;
	
}
