package com.example.bookmyshow.repository;

import com.example.bookmyshow.model.Hall;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HallRepository extends JpaRepository<Hall, Long> {
    List<Hall> findByTheatreId(Long theatreId);
} 