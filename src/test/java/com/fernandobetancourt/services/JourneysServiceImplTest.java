package com.fernandobetancourt.services;

import static com.fernandobetancourt.Data.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.fernandobetancourt.exceptions.AddingJourneyException;
import com.fernandobetancourt.exceptions.GroupNotFoundException;
import com.fernandobetancourt.exceptions.JourneyNotFoundException;
import com.fernandobetancourt.model.dao.IJourneysDao;
import com.fernandobetancourt.model.entity.Journey;

@SpringBootTest
class JourneysServiceImplTest {

	@MockBean
	private IJourneysDao journeysDao;

	@MockBean
	private IGroupsService groupsService;

	@Autowired
	private IJourneysService journeysService;

	// GET

	@Test
	void testGetAllJourneysSuccessed() {
		// Given
		when(journeysDao.findAll()).thenReturn(JOURNEYS_WITH_ID);

		// When
		List<Journey> journeys = journeysService.getAllJourneys();

		// Then
		assertFalse(journeys.isEmpty());
		assertEquals(15, journeys.size());
		assertTrue(journeys.contains(getJourneyByIdBreakingReference(1L)));
		assertTrue(journeys.contains(getJourneyByIdBreakingReference(15L)));

		verify(journeysDao).findAll();
	}

//	@Test
//	void testGetAllJourneysEmptyList() {
//		// Given
//		when(journeysDao.findAll()).thenReturn(Collections.emptyList());
//
//		// When
//		List<Journey> journeys = journeysService.getAllJourneys();
//
//		// Then
//		assertTrue(journeys.isEmpty());
//		verify(journeysDao).findAll();
//	}
	
	@Test
	void testGetAllJourneysEmptyList() {
		// Given
		when(journeysDao.findAll()).thenReturn(Collections.emptyList());
		
		// Then
		assertThrows(JourneyNotFoundException.class, () -> {
			// When
			journeysService.getAllJourneys();
		});
		
		verify(journeysDao).findAll();
	}

	@Test
	void testGetJourneySuccessed() {
		// Given
		when(journeysDao.findById(anyLong())).thenReturn(Optional.of(getJourneyByIdBreakingReference(1L)));

		// When
		Journey journey = journeysService.getJourney(1L);

		// Then
		assertEquals(getJourneyByIdBreakingReference(1L), journey);
		verify(journeysDao).findById(1L);
	}

	@Test
	void testGetJourneyFailed() {
		// Given
		when(journeysDao.findById(anyLong())).thenThrow(JourneyNotFoundException.class);

		// Then
		assertThrows(JourneyNotFoundException.class, () -> {
			// When
			journeysService.getJourney(1L);
		});

		verify(journeysDao).findById(1L);
	}

	// POST

	@Test
	void testAddJourneySuccessed() {
		// Given
		when(journeysDao.save(any(Journey.class))).then(invocation -> {
			Journey journey = invocation.getArgument(0);
			journey.setJourneyId(1L);
			return journey;
		});

		// When
		Journey journey = journeysService.addJourney(getJourneyWithoutIdByPositionBreakingReference(0));

		// Then
		assertEquals(1L, journey.getJourneyId());
		assertEquals(1, journey.getNumber());
		assertEquals(GROUPS_WITH_ID.get(0), journey.getGroup());

		verify(journeysDao).save(any(Journey.class));
	}

	@Test
	void testAddJourneyFailedJorneyNull() {
		// Then
		assertThrowsExactly(AddingJourneyException.class, () -> {
			// When
			journeysService.addJourney(null);
		});
	}

	@Test
	void testAddJourneyFailedNumberNull() {
		Journey journeyNumberNull = getJourneyWithoutIdByPositionBreakingReference(0);
		journeyNumberNull.setNumber(null);

		// Then
		assertThrowsExactly(AddingJourneyException.class, () -> {
			// When
			journeysService.addJourney(journeyNumberNull);
		});
	}

	@Test
	void testAddJourneyFailedNumberLessThan1() {
		Journey journeyNumberLessThan1 = getJourneyWithoutIdByPositionBreakingReference(0);
		journeyNumberLessThan1.setNumber(0);

		// Then
		assertThrowsExactly(AddingJourneyException.class, () -> {
			// When
			journeysService.addJourney(journeyNumberLessThan1);
		});
	}

	@Test
	void testAddJourneyFailedGroupNull() {
		Journey journeyGroupNull = getJourneyWithoutIdByPositionBreakingReference(0);
		journeyGroupNull.setGroup(null);

		// Then
		assertThrowsExactly(AddingJourneyException.class, () -> {
			// When
			journeysService.addJourney(journeyGroupNull);
		});
	}

	@Test
	void testAddJourneyFailedGroupIdNull() {
		Journey journeyGroupIdNull = getJourneyWithoutIdByPositionBreakingReference(0);
		journeyGroupIdNull.setGroup(getGroupWithoutIdByPositionBreakingReference(0));

		// Then
		assertThrowsExactly(AddingJourneyException.class, () -> {
			// When
			journeysService.addJourney(journeyGroupIdNull);
		});
	}

	@Test
	void testAddJourneyFailedGroupIdLessThan1() {
		Journey journeyGroupIdLessThan1 = getJourneyWithoutIdByPositionBreakingReference(0);
		journeyGroupIdLessThan1.setGroup(GROUP_WITH_ID_LESS_THAN_1);

		// Then
		assertThrowsExactly(AddingJourneyException.class, () -> {
			// When
			journeysService.addJourney(journeyGroupIdLessThan1);
		});
	}

