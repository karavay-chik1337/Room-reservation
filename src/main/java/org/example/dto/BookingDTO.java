package org.example.dto;

import java.time.LocalDateTime;

public record BookingDTO(int userId, int roomId,
                         LocalDateTime startTime, LocalDateTime endTime) {
}
