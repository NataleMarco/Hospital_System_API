package com.hospital.system.web.dto.request;


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

    private String name;

    private String address;

    private String phone;

    private String email;

    private LocalDate dateOfBirth;
}
