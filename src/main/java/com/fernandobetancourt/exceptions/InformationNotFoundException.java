package com.fernandobetancourt.exceptions;

public class InformationNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public InformationNotFoundException(String message) {
		super(message);
	}

}
