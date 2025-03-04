package com.carRental.car_rental_app.repository;

import com.carRental.car_rental_app.entity.Billing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillingRepository extends JpaRepository<Billing, Long> {
}
