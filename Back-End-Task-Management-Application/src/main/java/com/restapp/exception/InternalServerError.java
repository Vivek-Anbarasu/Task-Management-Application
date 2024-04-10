package com.restapp.exception;

public class InternalServerError extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3764963575075721886L;

	public InternalServerError(String message) {
		super(message);
	}
}
