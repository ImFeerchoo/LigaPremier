package com.fernandobetancourt.controllers;

import static com.fernandobetancourt.Data.*;
import static org.junit.jupiter.api.Assertions.*;
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
import com.fernandobetancourt.exceptions.AddingGroupException;
import com.fernandobetancourt.exceptions.GroupNotFoundException;
import com.fernandobetancourt.model.entity.Group;
import com.fernandobetancourt.services.IGroupsService;

@WebMvcTest(GroupsRestController.class)
class GroupsRestControllerTest {
	
	@MockBean
	private IGroupsService groupsService;
	
	@Autowired
	private MockMvc mockMvc;
	
	private ObjectMapper objectMapper;
	
	@BeforeEach
	void setUp() {
		objectMapper = new ObjectMapper();
	}
	
	//GET

	@Test
	void testGetGroupsSuccessed() throws Exception {
		//Given
		when(groupsService.getAllGroups()).thenReturn(GROUPS_WITH_ID);
		
		Map<String, Object> response = new HashMap<>();
		response.put("groups", GROUPS_WITH_ID);
		
		//When
		mockMvc.perform(get("/api/groups"))
		//Then
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(content().json(objectMapper.writeValueAsString(response)));
		
		verify(groupsService).getAllGroups();
	}
	
	@Test
	void testGetGroupsEmptyList() throws Exception {
		//Given
		when(groupsService.getAllGroups()).then(invocation -> {
			throw new GroupNotFoundException("There are not groups available");
		});
		
		//When
		mockMvc.perform(get("/api/groups"))
			//Then
			.andExpect(status().isNoContent())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.error").value("There are not groups available"));
		
		verify(groupsService).getAllGroups();
	}

	@Test
	void testGetGroupBySerieSuccessed() throws Exception {
		//Given
		when(groupsService.getGroupsBySerie(anyLong())).thenReturn(getGroupsBySerie(1L));
		Map<String, Object> response = Map.ofEntries(Map.entry("groups", getGroupsBySerie(1L)));
		
		//When
		mockMvc.perform(get("/api/groupsBySerie/1"))
			//Then
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(content().json(objectMapper.writeValueAsString(response)));
		
		verify(groupsService).getGroupsBySerie(1L);
	}
	
	@Test
	void testGetGroupBySerieEmptyList() throws Exception {
		//Given
		when(groupsService.getGroupsBySerie(anyLong())).then(invocation -> {
			var sb = new StringBuilder();
			sb.append("Groups of Serie A has not been found");
			throw new GroupNotFoundException(sb.toString());
		});
		
		//When
		mockMvc.perform(get("/api/groupsBySerie/1"))
			//Then
			.andExpect(status().isNoContent())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.error").value("Groups of Serie A has not been found"));
		
		verify(groupsService).getGroupsBySerie(1L);
	}
	
	@Test
	void getGroupSuccessed() throws Exception {
		//Given
		when(groupsService.getGroup(anyLong()))
								.then(invocation -> getGroupByIdBreakingReference(invocation.getArgument(0)));
		Map<String, Object> response = Map.ofEntries(Map.entry("group", getGroupByIdBreakingReference(1L)));
		
		//When
		mockMvc.perform(get("/api/groups/1"))
			//Then
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(content().json(objectMapper.writeValueAsString(response)));
		
		verify(groupsService).getGroup(1L);
	}
	
	@Test
	void getGroupFailed() throws Exception {
		//Given
		when(groupsService.getGroup(anyLong())).then(invocation -> {
			StringBuilder sb = new StringBuilder().append("Group with id ").append((Long)invocation.getArgument(0)).append(" has not been found");
			throw new GroupNotFoundException(sb.toString()); 
		});
		
		//When
		mockMvc.perform(get("/api/groups/1"))
			//Then
			.andExpect(status().isNoContent())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.error").value("Group with id 1 has not been found"));
		
		verify(groupsService).getGroup(1L);
	}
	
	//POST

