package com.carRental.car_rental_app.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.carRental.car_rental_app.entity.Vehicle;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    // JPQL Method: Find vehicles with registration number pattern
    @Query("SELECT v FROM Vehicle v WHERE v.registrationNumber LIKE %:pattern%")
    List<Vehicle> findByRegistrationPattern(String pattern);

    //JPQL : Top 3 cheapest vehicle prices
    @Query("SELECT v FROM Vehicle v WHERE v.available = true ORDER BY v.ratePerDay ASC LIMIT 3")
    List<Vehicle> findTop3CheapestAvailableVehicles();

    // JPQL : Get vehicles available from a specific rental company and price limit
    @Query("SELECT v FROM Vehicle v WHERE v.available = true AND v.rentalCompany.id = :companyId AND v.ratePerDay <= :price")
    List<Vehicle> findAvailableVehiclesByCompanyAndPrice(@Param("companyId") Long companyId, @Param("price") double price);

    // JPA
    void deleteByMake(String make);  // Returns the count of deleted records

    //RELATION

    // JPQL: Find available vehicles not booked in date range
    @Query("SELECT v FROM Vehicle v WHERE v.available = true AND NOT EXISTS (" +
           "SELECT b FROM Booking b WHERE b.vehicle = v AND " +
           "(b.startDate <= :endDate AND b.endDate >= :startDate))")  // Removed extra opening parenthesis
    List<Vehicle> findAvailableVehicles(@Param("startDate") LocalDate startDate, 
                                       @Param("endDate") LocalDate endDate);

    // JPQL: Count bookings per vehicle
    @Query("SELECT v, COUNT(b) FROM Vehicle v LEFT JOIN v.bookings b GROUP BY v")
    List<Object[]> countBookingsPerVehicle();

}