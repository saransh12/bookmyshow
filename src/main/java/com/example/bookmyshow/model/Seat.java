package com.example.bookmyshow.model;

import lombok.Data;

import com.example.bookmyshow.enums.SeatType;

import jakarta.persistence.*;

@Data
@Entity
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private SeatType seatType;

    @ManyToOne
    @JoinColumn(name = "hall_id")
    private Hall hall;
} 