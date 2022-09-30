package com.fernandobetancourt.exceptions;

public class CardNotFoundException extends InformationNotFoundException{
	
	private static final long serialVersionUID = 1L;

	public CardNotFoundException(String message) {
		super(message);
	}


}
