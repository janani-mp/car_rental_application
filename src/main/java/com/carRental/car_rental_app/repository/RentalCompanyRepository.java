package com.carRental.car_rental_app.repository;

import com.carRental.car_rental_app.entity.RentalCompany;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalCompanyRepository extends JpaRepository<RentalCompany, Long> {

    // JPQL Query
    @Query("SELECT rc FROM RentalCompany rc WHERE rc.location = :location")
    List<RentalCompany> findByLocation(String location);

    // JPA : Update company name by location
    @Modifying
    @Query("UPDATE RentalCompany rc SET rc.name = :newName WHERE rc.location = :location")
    int updateByLocation(@Param("location") String location, @Param("newName") String newName);




}