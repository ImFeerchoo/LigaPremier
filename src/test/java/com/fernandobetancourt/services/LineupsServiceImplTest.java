package com.fernandobetancourt.services;

import static com.fernandobetancourt.Data.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.fernandobetancourt.exceptions.LineupNotFoundException;
import com.fernandobetancourt.model.dao.ILineupsDao;
import com.fernandobetancourt.model.entity.Lineup;
import com.fernandobetancourt.validators.LineupValidator;

@SpringBootTest(classes = {LineupsServiceImpl.class, LineupValidator.class})
class LineupsServiceImplTest {
	
	@MockBean
	private ILineupsDao lineupsDao;
	
	@Autowired
	private ILineupsService lineupsService;

	//GET
	
	@Test
	void testGetLineupSuccessed() {
		//Given
		when(lineupsDao.findById(anyLong())).thenReturn(Optional.of(getLineupWithId()));
		
		//When
		Lineup lineup = lineupsService.getLineup(1L);
		
		//Then
		assertEquals(getLineupWithId(), lineup);
		verify(lineupsDao).findById(1L);
	}
	
	@Test
	void testGetLineupFailed() {
		//Given
		when(lineupsDao.findById(anyLong())).thenReturn(Optional.ofNullable(null));
		
		//Then
		assertThrows(LineupNotFoundException.class, () -> {
			//When
			lineupsService.getLineup(1L);
		});
		
		verify(lineupsDao).findById(1L);
	}
	
	//DELETE

	@Test
	void testDeleteLineupSuccessed() {
		//Given
		when(lineupsDao.findById(anyLong())).thenReturn(Optional.of(getLineupWithId()));
		
		//When
		Lineup lineup = lineupsService.deleteLineup(1L);
		
		//Then
		assertEquals(getLineupWithId(), lineup);
		
		verify(lineupsDao).findById(1L);
		verify(lineupsDao).deleteById(1L);
	}
	
	@Test
	void testDeleteLineupFailed() {
		//Given
		when(lineupsDao.findById(anyLong())).thenReturn(Optional.ofNullable(null));
		
		//Then
		assertThrows(LineupNotFoundException.class, () -> {
			//When
			lineupsService.deleteLineup(1L);
			
		});
		
		verify(lineupsDao).findById(1L);
		verify(lineupsDao, never()).deleteById(anyLong());
	}

}
