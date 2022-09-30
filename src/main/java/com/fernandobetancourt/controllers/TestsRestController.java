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
import com.fernandobetancourt.model.dao.IUsersDao;
import com.fernandobetancourt.model.entity.ClubScoreboard;
import com.fernandobetancourt.model.entity.Lineup;
import com.fernandobetancourt.model.entity.UserEntity;
import com.fernandobetancourt.model.entity.UserRole;
import com.fernandobetancourt.services.IClubesScoreboardsService;
import com.fernandobetancourt.services.ILineupsMatchesService;
import com.fernandobetancourt.services.ILineupsService;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class TestsRestController {
	
	//SI FUNCIONAN LOS MÉTODOS DEL ClubScoreboardServiceImpl

	@Autowired
	private IClubesScoreboardsService clubesScoreboardsService;
	
	@GetMapping("/clubScoreboard/{clubScoreboardId}")
	public ResponseEntity<?> getClubScoreboard(@PathVariable("clubScoreboardId") Long id){
		
		Map<String, Object> response = new HashMap<>();
		
		try {
			ClubScoreboard clubScoreboard = this.clubesScoreboardsService.getClubScoreboard(id);
			response.put("clubScoreboard", clubScoreboard);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}catch(InformationNotFoundException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		
	}
	
	@GetMapping("/clubScoreboard/{scoreboardId}/{clubStatus}")
	public ResponseEntity<?> getClubScoreboardByScoreboardAndClubStatus(@PathVariable("scoreboardId") Long scoreboardId, 
			@PathVariable("clubStatus") String clubStatus){
		Map<String, Object> response = new HashMap<>();
		
		try {
			ClubScoreboard clubScoreboardRecovered = this.clubesScoreboardsService.getClubScoreboardByScoreboardAndClubStatus(scoreboardId, clubStatus);
			response.put("ClubScoreboard", clubScoreboardRecovered);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}catch(InformationNotFoundException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/clubScoreboard")
	public ResponseEntity<?> getClubScoreboard(@RequestBody ClubScoreboard clubScoreboard){
		
		Map<String, Object> response = new HashMap<>();
		
		try {
			ClubScoreboard clubScoreboardAdded = this.clubesScoreboardsService.addClubScoreboard(clubScoreboard);
			response.put("clubScoreboard", clubScoreboardAdded);
			return new ResponseEntity<>(response, HttpStatus.CREATED);
		}catch(InformationNotFoundException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}catch(WritingInformationException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@PutMapping("/clubScoreboard")
	public ResponseEntity<?> updateClubScoreboard(@RequestBody ClubScoreboard clubScoreboard){
		
		Map<String, Object> response = new HashMap<>();
		
		try {
			ClubScoreboard clubScoreboardUpdated = this.clubesScoreboardsService.updateClubScoreboard(clubScoreboard);
			response.put("clubScoreboard", clubScoreboardUpdated);
			return new ResponseEntity<>(response, HttpStatus.CREATED);
		}catch(InformationNotFoundException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}catch(WritingInformationException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	
	@DeleteMapping("/clubScoreboard/{clubScoreboardId}")
	public ResponseEntity<?> deleteClubScoreboard(@PathVariable("clubScoreboardId") Long id){
		
		Map<String, Object> response = new HashMap<>();
		
		try {
			ClubScoreboard clubScoreboardDeleted = this.clubesScoreboardsService.deleteClubScoreboard(id);
			response.put("clubScoreboard", clubScoreboardDeleted);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}catch(InformationNotFoundException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		
	}
	
	@Autowired
	private ILineupsMatchesService lineupsMatchesService;
	
//	@GetMapping("/lineupMatch/{matchId}")
//	public ResponseEntity<?> getLineupMatchByMatch(@PathVariable("matchId") Long id){
//		Map<String, Object> response = new HashMap<>();
//		
//		try {
//			Match match = new Match();
//			match.setMatchId(id);
//			LineupMatch lineupMatchRecovered = this.lineupsMatchesService.getLineupMatchByMatch(match);
//			response.put("lineupMatch", lineupMatchRecovered);
//			return new ResponseEntity<>(response, HttpStatus.OK);
//		}catch(InformationNotFoundException e) {
//			response.put("error", e.getMessage());
//			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
//		}
//	}
	
	@Autowired
	private ILineupsService lineupsService;
	
	@GetMapping("/lineup/{lineupId}")
	public ResponseEntity<?> getLineup(@PathVariable("lineupId") Long lineupId){
		
		Map<String, Object> response = new HashMap<>();
		
		try {
			Lineup lineupRecovered = this.lineupsService.getLineup(lineupId);
			response.put("lineup", lineupRecovered);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}catch(InformationNotFoundException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
	}
	
	
	@Autowired
	private IUsersDao usersDao;
	
	@GetMapping("/testUsuario")
	public String getFirstUser() {
		
		List<UserEntity> users = usersDao.findAll();
		
		if(!users.isEmpty()) {
			UserEntity user = users.get(0);
			String res = user.toString();
			res += "\n";
			
			List<UserRole> userRoles = user.getUsersRoles();
			for(UserRole s: userRoles) {
				res += s.getRole().getName() + "\n";
			}
			
			return res;
		}
		
		return "No se encontró ningún usuario";
	}
	
	@GetMapping("/testUsuario2")
	public String getFirstUser2() {
		
		UserEntity user= usersDao.findByUsername("feerchoo").orElseThrow(() -> {
			throw new RuntimeException("Error");
		});
		
		return user.getName() + user.getLastName();
	}
	
	
}
