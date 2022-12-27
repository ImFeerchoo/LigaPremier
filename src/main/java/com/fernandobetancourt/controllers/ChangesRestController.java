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
import com.fernandobetancourt.model.entity.Change;
import com.fernandobetancourt.services.IChangesService;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class ChangesRestController {
	
	@Autowired
	private IChangesService changesService;
	
	@GetMapping("/changes/{matchId}")
	public ResponseEntity<?> getChangesByMatch(@PathVariable("matchId") Long matchId){
		Map<String, Object> response = new HashMap<String, Object>();
		
		try {
			List<Change> changesRecovered = this.changesService.getChangesByMatch(matchId);
			response.put("changes", changesRecovered);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (InformationNotFoundException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
	}
	
//	@PostMapping("/changes/{clubStatus}")
//	public ResponseEntity<?> addChange(@RequestBody Change change, @PathVariable("clubStatus") String clubStatus){
//		Map<String, Object> response = new HashMap<>();
//		
//		try {
//			Change changeSaved = this.changesService.addChange(change, clubStatus);
//			response.put("change", changeSaved);
//			return new ResponseEntity<>(response, HttpStatus.CREATED);
//		} catch (InformationNotFoundException e) {
//			response.put("error", e.getMessage());
//			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
//		} catch (WritingInformationException e) {
//			response.put("error", e.getMessage());
//			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}
//	
//	@PutMapping("/changes/{clubStatus}")
//	public ResponseEntity<?> updateChange(@RequestBody Change change, @PathVariable("clubStatus") String clubStatus){
//		Map<String, Object> response = new HashMap<>();
//		
//		try {
//			Change changeUpdated = this.changesService.updateChange(change, clubStatus);
//			response.put("change", changeUpdated);
//			return new ResponseEntity<>(response, HttpStatus.CREATED);
//		} catch (InformationNotFoundException e) {
//			response.put("error", e.getMessage());
//			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
//		} catch (WritingInformationException e) {
//			response.put("error", e.getMessage());
//			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}
	
	@DeleteMapping("/changes/{changeId}")
	public ResponseEntity<?> deleteChange(@PathVariable("changeId") Long changeId){
		Map<String, Object> response = new HashMap<>();
		
		try {
			Change changeDeleted = this.changesService.deleteChange(changeId);
			response.put("change", changeDeleted);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (InformationNotFoundException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
	}

}
