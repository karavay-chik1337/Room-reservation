package org.example.dto;

import java.time.LocalDateTime;

public record BookingOuterDTO(int id, int userId, int roomId,
                              LocalDateTime startTime, LocalDateTime endTime) {
}
