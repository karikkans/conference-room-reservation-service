package com.karikkans.reservation.validator;

import com.karikkans.reservation.config.ReservationConfig;
import com.karikkans.reservation.exception.ReservationException;
import com.karikkans.reservation.model.AvailabilityRequest;
import com.karikkans.reservation.model.Schedule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.time.LocalTime;

import static com.karikkans.reservation.constant.Constants.MAINTENANCE_SCHEDULE_CONFLICT_ERROR_MESSAGE;
import static com.karikkans.reservation.util.TimeUtil.convertToLocalTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class OverlappingScheduleValidator implements Validator<AvailabilityRequest> {

    private final ReservationConfig reservationConfig;

    @Override
    public void validate(AvailabilityRequest request) {
        log.info("Validating maintenance schedule");
        LocalTime startTime = convertToLocalTime(request.getStartTime());
        LocalTime endTime = convertToLocalTime(request.getEndTime());
        reservationConfig.getMaintenanceSchedule().entrySet().stream()
                .filter(entry -> isOverlappingMaintenance(startTime, endTime, entry.getValue()))
                .findFirst()
                .ifPresent(entry -> {
                    String errorMessage = MessageFormat.format(MAINTENANCE_SCHEDULE_CONFLICT_ERROR_MESSAGE, entry.getKey(),
                            entry.getValue().getStartTime(), entry.getValue().getEndTime());
                    log.warn(errorMessage);
                    throw new ReservationException(errorMessage);
                });
    }

    /*
    ( (res.startTime >= :startTime " +
            "AND res.startTime < :endTime ) OR ( res.endTime > :startTime AND res.endTime <= :endTime) )
    * */
    private boolean isOverlappingMaintenance(LocalTime requestedStartTime, LocalTime requestedEndTime, Schedule maintenanceSchedule) {
        LocalTime maintenanceStart = maintenanceSchedule.getStartTime();
        LocalTime maintenanceEnd = maintenanceSchedule.getEndTime();
        return ((maintenanceStart.isAfter(requestedStartTime) || maintenanceStart.equals(requestedStartTime)) && maintenanceStart.isBefore(requestedEndTime))
                || (maintenanceEnd.isAfter(requestedStartTime) && (maintenanceEnd.equals(requestedEndTime) || maintenanceEnd.isBefore(requestedEndTime)));
    }
}
