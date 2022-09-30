package com.fernandobetancourt.exceptions;

public class ScoreboardNotFoundException extends InformationNotFoundException {

	private static final long serialVersionUID = 1L;
	
	public ScoreboardNotFoundException(String message) {
		super(message);
	}

}
