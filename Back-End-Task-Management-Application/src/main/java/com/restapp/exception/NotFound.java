package com.restapp.exception;

public class NotFound extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3764963575075721886L;

	public NotFound(String message) {
		super(message);
	}
}
