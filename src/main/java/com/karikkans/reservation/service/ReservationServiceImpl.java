package com.karikkans.reservation.service;

import com.karikkans.reservation.entity.ReservationEntity;
import com.karikkans.reservation.entity.RoomEntity;
import com.karikkans.reservation.model.AvailabilityRequest;
import com.karikkans.reservation.model.Reservation;
import com.karikkans.reservation.model.ReservationRequest;
import com.karikkans.reservation.model.Response;
import com.karikkans.reservation.model.Room;
import com.karikkans.reservation.model.RoomAvailabilityResponse;
import com.karikkans.reservation.repository.ReservationRepository;
import com.karikkans.reservation.repository.RoomRepository;
import com.karikkans.reservation.validator.MaintenanceScheduleValidator;
import com.karikkans.reservation.validator.MaximumEndTimeValidator;
import com.karikkans.reservation.validator.MinimumStartTimeValidator;
import com.karikkans.reservation.validator.OverlappingScheduleValidator;
import com.karikkans.reservation.validator.TimeIntervalValidator;
import com.karikkans.reservation.validator.TimeRangeValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.karikkans.reservation.constant.Constants.NO_ROOMS_AVAILABLE_ERROR_MESSAGE;
import static com.karikkans.reservation.constant.Constants.RESERVATION_FAILED_ERROR_MESSAGE;
import static com.karikkans.reservation.constant.Constants.RESERVATION_SUCCESS_MESSAGE;
import static com.karikkans.reservation.enums.ResponseStatus.SUCCESS;
import static com.karikkans.reservation.util.ReservationUtil.buildErrorResponse;
import static com.karikkans.reservation.util.ReservationUtil.buildReservation;
import static com.karikkans.reservation.util.ReservationUtil.buildReservationEntity;
import static com.karikkans.reservation.util.ReservationUtil.buildReservations;
import static com.karikkans.reservation.util.ReservationUtil.buildRoomAvailabilityResponse;
import static com.karikkans.reservation.util.ReservationUtil.buildRooms;
import static com.karikkans.reservation.util.ReservationUtil.buildSuccessResponse;
import static com.karikkans.reservation.util.ReservationUtil.checkOverlappingSchedulesAndFindSuitableRoom;
import static com.karikkans.reservation.util.ReservationUtil.findAvailableRooms;
import static com.karikkans.reservation.util.TimeUtil.convertToLocalTime;
import static com.karikkans.reservation.util.TimeUtil.convertToSqlTime;
import static java.util.Collections.singletonList;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    private final TimeIntervalValidator timeIntervalValidator;
    private final MinimumStartTimeValidator minimumStartTimeValidator;
    private final MaximumEndTimeValidator maximumEndTimeValidator;
    private final TimeRangeValidator timeRangeValidator;
    private final MaintenanceScheduleValidator maintenanceScheduleValidator;
    private final OverlappingScheduleValidator overlappingScheduleValidator;


    @Override
    public Response<RoomAvailabilityResponse> checkAvailableRooms(String start, String end) {
        LocalTime startTime = convertToLocalTime(start);
        LocalTime endTime = convertToLocalTime(end);
        validateRequest(new AvailabilityRequest(start, end));
        List<ReservationEntity> overlappingReservations = reservationRepository.findOverlappingBookings(convertToSqlTime(startTime), convertToSqlTime(endTime));
        List<Reservation> reservations = buildReservations(overlappingReservations);
        List<Room> allRooms = retrieveAllRooms();
        List<Room> availableRooms = findAvailableRooms(reservations, allRooms);
        if (availableRooms.isEmpty()) {
            String message = MessageFormat.format(NO_ROOMS_AVAILABLE_ERROR_MESSAGE, startTime, endTime);
            log.info(message);
            return buildSuccessResponse(SUCCESS, message);
        }
        RoomAvailabilityResponse roomAvailabilityResponse = buildRoomAvailabilityResponse(availableRooms);
        return buildSuccessResponse(roomAvailabilityResponse);
    }

    @Override
    public Response<Reservation> reserveRoom(ReservationRequest reservationRequest) {
        validateReservationRequest(reservationRequest);
        List<ReservationEntity> overlappingReservations = reservationRepository.findOverlappingBookings(convertToSqlTime(reservationRequest.getStartTime()),
                convertToSqlTime(reservationRequest.getEndTime()));
        List<Reservation> reservations = buildReservations(overlappingReservations);
        List<Room> allRooms = retrieveAllRooms();
        Optional<Room> room = checkOverlappingSchedulesAndFindSuitableRoom(reservations, reservationRequest.getNumberOfParticipants(), allRooms);
        if (room.isEmpty()) {
            String errorMessage = MessageFormat.format(NO_ROOMS_AVAILABLE_ERROR_MESSAGE, reservationRequest.getStartTime(),
                    reservationRequest.getEndTime());
            log.warn(errorMessage);
            return buildErrorResponse(singletonList(errorMessage), RESERVATION_FAILED_ERROR_MESSAGE);
        }
        RoomEntity roomEntity = resolveRoomEntity(room.get()).orElse(null);
        ReservationEntity reservationEntity = buildReservationEntity(reservationRequest, roomEntity);
        reservationRepository.save(reservationEntity);
        String message = buildReservationMessage(reservationEntity);
        log.info(message);
        Reservation reservationResponse = buildReservation(reservationEntity.getRoom().getName(), reservationEntity.getStartTime(), reservationEntity.getEndTime());
        return buildSuccessResponse(reservationResponse, message);
    }

    private void validateReservationRequest(AvailabilityRequest request) {
        log.info("Validating reservation request");
        validateRequest(request);
        overlappingScheduleValidator.validate(request);
    }

    private void validateRequest(AvailabilityRequest request) {
        timeIntervalValidator.validate(request);
        minimumStartTimeValidator.validate(request);
        maximumEndTimeValidator.validate(request);
        timeRangeValidator.validate(request);
        maintenanceScheduleValidator.validate(request);
    }

    private static String buildReservationMessage(ReservationEntity reservationEntity) {
        log.debug("Building reservation message");
        return MessageFormat.format(RESERVATION_SUCCESS_MESSAGE, reservationEntity.getRoom().getName(), reservationEntity.getNumberOfParticipants(),
                convertToLocalTime(reservationEntity.getStartTime()), convertToLocalTime(reservationEntity.getEndTime()));
    }

    private List<Room> retrieveAllRooms() {
        List<RoomEntity> rooms = fetchAllRooms();
        return buildRooms(rooms);
    }

    @Cacheable
    private List<RoomEntity> fetchAllRooms() {
        log.debug("Fetching all rooms");
        return roomRepository.findAllByOrderByCapacityAsc();
    }

    private Optional<RoomEntity> resolveRoomEntity(Room room) {
        return fetchAllRooms().stream()
                .filter(roomEntity -> roomEntity.getRoomId() == room.getRoomId())
                .findFirst();
    }
}
