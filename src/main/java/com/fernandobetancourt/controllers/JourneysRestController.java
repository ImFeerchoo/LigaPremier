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
import com.fernandobetancourt.model.entity.Journey;
import com.fernandobetancourt.services.IJourneysService;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class JourneysRestController {
	
	@Autowired
	private IJourneysService journeysService;

	
	@GetMapping("/journeys")
	public ResponseEntity<?> getJourneys(){
		Map<String, Object> response = new HashMap<>();
		List<Journey> journeys = this.journeysService.getAllJourneys();
		response.put("journeys", journeys);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/journeys/{journeyId}")
	public ResponseEntity<?> getJourney(@PathVariable("journeyId") Long id){
		
		Map<String, Object> response = new HashMap<>();
		
		try {
			Journey journeyRecovered = this.journeysService.getJourney(id);
			response.put("journey", journeyRecovered);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}catch(InformationNotFoundException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		
	}
	
	@PostMapping("/journeys")
	public ResponseEntity<?> addJourney(@RequestBody Journey journey){
		Map<String, Object> response = new HashMap<>();
		
		try {
			Journey journeyAdded = this.journeysService.addJourney(journey);
			response.put("journey", journeyAdded);
			return new ResponseEntity<>(response, HttpStatus.CREATED);
		}catch(InformationNotFoundException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}catch(WritingInformationException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/journeys")
	public ResponseEntity<?> updateJourney(@RequestBody Journey journey){
		Map<String, Object> response = new HashMap<>();
		
		try {
			Journey journeyUpdated = this.journeysService.updateJourney(journey);
			response.put("journey", journeyUpdated);
			return new ResponseEntity<>(response, HttpStatus.CREATED);
		}catch(InformationNotFoundException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}catch(WritingInformationException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/journeys/{journeyId}")
	public ResponseEntity<?> deleteJourney(@PathVariable("journeyId") Long id){
		Map<String, Object> response = new HashMap<>();
		
		try {
			Journey journeyDeleted = this.journeysService.deleteJourney(id);
			response.put("journey", journeyDeleted);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}catch(InformationNotFoundException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
	}
	
}
