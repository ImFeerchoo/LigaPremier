package com.fernandobetancourt.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fernandobetancourt.exceptions.GroupNotFoundException;
import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.dao.IGroupsDao;
import com.fernandobetancourt.model.entity.Group;
import com.fernandobetancourt.model.entity.Serie;
import com.fernandobetancourt.validators.GroupValidator;

@Service
public class GroupsServiceImpl implements IGroupsService {
	
	@Autowired
	private IGroupsDao groupsDao;
	
	@Autowired
	private ISeriesService seriesService;
	
	@Autowired
	private GroupValidator groupValidator;

	//GET
	
	@Override
	public List<Group> getAllGroups() {
		List<Group> groups = groupsDao.findAll();
		if(groups.isEmpty()) throw new GroupNotFoundException("There are not groups available");
		return groups;
	}
	
//	@Override
//	public List<Group> getGroupsBySerie(Long serieId) throws InformationNotFoundException {
//		Serie serieRecovered = this.seriesService.getSerieById(serieId);
//		return this.groupsDao.findBySerie(serieRecovered);
//	}
	
	@Override
	public List<Group> getGroupsBySerie(Long serieId) throws InformationNotFoundException {
		Serie serieRecovered = this.seriesService.getSerieById(serieId);
		List<Group> groups = groupsDao.findBySerie(serieRecovered);
		if(groups.isEmpty()) {
			var sb = new StringBuilder();
			sb.append("Groups of ").append(serieRecovered.getName()).append(" has not been found");
			throw new GroupNotFoundException(sb.toString());
		}
		return groups;
	}
	
	@Override
	public Group getGroup(Long id) throws InformationNotFoundException {
		return groupValidator.groupExists(id);
	}
	
	//POST
	
	@Override
	public Group addGroup(Group group) throws InformationNotFoundException, WritingInformationException{
		groupValidator.isGroupValidToSave(group);
		return this.groupsDao.save(group);
	}
	
	//PUT
	
	@Override
	public Group updateGroup(Group group) throws InformationNotFoundException, WritingInformationException {
		groupValidator.isGroupValidToUpdate(group);
		return this.groupsDao.save(group);
	}
	
	//DELETE
	
	@Override
	public Group deleteGroupd(Long id) throws InformationNotFoundException {
		Group groupRecovered = groupValidator.groupExists(id);
		this.groupsDao.deleteById(id);
		return groupRecovered;
	}
}
