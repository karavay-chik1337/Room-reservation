package org.example.dto;

import java.time.LocalDateTime;

public record BookingInnerDTO(int userId, int roomId,
                              LocalDateTime startTime, LocalDateTime endTime) {
}
