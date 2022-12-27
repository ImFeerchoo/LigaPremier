package com.fernandobetancourt.controllers;

import static com.fernandobetancourt.Data.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernandobetancourt.exceptions.AddingJourneyException;
import com.fernandobetancourt.exceptions.JourneyNotFoundException;
import com.fernandobetancourt.model.entity.Journey;
import com.fernandobetancourt.services.IJourneysService;

@WebMvcTest(JourneysRestController.class)
class JourneysRestControllerTest {
	
	@MockBean
	private IJourneysService journeysService;
	
	@Autowired
	private MockMvc mockMvc;
	
	private ObjectMapper objectMapper;
	
	@BeforeEach
	void setUp() {
		objectMapper = new ObjectMapper();
	}
	
	//GET

	@Test
	void testGetJourneysSuccessed() throws Exception {
		//Given
		when(journeysService.getAllJourneys()).thenReturn(JOURNEYS_WITH_ID);
		Map<String, Object> response = Map.ofEntries(Map.entry("journeys", JOURNEYS_WITH_ID));
		
		//When
		mockMvc.perform(get("/api/journeys"))
			//Then
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(content().json(objectMapper.writeValueAsString(response)));
		
		verify(journeysService).getAllJourneys();
	}
	
	@Test
	void testGetJourneysEmptyList() throws Exception {
		//Given
		when(journeysService.getAllJourneys()).then(invocation -> {
			throw new JourneyNotFoundException("There are not journeys available");
		});
		
		//When
		mockMvc.perform(get("/api/journeys"))
			//Then
			.andExpect(status().isNoContent())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.error").value("There are not journeys available"));
		
		verify(journeysService).getAllJourneys();
	}

	@Test
	void testGetJourneySuccessed() throws Exception {
		//Given
		when(journeysService.getJourney(anyLong())).thenReturn(getJourneyByIdBreakingReference(1L));
		Map<String, Object> response = Map.ofEntries(Map.entry("journey", getJourneyByIdBreakingReference(1L)));
		
		//When
		mockMvc.perform(get("/api/journey/1"))
			//Then
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(content().json(objectMapper.writeValueAsString(response)));
		
		verify(journeysService).getJourney(1L);
	}
	
	@Test
	void testGetJourneyFailed() throws Exception {
		//Given
		when(journeysService.getJourney(anyLong())).then(invocation -> {
			StringBuilder sb = new StringBuilder().append("Journey with id ").append((Long)invocation.getArgument(0)).append(" has not been found");
			throw new JourneyNotFoundException(sb.toString());
		});
		
		//When
		mockMvc.perform(get("/api/journey/1"))
			//Then
			.andExpect(status().isNoContent())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.error").value("Journey with id 1 has not been found"));
		
		verify(journeysService).getJourney(1L);
	}
	
	//POST

	@Test
	void testAddJourneySuccessed() throws Exception {
		//Given
		when(journeysService.addJourney(any(Journey.class))).thenReturn(getJourneyByIdBreakingReference(1L));
		Map<String, Object> response = Map.ofEntries(Map.entry("journey", getJourneyByIdBreakingReference(1L)));
		
		//When
		mockMvc.perform(post("/api/journeys")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(getJourneyWithoutIdByPositionBreakingReference(0))))
			//Then
			.andExpect(status().isCreated())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(content().json(objectMapper.writeValueAsString(response)));
		
		verify(journeysService).addJourney(any(Journey.class));
	}
	
	@Test
	void testAddJourneyFailed() throws Exception {
		//Given
		when(journeysService.addJourney(any(Journey.class))).then(invocation -> {
			throw new AddingJourneyException("Journey is not valid to save");
		});
		
		//When
		mockMvc.perform(post("/api/journeys")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(getJourneyWithoutIdByPositionBreakingReference(0))))
			//Then
			.andExpect(status().isBadRequest())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.error").value("Journey is not valid to save"));
		
		verify(journeysService).addJourney(any(Journey.class));
	}
	
	//PUT

	@Test
	void testUpdateJourneySuccessed() throws Exception {
		//Given
		when(journeysService.updateJourney(any(Journey.class))).then(invocation -> invocation.getArgument(0));
		Map<String, Object> response = Map.ofEntries(Map.entry("journey", getJourneyByIdBreakingReference(1L)));
		
		//When
		mockMvc.perform(put("/api/journeys")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(getJourneyByIdBreakingReference(1L))))
			//Then
			.andExpect(status().isCreated())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(content().json(objectMapper.writeValueAsString(response)));
		
		verify(journeysService).updateJourney(any(Journey.class));
	}
	
	@Test
	void testUpdateJourneyFailed() throws Exception {
		//Given
		when(journeysService.updateJourney(any(Journey.class))).then(invocation -> {
			throw new AddingJourneyException("Journey is not valid to save");
		});
		
		//When
		mockMvc.perform(put("/api/journeys")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(getJourneyByIdBreakingReference(1L))))
			//Then
			.andExpect(status().isBadRequest())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.error").value("Journey is not valid to save"));
		
		verify(journeysService).updateJourney(any(Journey.class));
	}
	
	@Test
	void testUpdateJourneyJourneyDoesNotExists() throws Exception {
		//Given
		when(journeysService.updateJourney(any(Journey.class))).then(invocation -> {
			StringBuilder sb = new StringBuilder().append("Journey with id ").append(((Journey)invocation.getArgument(0)).getJourneyId()).append(" has not been found");
			throw new JourneyNotFoundException(sb.toString());
		});
		
		//When
		mockMvc.perform(put("/api/journeys")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(getJourneyByIdBreakingReference(1L))))
			//Then
			.andExpect(status().isNoContent())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.error").value("Journey with id 1 has not been found"));
		
		verify(journeysService).updateJourney(any(Journey.class));
	}
	
	//DELETE

	@Test
	void testDeleteJourneySuccessed() throws Exception {
		//Given
		when(journeysService.deleteJourney(anyLong())).thenReturn(getJourneyByIdBreakingReference(1L));
		Map<String, Object> response = Map.ofEntries(Map.entry("journey", getJourneyByIdBreakingReference(1L)));
		
		//When
		mockMvc.perform(delete("/api/journeys/1"))
			//Then
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(content().json(objectMapper.writeValueAsString(response)));
		
		verify(journeysService).deleteJourney(1L);
	}
	
	@Test
	void testDeleteJourneyFailed() throws Exception {
		//Given
		when(journeysService.deleteJourney(anyLong())).then(invocation -> {
			StringBuilder sb = new StringBuilder().append("Journey with id ").append((Long)invocation.getArgument(0)).append(" has not been found");
			throw new JourneyNotFoundException(sb.toString());
		});
		
		//When
		mockMvc.perform(delete("/api/journeys/1"))
			//Then
			.andExpect(status().isNoContent())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.error").value("Journey with id 1 has not been found"));
		
		verify(journeysService).deleteJourney(1L);
	}

}
