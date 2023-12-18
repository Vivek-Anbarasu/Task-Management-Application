package com.restapp.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;

@Documented
@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
//@Constraint(validatedBy = StatusValidationLogic.class)
public @interface StatusValidator {

	String message() default "Status must be either To Do or In Progress or Done";
	
}
