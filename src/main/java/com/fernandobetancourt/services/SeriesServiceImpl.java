package com.fernandobetancourt.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.SerieNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.dao.ISeriesDao;
import com.fernandobetancourt.model.entity.Serie;
import com.fernandobetancourt.validators.SerieValidator;

@Service
public class SeriesServiceImpl implements ISeriesService {
	
	@Autowired
	private SerieValidator serieValidator;
	
	@Autowired
	private ISeriesDao seriesDao;
	
	//GET

	@Override
	public List<Serie> getAllSeries() {
		List<Serie> series = seriesDao.findAll();
		if(series.isEmpty()) throw new SerieNotFoundException("There are not series available");
		return series;
	}
	
	@Override
	public Serie getSerieById(Long id) throws InformationNotFoundException {
		return serieValidator.serieExists(id);
	}
	
	//POST
	
	@Override
	public Serie addSerie(Serie serie) throws WritingInformationException {
		serieValidator.isSerieValidToSave(serie);
		return this.seriesDao.save(serie);
	}
	
	//PUT
	
	public Serie updateSerie(Serie serie) throws InformationNotFoundException, WritingInformationException{
		serieValidator.isSerieValidToUpdate(serie);
		return this.seriesDao.save(serie);
	}
	
	//DELETE
	
	@Override
	public Serie deleteSerie(Long id) throws InformationNotFoundException {
		Serie serieRecovered = serieValidator.serieExists(id);
		this.seriesDao.deleteById(id);
		return serieRecovered;
	}

}
