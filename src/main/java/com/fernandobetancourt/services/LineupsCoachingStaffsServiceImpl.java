package com.fernandobetancourt.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fernandobetancourt.exceptions.AddingLineupCoachingStaffException;
import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.LineupCoachingStaffNotFoundException;
import com.fernandobetancourt.exceptions.LineupMatchNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.dao.ILineupsCoachingStaffsDao;
import com.fernandobetancourt.model.entity.Club;
import com.fernandobetancourt.model.entity.CoachingStaff;
import com.fernandobetancourt.model.entity.Lineup;
import com.fernandobetancourt.model.entity.LineupCoachingStaff;
import com.fernandobetancourt.model.entity.LineupMatch;
import com.fernandobetancourt.model.entity.Match;
import com.fernandobetancourt.validators.LineupCoachingStaffValidator;

@Service
public class LineupsCoachingStaffsServiceImpl implements ILineupsCoachingStaffsService {

	@Autowired
	private ILineupsCoachingStaffsDao lineupsCoachingStaffsDao;

	@Autowired
	private IMatchesService matchesService;

	@Autowired
	private IClubesMatchesService clubesMatchesService;

	@Autowired
	private ILineupsMatchesService lineupsMatchesService;
	
	@Autowired
	private LineupCoachingStaffValidator lineupCoachingStaffValidator;

	// GET

	@Override
	public LineupCoachingStaff getLineupCoachingStaff(Long id) throws InformationNotFoundException {
		return lineupCoachingStaffValidator.lineupCoachingStaffExists(id);
	}
	
	@Override
	public List<LineupCoachingStaff> getLineupCoachingStaffByMatch(Long matchId) throws InformationNotFoundException {
		
		Match matchRecovered = this.matchesService.getMatch(matchId);
		List<LineupMatch> lineupsMatches = this.lineupsMatchesService.getLineupMatchesByMatch(matchRecovered);
		
		List<LineupCoachingStaff> lineupsCoachingStaffsReturned = new ArrayList<>();
		
		lineupsMatches.forEach(lineupMatch -> {
			lineupsCoachingStaffsReturned.addAll(lineupsCoachingStaffsDao.findByLineup(lineupMatch.getLineup()));
			
		});
		
		if(lineupsCoachingStaffsReturned.isEmpty()) throw new LineupCoachingStaffNotFoundException("LineupsCoachingStaffs not found"); 
		return lineupsCoachingStaffsReturned;
		
	}

	// POST

	@Override
	public List<LineupCoachingStaff> addLineupsCoachingStaffs(List<LineupCoachingStaff> lineupsCoachingStaffs, Long matchId, String clubStatus) throws InformationNotFoundException, WritingInformationException {

		Match matchRecovered = this.matchesService.getMatch(matchId);

		Club clubRecovered = this.getClub(clubStatus, matchRecovered);

		Lineup lineupRecovered = this.lineupsMatchesService.getLineupMatchesByMatch(matchRecovered).stream()
				.filter(lineupMatch -> lineupMatch.getClubStatus().equalsIgnoreCase(clubStatus))
				.map(lineupMatch -> lineupMatch.getLineup()).findFirst().orElseThrow(() -> {
					throw new LineupMatchNotFoundException("LineupMatch not found");
				});

		this.removeIfHasLineupsCoachingStaffs(lineupRecovered);

		lineupsCoachingStaffs.forEach(lineupCoachingStaff -> {
			if(lineupCoachingStaff == null) throw new AddingLineupCoachingStaffException("LineupCoachingStaff is not valid to save");
			
			lineupCoachingStaff.setLineup(lineupRecovered);
			CoachingStaff coachingStaff = lineupCoachingStaffValidator.isLineupCoachingStaffValidToSave(lineupCoachingStaff);
			lineupCoachingStaffValidator.coachingStaffBelongToClub(coachingStaff, clubRecovered, clubStatus);
			this.lineupsCoachingStaffsDao.save(lineupCoachingStaff);

		});

		return lineupsCoachingStaffs;
	}

	// DELETE

	@Override
	public LineupCoachingStaff deleteLineupCoachingStaff(Long id) throws InformationNotFoundException {
		LineupCoachingStaff lineupCoachingStaffDeleted = lineupCoachingStaffValidator.lineupCoachingStaffExists(id);
		this.lineupsCoachingStaffsDao.deleteById(id);
		return lineupCoachingStaffDeleted;
	}
	
	//UTILERY

	private Club getClub(String clubStatus, Match match)
			throws InformationNotFoundException, WritingInformationException {
		
		if(clubStatus == null) throw new WritingInformationException("ClubStatus is not valid");

		Club clubRecovered;
		if (clubStatus.trim().equalsIgnoreCase("Local")) {
			clubRecovered = this.clubesMatchesService.getClubMatchByMatch(match).getLocalClub();
		} else if (clubStatus.trim().equalsIgnoreCase("Visitor")) {
			clubRecovered = this.clubesMatchesService.getClubMatchByMatch(match).getVisitorClub();
		} else {
			throw new WritingInformationException("ClubStatus is not valid");
		}

		return clubRecovered;
	}
	
	private void removeIfHasLineupsCoachingStaffs(Lineup lineup) {
		
		List<LineupCoachingStaff> lineupCoachingStaffs = lineupsCoachingStaffsDao.findByLineup(lineup);
	
		lineupCoachingStaffs.forEach(lineupCoachingStaff -> {
			 this.lineupsCoachingStaffsDao.deleteById(lineupCoachingStaff.getLineupCoachingStaffId());
		});
	}

}
