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
import com.fernandobetancourt.model.entity.LineupPlayer;
import com.fernandobetancourt.services.ILineupsPlayersService;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class LineupsPlayersRestController {
	
	@Autowired
	private ILineupsPlayersService lineupsPlayersService;
	
	@GetMapping("/lineupsPlayers/{matchId}")
	public ResponseEntity<?> getLineupsPlayersByMatch(@PathVariable("matchId") Long matchId){
		
		Map<String, Object> response = new HashMap<>();
		
		try {
			List<LineupPlayer> lineupsPlayersRecovered = this.lineupsPlayersService.getLineupsPlayersByMatch(matchId);
			response.put("LineupsPlayers", lineupsPlayersRecovered);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}catch(InformationNotFoundException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		
	}
	
	@PostMapping("/lineupsPlayers/{matchId}/{clubStatus}")
	public ResponseEntity<?> addLineupsPlayers(@RequestBody List<LineupPlayer> lineupsPlayers, @PathVariable("matchId") Long matchId, 
			@PathVariable("clubStatus") String clubStatus){
		
		Map<String, Object> response = new HashMap<>();
		
		try {
			List<LineupPlayer> lineupsPlayersSaved = this.lineupsPlayersService.addLineupsPlayers(lineupsPlayers, matchId, clubStatus);
			response.put("LineupsPlayers", lineupsPlayersSaved);
			return new ResponseEntity<>(response, HttpStatus.CREATED);
		}catch(InformationNotFoundException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}catch(WritingInformationException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@DeleteMapping("/lineupsPlayers/{lineupPlayerId}")
	public ResponseEntity<?> deleteLineupPlayer(@PathVariable("lineupPlayerId") Long lineupPlayerId){
		
		Map<String, Object> response = new HashMap<>();
		
		try {
			LineupPlayer lineupPlayerDeleted = this.lineupsPlayersService.deleteLineupPlayer(lineupPlayerId);
			response.put("LineupPlayer", lineupPlayerDeleted);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}catch(InformationNotFoundException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		
	}
	
}
