package com.fernandobetancourt.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fernandobetancourt.exceptions.AddingGoalException;
import com.fernandobetancourt.exceptions.GoalNotFoundException;

@ControllerAdvice
public class GoalExceptionHandlerConfig {
	
	@ExceptionHandler(AddingGoalException.class)
	public ResponseEntity<?> addingGoalException(AddingGoalException e){
		Map<String, String> response = new HashMap<>();
		response.put("error", e.getMessage());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(GoalNotFoundException.class)
	public ResponseEntity<?> goalNotFoundException(GoalNotFoundException e){
		Map<String, String> response = new HashMap<>();
		response.put("error", e.getMessage());
		return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
	}
}
