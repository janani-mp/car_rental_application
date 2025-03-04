package com.carRental.car_rental_app.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
//@AllArgsConstructor
@Builder
public class RentalCompany {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String location;

    @Column(unique = true)
    private String email;

    public RentalCompany(Long id, String name, String location, String email) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}


/*

{
  "name": "City Rentals",
  "location": "Chicago",
  "email": "info@cityrentals.com"
}
```

---
#

|-------------------|------------|----------------------------------------------|
| Get All           | GET        | `/api/rentalcompanies`                      |    done
| Get By ID         | GET        | `/api/rentalcompanies/{id}`                 |    done
| Create            | POST       | `/api/rentalcompanies`                      |    done
| Update By ID      | PUT        | `/api/rentalcompanies/{id}`                 |    done
| Delete By ID      | DELETE     | `/api/rentalcompanies/{id}`                 |    done
| Delete By Name    | DELETE     | `/api/rentalcompanies/deleteByName/{name}`  |
| Pagination        | GET        | `/api/rentalcompanies/pagination?page=0&size=5` | done
| Sorting           | GET        | `/api/rentalcompanies/sorting?sortBy=name&direction=asc` | done
| Find By Location  | GET        | `/api/rentalcompanies/byLocation?location=NYC` |  done

 */