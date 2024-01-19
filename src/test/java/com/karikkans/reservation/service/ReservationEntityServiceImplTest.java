package com.karikkans.reservation.service;

import com.karikkans.reservation.model.Schedule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.TreeSet;

@RunWith(MockitoJUnitRunner.class)
public class ReservationEntityServiceImplTest {

    @Test
    public void test() {
        LocalTime currentTime = LocalTime.now();
        System.out.println("Current time :: " + currentTime);
        currentTime = currentTime.truncatedTo(ChronoUnit.MINUTES);
        System.out.println("Current time after update :: " + currentTime);
    }

    @Test
    public void test_set() {
        Set<Schedule> scheduleSet = new TreeSet<>();
        scheduleSet.add(new Schedule(LocalTime.parse("14:00"), LocalTime.parse("15:30")));
        scheduleSet.add(new Schedule(LocalTime.parse("02:30"), LocalTime.parse("05:45")));
        scheduleSet.add(new Schedule(LocalTime.parse("02:00"), LocalTime.parse("05:00")));
        scheduleSet.add(new Schedule(LocalTime.parse("10:10"), LocalTime.parse("12:25")));
        scheduleSet.add(new Schedule(LocalTime.parse("07:18"), LocalTime.parse("09:40")));

        System.out.println("Schedules :: " + scheduleSet);
    }
}