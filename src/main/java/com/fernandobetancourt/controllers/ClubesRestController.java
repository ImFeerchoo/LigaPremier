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
import com.fernandobetancourt.model.entity.Club;
import com.fernandobetancourt.services.IClubesService;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class ClubesRestController {

	@Autowired
	private IClubesService clubesService;

	@GetMapping("/clubes")
	public ResponseEntity<?> getClubes() {
		Map<String, Object> response = new HashMap<>();
		List<Club> clubes = this.clubesService.getClubes();
		response.put("clubes", clubes);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/clubesByGroup/{groupId}")
	public ResponseEntity<?> getClubByGroup(@PathVariable("groupId") Long groupId) {

		Map<String, Object> response = new HashMap<>();

		try {

			List<Club> clubRecovered  = this.clubesService.getClubesByGroup(groupId);
			response.put("clubes", clubRecovered);
			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (InformationNotFoundException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

	}

	@GetMapping("/club/{id}")
	public ResponseEntity<?> getClubById(@PathVariable("id") Long id) {

		Map<String, Object> response = new HashMap<>();

		try {

			Club clubRecovered  = this.clubesService.getClubById(id);
			response.put("club", clubRecovered);
			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (InformationNotFoundException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

	}

	@PostMapping("/clubes")
	public ResponseEntity<?> addClub(@RequestBody Club club) {

		Map<String, Object> response = new HashMap<>();

		try {
			Club clubSaved = this.clubesService.addClub(club);
			response.put("club", clubSaved);
			return new ResponseEntity<>(response, HttpStatus.CREATED);
		} catch (InformationNotFoundException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		} catch (WritingInformationException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	@PutMapping("/clubes")
	public ResponseEntity<?> updateClub(@RequestBody Club club) {

		Map<String, Object> response = new HashMap<>();

		try {
			Club clubUpdated = this.clubesService.updateClub(club);
			response.put("club", clubUpdated);
			return new ResponseEntity<>(response, HttpStatus.CREATED);
		} catch (InformationNotFoundException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		} catch (WritingInformationException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	@DeleteMapping("/clubes/{id}")
	public ResponseEntity<?> deleteClubById(@PathVariable("id") Long id) {

		Map<String, Object> response = new HashMap<>();

		try {

			Club clubDeleted = this.clubesService.deleteClub(id);
			response.put("club", clubDeleted);
			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (InformationNotFoundException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

	}

}
