package com.karikkans.reservation.util;

import com.karikkans.reservation.entity.ReservationEntity;
import com.karikkans.reservation.entity.RoomEntity;
import com.karikkans.reservation.enums.ResponseStatus;
import com.karikkans.reservation.model.Reservation;
import com.karikkans.reservation.model.ReservationRequest;
import com.karikkans.reservation.model.Response;
import com.karikkans.reservation.model.Room;
import com.karikkans.reservation.model.RoomAvailabilityResponse;
import com.karikkans.reservation.model.Schedule;
import lombok.extern.slf4j.Slf4j;

import java.sql.Time;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.karikkans.reservation.enums.ResponseStatus.ERROR;
import static com.karikkans.reservation.enums.ResponseStatus.SUCCESS;
import static com.karikkans.reservation.util.TimeUtil.convertToLocalTime;
import static com.karikkans.reservation.util.TimeUtil.convertToSqlTime;
import static com.karikkans.reservation.util.TimeUtil.getCurrentTime;
import static java.util.Collections.emptyList;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

@Slf4j
public class ReservationUtil {

    public static ReservationEntity buildReservationEntity(ReservationRequest reservationRequest, RoomEntity roomEntity) {
        return ReservationEntity.builder()
                .startTime(convertToSqlTime(reservationRequest.getStartTime()))
                .endTime(convertToSqlTime(reservationRequest.getEndTime()))
                .numberOfParticipants(reservationRequest.getNumberOfParticipants())
                .createdOn(getCurrentTime())
                .modifiedOn(getCurrentTime())
                .room(roomEntity)
                .build();
    }

    public static List<Reservation> buildReservations(List<ReservationEntity> reservations) {
        if (isEmpty(reservations)) {
            return emptyList();
        }

        return reservations.stream()
                .map(ReservationUtil::buildReservation)
                .collect(Collectors.toList());
    }

    public static <T> Response<T> buildSuccessResponse(ResponseStatus status, String message) {
        return Response.<T>builder()
                .status(status)
                .message(message)
                .build();
    }

    public static <T> Response<T> buildSuccessResponse(T data, String message) {
        Response<T> response = buildSuccessResponse(data);
        response.setMessage(message);
        return response;
    }

    public static <T> Response<T> buildSuccessResponse(T data) {
        return Response.<T>builder()
                .data(data)
                .status(SUCCESS)
                .build();
    }

    public static <T> Response<T> buildErrorResponse(List<String> errors) {
        return Response.<T>builder()
                .status(ERROR)
                .errors(errors)
                .build();
    }

    public static <T> Response<T> buildErrorResponse(List<String> errors, String message) {
        return Response.<T>builder()
                .status(ERROR)
                .errors(errors)
                .message(message)
                .build();
    }

    public static Reservation buildReservation(String room, Time startTime, Time endTime) {
        return Reservation.builder()
                .room(buildRoom(room))
                .schedule(buildSchedule(startTime, endTime))
                .build();
    }

    public static Reservation buildReservation(ReservationEntity reservationEntity) {
        return Reservation.builder()
                .room(buildRoom(reservationEntity.getRoom()))
                .schedule(buildSchedule(reservationEntity.getStartTime(), reservationEntity.getEndTime()))
                .build();
    }

    public static Schedule buildSchedule(Time startTime, Time endTime) {
        return Schedule.builder()
                .startTime(convertToLocalTime(startTime))
                .endTime(convertToLocalTime(endTime))
                .build();
    }

    public static RoomAvailabilityResponse buildRoomAvailabilityResponse(List<Room> rooms) {
        return RoomAvailabilityResponse.builder()
                .availableRooms(rooms)
                .build();
    }

    public static List<Room> buildRooms(List<RoomEntity> rooms) {
        return rooms.stream()
                .map(ReservationUtil::buildRoom)
                .collect(Collectors.toList());
    }

    private static Room buildRoom(String room) {
        return Room.builder()
                .name(room)
                .build();
    }

    private static Room buildRoom(RoomEntity room) {
        return Room.builder()
                .roomId(room.getRoomId())
                .name(room.getName())
                .capacity(room.getCapacity())
                .build();
    }

    public static Optional<Room> checkOverlappingSchedulesAndFindSuitableRoom(List<Reservation> overlappingReservations,
                                                                              int numberOfParticipants, List<Room> rooms) {
        log.info("Checking for overlapping rooms");
        if (isEmpty(overlappingReservations)) {
            return findSuitableRoom(numberOfParticipants, rooms);
        }

        List<Room> reservedRooms = findReservedRooms(overlappingReservations);
        log.debug("Reserved rooms: {}", reservedRooms);
        rooms.removeAll(reservedRooms);
        return findSuitableRoom(numberOfParticipants, rooms);
    }

    public static List<Room> findAvailableRooms(List<Reservation> overlappingReservations, List<Room> rooms) {
        if (isEmpty(overlappingReservations)) {
            return rooms;
        }

        List<Room> reservedRooms = findReservedRooms(overlappingReservations);
        rooms.removeAll(reservedRooms);
        return rooms;
    }

    private static Optional<Room> findSuitableRoom(int numberOfParticipants, List<Room> rooms) {
        return rooms.stream()
                .filter(room -> numberOfParticipants <= room.getCapacity())
                .findFirst();
    }


    private static List<Room> findReservedRooms(List<Reservation> overlappingBookings) {
        return overlappingBookings.stream()
                .map(Reservation::getRoom)
                .distinct()
                .collect(Collectors.toList());
    }
}
