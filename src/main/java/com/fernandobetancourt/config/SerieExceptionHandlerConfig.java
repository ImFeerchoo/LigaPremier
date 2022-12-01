package com.fernandobetancourt.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fernandobetancourt.exceptions.AddingSerieException;
import com.fernandobetancourt.exceptions.SerieNotFoundException;

@ControllerAdvice
public class SerieExceptionHandlerConfig extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(AddingSerieException.class)
	public ResponseEntity<?> addingSerieException(AddingSerieException e){
		Map<String, Object> response = new HashMap<>();
		response.put("error", e.getMessage());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);		
	}
	
	@ExceptionHandler(SerieNotFoundException.class)
	public ResponseEntity<?> serieNotFoundException(SerieNotFoundException e){
		Map<String, Object> response = new HashMap<>();
		response.put("error", e.getMessage());
		return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);		
	}
}
