package com.hospital.system.web.dto.response;

import com.hospital.system.domain.entity.Patient;
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
public class AppointmentResponseDTO {

    private UUID appointmentId;

    private PatientResponseDTO patient;

    private DoctorResponseDTO doctor;

    private LocalDateTime dateTime;

    private String reasons;
}
