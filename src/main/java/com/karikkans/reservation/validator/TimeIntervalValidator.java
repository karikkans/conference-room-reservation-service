package com.karikkans.reservation.validator;

import com.karikkans.reservation.config.ReservationConfig;
import com.karikkans.reservation.exception.ReservationException;
import com.karikkans.reservation.model.AvailabilityRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.time.LocalTime;

import static com.karikkans.reservation.constant.Constants.TIME_INTERVAL_ERROR_MESSAGE;
import static com.karikkans.reservation.constant.Constants.TIME_QUARTER_ERROR_MESSAGE;
import static com.karikkans.reservation.util.TimeUtil.convertToLocalTime;
import static java.time.temporal.ChronoUnit.MINUTES;

@Slf4j
@Component
@RequiredArgsConstructor
public class TimeIntervalValidator implements Validator<AvailabilityRequest> {

    private final ReservationConfig reservationConfig;

    @Override
    public void validate(AvailabilityRequest request) {
        log.info("Validating booking interval");
        int allowedTimeInterval = reservationConfig.getAllowedTimeInterval();
        LocalTime startTime = convertToLocalTime(request.getStartTime());
        LocalTime endTime = convertToLocalTime(request.getEndTime());

        if (startTime.getMinute() % allowedTimeInterval != 0 || endTime.getMinute() % allowedTimeInterval != 0) {
            log.warn(TIME_QUARTER_ERROR_MESSAGE);
            throw new ReservationException(TIME_QUARTER_ERROR_MESSAGE);
        }

        if (startTime.until(endTime, MINUTES) % allowedTimeInterval != 0) {
            String errorMessage = MessageFormat.format(TIME_INTERVAL_ERROR_MESSAGE, allowedTimeInterval);
            log.warn(errorMessage);
            throw new ReservationException(errorMessage);
        }
    }
}
