package com.fernandobetancourt.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.entity.LineupCoachingStaff;
import com.fernandobetancourt.services.ILineupsCoachingStaffsService;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class LineupsCoachingStaffsRestController {

	@Autowired
	private ILineupsCoachingStaffsService lineupsCoachingStaffsService;

	@GetMapping("/lineupsCoachingStaffs/{matchId}")
	public ResponseEntity<?> getLineupsCoachingStaffsByMatch(@PathVariable("matchId") Long matchId) {

		Map<String, Object> response = new HashMap<>();

		try {
			List<LineupCoachingStaff> lineupsCoachingStaffsRecovered = this.lineupsCoachingStaffsService
					.getLineupCoachingStaffByMatch(matchId);
			response.put("LineupsCoachingStaffs", lineupsCoachingStaffsRecovered);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (InformationNotFoundException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

	}

	@PostMapping("/lineupsCoachingStaffs/{matchId}/{clubStatus}")
	public ResponseEntity<?> addLineupsPlayers(@RequestBody List<LineupCoachingStaff> lineupsCoachingStaffs,
			@PathVariable("matchId") Long matchId, @PathVariable("clubStatus") String clubStatus) {

		Map<String, Object> response = new HashMap<>();

		try {
			List<LineupCoachingStaff> lineupsCoachingStaffsSaved = this.lineupsCoachingStaffsService
					.addLineupsCoachingStaffs(lineupsCoachingStaffs, matchId, clubStatus);
			response.put("LineupsCoachingStaffs", lineupsCoachingStaffsSaved);
			return new ResponseEntity<>(response, HttpStatus.CREATED);
		} catch (InformationNotFoundException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		} catch (WritingInformationException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@DeleteMapping("/lineupsCoachingStaffs/{lineupCoachingStaffId}")
	public ResponseEntity<?> deleteLineupCoachingStaff(
			@PathVariable("lineupCoachingStaffId") Long lineupCoachingStaffId) {

		Map<String, Object> response = new HashMap<>();

		try {
			LineupCoachingStaff lineupCoachingStaffDeleted = this.lineupsCoachingStaffsService
					.deleteLineupCoachingStaff(lineupCoachingStaffId);
			response.put("LineupCoachingStaff", lineupCoachingStaffDeleted);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (InformationNotFoundException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

	}

}
