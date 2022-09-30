package com.fernandobetancourt.controllers;

import java.util.HashMap;
import java.util.List;
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
import com.fernandobetancourt.model.entity.Player;
import com.fernandobetancourt.services.IClubesService;
import com.fernandobetancourt.services.IPlayersService;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class PlayersRestController {

	@Autowired
	private IPlayersService playersService;
	
	@Autowired
	private IClubesService clubesService;

	@GetMapping("/players")
	public ResponseEntity<?> getPlayers() {

		Map<String, Object> response = new HashMap<>();
		List<Player> players = this.playersService.getAllPlayers();

		response.put("players", players);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/player/{id}")
	public ResponseEntity<?> getPlayerById(@PathVariable("id") Long id) {

		Map<String, Object> response = new HashMap<>();
		Player playerRecovered = null;

		try {

			playerRecovered = this.playersService.getPlayerById(id);
			response.put("player", playerRecovered);
			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (InformationNotFoundException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

	}

	@GetMapping("/playersClub/{clubId}")
	public ResponseEntity<?> getPlayersByClub(@PathVariable("clubId") Long clubId) {

		Map<String, Object> response = new HashMap<>();

		try {
			List<Player> players = this.playersService.getPlayersByClub(this.clubesService.getClubById(clubId));
			response.put("players", players);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (InformationNotFoundException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

	}

	@PostMapping("/player")
	public ResponseEntity<?> addPlayer(@RequestBody Player player) {

		Map<String, Object> response = new HashMap<>();
		Player playerSaved = null;

		try {

			playerSaved = this.playersService.addPlayer(player);
			response.put("player", playerSaved);
			return new ResponseEntity<>(response, HttpStatus.CREATED);

		} catch (InformationNotFoundException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		} catch (WritingInformationException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PutMapping("/player")
	public ResponseEntity<?> updatePlayer(@RequestBody Player player) {

		Map<String, Object> response = new HashMap<>();
		Player playerUpdated = null;

		try {

			playerUpdated = this.playersService.updatePlayer(player);
			response.put("player", playerUpdated);
			return new ResponseEntity<>(response, HttpStatus.CREATED);

		} catch (InformationNotFoundException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		} catch (WritingInformationException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@DeleteMapping("/player/{id}")
	public ResponseEntity<?> deletePlayer(@PathVariable("id") Long id) {

		Map<String, Object> response = new HashMap<>();
		Player playerDeleted = null;

		try {

			playerDeleted = this.playersService.deletePlayer(id);
			response.put("player", playerDeleted);
			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (InformationNotFoundException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

	}

}
