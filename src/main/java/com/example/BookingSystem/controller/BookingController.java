package com.example.BookingSystem.controller;


import com.example.BookingSystem.model.Booking;
import com.example.BookingSystem.repository.UserRepository;
import com.example.BookingSystem.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final UserRepository userRepository;

    @PostMapping("/bookings")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Booking> bookSlot(@RequestBody Map<String, Long> body) {

        Long slotId = body.get("slotId");

        Long userId = getCurrentUserId();

        Booking booking = bookingService.bookSlot(slotId, userId);

        return ResponseEntity.ok(booking);
    }

    @PostMapping("/bookings/{id}/cancel")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> cancelBookingByUser(@PathVariable Long id) {

        Long userId = getCurrentUserId();

        bookingService.cancelBooking(id, userId, false);

        return ResponseEntity.ok("Canceled Booking");
    }

    @PostMapping("/admin/bookings/{id}/cancel")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> cancelBookingByAdmin(@PathVariable Long id) {

        bookingService.cancelBooking(id, null, true);

        return ResponseEntity.ok("Canceled by admin");
    }

    private Long getCurrentUserId() {

        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();
    }
}
