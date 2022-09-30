package com.fernandobetancourt.services;

import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.model.entity.Scoreboard;

public interface IScoreboardsService {
	
	//DELETE
	public abstract Scoreboard deleteScoreboard(Long id) throws InformationNotFoundException;

	//VALIDATIONS
	public abstract Scoreboard scoreboardExists(Long id) throws InformationNotFoundException;
	
}
