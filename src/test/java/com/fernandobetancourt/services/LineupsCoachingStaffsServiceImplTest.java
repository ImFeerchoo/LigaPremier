package com.fernandobetancourt.services;

import static com.fernandobetancourt.Data.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.fernandobetancourt.exceptions.AddingLineupCoachingStaffException;
import com.fernandobetancourt.exceptions.CoachingStaffNotFoundException;
import com.fernandobetancourt.exceptions.LineupCoachingStaffNotFoundException;
import com.fernandobetancourt.exceptions.LineupMatchNotFoundException;
import com.fernandobetancourt.exceptions.MatchNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.dao.ILineupsCoachingStaffsDao;
import com.fernandobetancourt.model.entity.Lineup;
import com.fernandobetancourt.model.entity.LineupCoachingStaff;
import com.fernandobetancourt.model.entity.Match;
import com.fernandobetancourt.validators.LineupCoachingStaffValidator;

@SpringBootTest(classes = { LineupsCoachingStaffsServiceImpl.class, LineupCoachingStaffValidator.class})
class LineupsCoachingStaffsServiceImplTest {

	@MockBean
	private ILineupsCoachingStaffsDao lineupsCoachingStaffsDao;

	@MockBean
	private ILineupsService lineupsService;

	@MockBean
	private ICoachingStaffsService coachingStaffsService;

	@MockBean
	private IMatchesService matchesService;

	@MockBean
	private IClubesMatchesService clubesMatchesService;

	@MockBean
	private ILineupsMatchesService lineupsMatchesService;

	@Autowired
	private ILineupsCoachingStaffsService lineupsCoachingStaffsService;

	// GET

	@Test
	void testGetLineupCoachingStaffSuccessed() {
		// Given
		when(lineupsCoachingStaffsDao.findById(anyLong())).then(
				invocation -> Optional.of(getLineupCoachingStaffByIdBreakingReference(invocation.getArgument(0))));

		// When
		LineupCoachingStaff lineupCoachingStaff = lineupsCoachingStaffsService.getLineupCoachingStaff(1L);

		// Then
		assertEquals(getLineupCoachingStaffByIdBreakingReference(1L), lineupCoachingStaff);
		verify(lineupsCoachingStaffsDao).findById(1L);
	}

	@Test
	void testGetLineupCoachingStaffFailed() {
		// Given
		when(lineupsCoachingStaffsDao.findById(anyLong())).thenReturn(Optional.empty());

		// Then
		assertThrows(LineupCoachingStaffNotFoundException.class, () -> {
			// When
			lineupsCoachingStaffsService.getLineupCoachingStaff(1L);
		});

		verify(lineupsCoachingStaffsDao).findById(1L);
	}

	@Test
	void testGetLineupCoachingStaffByMatchSuccessed() {
		// Given
		when(matchesService.getMatch(anyLong()))
				.then(invocation -> getMatchByIdBreakingReference(invocation.getArgument(0)));
		when(lineupsMatchesService.getLineupMatchesByMatch(any(Match.class))).thenReturn(LINEUPMATCHES);
		when(lineupsCoachingStaffsDao.findByLineup(getLineupWithId()))
				.thenReturn(LINEUP_COACHING_STAFFS_WITH_ID_CLUB_1);
		when(lineupsCoachingStaffsDao.findByLineup(getLineupWithId2()))
				.thenReturn(LINEUP_COACHING_STAFFS_WITH_ID_CLUB_2);

		// When
		List<LineupCoachingStaff> lineupCoachingStaffs = lineupsCoachingStaffsService.getLineupCoachingStaffByMatch(1L);

		// Then
		assertEquals(LINEUP_COACHING_STAFFS_MATCH(), lineupCoachingStaffs);
		verify(matchesService).getMatch(1L);
		verify(lineupsMatchesService).getLineupMatchesByMatch(any(Match.class));
		verify(lineupsCoachingStaffsDao, times(2)).findByLineup(any(Lineup.class));

	}

	@Test
	void testGetLineupCoachingStaffByMatchEmptyList() {
		// Given
		when(matchesService.getMatch(anyLong()))
				.then(invocation -> getMatchByIdBreakingReference(invocation.getArgument(0)));
		when(lineupsMatchesService.getLineupMatchesByMatch(any(Match.class))).thenReturn(LINEUPMATCHES);
		when(lineupsCoachingStaffsDao.findByLineup(getLineupWithId())).thenReturn(Collections.emptyList());
		when(lineupsCoachingStaffsDao.findByLineup(getLineupWithId2())).thenReturn(Collections.emptyList());

		// Then
		assertThrows(LineupCoachingStaffNotFoundException.class, () -> {
			// When
			lineupsCoachingStaffsService.getLineupCoachingStaffByMatch(1L);
		});

		verify(matchesService).getMatch(1L);
		verify(lineupsMatchesService).getLineupMatchesByMatch(any(Match.class));
		verify(lineupsCoachingStaffsDao, times(2)).findByLineup(any(Lineup.class));
	}

