package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.example.dto.RoomInnerDTO;
import org.example.dto.RoomOuterDTO;
import org.example.service.RoomService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
@Tag(name = "Room controller",
        description = "Этот контроллер позволяет управлять комнатами")
public class RoomController {
    private final RoomService roomService;

    @Operation(summary = "Создать комнату")
    @PostMapping
    public RoomOuterDTO create(@RequestBody @Valid RoomInnerDTO roomInnerDTO) {
        return roomService.create(roomInnerDTO);
    }

    @Operation(summary = "Показать все доступные комнаты")
    @GetMapping
    public List<RoomOuterDTO> findAllAvailable(
            @RequestParam(required = false) @Parameter(description = "Желаемое время начала бронирования." +
                    " Если оставить пустым, вернётся список всех комнат", example = "2025-05-25T18:30:00") LocalDateTime desiredTime) {
        return roomService.findAllAvailable(desiredTime);
    }

    @Operation(summary = "Найти комнату по id")
    @GetMapping("/{id}")
    public RoomOuterDTO findById(@PathVariable @Parameter(example = "1") @Valid @Min(value = 1) int id) {
        return roomService.findById(id);
    }

    @Operation(summary = "Обновить данные комнаты по id")
    @PatchMapping("/{id}")
    public RoomOuterDTO update(
            @PathVariable @Parameter(example = "1") @Valid @Min(value = 1) int id,
            @RequestBody RoomInnerDTO innerDTO
    ) {
        return roomService.update(id, innerDTO);
    }

    @Operation(summary = "Удалить комнату по id")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable @Parameter(example = "1") @Valid @Min(value = 1) int id) {
        roomService.deleteById(id);
    }
}
