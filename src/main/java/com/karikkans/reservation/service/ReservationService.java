package com.karikkans.reservation.service;

import com.karikkans.reservation.model.Reservation;
import com.karikkans.reservation.model.ReservationRequest;
import com.karikkans.reservation.model.Response;
import com.karikkans.reservation.model.RoomAvailabilityResponse;

public interface ReservationService {

    Response<RoomAvailabilityResponse> checkAvailableRooms(String startTime, String endTime);

    Response<Reservation> reserveRoom(ReservationRequest reservationRequest);
}
