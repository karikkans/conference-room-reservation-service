package com.karikkans.reservation.annotation;

import com.karikkans.reservation.validator.TimeFormatValidatorImpl;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = TimeFormatValidatorImpl.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@SupportedValidationTarget(ValidationTarget.ANNOTATED_ELEMENT)
public @interface TimeFormatValidator {

    String message() default "Given time is not in 24 hour format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