	// POST

	@Test
	void testAddLineupsCoachingStaffsSuccessed() {
		//Given
		when(matchesService.getMatch(anyLong())).then(invocation -> getMatchByIdBreakingReference(invocation.getArgument(0)));
		when(clubesMatchesService.getClubMatchByMatch(any(Match.class))).thenReturn(getClubMatch());
		when(lineupsMatchesService.getLineupMatchesByMatch(any(Match.class))).thenReturn(LINEUPMATCHES);
		when(lineupsCoachingStaffsDao.findByLineup(getLineupWithId())).thenReturn(LINEUP_COACHING_STAFFS_WITH_ID_CLUB_1);
		when(lineupsCoachingStaffsDao.findByLineup(getLineupWithId2())).thenReturn(LINEUP_COACHING_STAFFS_WITH_ID_CLUB_2);
		when(coachingStaffsService.getCoachingStaff(anyLong())).then(invocation -> getCoachingStaffByIdBreakingReference(invocation.getArgument(0)));
		when(lineupsCoachingStaffsDao.save(any(LineupCoachingStaff.class))).then(new Answer<LineupCoachingStaff>() {
			Long id = 1L;
			@Override
			public LineupCoachingStaff answer(InvocationOnMock invocation) throws Throwable {
				LineupCoachingStaff lineupCoachingStaff = invocation.getArgument(0);
				lineupCoachingStaff.setLineupCoachingStaffId(id++);
				return lineupCoachingStaff;
			}
		});
		
		//When
		List<LineupCoachingStaff> lineupCoachingStaffsSaved = lineupsCoachingStaffsService.addLineupsCoachingStaffs(LINEUP_COACHING_STAFFS_WITHOUT_ID_CLUB_1, 1L, "Local");
		
		//Then
		assertEquals(LINEUP_COACHING_STAFFS_WITH_ID_CLUB_1, lineupCoachingStaffsSaved);
		verify(matchesService).getMatch(1L);
		verify(clubesMatchesService).getClubMatchByMatch(any(Match.class));
		verify(lineupsMatchesService).getLineupMatchesByMatch(any(Match.class));		
		verify(lineupsCoachingStaffsDao, times(2)).deleteById(anyLong());
		verify(coachingStaffsService, atLeast(2)).getCoachingStaff(anyLong());
		verify(lineupsService, atLeast(2)).getLineup(anyLong());
		verify(lineupsCoachingStaffsDao, atLeast(2)).save(any(LineupCoachingStaff.class));
	}
	
	@Test
	void testAddLineupsCoachingStaffsSuccessed2() {
		//Given
		when(matchesService.getMatch(anyLong())).then(invocation -> getMatchByIdBreakingReference(invocation.getArgument(0)));
		when(clubesMatchesService.getClubMatchByMatch(any(Match.class))).thenReturn(getClubMatch());
		when(lineupsMatchesService.getLineupMatchesByMatch(any(Match.class))).thenReturn(LINEUPMATCHES);
		when(lineupsCoachingStaffsDao.findByLineup(getLineupWithId())).thenReturn(LINEUP_COACHING_STAFFS_WITH_ID_CLUB_1);
		when(lineupsCoachingStaffsDao.findByLineup(getLineupWithId2())).thenReturn(LINEUP_COACHING_STAFFS_WITH_ID_CLUB_2);
		when(coachingStaffsService.getCoachingStaff(anyLong())).then(invocation -> getCoachingStaffByIdBreakingReference(invocation.getArgument(0)));
		when(lineupsCoachingStaffsDao.save(any(LineupCoachingStaff.class))).then(new Answer<LineupCoachingStaff>() {
			Long id = 3L;
			@Override
			public LineupCoachingStaff answer(InvocationOnMock invocation) throws Throwable {
				LineupCoachingStaff lineupCoachingStaff = invocation.getArgument(0);
				lineupCoachingStaff.setLineupCoachingStaffId(id++);
				return lineupCoachingStaff;
			}
		});
		
		//When
		List<LineupCoachingStaff> lineupCoachingStaffsSaved = lineupsCoachingStaffsService.addLineupsCoachingStaffs(LINEUP_COACHING_STAFFS_WITHOUT_ID_CLUB_2, 1L, "Visitor");
		
		//Then
		assertEquals(LINEUP_COACHING_STAFFS_WITH_ID_CLUB_2, lineupCoachingStaffsSaved);
		verify(matchesService).getMatch(1L);
		verify(clubesMatchesService).getClubMatchByMatch(any(Match.class));
		verify(lineupsMatchesService).getLineupMatchesByMatch(any(Match.class));		
		verify(lineupsCoachingStaffsDao, times(2)).deleteById(anyLong());
		verify(coachingStaffsService, atLeast(2)).getCoachingStaff(anyLong());
		verify(lineupsService, atLeast(2)).getLineup(anyLong());
		verify(lineupsCoachingStaffsDao, atLeast(2)).save(any(LineupCoachingStaff.class));
	}

