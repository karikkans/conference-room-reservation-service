package com.karikkans.reservation.validator;

import com.karikkans.reservation.annotation.TimeFormatValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.isNull;

public class TimeFormatValidatorImpl implements ConstraintValidator<TimeFormatValidator, String> {

    private static final String regex = "([01]?[0-9]|2[0-3]):[0-5][0-9]";

    @Override
    public void initialize(TimeFormatValidator constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Pattern p = Pattern.compile(regex);

        if (isNull(value)) {
            return false;
        }

        Matcher m = p.matcher(value);

        return m.matches();
    }


}
