package com.karikkans.reservation.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;

import static javax.persistence.GenerationType.IDENTITY;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity(name = "room")
public class RoomEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long roomId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Long capacity;
    private Timestamp createdOn;
    private Timestamp modifiedOn;
}