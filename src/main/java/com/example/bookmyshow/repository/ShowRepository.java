package com.example.bookmyshow.repository;

import com.example.bookmyshow.model.Show;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowRepository extends JpaRepository<Show, Long> {
    List<Show> findByHallIdAndMovieId(Long id, Long movieId);
} 