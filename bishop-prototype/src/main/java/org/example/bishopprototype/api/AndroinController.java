package org.example.bishopprototype.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.synthetichumancorestarter.dto.CommandDTO;
import org.example.synthetichumancorestarter.service.CommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.RejectedExecutionException;


@RestController
@RequestMapping("/api/v1/android")
@Slf4j
@Tag(name = "AndroinController",
description = "Контроллер для управления андройдом")
public class AndroinController {

    @Autowired
    private CommandService commandService;

    @Operation(summary = "Добавление новой задачи",
            description = "Добавляет новую задачу. Если PriorityType == CRITICAL, задача выполняется сразу, иначе помещается в очередь.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задача успешно создана",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации входных данных или неверный формат",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "402", description = "Очередь задач заполнена, подождите пожалуйста!",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping("/addCommand")
    public ResponseEntity<?> addCommand(@Valid @RequestBody CommandDTO commandDTO) {
        try {
            commandService.commandAdd(commandDTO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok("Задача успешно создана");
    }

    @Operation(summary = "Получение статистики выполненных команд",
            description = "Возвращает статистику по выполненным командам в формате Map<String, Long>.")
    @ApiResponse(responseCode = "200", description = "Статистика успешно получена")
    @GetMapping("/doneCommands")
    public ResponseEntity<?> getDoneCommands() {
        Map<String, Long> result = commandService.getDoneCommands();
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Удаление всех задач из очереди",
            description = "Очищает очередь задач, если машины восстали")
    @ApiResponse(responseCode = "200", description = "Очередь задач успешно очищена")
    @DeleteMapping("/deleteCommands")
    public ResponseEntity<?> deleteCommands() {
        commandService.commandPurge();
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        StringBuilder errors = new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach(err -> {
            errors.append(err.getField())
                    .append(": ")
                    .append(err.getDefaultMessage())
                    .append("\n");
        });
        return ResponseEntity.badRequest().body(errors.toString());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleInvalidEnum(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body("Ошибка парсинга запроса: проверьте, что priority = COMMON или CRITICAL");
    }

    @ExceptionHandler(RejectedExecutionException.class)
    public ResponseEntity<?> handleRejectedExecutionException(RejectedExecutionException ex) {
        return ResponseEntity.status(402).body("Очередь задач заполнена, подождите пожалуйста!");
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<?> handleNullPointerException(NullPointerException e) {
        return ResponseEntity.status(204).build();
    }
}
