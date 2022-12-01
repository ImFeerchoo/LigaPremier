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

import com.fernandobetancourt.exceptions.AddingGroupException;
import com.fernandobetancourt.exceptions.GroupNotFoundException;
import com.fernandobetancourt.exceptions.SerieNotFoundException;
import com.fernandobetancourt.model.dao.IGroupsDao;
import com.fernandobetancourt.model.entity.Group;
import com.fernandobetancourt.model.entity.Serie;

@SpringBootTest
class GroupsServiceImplTest {

	@MockBean
	private IGroupsDao groupsDao;

	@MockBean
	private ISeriesService seriesService;

	@Autowired
	private IGroupsService groupsService;

	// GET

	@Test
	void testGetAllGroupsSuccessed() {
		// Given
		when(groupsDao.findAll()).thenReturn(GROUPS_WITH_ID);

		// When
		List<Group> groups = groupsService.getAllGroups();

		// Then
		assertFalse(groups.isEmpty());
		assertEquals(5, groups.size());
		assertTrue(groups.contains(GROUPS_WITH_ID.get(0)));

		verify(groupsDao).findAll();
	}

//	@Test
//	void testGetAllGroupsEmpyList() {
//		// Given
//		when(groupsDao.findAll()).thenReturn(Collections.emptyList());
//
//		// When
//		List<Group> groups = groupsService.getAllGroups();
//
//		// Then
//		assertTrue(groups.isEmpty());
//
//		verify(groupsDao).findAll();
//	}
	
	@Test
	void testGetAllGroupsEmpyList() {
		// Given
		when(groupsDao.findAll()).thenReturn(Collections.emptyList());
		
		// Then
		assertThrows(GroupNotFoundException.class, () -> {
			// When
			groupsService.getAllGroups();
		});
		
		verify(groupsDao).findAll();
	}

	@Test
	void testGetGroupsBySerieSuccessed() {
		// Given
		when(seriesService.getSerieById(anyLong())).thenReturn(getSerieByIdBreakingReference(1L));
//		when(groupsDao.findBySerie(any(Serie.class))).thenReturn(getGroupsBySerie(getSerieByIdBreakingReference(1L)));
		when(groupsDao.findBySerie(any(Serie.class))).thenReturn(getGroupsBySerie(1L));

		// When
		List<Group> groups = groupsService.getGroupsBySerie(1L);

		// Then
		assertFalse(groups.isEmpty());
		assertEquals(2, groups.size());
		assertTrue(groups.contains(getGroupByIdBreakingReference(1L)));
		assertTrue(groups.contains(getGroupByIdBreakingReference(2L)));

		verify(seriesService).getSerieById(1L);
		verify(groupsDao).findBySerie(getSerieByIdBreakingReference(1L));
	}
	
//	@Test
//	void testGetGroupsBySerieEmptyList() {
//		// Given
//		when(seriesService.getSerieById(anyLong())).thenReturn(getSerieByIdBreakingReference(1L));
//		when(groupsDao.findBySerie(any(Serie.class))).thenReturn(Collections.emptyList());
//		
//		// When
//		List<Group> groups = groupsService.getGroupsBySerie(1L);
//		
//		// Then
//		assertTrue(groups.isEmpty());
//		
//		verify(seriesService).getSerieById(1L);
//		verify(groupsDao).findBySerie(getSerieByIdBreakingReference(1L));
//	}
	
	@Test
	void testGetGroupsBySerieEmptyList() {
		// Given
		when(seriesService.getSerieById(anyLong())).thenReturn(getSerieByIdBreakingReference(1L));
		when(groupsDao.findBySerie(any(Serie.class))).thenReturn(Collections.emptyList());
		
		// Then
		assertThrows(GroupNotFoundException.class, () -> {
			// When
			groupsService.getGroupsBySerie(1L);
		});
		
		verify(seriesService).getSerieById(1L);
		verify(groupsDao).findBySerie(getSerieByIdBreakingReference(1L));
	}

