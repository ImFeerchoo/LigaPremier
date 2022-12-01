package com.fernandobetancourt.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.model.dao.IScoreboardsDao;
import com.fernandobetancourt.model.entity.Scoreboard;
import com.fernandobetancourt.validators.ScoreboardValidator;

@Service
public class ScoreboardsServiceImpl implements IScoreboardsService {
	
	@Autowired
	private IScoreboardsDao scoreboardsDao;
	
	@Autowired
	private ScoreboardValidator scoreboardValidator;
	
	//GET
	
	@Override
	public Scoreboard getScoreboard(Long id) throws InformationNotFoundException {
		return scoreboardValidator.scoreboardExists(id);
	}
	
	//DELETE
	
	@Override
	public Scoreboard deleteScoreboard(Long id) throws InformationNotFoundException{
		Scoreboard scoreboardDeleted = scoreboardValidator.scoreboardExists(id);
		this.scoreboardsDao.deleteById(id);
		return scoreboardDeleted;
	}

}