	@Test
	void testAddJourneyFailedGroupDoesNotExists() {
		// Given
		when(groupsService.getGroup(anyLong())).thenThrow(GroupNotFoundException.class);

		// Then
		assertThrowsExactly(GroupNotFoundException.class, () -> {
			// When
			journeysService.addJourney(getJourneyWithoutIdByPositionBreakingReference(0));
		});

		verify(groupsService).getGroup(anyLong());
	}

	// PUT

	@Test
	void testUpdateJourneySuccessed() {
		// Given
		when(journeysDao.findById(anyLong())).thenReturn(Optional.of(getJourneyByIdBreakingReference(1L)));
		when(journeysDao.save(any(Journey.class))).then(invocation -> {
			return invocation.getArgument(0);
		});
		Journey journey = getJourneyByIdBreakingReference(1L);
		journey.setNumber(10);
		journey.setGroup(getGroupByIdBreakingReference(5L));

		// When
		journey = journeysService.updateJourney(journey);

		// Then
		assertEquals(1, journey.getJourneyId());
		assertEquals(10, journey.getNumber());
		assertEquals(getGroupByIdBreakingReference(5L), journey.getGroup());

		verify(groupsService).getGroup(5L);
		verify(journeysDao).findById(1L);
		verify(journeysDao).save(any(Journey.class));
	}

	@Test
	void testUpdateJourneyFailedJourneyNull() {
		// Then
		assertThrows(AddingJourneyException.class, () -> {
			// When
			journeysService.updateJourney(null);
		});
	}

	@Test
	void testUpdateJourneyFailedJourneyIdNull() {
		// Then
		assertThrows(AddingJourneyException.class, () -> {
			// When
			journeysService.updateJourney(getJourneyWithoutIdByPositionBreakingReference(0));
		});
	}

	@Test
	void testUpdateJourneyFailedJourneyIdLessThan1() {
		// Then
		assertThrows(AddingJourneyException.class, () -> {
			// When
			journeysService.updateJourney(JOURNEY_WITH_ID_LESS_THAN_1);
		});
	}

	@Test
	void testUpdateJourneyFailedNumberNull() {
		// Given
		Journey journeyNumberNull = getJourneyByIdBreakingReference(1L);
		journeyNumberNull.setNumber(null);

		// Then
		assertThrows(AddingJourneyException.class, () -> {
			journeysService.updateJourney(journeyNumberNull);
		});
	}

	@Test
	void testUpdateJourneyFailedNumberLessThan1() {
		// Given
		Journey journeyNumberLessThan1 = getJourneyByIdBreakingReference(1L);
		journeyNumberLessThan1.setNumber(0);

		// Then
		assertThrows(AddingJourneyException.class, () -> {
			// When
			journeysService.updateJourney(journeyNumberLessThan1);
		});
	}

	@Test
	void testUpdateJourneyFailedGroupNull() {
		// Given
		Journey journeyGroupNull = getJourneyByIdBreakingReference(1L);
		journeyGroupNull.setGroup(null);

		// Then
		assertThrows(AddingJourneyException.class, () -> {
			// When
			journeysService.updateJourney(journeyGroupNull);
		});
	}

	@Test
	void testUpdateJourneyFailedGroupIdNull() {
		// Given
		Journey journeyGroupIdNull = getJourneyByIdBreakingReference(1L);
		journeyGroupIdNull.setGroup(getGroupWithoutIdByPositionBreakingReference(0));

		// Then
		assertThrows(AddingJourneyException.class, () -> {
			// When
			journeysService.updateJourney(journeyGroupIdNull);
		});
	}

	@Test
	void testUpdateJourneyFailedGroupIdLessThan1() {
		// Given
		Journey journeyGroupIdLessThan1 = getJourneyByIdBreakingReference(1L);
		journeyGroupIdLessThan1.setGroup(GROUP_WITH_ID_LESS_THAN_1);

		// Then
		assertThrows(AddingJourneyException.class, () -> {
			// When
			journeysService.updateJourney(journeyGroupIdLessThan1);
		});
	}

	@Test
	void testUpdateJourneyFailedGroupDoesNotExists() {
		// Given
		when(journeysDao.findById(anyLong())).thenReturn(Optional.of(getJourneyByIdBreakingReference(1L)));
		when(groupsService.getGroup(anyLong())).thenThrow(GroupNotFoundException.class);

		// Then
		assertThrows(GroupNotFoundException.class, () -> {
			// When
			journeysService.updateJourney(getJourneyByIdBreakingReference(1L));
		});
		
		verify(groupsService).getGroup(anyLong());
	}

	@Test
	void testUpdateJourneyFailedJourneyDoesNotExists() {
		// Given
		when(journeysDao.findById(anyLong())).thenReturn(Optional.ofNullable(null));

		// Then
		assertThrows(JourneyNotFoundException.class, () -> {
			// When
			journeysService.updateJourney(getJourneyByIdBreakingReference(1L));
		});
		
		verify(journeysDao).findById(anyLong());
		
	}

	// DELETE

	@Test
	void testDeleteJourneySuccessed() {
		//Given
		when(journeysDao.findById(anyLong())).thenReturn(Optional.of(getJourneyByIdBreakingReference(1L)));
		
		//When
		Journey journey = journeysService.deleteJourney(1L);
		
		//Then
		assertEquals(getJourneyByIdBreakingReference(1L), journey);
		
		verify(journeysDao).findById(1L);
		verify(journeysDao).deleteById(1L);
	}
	
	@Test
	void testDeleteJourneyFailed() {
		//Given
		when(journeysDao.findById(anyLong())).thenReturn(Optional.ofNullable(null));
		
		//Then
		assertThrows(JourneyNotFoundException.class, () -> {
			//When
			journeysService.deleteJourney(1L);
		});
		
		verify(journeysDao).findById(1L);
	}

}
