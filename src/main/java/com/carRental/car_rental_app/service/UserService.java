package com.carRental.car_rental_app.service;

import com.carRental.car_rental_app.entity.Booking;
import com.carRental.car_rental_app.entity.User;
import com.carRental.car_rental_app.repository.BookingRepository;
import com.carRental.car_rental_app.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;


    // added for relation
    @Autowired
    private final BookingRepository bookingRepository;

    public UserService(UserRepository userRepository, BookingRepository bookingRepository) {
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
    }


    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists!");
        }
        if (userRepository.existsByPhone(user.getPhone())) {
            throw new IllegalArgumentException("Phone number already exists!");
        }
        return userRepository.save(user);
    }

    // Retrieve all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Retrieve user by ID
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Update user by ID
    public Optional<User> updateUserById(Long id, User updatedUser) {
        return userRepository.findById(id).map(existingUser -> {
            existingUser.setName(updatedUser.getName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setPhone(updatedUser.getPhone());
            return userRepository.save(existingUser);
        });
    }

    // Delete user by ID
/*
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
 */
    public String deleteUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found!");
        }
        userRepository.deleteById(id);
        return "User deleted successfully!";
    }


    // Pagination (without sorting)
    public Page<User> getPaginatedUsers(int page, int size) {
        return userRepository.findAll(PageRequest.of(page, size));
    }

    // Sorting (without pagination)
    public List<User> getSortedUsers(String sortBy) {
        return userRepository.findAll(Sort.by(sortBy));
    }

    // Pagination + Sorting combined
    public Page<User> getPaginatedAndSortedUsers(int page, int size, String sortBy) {
        return userRepository.findAll(PageRequest.of(page, size, Sort.by(sortBy)));
    }

    // JPQL Query usage
    public List<User> searchUsersByName(String keyword) {
        return userRepository.findUsersByNameContainingIgnoreCase(keyword);
    }

    // JPA : Find users by exact name match
    public User findByName(String name) {
        return userRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("User not found with name: " + name));
    }

    // RELATION

    public List<Booking> getUserBookings(Long userId) {
    User user = getUserById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    return user.getBookings();
    }

    public Booking createBookingForUser(Long userId, Booking booking) {
        User user = getUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        // Set the user for the booking
        booking.setUser(user);
        
        // If you want to automatically add to user's bookings list
        user.getBookings().add(booking);
        
        // Save the booking (this will cascade if properly configured)
        return bookingRepository.save(booking);
    }
}

