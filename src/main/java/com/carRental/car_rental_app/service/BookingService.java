package com.carRental.car_rental_app.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.carRental.car_rental_app.entity.Booking;
import com.carRental.car_rental_app.repository.BookingRepository;

@Service
//@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    // Get all bookings with pagination & sorting
    public Page<Booking> getAllBookings(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return bookingRepository.findAll(pageable);
    }

    // Get all bookings without pagination
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    // Get booking by ID
    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id).orElse(null);
    }

    // PAGINATION: Fetch bookings with pagination (page & size only)
    public Page<Booking> getBookingsWithPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size); // Pagination without sorting
        return bookingRepository.findAll(pageable);
    }

    // SORTING: Fetch all bookings sorted by a specified field
    public List<Booking> getBookingsWithSorting(String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        return bookingRepository.findAll(sort); // Sorting without pagination
    }

    // Create a new booking
    public Booking createBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    // Update booking by ID
    public Booking updateBooking(Long id, Booking updatedBooking) {
        return bookingRepository.findById(id).map(booking -> {
            booking.setStartDate(updatedBooking.getStartDate());
            booking.setEndDate(updatedBooking.getEndDate());
            booking.setTotalAmount(updatedBooking.getTotalAmount());
            booking.setInsuranceIncluded(updatedBooking.isInsuranceIncluded());
            booking.setUser(updatedBooking.getUser());
            booking.setVehicle(updatedBooking.getVehicle());
            return bookingRepository.save(booking);
        }).orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
    }

    // Delete booking by ID
    public void deleteBookingById(Long id) {
        bookingRepository.deleteById(id);
    }

    // JPQL: Find bookings by start date using JPQL
    public List<Booking> findByStartDate(LocalDate startDate) {
        return bookingRepository.findByStartDate(startDate);
    }

    //Feature to be noted
    //JPQL: Find bookings between dates
    public List<Booking> findBookingsBetweenDates(LocalDate startDate, LocalDate endDate) {
        return bookingRepository.findBookingsBetweenDates(startDate, endDate);
    }

    // JPA: Find bookings by total amount
    public List<Booking> findByTotalAmount(BigDecimal totalAmount) {
        return bookingRepository.findByTotalAmount(totalAmount);
    }

}
