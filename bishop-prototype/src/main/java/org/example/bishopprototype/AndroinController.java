package org.example.bishopprototype;

import jakarta.validation.Valid;
import org.example.synthetichumancorestarter.dto.CommandDTO;
import org.example.synthetichumancorestarter.service.CommandService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.RejectedExecutionException;

@RestController
@RequestMapping("/api/v1/android")
public class AndroinController {

    CommandService commandService = new CommandService();

    @PostMapping("/addCommand")
    public ResponseEntity<?> addCommand(@Valid @RequestBody CommandDTO commandDTO) {
        try {
            commandService.commandAdd(commandDTO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(commandDTO);
    }

    @GetMapping("/commandCount")
    public ResponseEntity<?> getCommandCount() {
        StringBuilder responce = new StringBuilder();
        responce.append("Колличество задач в очереди: ").append(commandService.commandCount());
        return ResponseEntity.ok(responce);
    }

    @GetMapping("/doneCommands")
    public ResponseEntity<?> getDoneCommands() {
        return ResponseEntity.ok().body(commandService.getDoneCommands());
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
