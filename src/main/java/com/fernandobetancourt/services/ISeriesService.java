package com.fernandobetancourt.services;

import java.util.List;

import com.fernandobetancourt.exceptions.InformationNotFoundException;
import com.fernandobetancourt.exceptions.WritingInformationException;
import com.fernandobetancourt.model.entity.Serie;

public interface ISeriesService {

	//GET
	public abstract List<Serie> getAllSeries();
	public abstract Serie getSerieById(Long id) throws InformationNotFoundException;
	
	//POST
	public abstract Serie addSerie(Serie serie) throws WritingInformationException;
	
	//PUT
	public abstract Serie updateSerie(Serie serie) throws InformationNotFoundException, WritingInformationException;
	
	//DELETE
	public abstract Serie deleteSerie(Long id) throws InformationNotFoundException;
	
	//VALIDATIONS
	public abstract boolean isSerieValidToSave(Serie serie);
	public abstract boolean isSerieValidToUpdate(Serie serie);
	
}
