package com.karikkans.reservation.validator;

import com.karikkans.reservation.exception.ReservationException;
import com.karikkans.reservation.model.AvailabilityRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

import static com.karikkans.reservation.constant.Constants.MATCHING_TIME_RANGE_ERROR_MESSAGE;
import static com.karikkans.reservation.constant.Constants.TIME_RANGE_ERROR_MESSAGE;
import static com.karikkans.reservation.util.TimeUtil.convertToLocalTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class TimeRangeValidator implements Validator<AvailabilityRequest> {

    @Override
    public void validate(AvailabilityRequest request) {
        log.info("Validating time range");
        LocalTime startTime = convertToLocalTime(request.getStartTime());
        LocalTime endTime = convertToLocalTime(request.getEndTime());
        if (startTime.isAfter(endTime)) {
            log.warn(TIME_RANGE_ERROR_MESSAGE);
            throw new ReservationException(TIME_RANGE_ERROR_MESSAGE);
        } else if (startTime.equals(endTime)) {
            log.warn(MATCHING_TIME_RANGE_ERROR_MESSAGE);
            throw new ReservationException(MATCHING_TIME_RANGE_ERROR_MESSAGE);
        }
    }
}
