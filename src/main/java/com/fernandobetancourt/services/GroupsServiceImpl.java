package com.fernandobetancourt.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fernandobetancourt.exceptions.AddingGroupException;
import com.fernandobetancourt.exceptions.GroupNotFoundException;
import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.dao.IGroupsDao;
import com.fernandobetancourt.model.entity.Group;
import com.fernandobetancourt.model.entity.Serie;

@Service
public class GroupsServiceImpl implements IGroupsService {
	
	@Autowired
	private IGroupsDao groupsDao;
	
	@Autowired
	private ISeriesService seriesService;

	//GET
	
	@Override
	public List<Group> getAllGroups() {
		return this.groupsDao.findAll();
	}
	
	@Override
	public List<Group> getGroupsBySerie(Long serieId) throws InformationNotFoundException {
		Serie serieRecovered = this.seriesService.getSerieById(serieId);
		return this.groupsDao.findBySerie(serieRecovered);
	}

	@Override
	public Group getGroup(Long id) throws InformationNotFoundException {
		return this.groupExists(id);
	}
	
	//POST

	@Override
	public Group addGroup(Group group) throws InformationNotFoundException, WritingInformationException{
		
		isGroupValidToSave(group);
		
		return this.groupsDao.save(group);
	}
	
	//PUT

	@Override
	public Group updateGroup(Group group) throws InformationNotFoundException, WritingInformationException {
		
		isGroupValidToUpdate(group);
		
		this.groupExists(group.getGroupId());
		
		return this.groupsDao.save(group);
	}
	
	//DELETE

	@Override
	public Group deleteGroupd(Long id) throws InformationNotFoundException {
		
		Group groupRecovered = this.groupExists(id);
		
		this.groupsDao.deleteById(id);
		
		return groupRecovered;
	}
	
	//VALIDATIONS

	@Override
	public boolean isGroupValidToSave(Group group) throws InformationNotFoundException, WritingInformationException{
		
		if(		group == null ||
				group.getName() == null || group.getName().trim().equals("") ||
				group.getSerie() == null || group.getSerie().getSerieId() == null || group.getSerie().getSerieId() < 1 
				) {
			
			throw new AddingGroupException("Group is not valid to save");
			
		}
		
		this.seriesService.getSerieById(group.getSerie().getSerieId());
		
		return true;
	}

	@Override
	public boolean isGroupValidToUpdate(Group group) throws InformationNotFoundException, WritingInformationException{
		
		if(		group == null ||
				group.getGroupId() == null || group.getGroupId() < 1 ||
				group.getName() == null || group.getName().trim().equals("") ||
				group.getSerie() == null || group.getSerie().getSerieId() == null || group.getSerie().getSerieId() < 1 
				) {
			
			throw new AddingGroupException("Group is not valid to save");
			
		}
		
		this.seriesService.getSerieById(group.getSerie().getSerieId());
		
		return true;
	}
	
	@Override
	public Group groupExists(Long id) throws InformationNotFoundException{
		return this.groupsDao.findById(id).orElseThrow(() -> {
			StringBuilder sb = new StringBuilder().append("Group with id ").append(id).append(" has not been found");
			throw new GroupNotFoundException(sb.toString());
		});
	}
}
