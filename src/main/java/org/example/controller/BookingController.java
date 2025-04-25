package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.example.dto.BookingInnerDTO;
import org.example.dto.BookingOuterDTO;
import org.example.service.BookingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
@Tag(name = "Booking controller",
        description = "Этот контроллер позволяет управлять бронированиями")
public class BookingController {
    private final BookingService bookingService;

    @Operation(summary = "Создать бронь")
    @PostMapping
    public BookingOuterDTO create(@RequestBody @Valid BookingInnerDTO bookingInnerDTO) {
        return bookingService.create(bookingInnerDTO);
    }

    @Operation(summary = "Показать бронь пользователя")
    @GetMapping("/{userId}")
    public BookingOuterDTO findByUserId(@PathVariable @Parameter(example = "1") @Valid @Min(value = 1) int userId) {
        return bookingService.findByUserId(userId);
    }

    @Operation(summary = "Найти бронь по id")
    @GetMapping("/{id}")
    public BookingOuterDTO findById(@PathVariable @Parameter(example = "1") @Valid @Min(value = 1) int id) {
        return bookingService.findById(id);
    }

    @Operation(summary = "Показать все брони")
    @GetMapping
    public List<BookingOuterDTO> findAll() {
        return bookingService.findAll();
    }

    @Operation(summary = "Удалить бронь по id")
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable @Parameter(example = "1") @Valid @Min(value = 1) int id) {
        bookingService.deleteById(id);
    }

    @Operation(summary = "Отменить бронь по id")
    @PutMapping("/{id}")
    public BookingOuterDTO canceledBooking(@PathVariable @Parameter(example = "1") @Valid @Min(value = 1) int id){
        return bookingService.cancelBooking(id);
    }

}
