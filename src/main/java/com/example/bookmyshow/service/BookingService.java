package com.example.bookmyshow.service;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bookmyshow.enums.BookingStatus;
import com.example.bookmyshow.enums.SeatStatus;
import com.example.bookmyshow.model.Booking;
import com.example.bookmyshow.model.Show;
import com.example.bookmyshow.model.ShowSeat;
import com.example.bookmyshow.model.Ticket;
import com.example.bookmyshow.repository.BookingRepository;
import com.example.bookmyshow.repository.ShowRepository;
import com.example.bookmyshow.repository.ShowSeatRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BookingService {
    private final ShowRepository showRepository;
    private final ShowSeatRepository showSeatRepository;
    private final BookingRepository bookingRepository;
    // private final PaymentService paymentService;
    // private final BookingEventPublisher eventPublisher;

    @Transactional
    public String bookSeats(Long showId, List<Long> seatIds, String customerName) {
        Show show = showRepository.findById(showId).orElseThrow(() -> new RuntimeException("Show not found"));

        // Find and lock all seats
        List<ShowSeat> seats = showSeatRepository.findAndLockAvailableSeats(seatIds);
        if (seats.size() != seatIds.size()) {
            throw new RuntimeException("Some seats are already booked or unavailable");
        }

        double totalAmount = calculateTotalAmount(seats);

        // Create a new booking entity
        Booking booking = new Booking();
        booking.setCustomerName(customerName);
        booking.setShow(show);
        booking.setShowSeats(seats);
        booking.setStatus(BookingStatus.PENDING); // Set initial status
        booking.setTotalAmount(totalAmount);
        bookingRepository.save(booking);

        // Mark seats as HELD
        seats.forEach(seat -> {
            seat.setStatus(SeatStatus.HELD);
            seat.setBooking(booking);
            seat.setShow(show);
        });
        showSeatRepository.saveAll(seats);

        // Try payment synchronously with timeout
        // ExecutorService executor = Executors.newSingleThreadExecutor();
        // Future<Boolean> future = executor.submit(() -> paymentService.processPayment(userId, booking.getId()));
        boolean paymentSuccess = true;
        // try {
        //     paymentSuccess = future.get(5, TimeUnit.SECONDS);
        // } catch (TimeoutException e) {
        //     // Payment taking too long, process asynchronously
        //     eventPublisher.publishPaymentEvent(new PaymentEvent(userId, booking.getId()));
        //     return "Booking pending. Payment processing...";
        // } catch (Exception e) {
        //     throw new RuntimeException("Payment error", e);
        // } finally {
        //     executor.shutdown();
        // }

        // Finalize booking
        finalizeBooking(booking, paymentSuccess);
        generateTickets(booking);

        return paymentSuccess ? "Booking confirmed, tickets generated!" : "Payment failed, seats released.";
    }

    @Transactional
    public void generateTickets(Booking booking) {
        List<ShowSeat> showSeats = booking.getShowSeats();
        showSeats.forEach(seat -> {
            Ticket ticket = new Ticket();
            ticket.setShowSeat(seat);
            ticket.setBooking(booking);
            seat.setTicket(ticket);
            showSeatRepository.save(seat);
        });
    }

    @Transactional
    public void finalizeBooking(Booking booking, boolean paymentSuccess) {
        if (paymentSuccess) {
            booking.setStatus(BookingStatus.CONFIRMED);
            booking.getShowSeats().forEach(seat -> seat.setStatus(SeatStatus.BOOKED));
        } else {
            booking.setStatus(BookingStatus.FAILED);
            booking.getShowSeats().forEach(seat -> {
                seat.setStatus(SeatStatus.AVAILABLE);
                seat.setBooking(null);
            });
            throw new RuntimeException("Payment failed, seats released.");
        }
        bookingRepository.save(booking);
        showSeatRepository.saveAll(booking.getShowSeats());
    }

    public double calculateTotalAmount(List<ShowSeat> seats) {
        return seats.stream().mapToDouble(seat -> seat.getSeat().getSeatType().getPrice()).sum();
    }
}
