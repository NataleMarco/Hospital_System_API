package com.hospital.system.web.dto.request;

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
    @NotNull
    private UUID patientId;

    @NotNull
    private UUID doctorId;

    @NotNull
    private LocalDateTime dateTime;

    @NotNull
    private String reasons;
}
