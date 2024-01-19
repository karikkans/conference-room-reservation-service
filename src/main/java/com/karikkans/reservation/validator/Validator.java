package com.karikkans.reservation.validator;

public interface Validator<T> {
    void validate(T startTime);
}
