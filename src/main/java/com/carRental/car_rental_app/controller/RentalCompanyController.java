package com.carRental.car_rental_app.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.carRental.car_rental_app.entity.Booking;
import com.carRental.car_rental_app.entity.RentalCompany;
import com.carRental.car_rental_app.entity.Vehicle;
import com.carRental.car_rental_app.service.RentalCompanyService;

@RestController
@RequestMapping("/api/rentalcompanies")
public class RentalCompanyController {

    @Autowired
    private RentalCompanyService service;

    //  GET: All Companies → /api/rentalcompanies
    @GetMapping
    public ResponseEntity<List<RentalCompany>> getAllRentalCompanies() {
        return ResponseEntity.ok(service.getAllRentalCompanies());
    }

    //  GET: By ID → /api/rentalcompanies/{id}
    @GetMapping("/{id}")
    public ResponseEntity<RentalCompany> getRentalCompanyById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getRentalCompanyById(id));
    }

    //  POST: Create → /api/rentalcompanies
    @PostMapping
    public ResponseEntity<RentalCompany> createRentalCompany(@RequestBody RentalCompany company) {
        return ResponseEntity.ok(service.createRentalCompany(company));
    }

    //  PUT: Update by ID → /api/rentalcompanies/{id}
    @PutMapping("/{id}")
    public ResponseEntity<RentalCompany> updateRentalCompanyById(@PathVariable Long id, @RequestBody RentalCompany company) {
        return ResponseEntity.ok(service.updateRentalCompanyById(id, company));
    }

    //  DELETE: By ID → /api/rentalcompanies/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRentalCompanyById(@PathVariable Long id) {
        service.deleteRentalCompanyById(id);
        return ResponseEntity.ok("Deleted Successfully");
    }

    //  GET: Pagination → /api/rentalcompanies/pagination?page=0&size=5
    @GetMapping("/pagination")
    public ResponseEntity<Page<RentalCompany>> getRentalCompaniesWithPagination(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(service.getRentalCompaniesWithPagination(page, size));
    }

    //  GET: Sorting → /api/rentalcompanies/sorting?sortBy=name&direction=asc
    @GetMapping("/sorting")
    public ResponseEntity<List<RentalCompany>> getRentalCompaniesWithSorting(@RequestParam String sortBy, @RequestParam String direction) {
        return ResponseEntity.ok(service.getRentalCompaniesWithSorting(sortBy, direction));
    }

    //  GET: By Location (JPQL) → /api/rentalcompanies/byLocation?location=NYC
    @GetMapping("/byLocation")
    public ResponseEntity<List<RentalCompany>> getRentalCompaniesByLocation(@RequestParam String location) {
        return ResponseEntity.ok(service.getRentalCompaniesByLocation(location));
    }

    //JPQL - count vehicles per company
//    @GetMapping("/vehicle-count")
//    public ResponseEntity<List<Object[]>> getVehicleCountPerCompany() {
//        List<Object[]> counts = service.countVehiclesPerCompany();
//        return ResponseEntity.ok(counts);
//    }

    // JPA
    // PUT: Update company name by location → /api/rentalcompanies/updateByLocation
    @PutMapping("/updateByLocation")
    public ResponseEntity<String> updateCompanyNameByLocation(@RequestParam String location, @RequestParam String newName) {
        int updatedCount = service.updateCompanyNameByLocation(location, newName);
        return updatedCount > 0 ?
                ResponseEntity.ok(updatedCount + " company(s) updated successfully.") :
                ResponseEntity.ok("No companies found for the given location.");
    }


    //RELATIONS

    // Get all vehicles for a rental company
@GetMapping("/{id}/vehicles")
public ResponseEntity<List<Vehicle>> getVehiclesByRentalCompany(@PathVariable Long id) {
    return ResponseEntity.ok(service.getVehiclesByRentalCompany(id));
}

    // Add a vehicle to a rental company
    @PostMapping("/{id}/vehicles")
    public ResponseEntity<Vehicle> addVehicleToRentalCompany(
            @PathVariable Long id, 
            @RequestBody Vehicle vehicle) {
        return ResponseEntity.ok(service.addVehicleToRentalCompany(id, vehicle));
    }



    // many-to-many : rental company <--> booking

    // Get all bookings for a rental company (through vehicles)
    @GetMapping("/{companyId}/bookings")
    public ResponseEntity<List<Booking>> getCompanyBookings(@PathVariable Long companyId) {
        return ResponseEntity.ok(service.getCompanyBookings(companyId));
    }

    // JPQL: Find bookings for a rental company in date range
    @GetMapping("/{companyId}/bookings-by-date")
    public ResponseEntity<List<Booking>> getCompanyBookingsByDateRange(
            @PathVariable Long companyId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return ResponseEntity.ok(service.getCompanyBookingsByDateRange(companyId, start, end));
    }

    // JPA: Get rental company booking statistics
    @GetMapping("/{companyId}/booking-stats")
    public ResponseEntity<Map<String, Object>> getCompanyBookingStats(@PathVariable Long companyId) {
        return ResponseEntity.ok(service.getCompanyBookingStats(companyId));
    }


    //sorting
    @GetMapping("/{companyId}/bookings/sorted")
    public ResponseEntity<List<Booking>> getCompanyBookingsSorted(
        @PathVariable Long companyId,
        @RequestParam String sortBy,
        @RequestParam(defaultValue = "asc") String direction) {
    
        return ResponseEntity.ok(service.getCompanyBookingsSorted(companyId, sortBy, direction));
    }

    //pagination
    @GetMapping("/{companyId}/bookings/paginated")
    public ResponseEntity<Page<Booking>> getCompanyBookingsPaginated(
            @PathVariable Long companyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(service.getCompanyBookingsPaginated(companyId, page, size));
    }

}
