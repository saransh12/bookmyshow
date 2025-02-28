package com.example.bookmyshow.model;

import lombok.Data;

import jakarta.persistence.*;
import java.util.List;

@Data
@Entity
public class Hall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "theatre_id")
    private Theatre theatre;

    @OneToMany(mappedBy = "hall")
    private List<Seat> seats;

    @OneToMany(mappedBy = "hall")
    private List<Show> shows;
} 