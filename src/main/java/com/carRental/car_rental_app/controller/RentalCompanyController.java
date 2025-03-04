package com.carRental.car_rental_app.controller;

import com.carRental.car_rental_app.entity.RentalCompany;
import com.carRental.car_rental_app.service.RentalCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
