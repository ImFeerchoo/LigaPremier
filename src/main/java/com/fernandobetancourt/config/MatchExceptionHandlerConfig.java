package com.fernandobetancourt.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fernandobetancourt.exceptions.AddingMatchException;
import com.fernandobetancourt.exceptions.MatchNotFoundException;

@ControllerAdvice
public class MatchExceptionHandlerConfig {
	
	@ExceptionHandler(AddingMatchException.class)
	public ResponseEntity<?> addingMatchException(AddingMatchException e){
		Map<String, String> response = new HashMap<>();
		response.put("error", e.getMessage());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MatchNotFoundException.class)
	public ResponseEntity<?> matchNotFoundException(MatchNotFoundException e){
		Map<String, String> response = new HashMap<>();
		response.put("error", e.getMessage());
		return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
	}
}
