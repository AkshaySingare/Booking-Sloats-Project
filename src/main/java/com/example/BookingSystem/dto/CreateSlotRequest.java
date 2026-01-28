package com.example.BookingSystem.dto;

import java.time.LocalDateTime;

public record CreateSlotRequest(
        LocalDateTime startTime,
        LocalDateTime endTime
) {}

