package com.example.BookingSystem.controller;

import com.example.BookingSystem.dto.CreateSlotRequest;
import com.example.BookingSystem.enums.SlotStatus;
import com.example.BookingSystem.model.Slot;
import com.example.BookingSystem.repository.SlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/slots")
@RequiredArgsConstructor
public class SlotController {

    private final SlotRepository slotRepository;

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> testAdmin() {
        return ResponseEntity.ok("ADMIN OK");
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Slot> createSlot(@RequestBody CreateSlotRequest request) {

        System.out.println("\n Admin Sloat Api ");

        Slot slot = new Slot();
        slot.setStartTime(request.startTime());
        slot.setEndTime(request.endTime());
        slot.setStatus(SlotStatus.AVAILABLE);

        return ResponseEntity.ok(slotRepository.save(slot));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<Slot>> getSlots() {
        return ResponseEntity.ok(slotRepository.findAll());
    }
}
