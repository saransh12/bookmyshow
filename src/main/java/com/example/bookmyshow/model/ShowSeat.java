package com.example.bookmyshow.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.bookmyshow.enums.SeatStatus;

import jakarta.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class ShowSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "show_id")
    private Show show;

    @ManyToOne
    @JoinColumn(name = "seat_id")
    private Seat seat;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @OneToOne(mappedBy = "showSeat", cascade = CascadeType.ALL)
    private Ticket ticket;

    @Enumerated(EnumType.STRING)
    private SeatStatus status; // AVAILABLE, HELD, BOOKED
    
} 