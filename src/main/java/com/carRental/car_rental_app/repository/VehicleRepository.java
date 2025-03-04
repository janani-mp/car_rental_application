package com.carRental.car_rental_app.repository;

import com.carRental.car_rental_app.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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
}


