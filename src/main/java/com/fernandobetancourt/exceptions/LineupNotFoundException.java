package com.fernandobetancourt.exceptions;

public class LineupNotFoundException extends InformationNotFoundException {

	private static final long serialVersionUID = 1L;
	
	public LineupNotFoundException(String message) {
		super(message);
	}

}
