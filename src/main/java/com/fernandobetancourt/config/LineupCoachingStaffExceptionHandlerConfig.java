package com.fernandobetancourt.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fernandobetancourt.exceptions.AddingLineupCoachingStaffException;
import com.fernandobetancourt.exceptions.LineupCoachingStaffNotFoundException;

@ControllerAdvice
public class LineupCoachingStaffExceptionHandlerConfig {
	
	@ExceptionHandler(AddingLineupCoachingStaffException.class)
	public ResponseEntity<?> addingLineupCoachingStaffException(AddingLineupCoachingStaffException e){
		Map<String, String> response = new HashMap<>();
		response.put("error", e.getMessage());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(LineupCoachingStaffNotFoundException.class)
	public ResponseEntity<?> lineupCoachingStaffNotFoundException(LineupCoachingStaffNotFoundException e){
		Map<String, String> response = new HashMap<>();
		response.put("error", e.getMessage());
		return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
	}
}
