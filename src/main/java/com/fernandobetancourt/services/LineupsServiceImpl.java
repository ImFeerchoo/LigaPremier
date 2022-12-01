package com.fernandobetancourt.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.model.dao.ILineupsDao;
import com.fernandobetancourt.model.entity.Lineup;
import com.fernandobetancourt.validators.LineupValidator;

@Service
public class LineupsServiceImpl implements ILineupsService {
	
	@Autowired
	private ILineupsDao lineupsDao;
	
	@Autowired
	private LineupValidator lineupValidator;
	
	//GET
	public Lineup getLineup(Long id) throws InformationNotFoundException{
		return lineupValidator.lineupExists(id);
	}
	
	//DELETE
	
	@Override
	public Lineup deleteLineup(Long id)  throws InformationNotFoundException{
		Lineup lineupDeleted = lineupValidator.lineupExists(id);
		this.lineupsDao.deleteById(id);
		return lineupDeleted;
	}
	


}
