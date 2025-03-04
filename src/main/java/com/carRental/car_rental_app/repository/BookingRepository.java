package com.carRental.car_rental_app.repository;

import com.carRental.car_rental_app.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // JPQL: Find bookings by startDate (example of column directly in Booking)
    @Query("SELECT b FROM Booking b WHERE b.startDate = :startDate")
    List<Booking> findByStartDate(LocalDate startDate);

    // JPQL: Find bookings between start and end dates
    @Query("SELECT b FROM Booking b WHERE b.startDate >= :startDate AND b.endDate <= :endDate ")
    List<Booking> findBookingsBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // JPA Method: Find bookings by totalAmount
    List<Booking> findByTotalAmount(BigDecimal totalAmount);

}

























/*
HOW JPQL WORKS

1. User sends GET request to:
   /api/bookings/search-by-date-range?startDate=2025-03-01&endDate=2025-03-10

2. Controller receives request and calls BookingService.

3. Service layer handles business logic and calls BookingRepository.

4. Repository executes JPQL query and retrieves bookings.

5. Data returns to service ➡️ controller ➡️ HTTP response to the user.

 */


