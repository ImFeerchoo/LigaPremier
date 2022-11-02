package com.fernandobetancourt.services;

import static com.fernandobetancourt.Data.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.fernandobetancourt.exceptions.AddingSerieException;
import com.fernandobetancourt.exceptions.SerieNotFoundException;
import com.fernandobetancourt.model.dao.ISeriesDao;
import com.fernandobetancourt.model.entity.Serie;

@SpringBootTest
class SeriesServiceImplTest {

	@MockBean
	private ISeriesDao seriesDao;

	@Autowired
	private ISeriesService seriesService;

	// GET

	@Test
	void testGetAllSeriesSuccessed() {
		// Given
		when(seriesDao.findAll()).thenReturn(SERIES_WITH_ID);

		// When
		List<Serie> series = seriesService.getAllSeries();

		// Then
		assertFalse(series.isEmpty());
		assertEquals(5, series.size());
		assertTrue(series.contains(SERIES_WITH_ID.get(0)));

		verify(seriesDao).findAll();
	}

	@Test
	void testGetAllSeriesEmptyList() {
		// Given
		when(seriesDao.findAll()).thenReturn(Collections.emptyList());

		// When
		List<Serie> series = seriesService.getAllSeries();

		// Then
		assertTrue(series.isEmpty());
		assertEquals(0, series.size());

		verify(seriesDao).findAll();
	}

	@Test
	void testGetSerieByIdSuccessed() {
		// Given

		when(seriesDao.findById(anyLong())).thenReturn(Optional.of(getSerieByIdBreakingReference(1L)));

		// When

		Serie serie = seriesService.getSerieById(1L);

		// Then
		assertEquals(1L, serie.getSerieId());
		assertEquals("Serie A", serie.getName());

		verify(seriesDao).findById(1L);
	}

	@Test
	void testGetSerieByIdFailed() {
		// Given

		when(seriesDao.findById(anyLong())).thenReturn(Optional.ofNullable(null));

		// Then
		assertThrows(SerieNotFoundException.class, () -> {
			// When
			seriesService.getSerieById(1L);
		});

		verify(seriesDao).findById(1L);

	}

	// POST

	@Test
	void testAddSerieSuccessed() {
		// Given
		when(seriesDao.save(any(Serie.class))).then(invocation -> {
			Serie serie = invocation.getArgument(0);
			serie.setSerieId(1L);
			return serie;
		});

		// When

		Serie serie = seriesService.addSerie(getSerieWithoutIdBreakingReference(0));

		// Then

		assertEquals(1L, serie.getSerieId());
		assertEquals("Serie A", serie.getName());

		verify(seriesDao).save(any(Serie.class));

	}

	@Test
	void testAddSerieFailedSerieNull() {
		// Then
		assertThrows(AddingSerieException.class, () -> {
			// When
			seriesService.addSerie(null);
		});
	}

	@Test
	void testAddSerieFailedNameNull() {
		// Given
		Serie serieNameNull = getSerieWithoutIdBreakingReference(0);
		serieNameNull.setName(null);

		// Then
		assertThrows(AddingSerieException.class, () -> {
			// When
			seriesService.addSerie(serieNameNull);
		});
	}

	@Test
	void testAddSerieFailedNameEmptyString() {
		// Given
		Serie serieNameEmptyString = getSerieWithoutIdBreakingReference(0);
		serieNameEmptyString.setName("");

		// Then
		assertThrows(AddingSerieException.class, () -> {
			// When
			seriesService.addSerie(serieNameEmptyString);
		});
	}

	// PUT

	@Test
	void testUpdateSerieSuccessed() {
		// Given
		when(seriesDao.findById(anyLong())).thenReturn(Optional.of(getSerieByIdBreakingReference(1L)));
		when(seriesDao.save(any(Serie.class))).then(invocation -> {
			return invocation.getArgument(0);
		});

		Serie serie = getSerieByIdBreakingReference(1L);
		serie.setName("Serie Z");

		// When
		serie = seriesService.updateSerie(serie);

		// Then
		assertEquals(1L, serie.getSerieId());
		assertEquals("Serie Z", serie.getName());

		verify(seriesDao).findById(1L);
		verify(seriesDao).save(any(Serie.class));
	}

	@Test
	void testUpdateSerieFailedSerieNull() {
		// Then
		assertThrows(AddingSerieException.class, () -> {
			// When
			seriesService.updateSerie(null);
		});
	}

	@Test
	void testUpdateSerieFailedSerieIdNull() {
		// Then
		assertThrows(AddingSerieException.class, () -> {
			// When
			seriesService.updateSerie(getSerieWithoutIdBreakingReference(0));
		});
	}

	@Test
	void testUpdateSerieFailedSerieIdLessThan1() {
		// Then
		assertThrows(AddingSerieException.class, () -> {
			// When
			seriesService.updateSerie(SERIE_WITH_ID_LESS_THAN_1);
		});
	}

	@Test
	void testUpdateSerieFailedNameNull() {
		// Given
		Serie serieNameNull = getSerieByIdBreakingReference(1L);
		serieNameNull.setName(null);

		// Then
		assertThrows(AddingSerieException.class, () -> {
			// When
			seriesService.updateSerie(serieNameNull);
		});
	}

	@Test
	void testUpdateSerieFailedNameEmptyString() {
		//Given
		Serie serieNameEmptyString = getSerieByIdBreakingReference(1L);
		serieNameEmptyString.setName("");
		
		// Then
		assertThrows(AddingSerieException.class, () -> {
			// When
			seriesService.updateSerie(serieNameEmptyString);
		});
	}
	
	@Test
	void testUpdateSerieFailedSerieDoesNotExists() {
		//Given
		when(seriesDao.findById(anyLong())).thenReturn(Optional.ofNullable(null));
		
		// Then
		assertThrows(SerieNotFoundException.class, () -> {
			// When
			seriesService.updateSerie(getSerieByIdBreakingReference(1L));
		});
		
		verify(seriesDao).findById(1L);
	}

	// DELETE

	@Test
	void testDeleteSerieSuccessed() {
		//Given
		when(seriesDao.findById(anyLong())).thenReturn(Optional.of(getSerieByIdBreakingReference(1L)));
		
		//When
		Serie serie = seriesService.deleteSerie(1L);
		
		//Then
		assertEquals(1L, serie.getSerieId());
		assertEquals("Serie A", serie.getName());
		
		verify(seriesDao).findById(1L);
		verify(seriesDao).deleteById(1L);
	}
	
	@Test
	void testDeleteSerieFailed() {
		//Given
		when(seriesDao.findById(anyLong())).thenReturn(Optional.ofNullable(null));
		
		//Then
		assertThrows(SerieNotFoundException.class, () -> {
			//When
			seriesService.deleteSerie(1L);
		});
		
		verify(seriesDao).findById(1L);
	}

}
