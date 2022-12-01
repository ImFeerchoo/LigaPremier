package com.fernandobetancourt.services;

import java.util.List;

import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.entity.Group;

public abstract interface IGroupsService {

	//GET
	public abstract List<Group> getAllGroups();
	public abstract List<Group> getGroupsBySerie(Long serieId) throws InformationNotFoundException;
	public abstract Group getGroup(Long id) throws InformationNotFoundException;
	
	//POST
	public abstract Group addGroup(Group group) throws WritingInformationException;
	
	//PUT
	public abstract Group updateGroup(Group group) throws InformationNotFoundException, WritingInformationException;
	
	//DELETE
	public abstract Group deleteGroupd(Long id) throws InformationNotFoundException;
}
