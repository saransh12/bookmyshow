package com.example.bookmyshow.model;

import lombok.Data;

import jakarta.persistence.*;
import java.util.List;

@Data
@Entity
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String language;
    private String genre;

    @OneToMany(mappedBy = "movie")
    private List<Show> shows;
} 