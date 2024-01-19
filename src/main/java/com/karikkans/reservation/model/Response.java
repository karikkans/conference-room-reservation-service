package com.karikkans.reservation.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.karikkans.reservation.enums.ResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(NON_EMPTY)
public class Response<T> {
    private T data;
    private ResponseStatus status;
    private String message;
    List<String> errors;
}
