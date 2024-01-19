package com.karikkans.reservation.service;

import com.karikkans.reservation.entity.ReservationEntity;
import com.karikkans.reservation.entity.RoomEntity;
import com.karikkans.reservation.enums.ResponseStatus;
import com.karikkans.reservation.model.Response;
import com.karikkans.reservation.model.RoomAvailabilityResponse;
import com.karikkans.reservation.repository.ReservationRepository;
import com.karikkans.reservation.repository.RoomRepository;
import com.karikkans.reservation.validator.MaintenanceScheduleValidator;
import com.karikkans.reservation.validator.MaximumEndTimeValidator;
import com.karikkans.reservation.validator.MinimumStartTimeValidator;
import com.karikkans.reservation.validator.OverlappingScheduleValidator;
import com.karikkans.reservation.validator.TimeIntervalValidator;
import com.karikkans.reservation.validator.TimeRangeValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.karikkans.reservation.util.TestUtils.buildReservationEntity;
import static com.karikkans.reservation.util.TestUtils.buildRoomEntity;
import static com.karikkans.reservation.util.TestUtils.convertToSqlTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReservationEntityServiceImplTest {

    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private RoomRepository roomRepository;
    @Mock
    private TimeIntervalValidator timeIntervalValidator;
    @Mock
    private MinimumStartTimeValidator minimumStartTimeValidator;
    @Mock
    private MaximumEndTimeValidator maximumEndTimeValidator;
    @Mock
    private TimeRangeValidator timeRangeValidator;
    @Mock
    private MaintenanceScheduleValidator maintenanceScheduleValidator;
    @Mock
    private OverlappingScheduleValidator overlappingScheduleValidator;

    @InjectMocks
    ReservationServiceImpl reservationService;

    @Test
    public void should_return_available_rooms_when_start_and_end_time_is_passed() {
        // Given
        String startTime = "12:15";
        String endTime = "12:15";
        List<RoomEntity> allRooms = new ArrayList<>(Arrays.asList(buildRoomEntity(100L, "Hermes", 10),
                buildRoomEntity(110L, "Athena", 5)));
        when(roomRepository.findAllByOrderByCapacityAsc()).thenReturn(allRooms);


        // When
        Response<RoomAvailabilityResponse> availabilityResponseResponse = reservationService.checkAvailableRooms(startTime, endTime);

        // Then
        assertNotNull(availabilityResponseResponse);
        assertNotNull(availabilityResponseResponse.getData());
        assertEquals(ResponseStatus.SUCCESS, availabilityResponseResponse.getStatus());
        assertEquals(2, availabilityResponseResponse.getData().getAvailableRooms().size());

    }

    @Test
    public void should_return_no_rooms_when_no_rooms_are_available_for_reservation() {
        // Given
        String startTime = "12:15";
        String endTime = "12:30";
        List<RoomEntity> allRooms = Arrays.asList(buildRoomEntity(100L, "Hermes", 10),
                buildRoomEntity(110L, "Athena", 5));

        List<ReservationEntity> reservedRooms = Arrays.asList(buildReservationEntity(200L, convertToSqlTime(startTime), convertToSqlTime(endTime), 4, allRooms.get(0)),
                buildReservationEntity(200L, convertToSqlTime(startTime), convertToSqlTime(endTime), 4, allRooms.get(1)));

        when(reservationRepository.findOverlappingBookings(any(), any())).thenReturn(reservedRooms);
        when(roomRepository.findAllByOrderByCapacityAsc()).thenReturn(allRooms);


        // When
        Response<RoomAvailabilityResponse> availabilityResponseResponse = reservationService.checkAvailableRooms(startTime, endTime);

        // Then
        assertNotNull(availabilityResponseResponse);
        assertEquals(ResponseStatus.SUCCESS, availabilityResponseResponse.getStatus());
        assertEquals("No rooms are available for the requested time slot (12:15 - 12:30)", availabilityResponseResponse.getMessage());
    }
}