package com.fernandobetancourt.config;

import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fernandobetancourt.exceptions.AddingGroupException;
import com.fernandobetancourt.exceptions.GroupNotFoundException;

@ControllerAdvice
public class GroupExceptionHandlerConfig {

	@ExceptionHandler(AddingGroupException.class)
	public ResponseEntity<?> addingGroupException(AddingGroupException e){
		Map<String, Object> response = new HashMap<>();
		response.put("error", e.getMessage());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(GroupNotFoundException.class)
	public ResponseEntity<?> groupNotFoundException(GroupNotFoundException e){
		Map<String, Object> response = new HashMap<>();
		response.put("error", e.getMessage());
		return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
	}
}
