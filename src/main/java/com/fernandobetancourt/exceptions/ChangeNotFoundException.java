package com.fernandobetancourt.exceptions;

public class ChangeNotFoundException extends InformationNotFoundException {
	
	private static final long serialVersionUID = 1L;

	public ChangeNotFoundException(String message) {
		super(message);
	}

}
