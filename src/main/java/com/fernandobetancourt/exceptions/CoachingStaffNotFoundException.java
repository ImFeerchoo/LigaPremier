package com.fernandobetancourt.exceptions;

public class CoachingStaffNotFoundException extends InformationNotFoundException {

	private static final long serialVersionUID = 1L;

	public CoachingStaffNotFoundException(String message) {
		super(message);
	}

}
