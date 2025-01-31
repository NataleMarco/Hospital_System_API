package com.hospital.system.web.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequestDTO {

    @NotNull(message = "Patient ID is required")
    private UUID patientId;

    @NotNull(message = "Doctor ID is required")
    private UUID doctorId;

    @NotNull(message = "Date and time are required")
    @Future(message = "Date must be in the future")
    private LocalDateTime dateTime;

    @NotNull(message = "Reasons are required")
    @NotEmpty(message = "Reasons are required")
    private String reasons;
}
