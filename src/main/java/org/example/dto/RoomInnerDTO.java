package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

public record RoomInnerDTO(@Size(min = 4) @Schema(description = "Название", example = "Переговорная") String name,
                           @Size(min = 4) @Schema(description = "Местоположение", example = "Воронеж") String location) {
}
