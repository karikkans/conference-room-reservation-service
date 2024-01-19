package com.karikkans.reservation.repository;

import com.karikkans.reservation.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.util.List;


@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {

    @Query("SELECT res from reservation res where  ( (res.startTime >= :startTime " +
            "AND res.startTime < :endTime ) OR ( res.endTime > :startTime AND res.endTime <= :endTime) )")
    List<ReservationEntity> findOverlappingBookings(Time startTime, Time endTime);
}
