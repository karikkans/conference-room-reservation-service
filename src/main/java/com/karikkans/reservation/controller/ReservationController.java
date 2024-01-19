package com.karikkans.reservation.controller;

import com.karikkans.reservation.annotation.TimeFormatValidator;
import com.karikkans.reservation.model.Reservation;
import com.karikkans.reservation.model.ReservationRequest;
import com.karikkans.reservation.model.Response;
import com.karikkans.reservation.model.RoomAvailabilityResponse;
import com.karikkans.reservation.service.ReservationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/v1/conference-room")
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping("/reserve")
    public ResponseEntity<Response<Reservation>> reserveRoom(@RequestBody @Valid ReservationRequest reservationRequest) {
        log.info("Request received for reservation :: {}", reservationRequest);
        return ResponseEntity.ok(reservationService.reserveRoom(reservationRequest));
    }

    @GetMapping("/listAvailableRooms")
    public ResponseEntity<Response<RoomAvailabilityResponse>> checkAvailableRooms(@RequestParam @TimeFormatValidator String startTime, @RequestParam @TimeFormatValidator String endTime) {
        log.info("Request received for list schedules :: Start time: {} :: End time: {}", startTime, endTime);
        return ResponseEntity.ok(reservationService.checkAvailableRooms(startTime, endTime));
    }
}
