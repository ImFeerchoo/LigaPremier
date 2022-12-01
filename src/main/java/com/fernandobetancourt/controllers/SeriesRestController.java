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

import com.fernandobetancourt.model.entity.Serie;
import com.fernandobetancourt.services.ISeriesService;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class SeriesRestController {
	
	@Autowired
	private ISeriesService seriesService;
	
	@GetMapping("/series")
	public ResponseEntity<?> getSeries(){
		Map<String, Object> response = new HashMap<>();
		List<Serie> series= this.seriesService.getAllSeries();
		response.put("series", series);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/serie/{serieId}")
	public ResponseEntity<?> getSerie(@PathVariable("serieId") Long serieId){
		Map<String, Object> response = new HashMap<>();
		Serie serieRecovered = this.seriesService.getSerieById(serieId);
		response.put("serie", serieRecovered);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping("/serie")
	public ResponseEntity<?> addSerie(@RequestBody Serie serie){
		Map<String, Object> response = new HashMap<>();
		Serie serieAdded = this.seriesService.addSerie(serie);
		response.put("serie", serieAdded);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/serie")
	public ResponseEntity<?> updateSerie(@RequestBody Serie serie){
		Map<String, Object> response = new HashMap<>();
		Serie serieUpdated = this.seriesService.updateSerie(serie);
		response.put("serie", serieUpdated);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/serie/{serieId}")
	public ResponseEntity<?> deleteSerie(@PathVariable("serieId") Long id){
		Map<String, Object> response = new HashMap<>();
		Serie serieDeleted = this.seriesService.deleteSerie(id);
		response.put("serie", serieDeleted);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
}
