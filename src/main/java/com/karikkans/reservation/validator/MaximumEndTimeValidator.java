package com.karikkans.reservation.validator;

import com.karikkans.reservation.config.ReservationConfig;
import com.karikkans.reservation.exception.ReservationException;
import com.karikkans.reservation.model.AvailabilityRequest;
import com.karikkans.reservation.util.TimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.time.LocalTime;

import static com.karikkans.reservation.constant.Constants.MAXIMUM_START_TIME_ERROR_MESSAGE;

@Slf4j
@Component
@RequiredArgsConstructor
public class MaximumEndTimeValidator implements Validator<AvailabilityRequest> {

    private final ReservationConfig reservationConfig;

    @Override
    public void validate(AvailabilityRequest request) {
        log.info("Validating reservation end time ");
        LocalTime maximumEndTime = reservationConfig.getMaximumEndTime();
        LocalTime endTime = TimeUtil.convertToLocalTime(request.getEndTime());

        if (maximumEndTime.isBefore(endTime)) {
            String errorMessage = MessageFormat.format(MAXIMUM_START_TIME_ERROR_MESSAGE, maximumEndTime);
            log.warn(errorMessage);
            throw new ReservationException(errorMessage);
        }
    }
}