	@Test
	void testAddLineupsCoachingStaffsFailedMatchDoesNotExists() {
		//Given
		when(matchesService.getMatch(anyLong())).thenThrow(MatchNotFoundException.class);
		
		//Then
		assertThrows(MatchNotFoundException.class, () -> {
			//When
			lineupsCoachingStaffsService.addLineupsCoachingStaffs(LINEUP_COACHING_STAFFS_WITHOUT_ID_CLUB_2, 1L, "Visitor");
			
		});
		
		verify(matchesService).getMatch(1L);
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(lineupsMatchesService, never()).getLineupMatchesByMatch(any(Match.class));		
		verify(lineupsCoachingStaffsDao, never()).deleteById(anyLong());
		verify(coachingStaffsService, never()).getCoachingStaff(anyLong());
		verify(lineupsService, never()).getLineup(anyLong());
		verify(lineupsCoachingStaffsDao, never()).save(any(LineupCoachingStaff.class));
	}

	@Test
	void testAddLineupsCoachingStaffsFailedClubStatusIsNotValid() {
		//Then
		assertThrows(WritingInformationException.class, () -> {
			//When
			lineupsCoachingStaffsService.addLineupsCoachingStaffs(LINEUP_COACHING_STAFFS_WITHOUT_ID_CLUB_2, 1L, "ClubStausNotValid");
		});
		
		
		verify(matchesService).getMatch(1L);
		verify(clubesMatchesService, never()).getClubMatchByMatch(any(Match.class));
		verify(lineupsMatchesService, never()).getLineupMatchesByMatch(any(Match.class));		
		verify(lineupsCoachingStaffsDao, never()).deleteById(anyLong());
		verify(coachingStaffsService, never()).getCoachingStaff(anyLong());
		verify(lineupsService, never()).getLineup(anyLong());
		verify(lineupsCoachingStaffsDao, never()).save(any(LineupCoachingStaff.class));
	}

	@Test
	void testAddLineupsCoachingStaffsFailedLineupMatchesDoesNotExists() {
		//Given
		when(matchesService.getMatch(anyLong())).then(invocation -> getMatchByIdBreakingReference(invocation.getArgument(0)));
		when(clubesMatchesService.getClubMatchByMatch(any(Match.class))).thenReturn(getClubMatch());
		when(lineupsMatchesService.getLineupMatchesByMatch(any(Match.class))).thenReturn(Collections.emptyList());
		
		//Then
		assertThrows(LineupMatchNotFoundException.class, () -> {
			//When
			lineupsCoachingStaffsService.addLineupsCoachingStaffs(LINEUP_COACHING_STAFFS_WITHOUT_ID_CLUB_2, 1L, "Visitor");
			
		});
		
		verify(matchesService).getMatch(1L);
		verify(clubesMatchesService).getClubMatchByMatch(any(Match.class));
		verify(lineupsMatchesService).getLineupMatchesByMatch(any(Match.class));		
		verify(lineupsCoachingStaffsDao, never()).deleteById(anyLong());
		verify(coachingStaffsService, never()).getCoachingStaff(anyLong());
		verify(lineupsService, never()).getLineup(anyLong());
		verify(lineupsCoachingStaffsDao, never()).save(any(LineupCoachingStaff.class));
	}

	@Test
	void testAddLineupsCoachingStaffsFailedLineupCoachingStaffNull() {
		//Given
		when(matchesService.getMatch(anyLong())).then(invocation -> getMatchByIdBreakingReference(invocation.getArgument(0)));
		when(clubesMatchesService.getClubMatchByMatch(any(Match.class))).thenReturn(getClubMatch());
		when(lineupsMatchesService.getLineupMatchesByMatch(any(Match.class))).thenReturn(LINEUPMATCHES);
		when(lineupsCoachingStaffsDao.findByLineup(getLineupWithId())).thenReturn(LINEUP_COACHING_STAFFS_WITH_ID_CLUB_1);
		when(lineupsCoachingStaffsDao.findByLineup(getLineupWithId2())).thenReturn(LINEUP_COACHING_STAFFS_WITH_ID_CLUB_2);
		when(coachingStaffsService.getCoachingStaff(anyLong())).then(invocation -> getCoachingStaffByIdBreakingReference(invocation.getArgument(0)));
		when(lineupsCoachingStaffsDao.save(any(LineupCoachingStaff.class))).then(new Answer<LineupCoachingStaff>() {
			Long id = 3L;
			@Override
			public LineupCoachingStaff answer(InvocationOnMock invocation) throws Throwable {
				LineupCoachingStaff lineupCoachingStaff = invocation.getArgument(0);
				lineupCoachingStaff.setLineupCoachingStaffId(id++);
				return lineupCoachingStaff;
			}
		});
		
		List<LineupCoachingStaff> lineupCoachingStaffs = new ArrayList<>(LINEUP_COACHING_STAFFS_WITHOUT_ID_CLUB_2);
		lineupCoachingStaffs.add(null);
	
		//Then
		assertThrows(AddingLineupCoachingStaffException.class, () -> {
			//When
			lineupsCoachingStaffsService.addLineupsCoachingStaffs(lineupCoachingStaffs, 1L, "Visitor");
			
		});
		
		//Then
		verify(matchesService).getMatch(1L);
		verify(clubesMatchesService).getClubMatchByMatch(any(Match.class));
		verify(lineupsMatchesService).getLineupMatchesByMatch(any(Match.class));		
		verify(lineupsCoachingStaffsDao, times(2)).deleteById(anyLong());
		verify(coachingStaffsService, atLeast(2)).getCoachingStaff(anyLong());
		verify(lineupsService, atLeast(2)).getLineup(anyLong());
		verify(lineupsCoachingStaffsDao, atLeast(2)).save(any(LineupCoachingStaff.class));
	}

