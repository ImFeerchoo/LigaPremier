package com.fernandobetancourt.services;

import java.util.List;

import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.entity.Change;

public interface IChangesService {

	//GET
	public abstract List<Change> getChangesByMatch(Long matchId) throws InformationNotFoundException;
	
	//POST
	public abstract Change addChange(Change change, String clubStatus) throws InformationNotFoundException, WritingInformationException;
	
	//PUT
	public abstract Change updateChange(Change change, String clubStatus) throws InformationNotFoundException, WritingInformationException;
	
	//DELETE
	public abstract Change deleteChange(Long id) throws InformationNotFoundException;
	
	//VALIDATIONS
	public abstract boolean isChangeValidToSave(Change change) throws InformationNotFoundException, WritingInformationException;
	public abstract boolean isChangeValidToUpdate(Change change) throws InformationNotFoundException, WritingInformationException;
	public abstract Change changeExists(Long id) throws InformationNotFoundException;
	
}
