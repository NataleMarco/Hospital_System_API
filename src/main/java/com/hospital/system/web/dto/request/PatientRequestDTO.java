package com.hospital.system.web.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientRequestDTO{

    @Size(min = 2, max = 20, message = "Name must be between 2 and 100 characters")
    @NotEmpty
    @NotNull(message = "Name is required")
    private String name;

    @NotEmpty
    @NotNull(message = "Adress is required")
    private String address;

    @NotEmpty
    @NotNull(message = "Phone is required")
    private String phone;

    @NotEmpty
    @NotNull(message = "Email is required")
    @Email(message = "Email has to be valid")
    private String email;

    @NotEmpty
    @NotNull(message = "Date is required")
    private LocalDate dateOfBirth;
}
