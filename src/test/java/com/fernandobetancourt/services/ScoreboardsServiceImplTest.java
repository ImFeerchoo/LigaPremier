package com.fernandobetancourt.services;

import static com.fernandobetancourt.Data.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.fernandobetancourt.exceptions.ScoreboardNotFoundException;
import com.fernandobetancourt.model.dao.IScoreboardsDao;
import com.fernandobetancourt.model.entity.Scoreboard;
import com.fernandobetancourt.validators.ScoreboardValidator;

@SpringBootTest(classes = {ScoreboardsServiceImpl.class, ScoreboardValidator.class})
class ScoreboardsServiceImplTest {

	@MockBean
	private IScoreboardsDao scoreboardsDao;

	@Autowired
	private IScoreboardsService scoreboardsService;

	// GET

	@Test
	void testGetScoreboardSuccessed() {
		// Given
		when(scoreboardsDao.findById(anyLong())).thenReturn(Optional.of(getScoreboadWithId()));

		// When
		Scoreboard scoreboard = scoreboardsService.getScoreboard(1L);

		// Then
		assertEquals(getScoreboadWithId(), scoreboard);
		verify(scoreboardsDao).findById(1L);
	}

	@Test
	void testGetScoreboardFailed() {
		// Given
		when(scoreboardsDao.findById(anyLong())).thenReturn(Optional.ofNullable(null));

		// Then
		assertThrows(ScoreboardNotFoundException.class, () -> {
			// When
			scoreboardsService.getScoreboard(1L);
		});

		verify(scoreboardsDao).findById(1L);
	}

	// DELETE

	@Test
	void testDeleteScoreboardSuccessed() {
		// Given
		when(scoreboardsDao.findById(anyLong())).thenReturn(Optional.of(getScoreboadWithId()));

		// When
		Scoreboard scoreboard = scoreboardsService.deleteScoreboard(1L);

		// Then
		assertEquals(getScoreboadWithId(), scoreboard);

		verify(scoreboardsDao).findById(1L);
		verify(scoreboardsDao).deleteById(1L);
	}

	@Test
	void testDeleteScoreboardFailed() {
		// Given
		when(scoreboardsDao.findById(anyLong())).thenReturn(Optional.ofNullable(null));
		
		// Then
		assertThrows(ScoreboardNotFoundException.class, () -> {
			// When
			scoreboardsService.deleteScoreboard(1L);
		});

		verify(scoreboardsDao).findById(1L);
	}

}
