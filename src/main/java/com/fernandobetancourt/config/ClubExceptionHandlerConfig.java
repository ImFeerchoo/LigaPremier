package com.fernandobetancourt.config;

import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fernandobetancourt.exceptions.AddingClubException;
import com.fernandobetancourt.exceptions.ClubNotFoundException;

@ControllerAdvice
public class ClubExceptionHandlerConfig {

	@ExceptionHandler(AddingClubException.class)
	public ResponseEntity<?> addingClubException(AddingClubException ex){
		Map<String, Object> response = new HashMap<>();
		response.put("error", ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ClubNotFoundException.class)
	public ResponseEntity<?> clubNotFoundException(ClubNotFoundException ex){
		Map<String, Object> response = new HashMap<>();
		response.put("error", ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
	}
	
}
