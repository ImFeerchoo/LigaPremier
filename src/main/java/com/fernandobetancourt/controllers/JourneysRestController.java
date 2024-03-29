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
	
	@GetMapping("/journey/{journeyId}")
	public ResponseEntity<?> getJourney(@PathVariable("journeyId") Long id){
		Map<String, Object> response = new HashMap<>();
		Journey journeyRecovered = this.journeysService.getJourney(id);
		response.put("journey", journeyRecovered);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/journeys/{groupId}")
	public ResponseEntity<?> getJourneysByGroup(@PathVariable("groupId") Long groupId){
		Map<String, Object> response = new HashMap<>();
		List<Journey> journeys = journeysService.getJourneysByGroup(groupId);
		response.put("journeys", journeys);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping("/journeys")
	public ResponseEntity<?> addJourney(@RequestBody Journey journey){
		Map<String, Object> response = new HashMap<>();
		Journey journeyAdded = this.journeysService.addJourney(journey);
		response.put("journey", journeyAdded);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/journeys")
	public ResponseEntity<?> updateJourney(@RequestBody Journey journey){
		Map<String, Object> response = new HashMap<>();
		Journey journeyUpdated = this.journeysService.updateJourney(journey);
		response.put("journey", journeyUpdated);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/journeys/{journeyId}")
	public ResponseEntity<?> deleteJourney(@PathVariable("journeyId") Long id){
		Map<String, Object> response = new HashMap<>();
		Journey journeyDeleted = this.journeysService.deleteJourney(id);
		response.put("journey", journeyDeleted);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
}
