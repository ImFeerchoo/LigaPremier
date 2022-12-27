package com.fernandobetancourt.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fernandobetancourt.exceptions.AddingCardException;
import com.fernandobetancourt.exceptions.CardNotFoundException;

@ControllerAdvice
public class CardExceptionHandlerConfig {

	@ExceptionHandler(AddingCardException.class)
	public ResponseEntity<?> addingCardException(AddingCardException e) {
		Map<String, String> response = new HashMap<>();
		response.put("error", e.getMessage());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(CardNotFoundException.class)
	public ResponseEntity<?> cardNotFoundException(CardNotFoundException e) {
		Map<String, String> response = new HashMap<>();
		response.put("error", e.getMessage());
		return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
	}
}
