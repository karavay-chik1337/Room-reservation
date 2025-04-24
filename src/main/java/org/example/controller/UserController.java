package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.example.dto.UserInnerDTO;
import org.example.dto.UserOuterDTO;
import org.example.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name = "User controller",
        description = "Этот контроллер позволяет управлять пользователями")
public class UserController {
    private final UserService userService;

    @Operation(summary = "Создать пользователя")
    @PostMapping
    public UserOuterDTO create(@RequestBody @Valid UserInnerDTO innerDTO){
        return userService.create(innerDTO);
    }

    @Operation(summary = "Найти пользователя по id")
    @GetMapping("/{id}")
    public UserOuterDTO findById(@PathVariable @Parameter(example = "1") @Valid @Min(value = 1) int id){
        return userService.findById(id);
    }

    @Operation(summary = "Показать всех пользователей")
    @GetMapping
    public List<UserOuterDTO> findAll(){
        return userService.findAll();
    }

    @Operation(summary = "Обновить данные пользователя по id")
    @PatchMapping("/{id}")
    public UserOuterDTO update(
            @PathVariable @Parameter(example = "1") @Valid @Min(value = 1) int id,
            @RequestBody UserInnerDTO innerDTO){
        return userService.update(id, innerDTO);
    }

    @Operation(summary = "Удалить пользователя по id")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable @Parameter(example = "1") @Valid @Min(value = 1) int id){
        userService.deleteById(id);
    }
}
