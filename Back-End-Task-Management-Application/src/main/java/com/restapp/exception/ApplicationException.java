package com.restapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ApplicationException extends ResponseStatusException{
	
	private static final long serialVersionUID = -8894660499453090939L;

	public ApplicationException(HttpStatus status, String reason) {
		super(status, reason);
	}
}
