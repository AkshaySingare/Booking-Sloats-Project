package com.example.BookingSystem.repository;

import com.example.BookingSystem.enums.BookingStatus;
import com.example.BookingSystem.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Find all bookings of a user
    List<Booking> findByUserId(Long userId);

    // Optional: find active booking for a slot (useful for validation)
    boolean existsBySlotIdAndStatus(Long slotId, BookingStatus status);
}
