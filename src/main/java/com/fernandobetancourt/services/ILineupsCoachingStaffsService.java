package com.fernandobetancourt.services;

import java.util.List;

import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.entity.LineupCoachingStaff;

public interface ILineupsCoachingStaffsService {

	// GET
	public abstract LineupCoachingStaff getLineupCoachingStaff(Long id) throws InformationNotFoundException;
	public abstract List<LineupCoachingStaff> getLineupCoachingStaffByMatch(Long matchId) throws InformationNotFoundException;

	// POST
	public abstract List<LineupCoachingStaff> addLineupsCoachingStaffs(List<LineupCoachingStaff> lineupsCoachingStaffs, Long matchId,
			String clubStatus) throws InformationNotFoundException, WritingInformationException;

	// DELETE
	public abstract LineupCoachingStaff deleteLineupCoachingStaff(Long id) throws InformationNotFoundException;

}
