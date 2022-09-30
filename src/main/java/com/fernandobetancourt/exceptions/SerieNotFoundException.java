package com.fernandobetancourt.exceptions;

public class SerieNotFoundException extends InformationNotFoundException {
	
	private static final long serialVersionUID = 1L;

	public SerieNotFoundException(String message) {
		super(message);
	}
	
}
