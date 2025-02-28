package com.example.bookmyshow.command;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import com.example.bookmyshow.model.Theatre;
import com.example.bookmyshow.service.BookingService;
import com.example.bookmyshow.service.ShowService;
import com.example.bookmyshow.service.TheatreService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ShellComponent
@AllArgsConstructor
public class BookMyShowCommands {
    private TheatreService theatreService;
    private ShowService showService;
    private BookingService bookingService;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    @ShellMethod("Add a theatre")
    public void addTheatre(String theatreName, String city) {
        theatreService.addTheatre(theatreName, city);
    }

    @ShellMethod("List all theatres in city")
    public void listTheatres(String city) {
        theatreService.listTheatres(city);
    }

    @ShellMethod("Add a hall")
    public void addHall(Long theatreId, String hallName) {
        theatreService.addHall(theatreId, hallName);
        log.info("Adding hall: {} in theatre: {}", hallName, theatreId);
    }

    @ShellMethod("Add seats to a hall")
    public void addSeats(Long hallId, int numberOfSeats, String seatType) {
        theatreService.addSeats(hallId, numberOfSeats, seatType);
        log.info("Adding {} seats of type {} to hall: {}", numberOfSeats, seatType, hallId);
    }

    @ShellMethod("Add a movie")
    public void addMovie(String movieName, String language, String genre) {
        showService.addMovie(movieName, language, genre);
        log.info("Adding movie: {} in language: {} and genre: {}", movieName, language, genre);
    }

    @ShellMethod("Add a show")
    public void addShow(Long movieId, Long hallId, @ShellOption(arity = 2) String[] showTime) {
        String formattedTime = String.join(" ", showTime);
        log.info("Show time: {}", formattedTime);
        LocalDateTime showDateTime = LocalDateTime.parse(formattedTime, FORMATTER);
        log.info("Show date time: {}", showDateTime);
        showService.createShow(movieId, hallId, showDateTime);
        log.info("Adding show: {} for movie: {} in hall: {} at time: {}", movieId, hallId, showDateTime);
    }

    @ShellMethod("List all shows for a movie in a city")
    public void listShows(Long movieId, String city) {
        showService.listShows(movieId, city);
        log.info("Listing shows for movie: {} in city: {}", movieId, city);
    }

    @ShellMethod("List all seats for a show")
    public void listSeats(Long showId) {
        showService.listSeats(showId);
        log.info("Listing seats for show: {}", showId);
    }

    @ShellMethod("List all bookings")
    public void listBookings(@ShellOption(arity = -1) List<Long> bookingIds) {
        log.info("Listing all bookings: {}", bookingIds);
    }

    //Issue - How to pass list of longs as a single argument?
    @ShellMethod("Make reservation")
    public void makeReservation(Long showId, String customerName, @ShellOption(arity = -1) List<Long> seatIds) {
        // bookingService.bookSeats(showId, seatIds, customerName);
        log.info("Making reservation for show: {} for customer: {} with seat ids: {}", showId, customerName, seatIds);
    }
}