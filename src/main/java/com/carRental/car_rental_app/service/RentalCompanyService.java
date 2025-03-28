package com.carRental.car_rental_app.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.carRental.car_rental_app.entity.Booking;
import com.carRental.car_rental_app.entity.RentalCompany;
import com.carRental.car_rental_app.entity.Vehicle;
import com.carRental.car_rental_app.repository.BookingRepository;
import com.carRental.car_rental_app.repository.RentalCompanyRepository;
import com.carRental.car_rental_app.repository.VehicleRepository;

@Service
public class RentalCompanyService {

    @Autowired
    private RentalCompanyRepository repository;

    //inserted for relation
    @Autowired
    private VehicleRepository vehicleRepository;

    //many-to-many
    @Autowired
    private BookingRepository bookingRepository;

    //GET ALL
    public List<RentalCompany> getAllRentalCompanies() {
        return repository.findAll();
    }

    //GET BY ID
    public RentalCompany getRentalCompanyById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Company not found"));
    }

    //CREATE
    public RentalCompany createRentalCompany(RentalCompany company) {
        return repository.save(company);
    }

    // UPDATE BY ID
    public RentalCompany updateRentalCompanyById(Long id, RentalCompany updatedCompany) {
        RentalCompany existing = getRentalCompanyById(id);
        existing.setName(updatedCompany.getName());
        existing.setLocation(updatedCompany.getLocation());
        existing.setEmail(updatedCompany.getEmail());
        return repository.save(existing);
    }

    //DELETE BY ID
    public void deleteRentalCompanyById(Long id) {
        repository.deleteById(id);
    }

    // PAGINATION
    public Page<RentalCompany> getRentalCompaniesWithPagination(int page, int size) {
        return repository.findAll(PageRequest.of(page, size));
    }

    // SORTING
    public List<RentalCompany> getRentalCompaniesWithSorting(String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        return repository.findAll(sort);
    }

    // JPQL
    public List<RentalCompany> getRentalCompaniesByLocation(String location) {
        return repository.findByLocation(location);
    }

    //JPQL - count vehicles per company
//    public List<Object[]> countVehiclesPerCompany() {
//        return repository.countVehiclesPerCompany();
//    }

    //JPA
    //  Update name by location (Transactional at service layer)
    @Transactional
    public int updateCompanyNameByLocation(String location, String newName) {
        return repository.updateByLocation(location, newName);
    }


    //RELATIONS

    public List<Vehicle> getVehiclesByRentalCompany(Long companyId) {
    RentalCompany company = getRentalCompanyById(companyId);
    return company.getVehicles();
}

    public Vehicle addVehicleToRentalCompany(Long companyId, Vehicle vehicle) {
        RentalCompany company = getRentalCompanyById(companyId);
        vehicle.setRentalCompany(company);
        return vehicleRepository.save(vehicle); // You'll need to inject VehicleRepository
    }


    //many-to-many
    public List<Booking> getCompanyBookings(Long companyId) {
        RentalCompany company = getRentalCompanyById(companyId);
        return company.getVehicles().stream()
                .flatMap(vehicle -> vehicle.getBookings().stream())
                .toList();
    }

    // JPQL: Get bookings for company in date range
    public List<Booking> getCompanyBookingsByDateRange(Long companyId, LocalDate startDate, LocalDate endDate) {
        return bookingRepository.findByRentalCompanyAndDateRange(companyId, startDate, endDate);
    }

    // JPA: Get booking statistics for company
    public Map<String, Object> getCompanyBookingStats(Long companyId) {
        RentalCompany company = getRentalCompanyById(companyId);
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalBookings", bookingRepository.countByVehicle_RentalCompany(company));
        stats.put("totalRevenue", bookingRepository.sumTotalAmountByVehicle_RentalCompany(company));
        stats.put("avgBookingDuration", bookingRepository.avgBookingDurationByCompany(companyId));
        
        return stats;
    }

    //sorting
    public List<Booking> getCompanyBookingsSorted(Long companyId, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") 
            ? Sort.by(sortBy).descending() 
            : Sort.by(sortBy).ascending();
        
        return bookingRepository.findByVehicle_RentalCompany_Id(companyId, sort);
    }

    //pagination
    public Page<Booking> getCompanyBookingsPaginated(Long companyId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return bookingRepository.findByVehicle_RentalCompany_Id(companyId, pageable);
    }
    
}

