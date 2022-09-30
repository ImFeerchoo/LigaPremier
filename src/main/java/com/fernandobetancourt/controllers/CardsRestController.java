package com.fernandobetancourt.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.entity.Card;
import com.fernandobetancourt.services.ICardsService;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class CardsRestController {
	
	@Autowired
	private ICardsService cardsService;
	
	@GetMapping("/cards/{cardId}")
	public ResponseEntity<?> getCard(@PathVariable("cardId") Long cardId){
		Map<String, Object> response = new HashMap<>();
		
		try {
			Card cardRecovered = this.cardsService.getCard(cardId);
			response.put("card", cardRecovered);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (InformationNotFoundException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/cards")
	public ResponseEntity<?> addCard(@RequestBody Card card){
		Map<String, Object> response = new HashMap<>();
		
		try {
			Card cardSaved = this.cardsService.addCard(card);
			response.put("card", cardSaved);
			return new ResponseEntity<>(response, HttpStatus.CREATED);
		} catch (InformationNotFoundException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		} catch (WritingInformationException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/cards")
	public ResponseEntity<?> updateCard(@RequestBody Card card){
		Map<String, Object> response = new HashMap<>();
		
		try {
			Card cardUpdated = this.cardsService.updateCard(card);
			response.put("card", cardUpdated);
			return new ResponseEntity<>(response, HttpStatus.CREATED);
		} catch (InformationNotFoundException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		} catch (WritingInformationException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/cards/{cardId}")
	public ResponseEntity<?> deleteCard(@PathVariable("cardId") Long cardId){
		Map<String, Object> response = new HashMap<>();
		
		try {
			Card cardDeleted = this.cardsService.deleteCard(cardId);
			response.put("card", cardDeleted);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (InformationNotFoundException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		
	}

}
