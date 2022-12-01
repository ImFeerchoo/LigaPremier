package com.fernandobetancourt.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.ScoreboardNotFoundException;
import com.fernandobetancourt.model.dao.IScoreboardsDao;
import com.fernandobetancourt.model.entity.Scoreboard;

@Component
public class ScoreboardValidator {
	@Autowired
	private IScoreboardsDao scoreboardsDao;

	public Scoreboard scoreboardExists(Long id) throws InformationNotFoundException {

		return this.scoreboardsDao.findById(id).orElseThrow(() -> {
			StringBuilder sb = new StringBuilder().append("Scoreboard with id ").append(id)
					.append(" has not been found");
			throw new ScoreboardNotFoundException(sb.toString());
		});
	}
}
