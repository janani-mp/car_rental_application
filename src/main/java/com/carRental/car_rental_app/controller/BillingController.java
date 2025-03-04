//package com.carRental.car_rental_app.controller;
//
//import com.carRental.car_rental_app.dto.BillingDTO;
//import com.carRental.car_rental_app.service.BillingService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/billing")
//@RequiredArgsConstructor
//public class BillingController {
//
//    private final BillingService billingService;
//
//    public BillingController(BillingService billingService) {
//        this.billingService = billingService;
//    }
//
//    @PostMapping("/generate/{bookingId}")
//    public ResponseEntity<BillingDTO> generateBill(@PathVariable Long bookingId) {
//        BillingDTO bill = billingService.generateBill(bookingId);
//        return ResponseEntity.ok(bill);
//    }
//}
