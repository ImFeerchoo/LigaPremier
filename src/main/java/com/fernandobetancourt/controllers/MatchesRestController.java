package com.fernandobetancourt.controllers;

import java.util.HashMap;
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
import com.fernandobetancourt.model.entity.Match;
import com.fernandobetancourt.services.IMatchesService;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class MatchesRestController {
	
	@Autowired
	private IMatchesService matchesService;
	
	@GetMapping("/matches/{matchId}")
	public ResponseEntity<?> getMatch(@PathVariable("matchId") Long matchId){
		Map<String, Object> response = new HashMap<>();
		
		try {
			Match matchRecovered = this.matchesService.getMatch(matchId);
			response.put("match", matchRecovered);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}catch(InformationNotFoundException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/matches/{localClubId}/{visitorClubId}")
	public ResponseEntity<?> addMatch(@RequestBody Match match, @PathVariable("localClubId") Long localClubId, @PathVariable("visitorClubId") Long visitorClubId){ 

		Map<String, Object> response = new HashMap<>();
		
		try {
			Match matchSaved = this.matchesService.addMatch(match, localClubId, visitorClubId);
			response.put("match", matchSaved);
			return new ResponseEntity<>(response, HttpStatus.CREATED);
		}catch(InformationNotFoundException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}catch(WritingInformationException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/matches/{localClubId}/{visitorClubId}")
	public ResponseEntity<?> updateMatch(@RequestBody Match match, @PathVariable("localClubId") Long localClubId, @PathVariable("visitorClubId") Long visitorClubId){ 
		
		Map<String, Object> response = new HashMap<>();
		
		try {
			Match matchUpdated = this.matchesService.updateMatch(match, localClubId, visitorClubId);
			response.put("match", matchUpdated);
			return new ResponseEntity<>(response, HttpStatus.CREATED);
		}catch(InformationNotFoundException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}catch(WritingInformationException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@DeleteMapping("/matches/{matchId}")
	public ResponseEntity<?> deleteMatch(@PathVariable("matchId") Long matchId){
		
		Map<String, Object> response = new HashMap<>();
		
		try {
			Match matchDeleted = this.matchesService.deleteMatch(matchId);
			response.put("match", matchDeleted);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}catch(InformationNotFoundException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
	}
	
}
