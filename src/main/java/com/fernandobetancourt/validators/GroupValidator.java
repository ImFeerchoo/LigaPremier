package com.fernandobetancourt.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fernandobetancourt.exceptions.AddingGroupException;
import com.fernandobetancourt.exceptions.GroupNotFoundException;
import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.dao.IGroupsDao;
import com.fernandobetancourt.model.entity.Group;
import com.fernandobetancourt.services.ISeriesService;

@Component
public class GroupValidator {
	
	@Autowired
	private ISeriesService seriesService;
	
	@Autowired
	private IGroupsDao groupsDao;
	
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

	public boolean isGroupValidToUpdate(Group group) throws InformationNotFoundException, WritingInformationException{
		
		if(		group == null ||
				group.getGroupId() == null || group.getGroupId() < 1 ||
				group.getName() == null || group.getName().trim().equals("") ||
				group.getSerie() == null || group.getSerie().getSerieId() == null || group.getSerie().getSerieId() < 1 
				) {
			
			throw new AddingGroupException("Group is not valid to save");
			
		}

		groupExists(group.getGroupId());
		this.seriesService.getSerieById(group.getSerie().getSerieId());
		
		return true;
	}
	
	public Group groupExists(Long id) throws InformationNotFoundException{
		return this.groupsDao.findById(id).orElseThrow(() -> {
			StringBuilder sb = new StringBuilder().append("Group with id ").append(id).append(" has not been found");
			throw new GroupNotFoundException(sb.toString());
		});
	}

}
