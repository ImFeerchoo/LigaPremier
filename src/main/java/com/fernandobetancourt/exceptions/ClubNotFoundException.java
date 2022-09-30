package com.fernandobetancourt.exceptions;

public class ClubNotFoundException extends InformationNotFoundException {

	public ClubNotFoundException(String message) {
		super(message);
	}
	
	private static final long serialVersionUID = 1L;
	
}
