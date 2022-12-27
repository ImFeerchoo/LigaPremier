package com.fernandobetancourt.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fernandobetancourt.exceptions.AddingJourneyException;
import com.fernandobetancourt.exceptions.JourneyNotFoundException;

@ControllerAdvice
public class JourneyExceptionHandlerConfig {
	
	@ExceptionHandler(AddingJourneyException.class)
	public ResponseEntity<?> addingJourneyException(AddingJourneyException ex){
		Map<String, Object> response = new HashMap<>();
		response.put("error", ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(JourneyNotFoundException.class)
	public ResponseEntity<?> journeyNotFoundException(JourneyNotFoundException ex){
		Map<String, Object> response = new HashMap<>();
		response.put("error", ex.getMessage());
		System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%Se esta ejecutando el Exception Handler%%%%%%%%%%%%%%%%%%%%%%%%%%");
		System.out.println(response);
		return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
	}

}