	@Test
	void testAddLineupsCoachingStaffsFailedCoachingStaffNull() {
		//Given
		when(matchesService.getMatch(anyLong())).then(invocation -> getMatchByIdBreakingReference(invocation.getArgument(0)));
		when(clubesMatchesService.getClubMatchByMatch(any(Match.class))).thenReturn(getClubMatch());
		when(lineupsMatchesService.getLineupMatchesByMatch(any(Match.class))).thenReturn(LINEUPMATCHES);
		when(lineupsCoachingStaffsDao.findByLineup(getLineupWithId())).thenReturn(LINEUP_COACHING_STAFFS_WITH_ID_CLUB_1);
		when(lineupsCoachingStaffsDao.findByLineup(getLineupWithId2())).thenReturn(LINEUP_COACHING_STAFFS_WITH_ID_CLUB_2);
		when(coachingStaffsService.getCoachingStaff(anyLong())).then(invocation -> getCoachingStaffByIdBreakingReference(invocation.getArgument(0)));
		when(lineupsCoachingStaffsDao.save(any(LineupCoachingStaff.class))).then(new Answer<LineupCoachingStaff>() {
			Long id = 1L;
			@Override
			public LineupCoachingStaff answer(InvocationOnMock invocation) throws Throwable {
				LineupCoachingStaff lineupCoachingStaff = invocation.getArgument(0);
				lineupCoachingStaff.setLineupCoachingStaffId(id++);
				return lineupCoachingStaff;
			}
		});
		
		List<LineupCoachingStaff> lineupCoachingStaffs = new ArrayList<>(LINEUP_COACHING_STAFFS_WITHOUT_ID_CLUB_1);
		lineupCoachingStaffs.add(new LineupCoachingStaff(null, null, getLineupWithId()));
		
		//Then
		assertThrows(AddingLineupCoachingStaffException.class, () -> {
			//When
			lineupsCoachingStaffsService.addLineupsCoachingStaffs(lineupCoachingStaffs, 1L, "Local");
		});
		
		verify(matchesService).getMatch(1L);
		verify(clubesMatchesService).getClubMatchByMatch(any(Match.class));
		verify(lineupsMatchesService).getLineupMatchesByMatch(any(Match.class));		
		verify(lineupsCoachingStaffsDao, times(2)).deleteById(anyLong());
		verify(coachingStaffsService, atLeast(2)).getCoachingStaff(anyLong());
		verify(lineupsService, atLeast(2)).getLineup(anyLong());
		verify(lineupsCoachingStaffsDao, atLeast(2)).save(any(LineupCoachingStaff.class));
	}

	@Test
	void testAddLineupsCoachingStaffsFailedCoachingStaffIdNull() {
		//Given
		when(matchesService.getMatch(anyLong())).then(invocation -> getMatchByIdBreakingReference(invocation.getArgument(0)));
		when(clubesMatchesService.getClubMatchByMatch(any(Match.class))).thenReturn(getClubMatch());
		when(lineupsMatchesService.getLineupMatchesByMatch(any(Match.class))).thenReturn(LINEUPMATCHES);
		when(lineupsCoachingStaffsDao.findByLineup(getLineupWithId())).thenReturn(LINEUP_COACHING_STAFFS_WITH_ID_CLUB_1);
		when(lineupsCoachingStaffsDao.findByLineup(getLineupWithId2())).thenReturn(LINEUP_COACHING_STAFFS_WITH_ID_CLUB_2);
		when(coachingStaffsService.getCoachingStaff(anyLong())).then(invocation -> getCoachingStaffByIdBreakingReference(invocation.getArgument(0)));
		when(lineupsCoachingStaffsDao.save(any(LineupCoachingStaff.class))).then(new Answer<LineupCoachingStaff>() {
			Long id = 1L;
			@Override
			public LineupCoachingStaff answer(InvocationOnMock invocation) throws Throwable {
				LineupCoachingStaff lineupCoachingStaff = invocation.getArgument(0);
				lineupCoachingStaff.setLineupCoachingStaffId(id++);
				return lineupCoachingStaff;
			}
		});
		
		List<LineupCoachingStaff> lineupCoachingStaffs = new ArrayList<>(LINEUP_COACHING_STAFFS_WITHOUT_ID_CLUB_1);
		lineupCoachingStaffs.add(new LineupCoachingStaff(null, getCoachingStaffWithoutIdByPositionBreakingReference(0), getLineupWithId()));
		
		//Then
		assertThrows(AddingLineupCoachingStaffException.class, () -> {
			//When
			lineupsCoachingStaffsService.addLineupsCoachingStaffs(lineupCoachingStaffs, 1L, "Local");
		});
		
		verify(matchesService).getMatch(1L);
		verify(clubesMatchesService).getClubMatchByMatch(any(Match.class));
		verify(lineupsMatchesService).getLineupMatchesByMatch(any(Match.class));		
		verify(lineupsCoachingStaffsDao, times(2)).deleteById(anyLong());
		verify(coachingStaffsService, atLeast(2)).getCoachingStaff(anyLong());
		verify(lineupsService, atLeast(2)).getLineup(anyLong());
		verify(lineupsCoachingStaffsDao, atLeast(2)).save(any(LineupCoachingStaff.class));
	}

