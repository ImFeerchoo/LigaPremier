package com.fernandobetancourt.exceptions;

public class ClubMatchNotFoundException extends InformationNotFoundException {

	private static final long serialVersionUID = 1L;
	
	public ClubMatchNotFoundException(String message) {
		super(message);
	}

}