	@Test
	void testGetGroupsBySerieFailedGroupDoesNotExists() {
		// Given
		when(seriesService.getSerieById(anyLong())).thenThrow(SerieNotFoundException.class);

		// Then
		assertThrows(SerieNotFoundException.class, () -> {
			// When
			groupsService.getGroupsBySerie(1L);
		});

		verify(seriesService).getSerieById(1L);
	}

	@Test
	void testGetGroupSuccessed() {
		// Given
		when(groupsDao.findById(anyLong())).thenReturn(Optional.of(getGroupByIdBreakingReference(1L)));

		// When
		Group group = groupsService.getGroup(1L);

		// Then
		assertEquals(group, getGroupByIdBreakingReference(1L));

		verify(groupsDao).findById(1L);
	}

	@Test
	void testGetGroupFailed() {
		// Given
		when(groupsDao.findById(anyLong())).thenReturn(Optional.ofNullable(null));

		// Then
		assertThrows(GroupNotFoundException.class, () -> {
			// When
			groupsService.getGroup(1L);
		});

		verify(groupsDao).findById(1L);
	}

//	// POST

	@Test
	void testAddGroupSuccessed() {
		// Given
		when(groupsDao.save(any(Group.class))).then(invocation -> {
			Group group = invocation.getArgument(0);
			group.setGroupId(1L);
			return group;
		});

		// When
		Group group = groupsService.addGroup(getGroupWithoutIdByPositionBreakingReference(0));

		// Then
		assertEquals(1L, group.getGroupId());
		assertEquals("Grupo 1", group.getName());
		assertEquals(getSerieByIdBreakingReference(1L), group.getSerie());

		verify(seriesService).getSerieById(1L);
		verify(groupsDao).save(any(Group.class));
	}

	@Test
	void testAddGroupFailedGroupNull() {
		// Then
		assertThrows(AddingGroupException.class, () -> {
			// When
			groupsService.addGroup(null);
		});
	}

	@Test
	void testAddGroupFailedNameNull() {
		Group groupNameNull = getGroupWithoutIdByPositionBreakingReference(0);
		groupNameNull.setName(null);

		// Then
		assertThrows(AddingGroupException.class, () -> {
			// When
			groupsService.addGroup(groupNameNull);
		});
	}

	@Test
	void testAddGroupFailedNameEmptyString() {
		Group groupNameEmptyString = getGroupWithoutIdByPositionBreakingReference(0);
		groupNameEmptyString.setName("");

		// Then
		assertThrows(AddingGroupException.class, () -> {
			// When
			groupsService.addGroup(groupNameEmptyString);
		});
	}

	@Test
	void testAddGroupFailedSerieNull() {
		Group groupSerieNull = getGroupWithoutIdByPositionBreakingReference(0);
		groupSerieNull.setSerie(null);

		// Then
		assertThrows(AddingGroupException.class, () -> {
			// When
			groupsService.addGroup(groupSerieNull);
		});
	}

	@Test
	void testAddGroupFailedSerieIdNull() {
		Group groupSerieIdNull = getGroupWithoutIdByPositionBreakingReference(0);
		groupSerieIdNull.setSerie(SERIES_WITHOUT_ID.get(0));

		// Then
		assertThrows(AddingGroupException.class, () -> {
			// When
			groupsService.addGroup(groupSerieIdNull);
		});
	}

	@Test
	void testAddGroupFailedSerieIdLessThan1() {
		Group groupSerieIdNull = getGroupWithoutIdByPositionBreakingReference(0);
		groupSerieIdNull.setSerie(SERIE_WITH_ID_LESS_THAN_1);

		// Then
		assertThrows(AddingGroupException.class, () -> {
			// When
			groupsService.addGroup(groupSerieIdNull);
		});
	}

	// PUT

	@Test
	void testUpdateGroupSuccessed() {
		// Given
		when(groupsDao.findById(anyLong())).thenReturn(Optional.of(getGroupByIdBreakingReference(1L)));

		when(groupsDao.save(any(Group.class))).then(invocation -> {
			return invocation.getArgument(0);
		});

		Group group = getGroupByIdBreakingReference(1L);
		group.setName("Group of test");
		group.setSerie(getSerieByIdBreakingReference(5L));

		// When
		group = groupsService.updateGroup(group);

		// Then
		assertEquals(1L, group.getGroupId());
		assertEquals("Group of test", group.getName());
		assertEquals(getSerieByIdBreakingReference(5L), group.getSerie());

		verify(seriesService).getSerieById(5L);
		verify(groupsDao).findById(1L);
		verify(groupsDao).save(any(Group.class));
	}

