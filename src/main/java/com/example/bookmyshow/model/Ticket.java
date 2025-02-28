package com.example.bookmyshow.model;

import lombok.Data;

import jakarta.persistence.*;

@Data
@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @OneToOne
    @JoinColumn(name = "show_seat_id")
    private ShowSeat showSeat;
} 