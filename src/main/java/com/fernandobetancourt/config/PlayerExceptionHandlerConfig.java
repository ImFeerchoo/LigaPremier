package com.fernandobetancourt.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fernandobetancourt.exceptions.AddingPlayerException;
import com.fernandobetancourt.exceptions.PlayerNotFoundException;

@ControllerAdvice
public class PlayerExceptionHandlerConfig {

	@ExceptionHandler(AddingPlayerException.class)
	public ResponseEntity<?> addingPlayerException(AddingPlayerException e){
		Map<String, String> response = new HashMap<>();
		response.put("error", e.getMessage());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(PlayerNotFoundException.class)
	public ResponseEntity<?> playerNotFoundException(PlayerNotFoundException e){
		Map<String, String> response = new HashMap<>();
		response.put("error", e.getMessage());
		return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
	}
}
