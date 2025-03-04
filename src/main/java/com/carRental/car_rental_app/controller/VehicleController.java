package com.carRental.car_rental_app.controller;

import com.carRental.car_rental_app.entity.Vehicle;
import com.carRental.car_rental_app.repository.VehicleRepository;
import com.carRental.car_rental_app.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/vehicles")
//@RequiredArgsConstructor -> It denies the need of manual constructor which may cause duplication error
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    // Get all vehicles
    @GetMapping
    public ResponseEntity<List<Vehicle>> getAllVehicles() {
        return ResponseEntity.ok(vehicleService.getAllVehicles());
    }

    // Get vehicle by ID: /api/vehicles/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Vehicle>> getVehicleById(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleService.getVehicleById(id));
    }

    // Create new vehicle (POST) with sample JSON:
    /*
    {
        "make": "Toyota",
        "model": "Corolla",
        "registrationNumber": "ABC123",
        "ratePerDay": 50.0,
        "available": true
    }
    */
    @PostMapping
    public ResponseEntity<Vehicle> createVehicle(@RequestBody Vehicle vehicle) {
        return ResponseEntity.ok(vehicleService.createVehicle(vehicle));
    }

    // Update vehicle by ID: /api/vehicles/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Vehicle> updateVehicle(@PathVariable Long id, @RequestBody Vehicle updatedVehicle) {
        return ResponseEntity.ok(vehicleService.updateVehicle(id, updatedVehicle));
    }

    // Delete vehicle by ID: /api/vehicles/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.noContent().build();
    }

    // Pagination: /api/vehicles/paginated?page=0&size=5
    @GetMapping("/paginated")
    public ResponseEntity<Page<Vehicle>> getVehiclesPaginated(@RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(vehicleService.getVehiclesPaginated(page, size));
    }

    // Sorting: /api/vehicles/sorted?sortBy=ratePerDay&direction=asc
    @GetMapping("/sorted")
    public ResponseEntity<List<Vehicle>> getVehiclesSorted(@RequestParam(defaultValue = "id") String sortBy,
                                                           @RequestParam(defaultValue = "asc") String direction) {
        return ResponseEntity.ok(vehicleService.getVehiclesSorted(sortBy, direction));
    }

    // JPQL search: /api/vehicles/registration-pattern?pattern=ABC
    @GetMapping("/registration-pattern")
    public ResponseEntity<List<Vehicle>> getVehiclesByRegistrationPattern(@RequestParam String pattern) {
        return ResponseEntity.ok(vehicleService.getVehiclesByRegistrationPattern(pattern));
    }

    // JPQL : Top 3 cheapest vehicle prices
    @GetMapping("/top3-cheapest")
    public ResponseEntity<List<Vehicle>> findTop3CheapestAvailableVehicles() {
        List<Vehicle> vehicles = vehicleService.findTop3CheapestAvailableVehicles();
        return ResponseEntity.ok(vehicles);
    }

    // JPQL : Get vehicles available from a specific rental company and price limit
    @GetMapping("/available-by-company-and-price")
    public ResponseEntity<List<Vehicle>> getAvailableVehiclesByCompanyAndPrice(
            @RequestParam Long companyId,
            @RequestParam double price) {

        List<Vehicle> vehicles = vehicleService.getAvailableVehiclesByCompanyAndPrice(companyId, price);
        return ResponseEntity.ok(vehicles);
    }


    // JPA
    //endpoint: /api/vehicles/delete-by-make/{make}
    @DeleteMapping("/delete-by-make/{make}")
    public ResponseEntity<Void> deleteByMake(@PathVariable String make) {
        vehicleService.deleteByMake(make);
        return ResponseEntity.noContent().build();   //204 no content for success
    }
}
