package com.carRental.car_rental_app.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonBackReference  // Prevents infinite recursion
    @JoinColumn(name = "user_id")
    @JsonIgnore // Prevents infinite recursion by ignoring user inside Booking
    private User user;

    // vehicle <--> booking
    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    @JsonBackReference("vehicle-bookings") // Match reference name
    private Vehicle vehicle;

    private LocalDate startDate;
    private LocalDate endDate;
    //private double totalAmount;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    private boolean insuranceIncluded;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public boolean isInsuranceIncluded() {
        return insuranceIncluded;
    }

    public void setInsuranceIncluded(boolean insuranceIncluded) {
        this.insuranceIncluded = insuranceIncluded;
    }


}



/*
{
  "user": { "id": 1 },
  "vehicle": { "id": 2 },
  "startDate": "2025-03-01",
  "endDate": "2025-03-10",
  "totalAmount": 500.0,
  "insuranceIncluded": true
}





| GET       | /api/bookings                          done
| GET       | /api/bookings/{id}                     done
| POST      | /api/bookings                          done
| PUT       | /api/bookings/{id}                     done
| DELETE    | /api/bookings/{id}                     done


Pagination : http://localhost:9191/api/bookings/paginated?page=3&size=1                     done
Sorting :    http://localhost:9191/api/bookings/sorted?sortBy=startDate&direction=desc      done
JPQL :       http://localhost:9191/api/bookings/search-by-start-date?startDate=2025-03-01   done
JPA :

 */


