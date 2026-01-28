package com.example.BookingSystem.service;

import com.example.BookingSystem.enums.BookingStatus;
import com.example.BookingSystem.enums.SlotStatus;
import com.example.BookingSystem.exception.ResourceNotFoundException;
import com.example.BookingSystem.exception.SlotAlreadyBookedException;
import com.example.BookingSystem.model.Booking;
import com.example.BookingSystem.model.Slot;
import com.example.BookingSystem.repository.BookingRepository;
import com.example.BookingSystem.repository.SlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.access.AccessDeniedException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final SlotRepository slotRepository;
    private final BookingRepository bookingRepository;

    @Transactional
    public Booking bookSlot(Long slotId, Long userId) {

        if (bookingRepository.existsBySlotIdAndStatus(slotId, BookingStatus.ACTIVE)) {
            throw new SlotAlreadyBookedException();
        }

        Slot slot = slotRepository.findByIdForUpdate(slotId)
                .orElseThrow(() -> new RuntimeException("Slot not found"));

        slot.setStatus(SlotStatus.BOOKED);

        Booking booking = new Booking();
        booking.setSlot(slot);
        booking.setUserId(userId);
        booking.setStatus(BookingStatus.ACTIVE);
        booking.setCreatedAt(LocalDateTime.now());

        return bookingRepository.save(booking);
    }

    @Transactional
    public void cancelBooking(Long bookingId, Long userId, boolean isAdmin) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (!isAdmin && !booking.getUserId().equals(userId)) {
            throw new AccessDeniedException("Not allowed");
        }

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            return;
        }

        Slot slot = slotRepository.findByIdForUpdate(booking.getSlot().getId())
                .orElseThrow();

        booking.setStatus(BookingStatus.CANCELLED);
        slot.setStatus(SlotStatus.AVAILABLE);

        bookingRepository.save(booking);   // explicit save
    }



}
