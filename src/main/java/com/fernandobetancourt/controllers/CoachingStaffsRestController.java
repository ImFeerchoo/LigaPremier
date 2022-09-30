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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.entity.CoachingStaff;
import com.fernandobetancourt.services.ICoachingStaffsService;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class CoachingStaffsRestController {

	@Autowired
	private ICoachingStaffsService coachingStaffsService;

	@GetMapping("/coachingStaffs")
	public ResponseEntity<?> getCoachingStaffs() {
		Map<String, Object> response = new HashMap<>();
		List<CoachingStaff> coachingStaffs = this.coachingStaffsService.getCoachingStaffs();
		response.put("coachingStaffs", coachingStaffs);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/coachingStaffsClub/{clubId}")
	public ResponseEntity<?> getCoachingStaffsByClub(@PathVariable("clubId") Long clubId) {

		Map<String, Object> response = new HashMap<>();

		try {
			List<CoachingStaff> coachingStaffsRecovered = this.coachingStaffsService.getCoachingStaffsByClub(clubId);
			response.put("coachingStaffs", coachingStaffsRecovered);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (InformationNotFoundException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/coachingStaffs/{coachingStaffsId}")
	public ResponseEntity<?> getCoachingStaff(@PathVariable("coachingStaffsId") Long id) {

		Map<String, Object> response = new HashMap<>();

		try {
			CoachingStaff coachingStaffRecovered = this.coachingStaffsService.getCoachingStaff(id);
			response.put("coachingStaff", coachingStaffRecovered);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (InformationNotFoundException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/coachingStaffs")
	public ResponseEntity<?> addCoachingStaff(@RequestBody CoachingStaff coachingStaff) {

		Map<String, Object> response = new HashMap<>();

		try {
			CoachingStaff coachingStaffSaved = this.coachingStaffsService.addCoachingStaff(coachingStaff);
			response.put("coachingStaff", coachingStaffSaved);
			return new ResponseEntity<>(response, HttpStatus.CREATED);
		} catch (InformationNotFoundException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		} catch (WritingInformationException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	@PutMapping("/coachingStaffs")
	public ResponseEntity<?> updateCoachingStaff(@RequestBody CoachingStaff coachingStaff){
		
		Map<String, Object> response = new HashMap<>();

		try {
			CoachingStaff coachingStaffUpdated = this.coachingStaffsService.updateCoachingStaff(coachingStaff);
			response.put("coachingStaff", coachingStaffUpdated);
			return new ResponseEntity<>(response, HttpStatus.CREATED);
		} catch (InformationNotFoundException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		} catch (WritingInformationException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}

	@DeleteMapping("/coachingStaffs/{coachingStaffsId}")
	public ResponseEntity<?> deleteCoachingStaff(@PathVariable("coachingStaffsId") Long coachingStaffsId){
		
		Map<String, Object> response = new HashMap<>();

		try {
			CoachingStaff coachingStaffDeleted = this.coachingStaffsService.deleteCoachingStaff(coachingStaffsId);
			response.put("coachingStaff", coachingStaffDeleted);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (InformationNotFoundException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		
	}

}
