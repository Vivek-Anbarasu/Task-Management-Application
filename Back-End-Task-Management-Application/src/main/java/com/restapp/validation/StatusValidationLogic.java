package com.restapp.validation;

import java.util.Arrays;
import java.util.List;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StatusValidationLogic implements ConstraintValidator<StatusValidator, String> {

	@Override
	public boolean isValid(String status, ConstraintValidatorContext context) {
		List<String> list = Arrays.asList(new String[]{"To Do","In Progress","Done"});
        return list.contains(status);
	}

}
