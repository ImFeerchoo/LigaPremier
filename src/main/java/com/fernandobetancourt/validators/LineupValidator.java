package com.fernandobetancourt.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.LineupNotFoundException;
import com.fernandobetancourt.model.dao.ILineupsDao;
import com.fernandobetancourt.model.entity.Lineup;

@Component
public class LineupValidator {
	
	@Autowired
	private ILineupsDao lineupsDao;
	
	public Lineup lineupExists(Long id) throws InformationNotFoundException {
		return this.lineupsDao.findById(id).orElseThrow(() -> {
			StringBuilder sb = new StringBuilder().append("Lineup with id ").append(id).append(" has not been found");
			throw new LineupNotFoundException(sb.toString());
		});
	}
}
