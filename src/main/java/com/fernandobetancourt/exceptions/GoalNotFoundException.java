package com.fernandobetancourt.exceptions;

public class GoalNotFoundException extends InformationNotFoundException {

	private static final long serialVersionUID = 1L;

	public GoalNotFoundException(String message) {
		super(message);
	}

}
