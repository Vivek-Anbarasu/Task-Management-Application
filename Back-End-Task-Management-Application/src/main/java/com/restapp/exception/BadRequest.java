package com.restapp.exception;

public class BadRequest extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3764963575075721886L;

	public BadRequest(String message) {
		super(message);
	}
}
