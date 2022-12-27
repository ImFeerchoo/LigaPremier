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
import com.fernandobetancourt.model.entity.Goal;
import com.fernandobetancourt.services.IGoalsService;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class GoalsRestController {
	
	@Autowired
	private IGoalsService goalsService;
	
	@GetMapping("/goals/{matchId}")
	public ResponseEntity<?> getGoalsByMatch(@PathVariable("matchId") Long matchId){
		Map<String, Object> response = new HashMap<>();
		
		try {
			List<Goal> goals = this.goalsService.getGoalsByMatch(matchId);
			response.put("goals", goals);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (InformationNotFoundException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/goals/{matchId}")
	public ResponseEntity<?> addGoal(@RequestBody Goal goal, @PathVariable("matchId") Long matchId){
		Map<String, Object> response = new HashMap<>();
		
		try {
			Goal goalSaved = this.goalsService.addGoal(goal, matchId);
			response.put("goal", goalSaved);
			return new ResponseEntity<>(response, HttpStatus.CREATED);
		} catch (InformationNotFoundException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		} catch (WritingInformationException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
//	@PutMapping("/goals/{matchId}")
//	public ResponseEntity<?> updateGoal(@RequestBody Goal goal, @PathVariable("matchId") Long matchId){
//		Map<String, Object> response = new HashMap<>();
//		
//		try {
//			Goal goalUpdated = this.goalsService.updateGoal(goal, matchId);
//			response.put("goal", goalUpdated);
//			return new ResponseEntity<>(response, HttpStatus.CREATED);
//		} catch (InformationNotFoundException e) {
//			response.put("error", e.getMessage());
//			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
//		} catch (WritingInformationException e) {
//			response.put("error", e.getMessage());
//			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}
	
	@DeleteMapping("/goals/{goalId}")
	public ResponseEntity<?> deleteGoal(@PathVariable("goalId") Long goalId){
		Map<String, Object> response = new HashMap<>();
		
		try {
			Goal goalDeleted = this.goalsService.deleteGoal(goalId);
			response.put("goal", goalDeleted);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (InformationNotFoundException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		} catch (WritingInformationException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
