package com.fernandobetancourt.services;

import java.util.List;

import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.entity.Goal;

public interface IGoalsService {

	//GET
	public abstract List<Goal> getGoalsByMatch(Long matchId) throws InformationNotFoundException;
	
	//POST
	public abstract Goal addGoal(Goal goal, Long matchId) throws InformationNotFoundException, WritingInformationException;
	
	//PUT
//	public abstract Goal updateGoal(Goal goal, Long matchId) throws InformationNotFoundException, WritingInformationException;
	
	//DELETE
	public abstract Goal deleteGoal(Long id) throws InformationNotFoundException;
	
	//VALIDATIONS
//	public abstract boolean isGoalValidToSave(Goal goal) throws InformationNotFoundException, WritingInformationException;
//	public abstract boolean isGoalValidToUpdate(Goal goal) throws InformationNotFoundException, WritingInformationException;
//	public abstract Goal goalExists(Long id) throws InformationNotFoundException;
	
}
