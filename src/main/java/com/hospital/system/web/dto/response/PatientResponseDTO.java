package com.hospital.system.web.dto.response;

import lombok.*;

import java.time.LocalDate;

@Data
public class PatientResponseDTO {

    private String name;

    private String address;

    private String phone;

    private String email;

    private LocalDate dateOfBirth;
}
