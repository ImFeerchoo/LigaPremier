package com.fernandobetancourt.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fernandobetancourt.exceptions.AddingLineupCoachingStaffException;
import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.LineupCoachingStaffNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.dao.ILineupsCoachingStaffsDao;
import com.fernandobetancourt.model.entity.Club;
import com.fernandobetancourt.model.entity.CoachingStaff;
import com.fernandobetancourt.model.entity.LineupCoachingStaff;
import com.fernandobetancourt.services.ICoachingStaffsService;
import com.fernandobetancourt.services.ILineupsService;

@Component
public class LineupCoachingStaffValidator {
	
	@Autowired
	private ILineupsService lineupsService;
	
	@Autowired
	private ICoachingStaffsService coachingStaffsService;
	
	@Autowired
	private ILineupsCoachingStaffsDao lineupsCoachingStaffsDao;
	
	public CoachingStaff isLineupCoachingStaffValidToSave(LineupCoachingStaff lineupCoachingStaff)
			throws InformationNotFoundException, WritingInformationException {

		if (
//				lineupCoachingStaff == null|| 
		lineupCoachingStaff.getCoachingStaff() == null
				|| lineupCoachingStaff.getCoachingStaff().getCoachingStaffId() == null
				|| lineupCoachingStaff.getCoachingStaff().getCoachingStaffId() < 1
//				|| lineupCoachingStaff.getLineup() == null || lineupCoachingStaff.getLineup().getLineupId() == null
//				|| lineupCoachingStaff.getLineup().getLineupId() < 1
		) {

			throw new AddingLineupCoachingStaffException("LineupCoachingStaff is not valid to save");

		}

		this.lineupsService.getLineup(lineupCoachingStaff.getLineup().getLineupId());
		return this.coachingStaffsService.getCoachingStaff(lineupCoachingStaff.getCoachingStaff().getCoachingStaffId());
	}

//	@Override
//	public boolean isLineupCoachingStaffValidToUpdate(LineupCoachingStaff lineupCoachingStaff)
//			throws InformationNotFoundException, WritingInformationException {
//
//		if (lineupCoachingStaff == null || lineupCoachingStaff.getLineupCoachingStaffId() == null
//				|| lineupCoachingStaff.getLineupCoachingStaffId() < 1 || lineupCoachingStaff.getCoachingStaff() == null
//				|| lineupCoachingStaff.getCoachingStaff().getCoachingStaffId() == null
//				|| lineupCoachingStaff.getCoachingStaff().getCoachingStaffId() < 1
//				|| lineupCoachingStaff.getLineup() == null || lineupCoachingStaff.getLineup().getLineupId() == null
//				|| lineupCoachingStaff.getLineup().getLineupId() < 1) {
//
//			throw new AddingLineupCoachingStaffException("LineupCoachingStaff is not valid to save");
//
//		}
//
//		this.lineupCoachingStaffExists(lineupCoachingStaff.getLineupCoachingStaffId());
//		//coachingStaffExists se remplazó con getCoachingStaff
////		this.coachingStaffsService.coachingStaffExists(lineupCoachingStaff.getCoachingStaff().getCoachingStaffId());
//		this.coachingStaffsService.getCoachingStaff(lineupCoachingStaff.getCoachingStaff().getCoachingStaffId());
//		//lineupExists se remplaza con getLineup
////		this.lineupsService.lineupExists(lineupCoachingStaff.getLineup().getLineupId());
//		this.lineupsService.getLineup(lineupCoachingStaff.getLineup().getLineupId());
//
//		return true;
//	}

	public LineupCoachingStaff lineupCoachingStaffExists(Long id) throws InformationNotFoundException {
		return this.lineupsCoachingStaffsDao.findById(id).orElseThrow(() -> {
			StringBuilder sb = new StringBuilder();
			sb.append("LineupCoachingStaff with id ").append(id).append(" has not been found");
			throw new LineupCoachingStaffNotFoundException(sb.toString());
		});
	}

	public boolean coachingStaffBelongToClub(CoachingStaff coachingStaff, Club club, String clubStatus)
			throws WritingInformationException {

		if (coachingStaff.getClub().getClubId() != club.getClubId()) {
			StringBuilder sb = new StringBuilder();
			sb.append("CoachingStaff with id ").append(coachingStaff.getCoachingStaffId()).append(" don't belong to ")
					.append(clubStatus).append(" team");
			throw new AddingLineupCoachingStaffException(sb.toString());
		}

		return true;
	}
}
