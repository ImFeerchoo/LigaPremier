package com.fernandobetancourt.exceptions;

public class MatchNotFoundException extends InformationNotFoundException {

	private static final long serialVersionUID = 1L;
	
	public MatchNotFoundException(String message) {
		super(message);
	}

}
