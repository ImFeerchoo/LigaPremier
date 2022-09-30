package com.fernandobetancourt.exceptions;

public class GroupNotFoundException extends InformationNotFoundException {

	private static final long serialVersionUID = 1L;
	
	public GroupNotFoundException(String message) {
		super(message);
	}

}
