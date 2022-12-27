package com.fernandobetancourt.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fernandobetancourt.exceptions.AddingLineupMatchException;
import com.fernandobetancourt.exceptions.LineupMatchNotFoundException;

@ControllerAdvice
public class LineupMatchExceptionHandlerConfig {

	@ExceptionHandler(AddingLineupMatchException.class)
	public ResponseEntity<?> addingLineupMatchException(AddingLineupMatchException e){
		Map<String, String> response = new HashMap<>();
		response.put("error", e.getMessage());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(LineupMatchNotFoundException.class)
	public ResponseEntity<?> lineupMatchNotFoundException(LineupMatchNotFoundException e){
		Map<String, String> response = new HashMap<>();
		response.put("error", e.getMessage());
		return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
	}
}
