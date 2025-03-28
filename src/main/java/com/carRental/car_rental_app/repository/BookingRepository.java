package com.carRental.car_rental_app.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.carRental.car_rental_app.entity.Booking;
import com.carRental.car_rental_app.entity.RentalCompany;
import com.carRental.car_rental_app.entity.Vehicle;

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



    //many-to-many

    // JPQL: Find bookings for a rental company in date range
    @Query("SELECT b FROM Booking b WHERE b.vehicle.rentalCompany.id = :companyId " +
           "AND b.startDate >= :startDate AND b.endDate <= :endDate")
    List<Booking> findByRentalCompanyAndDateRange(
            @Param("companyId") Long companyId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    // JPA: Count bookings by rental company
    long countByVehicle_RentalCompany(RentalCompany rentalCompany);

    // JPQL: Sum total amount by rental company
    @Query("SELECT SUM(b.totalAmount) FROM Booking b WHERE b.vehicle.rentalCompany = :rentalCompany")
    BigDecimal sumTotalAmountByVehicle_RentalCompany(@Param("rentalCompany") RentalCompany rentalCompany);

    // JPQL: Average booking duration by company
    @Query("SELECT AVG(DATEDIFF(b.endDate, b.startDate)) FROM Booking b " +
           "WHERE b.vehicle.rentalCompany.id = :companyId")
    Double avgBookingDurationByCompany(@Param("companyId") Long companyId);


    //relation
    //sorting
    List<Booking> findByVehicle_RentalCompany_Id(Long companyId, Sort sort);

    //pagination
    Page<Booking> findByVehicle_RentalCompany_Id(Long companyId, Pageable pageable);



    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END " +
       "FROM Booking b WHERE b.vehicle = :vehicle " +
       "AND (b.startDate <= :endDate AND b.endDate >= :startDate)")
boolean existsByVehicleAndDateRange(
        @Param("vehicle") Vehicle vehicle,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate);
    
}




/*
HOW JPQL WORKS

1. User sends GET request to:
   /api/bookings/search-by-date-range?startDate=2025-03-01&endDate=2025-03-10

2. Controller receives request and calls BookingService.

3. Service layer handles business logic and calls BookingRepository.

4. Repository executes JPQL query and retrieves bookings.

5. Data returns to service ➡ controller ➡ HTTP response to the user.

 */