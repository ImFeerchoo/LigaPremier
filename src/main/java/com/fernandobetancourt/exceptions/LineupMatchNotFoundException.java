package com.fernandobetancourt.exceptions;

public class LineupMatchNotFoundException extends InformationNotFoundException {

	private static final long serialVersionUID = 1L;
	
	public LineupMatchNotFoundException(String message) {
		super(message);
	}

}
