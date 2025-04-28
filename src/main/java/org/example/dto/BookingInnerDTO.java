package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;

import java.time.LocalDateTime;

public record BookingInnerDTO(@Min(value = 1) @Schema(description = "Id пользователя", example = "1") int userId,
                              @Min(value = 1) @Schema(description = "Id комнаты", example = "1") int roomId,
                              @Schema(description = "Начало бронирования", example = "2025-05-25T17:30:00.001Z") LocalDateTime startTime,
                              @Schema(description = "Конец бронирования", example = "2025-05-25T18:00:00.001Z") LocalDateTime endTime) {
}
