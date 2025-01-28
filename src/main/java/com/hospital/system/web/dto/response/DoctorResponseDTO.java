package com.hospital.system.web.dto.response;

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
public class DoctorResponseDTO {
    @Size(min = 2, max = 20, message = "Name must be between 2 and 100 characters")
    @NotNull(message = "Name cannot be null")
    private String name;

    @NotNull(message = "Specialty cannot be null")
    private String specialty;

    @NotNull(message = "Phone cannot be null")
    private String phone;

    @NotNull(message = "Email cannot be null")
    private String email;

    @NotNull(message = "DNI cannot be null")
    private Long dni;
}
