package com.karikkans.reservation.util;

import lombok.extern.slf4j.Slf4j;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static java.time.temporal.ChronoUnit.MINUTES;

@Slf4j
public class TimeUtil {
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("H[:mm]");


    public static Time convertToSqlTime(LocalTime time) {
        return Time.valueOf(time);
    }

    public static Time convertToSqlTime(String time) {
        try {
            long value = dateFormat.parse(time).getTime();
            return new Time(value);

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static LocalTime convertToLocalTime(Time time) {
        return time.toLocalTime().truncatedTo(MINUTES);
    }

    public static LocalTime convertToLocalTime(String time) {
        return LocalTime.parse(time, dateTimeFormatter).truncatedTo(MINUTES);
    }

    public static Timestamp getCurrentTime() {
        return new Timestamp(System.currentTimeMillis());
    }
}
