package com.fernandobetancourt.controllers;

import static com.fernandobetancourt.Data.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernandobetancourt.exceptions.AddingSerieException;
import com.fernandobetancourt.exceptions.SerieNotFoundException;
import com.fernandobetancourt.model.entity.Serie;
import com.fernandobetancourt.services.ISeriesService;

@WebMvcTest(SeriesRestController.class)
class SeriesRestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ISeriesService seriesService;

	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {
		objectMapper = new ObjectMapper();
	}

	// GET

	@Test
	void testGetSeriesSuccessed() throws Exception {
		// Given
		when(seriesService.getAllSeries()).thenReturn(SERIES_WITH_ID);
		Map<String, Object> response = new HashMap<>();
		response.put("series", SERIES_WITH_ID);

		// When
		mockMvc.perform(get("/api/series"))
				// Then
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json(objectMapper.writeValueAsString(response)));

		verify(seriesService).getAllSeries();
	}

//	@Test
//	void testGetSeriesEmptyList() throws Exception {
//		// Given
//		when(seriesService.getAllSeries()).thenReturn(Collections.emptyList());
//		Map<String, Object> response = new HashMap<>();
//		response.put("series", Collections.emptyList());
//
//		// When
//		mockMvc.perform(get("/api/series"))
//				// Then
//				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
//				.andExpect(content().json(objectMapper.writeValueAsString(response)));
//
//		verify(seriesService).getAllSeries();
//	}
	
	@Test
	void testGetSeriesEmptyList() throws Exception {
		// Given
		when(seriesService.getAllSeries()).then(invocation -> {
			throw new SerieNotFoundException("There are not series available");
		});
		
		// When
		mockMvc.perform(get("/api/series"))
		// Then
		.andExpect(status().isNoContent())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.error").value("There are not series available"));
		
		verify(seriesService).getAllSeries();
	}

	@Test
	void testGetSerieSuccessed() throws Exception {
		// Given
		when(seriesService.getSerieById(anyLong())).thenReturn(getSerieByIdBreakingReference(1L));

		// When
		mockMvc.perform(get("/api/serie/1"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.serie").value(getSerieByIdBreakingReference(1L)));
		
		verify(seriesService).getSerieById(1L);
	}

	@Test
	void testGetSerieFailed() throws Exception {
		// Given
		when(seriesService.getSerieById(anyLong())).then(invocation -> {
			var sb = new StringBuilder();
			sb.append("Serie with id ").append((Long)invocation.getArgument(0)).append(" has not been found.");
			throw new SerieNotFoundException(sb.toString());
		});

		// When
		mockMvc.perform(get("/api/serie/1"))
				.andExpect(status().isNoContent())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.error").value("Serie with id 1 has not been found."));
		
		verify(seriesService).getSerieById(1L);
	}

	// POST

	@Test
	void testAddSerieSuccessed() throws Exception {
		//Given
		when(seriesService.addSerie(any(Serie.class))).then(invocation -> {
			Serie serie = invocation.getArgument(0);
			serie.setSerieId(1L);
			return serie;
		});
		
		//When
		mockMvc.perform(post("/api/serie")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(getSerieWithoutIdBreakingReference(0)))
				)
			//Then
			.andExpect(status().isCreated())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.serie.serieId").value(1L))
			.andExpect(jsonPath("$.serie.name").value("Serie A"));
		
		verify(seriesService).addSerie(any(Serie.class));
	}
	
	@Test
	void testAddSerieFailed() throws Exception {
		//Given
		when(seriesService.addSerie(any(Serie.class))).then(invocation -> {
			throw new AddingSerieException("Serie is not valid to save");
		});
		
		Serie serieNotValid = new Serie();
		
		//When
		mockMvc.perform(post("/api/serie")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(serieNotValid))
				)
			//Then
			.andExpect(status().isBadRequest())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.error").value("Serie is not valid to save"));
		
		verify(seriesService).addSerie(any(Serie.class));
	}

	// PUT

	@Test
	void testUpdateSerieSuccessed() throws Exception {
		//Given
		when(seriesService.updateSerie(any(Serie.class))).then(invocation -> {
			return invocation.getArgument(0);
		});
		
		Serie serie = getSerieByIdBreakingReference(1L);
		serie.setName("Serie Z");
		
		//When
		mockMvc.perform(put("/api/serie")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(serie)))
		//Then
		.andExpect(status().isCreated())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.serie.serieId").value(1))
		.andExpect(jsonPath("$.serie.name").value("Serie Z"));
		
		verify(seriesService).updateSerie(any(Serie.class));
	}
	
	@Test
	void testUpdateSerieFailedAdding() throws Exception {
		//Given
		when(seriesService.updateSerie(any(Serie.class))).then(invocation -> {
			throw new AddingSerieException("Serie is not valid to save");
		});
		
		Serie serieNotValid = new Serie();
		
		//When
		mockMvc.perform(put("/api/serie")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(serieNotValid)))
		//Then
		.andExpect(status().isBadRequest())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.error").value("Serie is not valid to save"));
		
		verify(seriesService).updateSerie(any(Serie.class));
	}
	
	@Test
	void testUpdateSerieFailedNotFound() throws Exception {
		//Given
		when(seriesService.updateSerie(any(Serie.class))).then(invocation -> {
			Serie serie = invocation.getArgument(0);
			var sb = new StringBuilder();
			sb.append("Serie with id ").append(serie.getSerieId()).append(" has not been found.");
			throw new SerieNotFoundException(sb.toString());
		});
		
		Serie serieWichDoesNotExists = getSerieByIdBreakingReference(1L);
		
		//When
		mockMvc.perform(put("/api/serie")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(serieWichDoesNotExists)))
		//Then
		.andExpect(status().isNoContent())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.error").value("Serie with id 1 has not been found."));
		
		verify(seriesService).updateSerie(any(Serie.class));
	}

	// DELETE

	@Test
	void deleteSerieSuccessed() throws Exception {
		//Given
		when(seriesService.deleteSerie(anyLong())).thenReturn(getSerieByIdBreakingReference(1L));
		
		//When
		mockMvc.perform(delete("/api/serie/1").
				contentType(MediaType.APPLICATION_JSON))
		//Then
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.serie.serieId").value(1))
		.andExpect(jsonPath("$.serie.name").value("Serie A"));
		
		verify(seriesService).deleteSerie(1L);
	}
	
	@Test
	void deleteSerieFailed() throws Exception {
		//Given
		when(seriesService.deleteSerie(anyLong())).then(invocation -> {
			var sb = new StringBuilder();
			sb.append("Serie with id ").append((Long)invocation.getArgument(0)).append(" has not been found.");
			throw new SerieNotFoundException(sb.toString());
		});
		
		//When
		mockMvc.perform(delete("/api/serie/1").
				contentType(MediaType.APPLICATION_JSON))
		//Then
		.andExpect(status().isNoContent())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.error").value("Serie with id 1 has not been found."));
		
		verify(seriesService).deleteSerie(1L);
	}

}
