package com.fernandobetancourt.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.ScoreboardNotFoundException;
import com.fernandobetancourt.model.dao.IScoreboardsDao;
import com.fernandobetancourt.model.entity.Scoreboard;

@Service
public class ScoreboardsServiceImpl implements IScoreboardsService {
	
	@Autowired
	private IScoreboardsDao scoreboardsDao;
	
	//DELETE
	
	@Override
	public Scoreboard deleteScoreboard(Long id) throws InformationNotFoundException{
		Scoreboard scoreboardDeleted = this.scoreboardExists(id);
		this.scoreboardsDao.deleteById(id);
		return scoreboardDeleted;
	}

	//VALIDATIONS
	
	@Override
	public Scoreboard scoreboardExists(Long id) throws InformationNotFoundException{
		
		return this.scoreboardsDao.findById(id).orElseThrow(() -> {
			StringBuilder sb = new StringBuilder().append("Scoreboard with id ").append(id).append(" has not been found");
			throw new ScoreboardNotFoundException(sb.toString());
		});
	}

}
