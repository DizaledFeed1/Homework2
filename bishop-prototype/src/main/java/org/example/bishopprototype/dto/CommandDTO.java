//package org.example.bishopprototype.dto;
//
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.Pattern;
//import jakarta.validation.constraints.Size;
//import lombok.Data;
//
//@Data
//public class CommandDTO {
//
//    @NotBlank
//    @Size(max = 1000)
//    String description;
//
//    @NotBlank
//    PriorityType priority;
//
//    @NotBlank
//    @Size(max = 100)
//    String author;
//
//    @NotBlank
//    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}Z")
//    String time;
//}
//
