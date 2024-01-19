package com.karikkans.reservation.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.sql.Time;
import java.sql.Timestamp;

import static javax.persistence.GenerationType.IDENTITY;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity(name = "reservation")
public class ReservationEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long reservationId;

    @Column(nullable = false)
    private Time startTime;

    @Column(nullable = false)
    private Time endTime;

    @Column(nullable = false)
    private int numberOfParticipants;

    @OneToOne()
    @JoinColumn(name = "room_id")
    private RoomEntity room;

    private Timestamp createdOn;
    private Timestamp modifiedOn;
}
