package com.karikkans.reservation.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.karikkans.reservation.annotation.TimeFormatValidator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(NON_EMPTY)
public class AvailabilityRequest {
    @NotNull(message = "Start time is required to proceed with reservation")
    @TimeFormatValidator(message = "Start time is not in 24 hours format")
    private String startTime;
    @NotNull(message = "End time is required to proceed with reservation")
    @TimeFormatValidator(message = "End time is not in 24 hours format")
    private String endTime;
}