	@Test
	void testAddLineupsCoachingStaffsFailedCoachingStaffIdLessThan1() {
		//Given
		when(matchesService.getMatch(anyLong())).then(invocation -> getMatchByIdBreakingReference(invocation.getArgument(0)));
		when(clubesMatchesService.getClubMatchByMatch(any(Match.class))).thenReturn(getClubMatch());
		when(lineupsMatchesService.getLineupMatchesByMatch(any(Match.class))).thenReturn(LINEUPMATCHES);
		when(lineupsCoachingStaffsDao.findByLineup(getLineupWithId())).thenReturn(LINEUP_COACHING_STAFFS_WITH_ID_CLUB_1);
		when(lineupsCoachingStaffsDao.findByLineup(getLineupWithId2())).thenReturn(LINEUP_COACHING_STAFFS_WITH_ID_CLUB_2);
		when(coachingStaffsService.getCoachingStaff(anyLong())).then(invocation -> getCoachingStaffByIdBreakingReference(invocation.getArgument(0)));
		when(lineupsCoachingStaffsDao.save(any(LineupCoachingStaff.class))).then(new Answer<LineupCoachingStaff>() {
			Long id = 1L;
			@Override
			public LineupCoachingStaff answer(InvocationOnMock invocation) throws Throwable {
				LineupCoachingStaff lineupCoachingStaff = invocation.getArgument(0);
				lineupCoachingStaff.setLineupCoachingStaffId(id++);
				return lineupCoachingStaff;
			}
		});
		
		List<LineupCoachingStaff> lineupCoachingStaffs = new ArrayList<>(LINEUP_COACHING_STAFFS_WITHOUT_ID_CLUB_1);
		lineupCoachingStaffs.add(new LineupCoachingStaff(null, COACHING_STAFF_WITH_ID_LESS_THAN_1, getLineupWithId()));
		
		//Then
		assertThrows(AddingLineupCoachingStaffException.class, () -> {
			//When
			lineupsCoachingStaffsService.addLineupsCoachingStaffs(lineupCoachingStaffs, 1L, "Local");
		});
		
		verify(matchesService).getMatch(1L);
		verify(clubesMatchesService).getClubMatchByMatch(any(Match.class));
		verify(lineupsMatchesService).getLineupMatchesByMatch(any(Match.class));		
		verify(lineupsCoachingStaffsDao, times(2)).deleteById(anyLong());
		verify(coachingStaffsService, atLeast(2)).getCoachingStaff(anyLong());
		verify(lineupsService, atLeast(2)).getLineup(anyLong());
		verify(lineupsCoachingStaffsDao, atLeast(2)).save(any(LineupCoachingStaff.class));
	}
	
