package com.example.bookmyshow.service;

import com.example.bookmyshow.enums.SeatStatus;
import com.example.bookmyshow.model.*;
import com.example.bookmyshow.repository.*;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ShowService {

    private final ShowSeatRepository showSeatRepository;
    private final ShowRepository showRepository;
    private final MovieRepository movieRepository;
    private final HallRepository hallRepository;
    private final SeatRepository seatRepository;
    private final TheatreRepository theatreRepository;

    public ShowService(ShowSeatRepository showSeatRepository,
                      ShowRepository showRepository,
                      MovieRepository movieRepository,
                      HallRepository hallRepository,
                      SeatRepository seatRepository,
                      TheatreRepository theatreRepository) {
        this.showSeatRepository = showSeatRepository;
        this.showRepository = showRepository;
        this.movieRepository = movieRepository;
        this.hallRepository = hallRepository;
        this.seatRepository = seatRepository;
        this.theatreRepository = theatreRepository;
    }

    public Movie addMovie(String name, String language, String genre) {
        Movie movie = new Movie();
        movie.setName(name);
        movie.setLanguage(language);
        movie.setGenre(genre);
        return movieRepository.save(movie);
    }

    @Transactional
    public Show createShow(Long movieId, Long hallId, LocalDateTime showTime) {
        Show show = new Show();
        show.setMovie(movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found")));
        show.setHall(hallRepository.findById(hallId)
                .orElseThrow(() -> new RuntimeException("Hall not found")));
        show.setShowTime(showTime);

        // Save the show first to get its ID
        Show savedShow = showRepository.save(show);

        // Create ShowSeats for the hall associated with the show
        List<ShowSeat> showSeats = createShowSeatsForHall(savedShow);

        // Save all ShowSeats
        showSeatRepository.saveAll(showSeats);

        return savedShow; // Return the saved show
    }

    private List<ShowSeat> createShowSeatsForHall(Show show) {
        List<Seat> seats = seatRepository.findByHallId(show.getHall().getId());
        return seats.stream().map(seat -> {
            ShowSeat showSeat = new ShowSeat();
            showSeat.setShow(show);
            showSeat.setSeat(seat);
            showSeat.setStatus(SeatStatus.AVAILABLE);
            return showSeat;
        }).collect(Collectors.toList());
    }

    public void listShows(Long movieId, String city) {
        List<Theatre> theatres = theatreRepository.findByCity(city);
        theatres.forEach(theatre -> {
            List<Hall> halls = hallRepository.findByTheatreId(theatre.getId());
            halls.forEach(hall -> {
                List<Show> shows = showRepository.findByHallIdAndMovieId(hall.getId(), movieId);
                shows.forEach(show -> log.info("Show: {} in hall: {} at time: {}", show.getMovie().getName(), show.getHall().getName(), show.getShowTime()));
            });
        });
    }

    public void listSeats(Long showId) {
        List<ShowSeat> showSeats = showSeatRepository.findByShowId(showId);
        showSeats.forEach(showSeat -> log.info("Seat: {} in show: {} is {}", showSeat.getSeat().getId(), showSeat.getShow().getId(), showSeat.getStatus()));
    }

    // public List<Show> getShowsByMovie(Long movieId) {
    //     return showRepository.findByMovieId(movieId);
    // }
}