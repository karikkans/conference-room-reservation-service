package com.karikkans.reservation.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;

@Data
@ToString
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Schedule implements Comparable<Schedule> {
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @JsonFormat(pattern = "HH:[mm]")
    private LocalTime startTime;
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @JsonFormat(pattern = "HH:[mm]")
    private LocalTime endTime;

    @Override
    public int compareTo(Schedule schedule) {
        if (this.startTime.isAfter(schedule.startTime)) {
            return 1;
        } else if (this.startTime.isBefore(schedule.startTime)) {
            return -1;
        } else {
            return 0;
        }
    }
}