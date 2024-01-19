package com.karikkans.reservation.exception;

import com.karikkans.reservation.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

import static com.karikkans.reservation.util.ReservationUtil.buildErrorResponse;
import static java.util.Collections.singletonList;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response<String>> handleValidationExceptions(MethodArgumentNotValidException e) {
        log.error("Invalid inputs received in request body", e);
        List<String> errors = new ArrayList<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            errors.add(errorMessage);
        });
        return ResponseEntity
                .badRequest()
                .body(buildErrorResponse(errors));
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Response<String>> handleValidationExceptions(HttpMessageNotReadableException e) {
        log.error("Error occurred while reading request body", e);
        return ResponseEntity
                .badRequest()
                .body(buildErrorResponse(singletonList(e.getCause().getMessage())));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<String>> handleProcessingExceptions(Exception e) {
        log.error("Error occurred while processing request", e);
        return ResponseEntity
                .ok()
                .body(buildErrorResponse(singletonList("Error occurred while processing request Error: " + e.getMessage())));
    }

    @ExceptionHandler(ReservationException.class)
    public ResponseEntity<Response<String>> handleCustomValidationExceptions(ReservationException e) {
        log.error("Error occurred while processing request", e);
        return ResponseEntity
                .ok()
                .body(buildErrorResponse(singletonList(e.getMessage())));
    }
}