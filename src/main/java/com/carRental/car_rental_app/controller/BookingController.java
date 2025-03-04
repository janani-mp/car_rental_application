package com.carRental.car_rental_app.controller;

import com.carRental.car_rental_app.entity.Booking;
import com.carRental.car_rental_app.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
//@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // GET: All bookings with optional pagination & sorting
    @GetMapping
    public ResponseEntity<?> getAllBookings(@RequestParam(value = "page", required = false) Integer page,
                                            @RequestParam(value = "size", required = false) Integer size,
                                            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
                                            @RequestParam(value = "direction", defaultValue = "asc") String direction) {
        if (page != null && size != null) {
            Page<Booking> bookingsPage = bookingService.getAllBookings(page, size, sortBy, direction);
            return ResponseEntity.ok(bookingsPage);
        } else {
            List<Booking> bookings = bookingService.getAllBookings();
            return ResponseEntity.ok(bookings); // ✅ Returns ALL bookings if no pagination params are provided
        }
    }

    // GET: Booking by ID
    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
        Booking booking = bookingService.getBookingById(id);
        return booking != null ? ResponseEntity.ok(booking) : ResponseEntity.notFound().build();
    }

    // GET: Bookings with pagination
    @GetMapping("/paginated")
    public ResponseEntity<Page<Booking>> getBookingsWithPagination(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(bookingService.getBookingsWithPagination(page, size));
    }

    // GET: Bookings with sorting
    @GetMapping("/sorted")
    public ResponseEntity<List<Booking>> getBookingsWithSorting(@RequestParam String sortBy,
                                                                @RequestParam(defaultValue = "asc") String direction) {
        return ResponseEntity.ok(bookingService.getBookingsWithSorting(sortBy, direction));
    }

    // GET: Find bookings by start date (JPQL usage)
    @GetMapping("/search-by-start-date")
    public ResponseEntity<List<Booking>> findByStartDate(@RequestParam String startDate) {
        LocalDate date = LocalDate.parse(startDate); // Convert String to LocalDate
        return ResponseEntity.ok(bookingService.findByStartDate(date));
    }


    // POST: Create booking
    @PostMapping(consumes = "application/json")
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.createBooking(booking));
    }

    // PUT: Update booking by ID
    @PutMapping("/{id}")
    public ResponseEntity<Booking> updateBooking(@PathVariable Long id, @RequestBody Booking updatedBooking) {
        return ResponseEntity.ok(bookingService.updateBooking(id, updatedBooking));
    }

    // DELETE: Booking by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBookingById(id);
        return ResponseEntity.ok("Booking deleted successfully");
    }

    //JPQL - search by range
    @GetMapping("/search-by-date-range")
    public ResponseEntity<List<Booking>> findBookingsBetweenDates(@RequestParam String startDate, @RequestParam String endDate) {
        // ResponseEntity.ok(bookingService.findBookingsBetweenDates(LocalDate.parse(startDate), LocalDate.parse(endDate)));
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        List<Booking> bookings = bookingService.findBookingsBetweenDates(start, end);
        return ResponseEntity.ok(bookings);
    }

    // JPA: Find bookings by total amount
    @GetMapping("/search-by-total-amount")
    public ResponseEntity<List<Booking>> findByTotalAmount(@RequestParam("amount") BigDecimal amount) {
        List<Booking> bookings = bookingService.findByTotalAmount(amount);
        return ResponseEntity.ok(bookings);
    }


}

