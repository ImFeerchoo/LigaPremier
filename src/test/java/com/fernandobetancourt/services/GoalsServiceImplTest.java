package com.fernandobetancourt.services;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.fernandobetancourt.model.dao.IGoalsDao;

@SpringBootTest
class GoalsServiceImplTest {
	
	@MockBean
	private IGoalsDao goalsDao;
	
	@MockBean
	private IMatchesService matchesService;
	
	@MockBean
	private IPlayersService playersService;
	
	@MockBean
	private IClubesMatchesService clubesMatchesService;
	
	@Autowired
	private IGoalsService goalsService;
	
	//GET

	@Test
	void testGetGoalsByMatch() {
	}
	
	//POST

	@Test
	void testAddGoal() {
	}
	
	//PUT

	@Test
	void testUpdateGoal() {
	}
	
	//DELETE

	@Test
	void testDeleteGoal() {
	}

}
