package com.fernandobetancourt.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fernandobetancourt.exceptions.AddingCoachingStaffException;
import com.fernandobetancourt.exceptions.CoachingStaffNotFoundException;

@ControllerAdvice
public class CoachingStaffExceptionHandler {
	
	@ExceptionHandler(AddingCoachingStaffException.class)
	public ResponseEntity<?> addingCoachingStaffException(AddingCoachingStaffException e){
		Map<String, String> response = new HashMap<>();
		response.put("error", e.getMessage());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(CoachingStaffNotFoundException.class)
	public ResponseEntity<?> coachingStaffNotFoundException(CoachingStaffNotFoundException e){
		Map<String, String> response = new HashMap<>();
		response.put("error", e.getMessage());
		return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
	}

}
