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

import com.fernandobetancourt.model.entity.Group;
import com.fernandobetancourt.services.IGroupsService;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class GroupsRestController {
	
	@Autowired
	private IGroupsService groupsService;
	
	@GetMapping("/groups")
	public ResponseEntity<?> getGroups(){
		Map<String, Object> response = new HashMap<>();
		List<Group> groups = this.groupsService.getAllGroups();
		response.put("groups", groups);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/groupsBySerie/{serieId}")
	public ResponseEntity<?> getGroupBySerie(@PathVariable("serieId") Long serieId){
		Map<String, Object> response = new HashMap<>();
		List<Group> groupsRecovered = this.groupsService.getGroupsBySerie(serieId);
		response.put("groups", groupsRecovered);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/groups/{groupId}")
	public ResponseEntity<?> getGroup(@PathVariable("groupId") Long id){
		Map<String, Object> response = new HashMap<>();
		Group groupRecovered = this.groupsService.getGroup(id);
		response.put("group", groupRecovered);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping("/groups")
	public ResponseEntity<?> addGroup(@RequestBody Group group){
		Map<String, Object> response = new HashMap<>();
		Group groupAdded = this.groupsService.addGroup(group);
		response.put("group", groupAdded);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/groups")
	public ResponseEntity<?> updateGroup(@RequestBody Group group){
		Map<String, Object> response = new HashMap<>();
		Group groupUpdated = this.groupsService.updateGroup(group);
		response.put("group", groupUpdated);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/groups/{groupId}")
	public ResponseEntity<?> deleteGroup(@PathVariable("groupId") Long id){
		Map<String, Object> response = new HashMap<>();
		Group groupDeleted = this.groupsService.deleteGroupd(id);
		response.put("group", groupDeleted);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
}
