package com.carRental.car_rental_app.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String phone;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // To handle JSON serialization of bookings
    private List<Booking> bookings;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}



/*

# *API Endpoints & URLs:*
| *Operation*        | *HTTP Method* | *URL*                                        |
|----------------------|-----------------|------------------------------------------------|
| Create User          | POST            | /api/users                                  |          done
| Get All Users        | GET             | /api/users                                  |          done
| Get User by ID       | GET             | /api/users/{id}                             |          done
| Update User by ID    | PUT             | /api/users/{id}                             |          done
| Delete User by ID    | DELETE          | /api/users/{id}                             |          done - message?
| Delete User by Name  | DELETE          | /api/users/delete-by-name/{name}            |          not done
| Pagination           | GET             | /api/users/paginated?page=0&size=5          |          done
| Sorting              | GET             | /api/users/sorted?sortBy=name               |          done
| Pagination + Sorting | GET             | /api/users/paginated-sorted?page=0&size=5&sortBy=name |done
| Search Users (JPQL)  | GET             | /api/users/search?keyword=jane              |          done

 */