	@Test
	void testAddGroupSuccessed() throws Exception {
		//Given
		when(groupsService.addGroup(any(Group.class))).thenReturn(getGroupByIdBreakingReference(1L));
		Map<String, Object> response = Map.ofEntries(Map.entry("group", getGroupByIdBreakingReference(1L)));
		
		//When
		mockMvc.perform(post("/api/groups")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(getGroupWithoutIdByPositionBreakingReference(0))))
			//Then
			.andExpect(status().isCreated())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(content().json(objectMapper.writeValueAsString(response)));
		
		verify(groupsService).addGroup(any(Group.class));
	}
	
	@Test
	void testAddGroupFailed() throws Exception {
		//Given
		when(groupsService.addGroup(any(Group.class))).then(invocation -> {
			throw new AddingGroupException("Group is not valid to save");
		});
		
		//When
		mockMvc.perform(post("/api/groups")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(getGroupWithoutIdByPositionBreakingReference(0))))
			//Then
			.andExpect(status().isBadRequest())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.error").value("Group is not valid to save"));
		
		verify(groupsService).addGroup(any(Group.class));
	}
	
	//PUT

	@Test
	void testUpdateGroupSuccessed() throws Exception {
		//Given
		when(groupsService.updateGroup(any(Group.class))).then(invocation -> invocation.getArgument(0));
		Map<String, Object> response = Map.ofEntries(Map.entry("group", getGroupByIdBreakingReference(1L)));
		
		//When
		mockMvc.perform(put("/api/groups")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(getGroupByIdBreakingReference(1L))))
			//Then
			.andExpect(status().isCreated())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(content().json(objectMapper.writeValueAsString(response)));
	
		verify(groupsService).updateGroup(any(Group.class));
	}
	
	@Test
	void testUpdateGroupFailed() throws Exception {
		//Given
		when(groupsService.updateGroup(any(Group.class))).then(invocation -> {
			throw new AddingGroupException("Group is not valid to save");
		});
		
		
		//When
		mockMvc.perform(put("/api/groups")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(getGroupByIdBreakingReference(1L))))
			//Then
			.andExpect(status().isBadRequest())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.error").value("Group is not valid to save"));
		
		verify(groupsService).updateGroup(any(Group.class));
	}
	
	@Test
	void testUpdateGroupGroupsDoesNotExists() throws Exception {
		//Given
		when(groupsService.updateGroup(any(Group.class))).then(invocation -> {
			Group group = invocation.getArgument(0);
			StringBuilder sb = new StringBuilder().append("Group with id ").append(group.getGroupId()).append(" has not been found");
			throw new GroupNotFoundException(sb.toString());
		});
		
		
		//When
		mockMvc.perform(put("/api/groups")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(getGroupByIdBreakingReference(1L))))
			//Then
			.andExpect(status().isNoContent())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.error").value("Group with id 1 has not been found"));
		
		verify(groupsService).updateGroup(any(Group.class));
	}
	
	//DELETE

	@Test
	void testDeleteGroupSuccessed() throws Exception {
		//Given
		when(groupsService.deleteGroupd(anyLong()))
						.then(invocation -> getGroupByIdBreakingReference(invocation.getArgument(0)));
		Map<String, Object> response = Map.ofEntries(Map.entry("group", getGroupByIdBreakingReference(1L)));
		
		//When
		mockMvc.perform(delete("/api/groups/1"))
			//Then
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(content().json(objectMapper.writeValueAsString(response)));
		
		verify(groupsService).deleteGroupd(1L);
	}
	
	@Test
	void testDeleteGroupFailed() throws Exception {
		//Given
		when(groupsService.deleteGroupd(anyLong())).then(invocation -> {
			StringBuilder sb = new StringBuilder().append("Group with id ").append((Long)invocation.getArgument(0)).append(" has not been found");
			throw new GroupNotFoundException(sb.toString());
		});
		
		//When
		mockMvc.perform(delete("/api/groups/1"))
			//Then
			.andExpect(status().isNoContent())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.error").value("Group with id 1 has not been found"));
		
		verify(groupsService).deleteGroupd(1L);
	}

}
