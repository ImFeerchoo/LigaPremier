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

@Service
public class LineupsCoachingStaffsServiceImpl implements ILineupsCoachingStaffsService {

	@Autowired
	private ILineupsCoachingStaffsDao lineupsCoachingStaffsDao;

	@Autowired
	private ILineupsService lineupsService;

	@Autowired
	private ICoachingStaffsService coachingStaffsService;

	@Autowired
	private IMatchesService matchesService;

	@Autowired
	private IClubesMatchesService clubesMatchesService;

	@Autowired
	private ILineupsMatchesService lineupsMatchesService;

	// GET

	@Override
	public LineupCoachingStaff getLineupCoachingStaff(Long id) throws InformationNotFoundException {
		return this.lineupCoachingStaffExists(id);
	}

	@Override
	public List<LineupCoachingStaff> getLineupCoachingStaffByMatch(Long matchId) throws InformationNotFoundException {

		Match matchRecovered = this.matchesService.getMatch(matchId);
		List<LineupMatch> lineupsMatches = this.lineupsMatchesService.getLineupMatchesByMatch(matchRecovered);

		List<LineupCoachingStaff> lineupsCoachingStaffs = new ArrayList<>();
		
		lineupsMatches.forEach(lineupMatch -> {
			
			lineupsCoachingStaffs.addAll(this.lineupsCoachingStaffsDao.findByLineup(lineupMatch.getLineup()).orElseThrow(() -> {
				throw new LineupCoachingStaffNotFoundException("LineupsCoachingStaffs not found");
			}));
			
		});
		
		return lineupsCoachingStaffs;

	}

	// POST

	@Override
	public List<LineupCoachingStaff> addLineupsCoachingStaffs(List<LineupCoachingStaff> lineupsCoachingStaffs,
			Long matchId, String clubStatus) throws InformationNotFoundException, WritingInformationException {

		Match matchRecovered = this.matchesService.getMatch(matchId);

		Club clubRecovered = this.getClub(clubStatus, matchRecovered);

		Lineup lineupRecovered = this.lineupsMatchesService.getLineupMatchesByMatch(matchRecovered).stream()
				.filter(lineupMatch -> lineupMatch.getClubStatus().equalsIgnoreCase(clubStatus))
				.map(lineupMatch -> lineupMatch.getLineup()).findFirst().orElseThrow(() -> {
					throw new LineupMatchNotFoundException("LineupMatch not found");
				});

		this.removeIfHasLineupsCoachingStaffs(lineupRecovered);

		lineupsCoachingStaffs.forEach(lineupCoachingStaff -> {

			lineupCoachingStaff.setLineup(lineupRecovered);
			this.isLineupCoachingStaffValidToSave(lineupCoachingStaff);
			this.playerBelongToClub(this.coachingStaffsService.getCoachingStaff(
					lineupCoachingStaff.getCoachingStaff().getCoachingStaffId()), clubRecovered, clubStatus);
			this.lineupsCoachingStaffsDao.save(lineupCoachingStaff);

		});

		return lineupsCoachingStaffs;
	}

	// DELETE

	@Override
	public LineupCoachingStaff deleteLineupCoachingStaff(Long id) throws InformationNotFoundException {
		LineupCoachingStaff lineupCoachingStaffDeleted = this.lineupCoachingStaffExists(id);
		this.lineupsCoachingStaffsDao.deleteById(id);
		return lineupCoachingStaffDeleted;
	}

	// VALIDATIONS

