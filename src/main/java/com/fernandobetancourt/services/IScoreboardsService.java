package com.fernandobetancourt.services;

import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.model.entity.Scoreboard;

public interface IScoreboardsService {
	
	//GET
	public abstract Scoreboard getScoreboard(Long id) throws InformationNotFoundException;
	
	//DELETE
	public abstract Scoreboard deleteScoreboard(Long id) throws InformationNotFoundException;
	
}
