package com.example.bookmyshow.repository;

import com.example.bookmyshow.model.ShowSeat;

import jakarta.persistence.LockModeType;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowSeatRepository extends JpaRepository<ShowSeat, Long> {
    List<ShowSeat> findByShowId(Long showId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM ShowSeat s WHERE s.id IN :showSeatIds AND s.status = 'AVAILABLE'")
    List<ShowSeat> findAndLockAvailableSeats(@Param("showSeatIds") List<Long> showSeatIds);
} 