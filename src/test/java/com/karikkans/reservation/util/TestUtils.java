package com.karikkans.reservation.util;

import com.karikkans.reservation.entity.ReservationEntity;
import com.karikkans.reservation.entity.RoomEntity;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TestUtils {

    public static RoomEntity buildRoomEntity(long roomId, String name, long capacity) {
        return RoomEntity.builder()
                .roomId(roomId)
                .name(name)
                .capacity(capacity)
                .build();
    }

    public static ReservationEntity buildReservationEntity(long id, Time startTime, Time endTime, int numberOfParticipants, RoomEntity room) {
        return ReservationEntity.builder()
                .reservationId(id)
                .startTime(startTime)
                .endTime(endTime)
                .numberOfParticipants(numberOfParticipants)
                .room(room)
                .build();
    }

    public static Time convertToSqlTime(String time) {
        try {
            long value = new SimpleDateFormat("HH:mm").parse(time).getTime();
            return new Time(value);

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
