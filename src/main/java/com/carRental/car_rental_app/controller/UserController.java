package com.carRental.car_rental_app.controller;

import com.carRental.car_rental_app.entity.User;
import com.carRental.car_rental_app.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /*
    //  Create a new user (POST)
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }
     */

    //  Create a new user (POST) with duplicate check handling
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            return ResponseEntity.ok(userService.createUser(user));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //  Get all users (GET)
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    //  Get user by ID (GET)
    @GetMapping("/{id}")
    public ResponseEntity<Optional<User>> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // 📝 Update user by ID (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<Optional<User>> updateUserById(
            @PathVariable Long id,
            @RequestBody User updatedUser
    ) {
        return ResponseEntity.ok(userService.updateUserById(id, updatedUser));
    }

    // Delete user by ID (DELETE)
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
//        userService.deleteUserById(id);
//        return ResponseEntity.noContent().build();
//    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userService.deleteUserById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    // 🧭 Pagination (GET)
    @GetMapping("/paginated")
    public ResponseEntity<Page<User>> getPaginatedUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return ResponseEntity.ok(userService.getPaginatedUsers(page, size));
    }

    // 📑 Sorting (GET)
    @GetMapping("/sorted")
    public ResponseEntity<List<User>> getSortedUsers(@RequestParam(defaultValue = "name") String sortBy) {
        return ResponseEntity.ok(userService.getSortedUsers(sortBy));
    }

    // 🧭📑 Combined Pagination & Sorting (GET)
    @GetMapping("/paginated-sorted")
    public ResponseEntity<Page<User>> getPaginatedAndSortedUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "name") String sortBy
    ) {
        return ResponseEntity.ok(userService.getPaginatedAndSortedUsers(page, size, sortBy));
    }


    //JPQL
    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsersByName(@RequestParam String keyword) {
        return ResponseEntity.ok(userService.searchUsersByName(keyword));
    }

    // JPA : Find users by exact name (GET)
    @GetMapping("/by-name/{name}")
    public ResponseEntity<User> getUserByName(@PathVariable String name) {
        User user = userService.findByName(name);
        return ResponseEntity.ok(user);
    }

}
