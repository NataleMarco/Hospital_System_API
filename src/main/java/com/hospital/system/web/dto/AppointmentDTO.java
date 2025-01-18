package com.hospital.system.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDTO {
    private Long patientId;
    private Long doctorId;
    private LocalDateTime dateTime;
    private String reasons;
}
