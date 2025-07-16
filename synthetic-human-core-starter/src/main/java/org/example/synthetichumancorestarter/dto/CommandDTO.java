package org.example.synthetichumancorestarter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommandDTO {

    @NotBlank
    @Size(max = 1000)
    String description;

    @NotNull
    PriorityType priority;

    @NotBlank
    @Size(max = 100)
    String author;

    @NotBlank
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}Z", message = "time должен быть в формате yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    String time;
}
