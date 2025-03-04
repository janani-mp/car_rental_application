package com.carRental.car_rental_app.repository;

import com.carRental.car_rental_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // JPQL Query: Find users with names containing a keyword (case-insensitive)
    @Query("SELECT u FROM User u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<User> findUsersByNameContainingIgnoreCase(String keyword);


    //Prevent Duplicate Entries (Phone Number & Email)
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);

    //JPA - DELETE BY NAME
    //Spring Data JPA (Minimal Boilerplate)
    //Spring automatically understands SELECT * FROM user WHERE name = ?
    Optional<User> findByName(String name);

}


