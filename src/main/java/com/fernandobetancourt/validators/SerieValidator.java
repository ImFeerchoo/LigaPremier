package com.fernandobetancourt.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fernandobetancourt.exceptions.AddingSerieException;
import com.fernandobetancourt.exceptions.SerieNotFoundException;
import com.fernandobetancourt.model.dao.ISeriesDao;
import com.fernandobetancourt.model.entity.Serie;

@Component
public class SerieValidator {
	
	@Autowired
	private ISeriesDao seriesDao;
	
	public boolean isSerieValidToSave(Serie serie) {		
		if(		serie == null ||
				serie.getName() == null || serie.getName().trim().equals("")
				) {
			throw new AddingSerieException("Serie is not valid to save");
		}
		return true;
	}

	public boolean isSerieValidToUpdate(Serie serie) {
		
		if(		serie == null ||
				serie.getSerieId() == null || serie.getSerieId() < 1 ||
				serie.getName() == null || serie.getName().trim().equals("")
				) {
			throw new AddingSerieException("Serie is not valid to save");
		}
		
		serieExists(serie.getSerieId());
		
		return true;
	}
	
	public Serie serieExists(Long serieId) {
		return seriesDao.findById(serieId).orElseThrow(() -> {
			var sb = new StringBuilder();
			sb.append("Serie with id ").append(serieId).append(" has not been found.");
			throw new SerieNotFoundException(sb.toString());
		});
	}
}
