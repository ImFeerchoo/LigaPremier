package com.fernandobetancourt.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fernandobetancourt.exceptions.ScoreboardNotFoundException;

@ControllerAdvice
public class ScoreboardEsceptionHandlerConfig {

	@ExceptionHandler(ScoreboardNotFoundException.class)
	public ResponseEntity<?> scoreboardNotFoundException(ScoreboardNotFoundException e){
		Map<String, String> response = new HashMap<>();
		response.put("error", e.getMessage());
		return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
	}
	
}
