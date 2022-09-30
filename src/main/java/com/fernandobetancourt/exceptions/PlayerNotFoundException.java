package com.fernandobetancourt.exceptions;

public class PlayerNotFoundException extends InformationNotFoundException {


	public PlayerNotFoundException(String message) {
		super(message);
	}
	
	private static final long serialVersionUID = 1L;
}
