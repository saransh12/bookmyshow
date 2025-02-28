package com.example.bookmyshow.model;

import lombok.Data;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import com.example.bookmyshow.enums.BookingStatus;

@Data
@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;
    private LocalDateTime bookingTime;
    private Double totalAmount;
    private String customerName;

    @ManyToOne
    @JoinColumn(name = "show_id")
    private Show show;

    @OneToMany(mappedBy = "booking")
    private List<ShowSeat> showSeats; // Reference to ShowSeat instead of Ticket
}