	//EL Lineup no puede ser nulo, ya que se obtiene desde una de sus relaciones

//	@Test
//	void testAddLineupsCoachingStaffsFailedLineupNull() {
//		//Given
//		when(matchesService.getMatch(anyLong())).then(invocation -> getMatchByIdBreakingReference(invocation.getArgument(0)));
//		when(clubesMatchesService.getClubMatchByMatch(any(Match.class))).thenReturn(getClubMatch());
//		when(lineupsMatchesService.getLineupMatchesByMatch(any(Match.class))).thenReturn(LINEUPMATCHES);
//		when(lineupsCoachingStaffsDao.findByLineup(getLineupWithId())).thenReturn(LINEUP_COACHING_STAFFS_WITH_ID_CLUB_1);
//		when(lineupsCoachingStaffsDao.findByLineup(getLineupWithId2())).thenReturn(LINEUP_COACHING_STAFFS_WITH_ID_CLUB_2);
//		when(coachingStaffsService.getCoachingStaff(anyLong())).then(invocation -> getCoachingStaffByIdBreakingReference(invocation.getArgument(0)));
//		when(lineupsCoachingStaffsDao.save(any(LineupCoachingStaff.class))).then(new Answer<LineupCoachingStaff>() {
//			Long id = 1L;
//			@Override
//			public LineupCoachingStaff answer(InvocationOnMock invocation) throws Throwable {
//				LineupCoachingStaff lineupCoachingStaff = invocation.getArgument(0);
//				lineupCoachingStaff.setLineupCoachingStaffId(id++);
//				return lineupCoachingStaff;
//			}
//		});
//		
//		//When
//		List<LineupCoachingStaff> lineupCoachingStaffsSaved = lineupsCoachingStaffsService.addLineupsCoachingStaffs(LINEUP_COACHING_STAFFS_WITHOUT_ID_CLUB_1, 1L, "Local");
//		
//		//Then
//		assertEquals(LINEUP_COACHING_STAFFS_WITH_ID_CLUB_1, lineupCoachingStaffsSaved);
//		verify(matchesService).getMatch(1L);
//		verify(clubesMatchesService).getClubMatchByMatch(any(Match.class));
//		verify(lineupsMatchesService).getLineupMatchesByMatch(any(Match.class));		
//		verify(lineupsCoachingStaffsDao, times(2)).deleteById(anyLong());
//		verify(coachingStaffsService, atLeast(2)).getCoachingStaff(anyLong());
//		verify(lineupsService, atLeast(2)).getLineup(anyLong());
//		verify(lineupsCoachingStaffsDao, atLeast(2)).save(any(LineupCoachingStaff.class));
//	}

//	@Test
//	void testAddLineupsCoachingStaffsFailedLineupIdNull() {
//		//Given
//		when(matchesService.getMatch(anyLong())).then(invocation -> getMatchByIdBreakingReference(invocation.getArgument(0)));
//		when(clubesMatchesService.getClubMatchByMatch(any(Match.class))).thenReturn(getClubMatch());
//		when(lineupsMatchesService.getLineupMatchesByMatch(any(Match.class))).thenReturn(LINEUPMATCHES);
//		when(lineupsCoachingStaffsDao.findByLineup(getLineupWithId())).thenReturn(LINEUP_COACHING_STAFFS_WITH_ID_CLUB_1);
//		when(lineupsCoachingStaffsDao.findByLineup(getLineupWithId2())).thenReturn(LINEUP_COACHING_STAFFS_WITH_ID_CLUB_2);
//		when(coachingStaffsService.getCoachingStaff(anyLong())).then(invocation -> getCoachingStaffByIdBreakingReference(invocation.getArgument(0)));
//		when(lineupsCoachingStaffsDao.save(any(LineupCoachingStaff.class))).then(new Answer<LineupCoachingStaff>() {
//			Long id = 1L;
//			@Override
//			public LineupCoachingStaff answer(InvocationOnMock invocation) throws Throwable {
//				LineupCoachingStaff lineupCoachingStaff = invocation.getArgument(0);
//				lineupCoachingStaff.setLineupCoachingStaffId(id++);
//				return lineupCoachingStaff;
//			}
//		});
//		
//		//When
//		List<LineupCoachingStaff> lineupCoachingStaffsSaved = lineupsCoachingStaffsService.addLineupsCoachingStaffs(LINEUP_COACHING_STAFFS_WITHOUT_ID_CLUB_1, 1L, "Local");
//		
//		//Then
//		assertEquals(LINEUP_COACHING_STAFFS_WITH_ID_CLUB_1, lineupCoachingStaffsSaved);
//		verify(matchesService).getMatch(1L);
//		verify(clubesMatchesService).getClubMatchByMatch(any(Match.class));
//		verify(lineupsMatchesService).getLineupMatchesByMatch(any(Match.class));		
//		verify(lineupsCoachingStaffsDao, times(2)).deleteById(anyLong());
//		verify(coachingStaffsService, atLeast(2)).getCoachingStaff(anyLong());
//		verify(lineupsService, atLeast(2)).getLineup(anyLong());
//		verify(lineupsCoachingStaffsDao, atLeast(2)).save(any(LineupCoachingStaff.class));
//	}
//
//	@Test
//	void testAddLineupsCoachingStaffsFailedLineupIdLessThan1() {
//		//Given
//		when(matchesService.getMatch(anyLong())).then(invocation -> getMatchByIdBreakingReference(invocation.getArgument(0)));
//		when(clubesMatchesService.getClubMatchByMatch(any(Match.class))).thenReturn(getClubMatch());
//		when(lineupsMatchesService.getLineupMatchesByMatch(any(Match.class))).thenReturn(LINEUPMATCHES);
//		when(lineupsCoachingStaffsDao.findByLineup(getLineupWithId())).thenReturn(LINEUP_COACHING_STAFFS_WITH_ID_CLUB_1);
//		when(lineupsCoachingStaffsDao.findByLineup(getLineupWithId2())).thenReturn(LINEUP_COACHING_STAFFS_WITH_ID_CLUB_2);
//		when(coachingStaffsService.getCoachingStaff(anyLong())).then(invocation -> getCoachingStaffByIdBreakingReference(invocation.getArgument(0)));
//		when(lineupsCoachingStaffsDao.save(any(LineupCoachingStaff.class))).then(new Answer<LineupCoachingStaff>() {
//			Long id = 1L;
//			@Override
//			public LineupCoachingStaff answer(InvocationOnMock invocation) throws Throwable {
//				LineupCoachingStaff lineupCoachingStaff = invocation.getArgument(0);
//				lineupCoachingStaff.setLineupCoachingStaffId(id++);
//				return lineupCoachingStaff;
//			}
//		});
//		
//		//When
//		List<LineupCoachingStaff> lineupCoachingStaffsSaved = lineupsCoachingStaffsService.addLineupsCoachingStaffs(LINEUP_COACHING_STAFFS_WITHOUT_ID_CLUB_1, 1L, "Local");
//		
//		//Then
//		assertEquals(LINEUP_COACHING_STAFFS_WITH_ID_CLUB_1, lineupCoachingStaffsSaved);
//		verify(matchesService).getMatch(1L);
//		verify(clubesMatchesService).getClubMatchByMatch(any(Match.class));
//		verify(lineupsMatchesService).getLineupMatchesByMatch(any(Match.class));		
//		verify(lineupsCoachingStaffsDao, times(2)).deleteById(anyLong());
//		verify(coachingStaffsService, atLeast(2)).getCoachingStaff(anyLong());
//		verify(lineupsService, atLeast(2)).getLineup(anyLong());
//		verify(lineupsCoachingStaffsDao, atLeast(2)).save(any(LineupCoachingStaff.class));
//	}
//
	@Test
	void testAddLineupsCoachingStaffsFailedCoachingStaffDoesNotExists() {
		//Given
		when(matchesService.getMatch(anyLong())).then(invocation -> getMatchByIdBreakingReference(invocation.getArgument(0)));
		when(clubesMatchesService.getClubMatchByMatch(any(Match.class))).thenReturn(getClubMatch());
		when(lineupsMatchesService.getLineupMatchesByMatch(any(Match.class))).thenReturn(LINEUPMATCHES);
		when(lineupsCoachingStaffsDao.findByLineup(getLineupWithId())).thenReturn(LINEUP_COACHING_STAFFS_WITH_ID_CLUB_1);
		when(lineupsCoachingStaffsDao.findByLineup(getLineupWithId2())).thenReturn(LINEUP_COACHING_STAFFS_WITH_ID_CLUB_2);
		when(coachingStaffsService.getCoachingStaff(anyLong())).thenThrow(CoachingStaffNotFoundException.class);
		
		//Then
		assertThrows(CoachingStaffNotFoundException.class, () -> {
			//When
			lineupsCoachingStaffsService.addLineupsCoachingStaffs(LINEUP_COACHING_STAFFS_WITHOUT_ID_CLUB_1, 1L, "Local");
			
		});
		
		verify(matchesService).getMatch(1L);
		verify(clubesMatchesService).getClubMatchByMatch(any(Match.class));
		verify(lineupsMatchesService).getLineupMatchesByMatch(any(Match.class));		
		verify(lineupsCoachingStaffsDao, times(2)).deleteById(anyLong());
		verify(coachingStaffsService).getCoachingStaff(anyLong());
		verify(lineupsService).getLineup(anyLong());
		verify(lineupsCoachingStaffsDao, never()).save(any(LineupCoachingStaff.class));
	}
	
