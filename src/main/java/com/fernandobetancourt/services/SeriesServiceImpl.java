package com.fernandobetancourt.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fernandobetancourt.exceptions.AddingSerieException;
import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.SerieNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.dao.ISeriesDao;
import com.fernandobetancourt.model.entity.Serie;

@Service
public class SeriesServiceImpl implements ISeriesService {
	
	@Autowired
	private ISeriesDao seriesDao;
	
	//GET

	@Override
	public List<Serie> getAllSeries() {
		return this.seriesDao.findAll();
	}

	@Override
	public Serie getSerieById(Long id) throws InformationNotFoundException {
		return this.seriesDao.findById(id).orElseThrow(() -> {
			StringBuilder sb = new StringBuilder().append("Serie with id ").append(id).append(" has not been found");
			throw new SerieNotFoundException(sb.toString());
		});
	}
	
	//POST

	@Override
	public Serie addSerie(Serie serie) throws WritingInformationException {
		
		if(!isSerieValidToSave(serie)) {
			StringBuilder sb = new StringBuilder("Serie is not valid to save");
			throw new AddingSerieException(sb.toString());
		}
		
		return this.seriesDao.save(serie);
	}
	
	//PUT
	public Serie updateSerie(Serie serie) throws InformationNotFoundException, WritingInformationException{
		
		if(!this.isSerieValidToUpdate(serie)) {
			throw new AddingSerieException("Serie is not valid to save");
		}
		
		this.seriesDao.findById(serie.getSerieId()).orElseThrow(() -> {
			StringBuilder sb = new StringBuilder().append("Serie eith id ").append(serie.getSerieId()).append(" has not been found");
			throw new SerieNotFoundException(sb.toString());
		});
		
		return this.seriesDao.save(serie);
	}
	
	//DELETE

	@Override
	public Serie deleteSerie(Long id) throws InformationNotFoundException {
		
		Serie serieRecovered = this.seriesDao.findById(id).orElseThrow(() -> {
			StringBuilder sb = new StringBuilder().append("Serie with id ").append(id).append(" has not been found");
			throw new SerieNotFoundException(sb.toString());
		});
		
		this.seriesDao.deleteById(id);
		
		return serieRecovered;
	}

	//VALIDATIONS
	
	@Override
	public boolean isSerieValidToSave(Serie serie) {
		
		if(		serie == null ||
				serie.getName() == null || serie.getName().trim().equals("")
				) {
			
			return false;
		}
		return true;
	}

	public boolean isSerieValidToUpdate(Serie serie) {
		
		if(		serie == null ||
				serie.getSerieId() == null || serie.getSerieId() < 1 ||
				serie.getName() == null || serie.getName().trim().equals("")
				) {
			
			return false;
		}
		return true;
	}

}
