package com.fernandobetancourt.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.LineupNotFoundException;
import com.fernandobetancourt.model.dao.ILineupsDao;
import com.fernandobetancourt.model.entity.Lineup;

@Service
public class LineupsServiceImpl implements ILineupsService {
	
	@Autowired
	private ILineupsDao lineupsDao;
	
	//GET
	public Lineup getLineup(Long id) throws InformationNotFoundException{
		return this.lineupExists(id);
	}
	
	//DELETE
	
	@Override
	public Lineup deleteLineup(Long id)  throws InformationNotFoundException{
		Lineup lineupDeleted = this.lineupExists(id);
		this.lineupsDao.deleteById(id);
		return lineupDeleted;
	}
	
	//VALIDATIONS

	@Override
	public Lineup lineupExists(Long id) throws InformationNotFoundException {
		return this.lineupsDao.findById(id).orElseThrow(() -> {
			StringBuilder sb = new StringBuilder().append("Lineup with id ").append(id).append(" has not been found");
			throw new LineupNotFoundException(sb.toString());
		});
	}

}
