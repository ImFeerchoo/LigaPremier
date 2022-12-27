package com.fernandobetancourt.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fernandobetancourt.exceptions.AddingClubMatchException;
import com.fernandobetancourt.exceptions.ClubMatchNotFoundException;

@ControllerAdvice
public class ClubMatchExceptionHandlerConfig {
	
	@ExceptionHandler(AddingClubMatchException.class)
	public ResponseEntity<?> addingClubMatchException(AddingClubMatchException e){
		Map<String, String> response = new HashMap<>();
		response.put("error", e.getMessage());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ClubMatchNotFoundException.class)
	public ResponseEntity<?> clubMatchNotFoundException(ClubMatchNotFoundException e){
		Map<String, String> response = new HashMap<>();
		response.put("error", e.getMessage());
		return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
	}
}
