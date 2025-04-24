package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserInnerDTO(@Size(min = 4) @Schema(description = "Имя", example = "Дмитрий") String name,
                           @Size(min = 4) @Schema(description = "Фамилия", example = "Караваев") String surname,
                           @Email @Schema(description = "Адрес почты", example = "dmitriy@mail.ru") String email,
                           @Size(min = 4) @Schema(description = "Отдел", example = "Финансовый") String department) {
}
