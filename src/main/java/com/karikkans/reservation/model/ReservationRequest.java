package com.karikkans.reservation.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(NON_EMPTY)
public class ReservationRequest extends AvailabilityRequest {
    @Min(value = 1, message = "One or more attendees required to reserve room")
    @Max(value = 20, message = "No rooms available for the requested number of people")
    private int numberOfParticipants;
}
