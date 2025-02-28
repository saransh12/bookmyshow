package com.example.bookmyshow.service;

import com.example.bookmyshow.enums.SeatType;
import com.example.bookmyshow.model.Hall;
import com.example.bookmyshow.model.Seat;
import com.example.bookmyshow.model.Theatre;
import com.example.bookmyshow.repository.HallRepository;
import com.example.bookmyshow.repository.SeatRepository;
import com.example.bookmyshow.repository.TheatreRepository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TheatreService {
    @Autowired
    private TheatreRepository theatreRepository;

    @Autowired
    private HallRepository hallRepository;

    @Autowired
    private SeatRepository seatRepository;

    public void addTheatre(String theatreName, String city) {
        Theatre theatre = new Theatre();
        theatre.setName(theatreName);
        theatre.setCity(city);
        theatreRepository.save(theatre);
    }

    public void listTheatres(String city) {
        List<Theatre> theatres = theatreRepository.findByCity(city);
        theatres.forEach(theatre -> log.info("Theatre: {} in location: {}", theatre.getName(), theatre.getCity()));
    }

    public Hall addHall(Long theatreId, String hallName) {
        Hall hall = new Hall();
        hall.setName(hallName);
        hall.setTheatre(theatreRepository.findById(theatreId)
                .orElseThrow(() -> new RuntimeException("Theatre not found")));
        return hallRepository.save(hall);
    }

    public List<Seat> addSeats(Long hallId, int numberOfSeats, String seatType) {
        List<Seat> seats = new ArrayList<>();
        for (int i = 0; i < numberOfSeats; i++) {
            Seat seat = new Seat();
            seat.setSeatType(SeatType.valueOf(seatType.toUpperCase())); // Assuming SeatType is an enum
            seat.setHall(hallRepository.findById(hallId)
                    .orElseThrow(() -> new RuntimeException("Hall not found")));
            seats.add(seatRepository.save(seat));
        }
        return seats;
    }

}
