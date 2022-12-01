package com.fernandobetancourt.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fernandobetancourt.exceptions.AddingLineupPlayerException;
import com.fernandobetancourt.exceptions.LineupPlayerNotFoundException;

@ControllerAdvice
public class LineupPlayerExceptionHandlerConfig {
	
	@ExceptionHandler(AddingLineupPlayerException.class)
	public ResponseEntity<?> addingLineupPlayerException(AddingLineupPlayerException e){
		Map<String, String> response = new HashMap<>();
		response.put("error", e.getMessage());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(LineupPlayerNotFoundException.class)
	public ResponseEntity<?> lineupPlayerNotFoundException(LineupPlayerNotFoundException e){
		Map<String, String> response = new HashMap<>();
		response.put("error", e.getMessage());
		return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
	}
}
