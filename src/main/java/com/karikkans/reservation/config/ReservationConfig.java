package com.karikkans.reservation.config;

import com.karikkans.reservation.model.Schedule;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;
import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "reservation")
public class ReservationConfig {
    int allowedTimeInterval;
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime minimumStartTime;
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime maximumEndTime;
    private Map<String, Schedule> maintenanceSchedule;
}
