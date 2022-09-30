package com.fernandobetancourt.services;

import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.model.entity.Lineup;

public interface ILineupsService {
	
	//GET
	public abstract Lineup getLineup(Long id) throws InformationNotFoundException;
	
	//DELETE
	public abstract Lineup deleteLineup(Long id) throws InformationNotFoundException;

	//VALIDATIONS
	public abstract Lineup lineupExists(Long id) throws InformationNotFoundException;
	
}