	//El lineup tiene que existir, ya que este lo recupero de una de sus relaciones
	
//	@Test
//	void testAddLineupsCoachingStaffsFailedLineupDoesNotExists() {
//		//Given
//		when(matchesService.getMatch(anyLong())).then(invocation -> getMatchByIdBreakingReference(invocation.getArgument(0)));
//		when(clubesMatchesService.getClubMatchByMatch(any(Match.class))).thenReturn(getClubMatch());
//		when(lineupsMatchesService.getLineupMatchesByMatch(any(Match.class))).thenReturn(LINEUPMATCHES);
//		when(lineupsCoachingStaffsDao.findByLineup(getLineupWithId())).thenReturn(LINEUP_COACHING_STAFFS_WITH_ID_CLUB_1);
//		when(lineupsCoachingStaffsDao.findByLineup(getLineupWithId2())).thenReturn(LINEUP_COACHING_STAFFS_WITH_ID_CLUB_2);
//		when(coachingStaffsService.getCoachingStaff(anyLong())).then(invocation -> getCoachingStaffByIdBreakingReference(invocation.getArgument(0)));
//		when(lineupsCoachingStaffsDao.save(any(LineupCoachingStaff.class))).then(new Answer<LineupCoachingStaff>() {
//			Long id = 1L;
//			@Override
//			public LineupCoachingStaff answer(InvocationOnMock invocation) throws Throwable {
//				LineupCoachingStaff lineupCoachingStaff = invocation.getArgument(0);
//				lineupCoachingStaff.setLineupCoachingStaffId(id++);
//				return lineupCoachingStaff;
//			}
//		});
//		
//		//When
//		List<LineupCoachingStaff> lineupCoachingStaffsSaved = lineupsCoachingStaffsService.addLineupsCoachingStaffs(LINEUP_COACHING_STAFFS_WITHOUT_ID_CLUB_1, 1L, "Local");
//		
//		//Then
//		assertEquals(LINEUP_COACHING_STAFFS_WITH_ID_CLUB_1, lineupCoachingStaffsSaved);
//		verify(matchesService).getMatch(1L);
//		verify(clubesMatchesService).getClubMatchByMatch(any(Match.class));
//		verify(lineupsMatchesService).getLineupMatchesByMatch(any(Match.class));		
//		verify(lineupsCoachingStaffsDao, times(2)).deleteById(anyLong());
//		verify(coachingStaffsService, atLeast(2)).getCoachingStaff(anyLong());
//		verify(lineupsService, atLeast(2)).getLineup(anyLong());
//		verify(lineupsCoachingStaffsDao, atLeast(2)).save(any(LineupCoachingStaff.class));
//	}

