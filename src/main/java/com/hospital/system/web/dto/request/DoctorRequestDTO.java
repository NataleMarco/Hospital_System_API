package com.hospital.system.web.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorRequestDTO {

    @Size(min = 2, max = 20, message = "Name must be between 2 and 100 characters")
    @NotNull(message = "Name is required")
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Specialty is required")
    @NotNull(message = "Specialty is required")
    private String specialty;

    @NotBlank(message = "Phone is required")
    @NotNull(message = "Phone is required")
    private String phone;

    @NotBlank(message = "Email is required" )
    @NotNull(message = "Email is required")
    @Email(message = "Email has to be valid")
    private String email;

    @Positive(message = "DNI must be a positive number")
    @NotNull(message = "DNI is required")
    private Long dni;
}