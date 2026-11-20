package com.example.ProfileSearch.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlumniSearchRequest {

    @NotBlank(message = "University name is required")
    private String university;

    @NotBlank(message = "Designation is required")
    private String designation;

    @Min(value = 1950, message = "Passout year must be valid")
    @Max(value = 2035, message = "Passout year must be valid")
    private Integer passoutYear; // Optional
}