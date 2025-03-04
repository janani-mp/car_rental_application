package com.carRental.car_rental_app.service;

import com.carRental.car_rental_app.entity.Vehicle;
import com.carRental.car_rental_app.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
//@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    // Create a new vehicle
    public Vehicle createVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    // Get all vehicles
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    // Get vehicle by ID
    public Optional<Vehicle> getVehicleById(Long id) {
        return vehicleRepository.findById(id);
    }

    // Update vehicle by ID
    public Vehicle updateVehicle(Long id, Vehicle updatedVehicle) {
        return vehicleRepository.findById(id)
                .map(vehicle -> {
                    vehicle.setMake(updatedVehicle.getMake());
                    vehicle.setModel(updatedVehicle.getModel());
                    vehicle.setRegistrationNumber(updatedVehicle.getRegistrationNumber());
                    vehicle.setRatePerDay(updatedVehicle.getRatePerDay());
                    vehicle.setAvailable(updatedVehicle.isAvailable());
                    return vehicleRepository.save(vehicle);
                })
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + id));
    }

    // Delete vehicle by ID
    public void deleteVehicle(Long id) {
        vehicleRepository.deleteById(id);
    }


    // Pagination only
    public Page<Vehicle> getVehiclesPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return vehicleRepository.findAll(pageable);
    }

    // Sorting only
    public List<Vehicle> getVehiclesSorted(String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        return vehicleRepository.findAll(sort);
    }

    // JPQL: Find vehicles by registration number pattern
    public List<Vehicle> getVehiclesByRegistrationPattern(String pattern) {
        return vehicleRepository.findByRegistrationPattern(pattern);
    }

    // JPQL: Top 3 cheapest vehicle prices
    public List<Vehicle> findTop3CheapestAvailableVehicles() {
        return vehicleRepository.findTop3CheapestAvailableVehicles();
    }

    // JPQL : Get vehicles available from a specific rental company and price limit
    public List<Vehicle> getAvailableVehiclesByCompanyAndPrice(Long companyId, double price) {
        return vehicleRepository.findAvailableVehiclesByCompanyAndPrice(companyId, price);
    }

    // JPA
    @Transactional
    public void deleteByMake(String make) {
        vehicleRepository.deleteByMake(make);
    }
}