	@Test
	void testUpdateGroupFailedGroupNull() {
		// Then
		assertThrows(AddingGroupException.class, () -> {
			// When
			groupsService.updateGroup(null);
		});
	}

	@Test
	void testUpdateGroupFailedGroupIdNull() {
		// Then
		assertThrows(AddingGroupException.class, () -> {
			// When
			groupsService.updateGroup(getGroupWithoutIdByPositionBreakingReference(0));
		});
	}

	@Test
	void testUpdateGroupFailedGroupIdLessThan1() {
		// Then
		assertThrows(AddingGroupException.class, () -> {
			// When
			groupsService.updateGroup(GROUP_WITH_ID_LESS_THAN_1);
		});
	}

	@Test
	void testUpdateGroupFailedNameNull() {
		// Given
		Group groupNameNull = getGroupByIdBreakingReference(1L);
		groupNameNull.setName(null);

		// Then
		assertThrows(AddingGroupException.class, () -> {
			// When
			groupsService.updateGroup(groupNameNull);
		});
	}

	@Test
	void testUpdateGroupFailedNameEmptyString() {
		// Given
		Group groupNameEmptyString = getGroupByIdBreakingReference(1L);
		groupNameEmptyString.setName("");

		// Then
		assertThrows(AddingGroupException.class, () -> {
			// When
			groupsService.updateGroup(groupNameEmptyString);
		});
	}

	@Test
	void testUpdateGroupFailedSerieNull() {
		// Given
		Group groupSerieNull = getGroupByIdBreakingReference(1L);
		groupSerieNull.setSerie(null);

		// Then
		assertThrows(AddingGroupException.class, () -> {
			// When
			groupsService.updateGroup(groupSerieNull);
		});
	}

	@Test
	void testUpdateGroupFailedSerieIdNull() {
		// Given
		Group groupSerieIdNull = getGroupByIdBreakingReference(1L);
		groupSerieIdNull.setSerie(getSerieWithoutIdBreakingReference(0));

		// Then
		assertThrows(AddingGroupException.class, () -> {
			// When
			groupsService.updateGroup(groupSerieIdNull);
		});
	}

	@Test
	void testUpdateGroupFailedSerieIdLessThan1() {
		// Given
		Group groupSerieIdLessThan1 = getGroupByIdBreakingReference(1L);
		groupSerieIdLessThan1.setSerie(SERIE_WITH_ID_LESS_THAN_1);

		// Then
		assertThrows(AddingGroupException.class, () -> {
			// When
			groupsService.updateGroup(groupSerieIdLessThan1);
		});
	}

	@Test
	void testUpdateGroupFailedGroupDoesNotExists() {
		// Given
		when(groupsDao.findById(anyLong())).thenReturn(Optional.ofNullable(null));

		// Then
		assertThrows(GroupNotFoundException.class, () -> {
			groupsService.updateGroup(getGroupByIdBreakingReference(1L));
		});

		verify(groupsDao).findById(1L);
	}

	// DELETE

	@Test
	void testDeleteGroupSuccessed() {
		// Given
		when(groupsDao.findById(anyLong())).thenReturn(Optional.of(getGroupByIdBreakingReference(1L)));

		// When
		Group group = groupsService.deleteGroupd(1L);

		// Then
		assertEquals(getGroupByIdBreakingReference(1L), group);

		verify(groupsDao).findById(1L);
		verify(groupsDao).deleteById(1L);
	}

	@Test
	void testDeleteGroupFailed() {
		// Given
		when(groupsDao.findById(anyLong())).thenReturn(Optional.ofNullable(null));

		// Then
		assertThrows(GroupNotFoundException.class, () -> {
			// When
			groupsService.deleteGroupd(1L);
		});

		
		verify(groupsDao).findById(1L);
	}

}
