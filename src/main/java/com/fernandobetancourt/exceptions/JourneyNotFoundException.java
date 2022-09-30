package com.fernandobetancourt.exceptions;

public class JourneyNotFoundException extends InformationNotFoundException {

	private static final long serialVersionUID = 1L;
	
	public JourneyNotFoundException(String message) {
		super(message);
	}
}
