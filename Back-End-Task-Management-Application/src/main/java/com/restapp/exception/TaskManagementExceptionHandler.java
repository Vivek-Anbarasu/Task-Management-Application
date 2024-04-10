package com.restapp.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TaskManagementExceptionHandler {
	
	    @ExceptionHandler(MethodArgumentNotValidException.class)
	    @ResponseBody
	    public ResponseEntity<List> processUnmergeException(final MethodArgumentNotValidException ex) {
	       List list = ex.getBindingResult().getAllErrors().stream()
	               .map(fieldError -> fieldError.getDefaultMessage())
	               .collect(Collectors.toList());
	        return new ResponseEntity<>(list, HttpStatus.BAD_REQUEST);
	    }
	 
	    @ExceptionHandler(BadRequest.class)
	    @ResponseStatus(HttpStatus.BAD_REQUEST)
	    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	    public ResponseEntity<ErrorResponse> badRequest(Exception ex) {
	       ErrorResponse response =  new ErrorResponse();
	       response.setCode(400);
	       response.setMessage(ex.getMessage());
	        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	    }
	    
	    @ExceptionHandler(InternalServerError.class)
	    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	    public ResponseEntity<ErrorResponse> internalServerError(Exception ex) {
	       ErrorResponse response =  new ErrorResponse();
	       response.setCode(500);
	       response.setMessage(ex.getMessage());
	        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	    
	    @ExceptionHandler(NotFound.class)
	    @ResponseStatus(HttpStatus.NOT_FOUND)
	    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	    public ResponseEntity<ErrorResponse> notFound(Exception ex) {
	       ErrorResponse response =  new ErrorResponse();
	       response.setCode(404);
	       response.setMessage(ex.getMessage());
	        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	    }
}