	@Override
	public boolean isLineupCoachingStaffValidToSave(LineupCoachingStaff lineupCoachingStaff)
			throws InformationNotFoundException, WritingInformationException {

		if (lineupCoachingStaff == null || lineupCoachingStaff.getCoachingStaff() == null
				|| lineupCoachingStaff.getCoachingStaff().getCoachingStaffId() == null
				|| lineupCoachingStaff.getCoachingStaff().getCoachingStaffId() < 1
				|| lineupCoachingStaff.getLineup() == null || lineupCoachingStaff.getLineup().getLineupId() == null
				|| lineupCoachingStaff.getLineup().getLineupId() < 1) {

			throw new AddingLineupCoachingStaffException("LineupCoachingStaff is not valid to save");

		}

		//coachingStaffExists se remplaza con getCoachingStaff
//		this.coachingStaffsService.coachingStaffExists(lineupCoachingStaff.getCoachingStaff().getCoachingStaffId());
		this.coachingStaffsService.getCoachingStaff(lineupCoachingStaff.getCoachingStaff().getCoachingStaffId());
		//lineupExists se remplaza con getLineup
//		this.lineupsService.lineupExists(lineupCoachingStaff.getLineup().getLineupId());
		this.lineupsService.getLineup(lineupCoachingStaff.getLineup().getLineupId());

		return true;
	}

	@Override
	public boolean isLineupCoachingStaffValidToUpdate(LineupCoachingStaff lineupCoachingStaff)
			throws InformationNotFoundException, WritingInformationException {

		if (lineupCoachingStaff == null || lineupCoachingStaff.getLineupCoachingStaffId() == null
				|| lineupCoachingStaff.getLineupCoachingStaffId() < 1 || lineupCoachingStaff.getCoachingStaff() == null
				|| lineupCoachingStaff.getCoachingStaff().getCoachingStaffId() == null
				|| lineupCoachingStaff.getCoachingStaff().getCoachingStaffId() < 1
				|| lineupCoachingStaff.getLineup() == null || lineupCoachingStaff.getLineup().getLineupId() == null
				|| lineupCoachingStaff.getLineup().getLineupId() < 1) {

			throw new AddingLineupCoachingStaffException("LineupCoachingStaff is not valid to save");

		}

		this.lineupCoachingStaffExists(lineupCoachingStaff.getLineupCoachingStaffId());
		//coachingStaffExists se remplazó con getCoachingStaff
//		this.coachingStaffsService.coachingStaffExists(lineupCoachingStaff.getCoachingStaff().getCoachingStaffId());
		this.coachingStaffsService.getCoachingStaff(lineupCoachingStaff.getCoachingStaff().getCoachingStaffId());
		//lineupExists se remplaza con getLineup
//		this.lineupsService.lineupExists(lineupCoachingStaff.getLineup().getLineupId());
		this.lineupsService.getLineup(lineupCoachingStaff.getLineup().getLineupId());

		return true;
	}

	@Override
	public LineupCoachingStaff lineupCoachingStaffExists(Long id) throws InformationNotFoundException {
		return this.lineupsCoachingStaffsDao.findById(id).orElseThrow(() -> {
			StringBuilder sb = new StringBuilder();
			sb.append("LineupCoachingStaff with id ").append(id).append(" has not been found");
			throw new LineupCoachingStaffNotFoundException(sb.toString());
		});
	}

	public boolean playerBelongToClub(CoachingStaff coachingStaff, Club club, String clubStatus)
			throws WritingInformationException {

		if (coachingStaff.getClub().getClubId() != club.getClubId()) {
			StringBuilder sb = new StringBuilder();
			sb.append("CoachingStaff with id ").append(coachingStaff.getCoachingStaffId()).append(" don't belong to ")
					.append(clubStatus).append(" team");
			throw new AddingLineupCoachingStaffException(sb.toString());
		}

		return true;
	}

	public Club getClub(String clubStatus, Match match)
			throws InformationNotFoundException, WritingInformationException {

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

	public void removeIfHasLineupsCoachingStaffs(Lineup lineup) {

		this.lineupsCoachingStaffsDao.findByLineup(lineup).ifPresent(lineupsCoachingStaffs -> {

			lineupsCoachingStaffs.forEach(lineupCoachingStaff -> this.lineupsCoachingStaffsDao
					.deleteById(lineupCoachingStaff.getLineupCoachingStaffId()));

		});
	}

}
