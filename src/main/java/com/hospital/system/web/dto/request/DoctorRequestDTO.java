package com.hospital.system.web.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @NotNull(message = "Name cannot be null")
    @NotBlank
    private String name;

    @NotBlank
    @NotNull(message = "Specialty cannot be null")
    private String specialty;

    @NotBlank
    @NotNull(message = "Phone cannot be null")
    private String phone;

    @NotBlank
    @NotNull(message = "Email cannot be null")
    @Email(message = "Email has to be valid")
    private String email;

    @NotBlank
    @NotNull(message = "DNI cannot be null")
    private Long dni;
}
