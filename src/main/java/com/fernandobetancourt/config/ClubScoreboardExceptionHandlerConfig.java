package com.fernandobetancourt.config;

import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fernandobetancourt.exceptions.AddingClubScoreboardException;
import com.fernandobetancourt.exceptions.ClubScoreboardNotFoundException;

@ControllerAdvice
public class ClubScoreboardExceptionHandlerConfig {

	@ExceptionHandler(ClubScoreboardNotFoundException.class)
	public ResponseEntity<?> clubScoreboardNotFoundException(ClubScoreboardNotFoundException e){
		Map<String, String> response = new HashMap<>();
		response.put("error", e.getMessage());
		return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
	}
	
	@ExceptionHandler(AddingClubScoreboardException.class)
	public ResponseEntity<?> addingClubScoreboardException(AddingClubScoreboardException e){
		Map<String, String> response = new HashMap<>();
		response.put("error", e.getMessage());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
}
