package com.hospital.system.web.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientResponseDTO {

    private UUID id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private LocalDate dateOfBirth;
}
