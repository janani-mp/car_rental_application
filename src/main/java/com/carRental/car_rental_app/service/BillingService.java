package com.carRental.car_rental_app.service;

import com.carRental.car_rental_app.dto.BillingDTO;
import com.carRental.car_rental_app.entity.Billing;
import com.carRental.car_rental_app.entity.Booking;
import com.carRental.car_rental_app.repository.BillingRepository;
import com.carRental.car_rental_app.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BillingService {

    private final BillingRepository billingRepository;
    private final BookingRepository bookingRepository;

    private static final BigDecimal TAX_RATE = BigDecimal.valueOf(0.18);
    private static final BigDecimal SURGE_MULTIPLIER = BigDecimal.valueOf(1.2);
    private static final BigDecimal LATE_RETURN_PENALTY = BigDecimal.valueOf(50.00);

    public BillingService(BillingRepository billingRepository, BookingRepository bookingRepository) {
        this.billingRepository = billingRepository;
        this.bookingRepository = bookingRepository;
    }

    @Transactional
    public BillingDTO generateBill(Long bookingId) {
        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);
        if (bookingOpt.isEmpty()) {
            throw new RuntimeException("Booking not found");
        }

        Booking booking = bookingOpt.get();
        BigDecimal baseAmount = booking.getVehicle().getRatePerDay()
                .multiply(BigDecimal.valueOf(booking.getDurationInDays()));

        BigDecimal surgeCharge = applySurgePricing(baseAmount, booking.getBookingDate());
        BigDecimal tax = baseAmount.multiply(TAX_RATE);
        BigDecimal discount = applyLoyaltyDiscount(booking);
        BigDecimal penalty = applyLateReturnPenalty(booking);
        BigDecimal totalAmount = baseAmount.add(tax).add(surgeCharge).subtract(discount).add(penalty);

        Billing billing = Billing.builder()
                .booking(booking)
                .baseAmount(baseAmount)
                .tax(tax)
                .surgeCharge(surgeCharge)
                .discount(discount)
                .penalty(penalty)
                .totalAmount(totalAmount)
                .billingDate(LocalDateTime.now())
                .isPaid(false)
                .build();

        billingRepository.save(billing);
        return new BillingDTO(
                billing.getId(),
                bookingId,
                baseAmount,
                tax,
                discount,
                surgeCharge,
                penalty,
                totalAmount,
                billing.getBillingDate(),
                billing.isPaid()
        );
    }

    private BigDecimal applySurgePricing(BigDecimal baseAmount, LocalDateTime bookingDate) {
        int hour = bookingDate.getHour();
        return (hour >= 18 && hour <= 22) ? baseAmount.multiply(SURGE_MULTIPLIER.subtract(BigDecimal.ONE)) : BigDecimal.ZERO;
    }

    private BigDecimal applyLoyaltyDiscount(Booking booking) {
        long totalBookings = bookingRepository.countByUser(booking.getUser());
        return (totalBookings > 5) ? booking.getVehicle().getRatePerDay().multiply(BigDecimal.valueOf(0.10)) : BigDecimal.ZERO;
    }

    private BigDecimal applyLateReturnPenalty(Booking booking) {
        if (booking.getReturnDate().isAfter(booking.getExpectedReturnDate())) {
            long hoursLate = Duration.between(booking.getExpectedReturnDate(), booking.getReturnDate()).toHours();
            return BigDecimal.valueOf(hoursLate).multiply(LATE_RETURN_PENALTY);
        }
        return BigDecimal.ZERO;
    }
}
