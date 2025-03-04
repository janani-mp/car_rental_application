package com.carRental.car_rental_app.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String make;
    private String model;
    private String registrationNumber;
    private double ratePerDay;
    private boolean available;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "rental_company_id")
    @JsonBackReference  // Prevents recursive nesting in JSON output
    private RentalCompany rentalCompany;

    @JsonIgnore
    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public double getRatePerDay() {
        return ratePerDay;
    }

    public void setRatePerDay(double ratePerDay) {
        this.ratePerDay = ratePerDay;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public RentalCompany getRentalCompany() {
        return rentalCompany;
    }

    public void setRentalCompany(RentalCompany rentalCompany) {
        this.rentalCompany = rentalCompany;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}


/*
- Get all vehicles:         `GET /api/vehicles`             done
- Get vehicle by ID:        `GET /api/vehicles/{id}`        done
- Create vehicle:           `POST /api/vehicles`            done
- Update vehicle:           `PUT /api/vehicles/{id}`        done
- Delete vehicle by ID:     `DELETE /api/vehicles/{id}`     done
- Delete vehicles by make:  `DELETE /api/vehicles/delete-by-make/{make}`                not done
- Pagination:               `GET /api/vehicles/paginated?page=0&size=5`                 done
- Sorting:                  `GET /api/vehicles/sorted?sortBy=ratePerDay&direction=asc`  done
- JPQL search:              `GET /api/vehicles/registration-pattern?pattern=ABC`        done
 */



// ✅ Create new vehicle (POST) with sample JSON:
/*
    {
        "make": "Toyota",
        "model": "Corolla",
        "registrationNumber": "ABC123",
        "ratePerDay": 50.0,
        "available": true
    }
*/