	@Test
	void testAddLineupsCoachingStaffsFailedCoachingStaffDoesNotBelongToClub() {
		//Given
		when(matchesService.getMatch(anyLong())).then(invocation -> getMatchByIdBreakingReference(invocation.getArgument(0)));
		when(clubesMatchesService.getClubMatchByMatch(any(Match.class))).thenReturn(getClubMatch());
		when(lineupsMatchesService.getLineupMatchesByMatch(any(Match.class))).thenReturn(LINEUPMATCHES);
		when(lineupsCoachingStaffsDao.findByLineup(getLineupWithId())).thenReturn(LINEUP_COACHING_STAFFS_WITH_ID_CLUB_1);
		when(lineupsCoachingStaffsDao.findByLineup(getLineupWithId2())).thenReturn(LINEUP_COACHING_STAFFS_WITH_ID_CLUB_2);
		when(coachingStaffsService.getCoachingStaff(anyLong())).then(invocation -> getCoachingStaffByIdBreakingReference(invocation.getArgument(0)));
		when(lineupsCoachingStaffsDao.save(any(LineupCoachingStaff.class))).then(new Answer<LineupCoachingStaff>() {
			Long id = 1L;
			@Override
			public LineupCoachingStaff answer(InvocationOnMock invocation) throws Throwable {
				LineupCoachingStaff lineupCoachingStaff = invocation.getArgument(0);
				lineupCoachingStaff.setLineupCoachingStaffId(id++);
				return lineupCoachingStaff;
			}
		});
		
		List<LineupCoachingStaff> lineupCoachingStaffs = new ArrayList<>(LINEUP_COACHING_STAFFS_WITHOUT_ID_CLUB_1);
		lineupCoachingStaffs.add(new LineupCoachingStaff(null, getCoachingStaffByIdBreakingReference(10L), getLineupWithId()));
		
		//Then
		assertThrows(AddingLineupCoachingStaffException.class, () -> {
			//When
			lineupsCoachingStaffsService.addLineupsCoachingStaffs(lineupCoachingStaffs, 1L, "Local");
		});
		
		
		verify(matchesService).getMatch(1L);
		verify(clubesMatchesService).getClubMatchByMatch(any(Match.class));
		verify(lineupsMatchesService).getLineupMatchesByMatch(any(Match.class));		
		verify(lineupsCoachingStaffsDao, times(2)).deleteById(anyLong());
		verify(coachingStaffsService, atLeast(2)).getCoachingStaff(anyLong());
		verify(lineupsService, atLeast(2)).getLineup(anyLong());
		verify(lineupsCoachingStaffsDao, atLeast(2)).save(any(LineupCoachingStaff.class));
	}

	// DELETE

	@Test
	void testDeleteLineupCoachingStaffSuccessed() {
		//Given
		when(lineupsCoachingStaffsDao.findById(anyLong())).then(invocation -> Optional.of(getLineupCoachingStaffByIdBreakingReference(invocation.getArgument(0))));
		
		//When
		LineupCoachingStaff lineupCoachingStaffDeleted = lineupsCoachingStaffsService.deleteLineupCoachingStaff(1L);
		
		//Then
		assertEquals(getLineupCoachingStaffByIdBreakingReference(1L), lineupCoachingStaffDeleted);
		verify(lineupsCoachingStaffsDao).findById(1L);
		verify(lineupsCoachingStaffsDao).deleteById(1L);
	}

	@Test
	void testDeleteLineupCoachingStaffFailed() {
		//Given
		when(lineupsCoachingStaffsDao.findById(anyLong())).thenReturn(Optional.empty());
		
		//Then
		assertThrows(LineupCoachingStaffNotFoundException.class, () -> {
			//When
			lineupsCoachingStaffsService.deleteLineupCoachingStaff(1L);
		});
		
		verify(lineupsCoachingStaffsDao).findById(1L);
		verify(lineupsCoachingStaffsDao, never()).deleteById(1L);
	}
}
