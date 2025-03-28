package com.carRental.car_rental_app.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.carRental.car_rental_app.entity.Booking;
import com.carRental.car_rental_app.entity.Vehicle;
import com.carRental.car_rental_app.repository.BookingRepository;
import com.carRental.car_rental_app.repository.VehicleRepository;

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


    //RELATION vehicle <--> booking

     @Autowired
    private BookingRepository bookingRepository;

    public List<Booking> getVehicleBookings(Long vehicleId) {
        Vehicle vehicle = getVehicleById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
        return vehicle.getBookings();
    }

    // public Booking createBookingForVehicle(Long vehicleId, Booking booking) {
    //     Vehicle vehicle = getVehicleById(vehicleId)
    //             .orElseThrow(() -> new RuntimeException("Vehicle not found"));
        
    //     // Check if vehicle is available
    //     if (!vehicle.isAvailable()) {
    //         throw new RuntimeException("Vehicle is not available for booking");
    //     }

    //     booking.setVehicle(vehicle);
    //     return bookingRepository.save(booking);
    // }

    public Booking createBookingForVehicle(Long vehicleId, Booking booking) {
        Vehicle vehicle = getVehicleById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
        
        // Enhanced availability check
        if (!vehicle.isAvailable()) {
            throw new RuntimeException("Vehicle with ID " + vehicleId + " is not available for booking");
        }
        
        // Additional check for date conflicts
        if (bookingRepository.existsByVehicleAndDateRange(
                vehicle, 
                booking.getStartDate(), 
                booking.getEndDate())) {
            throw new RuntimeException("Vehicle is already booked for the selected dates");
        }
    
        booking.setVehicle(vehicle);
        return bookingRepository.save(booking);
    }

    // JPQL: Find available vehicles in date range
    public List<Vehicle> findAvailableVehicles(LocalDate startDate, LocalDate endDate) {
        return vehicleRepository.findAvailableVehicles(startDate, endDate);
    }

    // JPA: Count bookings per vehicle
    public List<Object[]> findVehiclesWithBookingCount() {
        return vehicleRepository.countBookingsPerVehicle();
    }
}
