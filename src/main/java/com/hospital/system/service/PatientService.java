package com.hospital.system.service;

import com.hospital.system.domain.entity.Patient;
import com.hospital.system.web.dto.PatientDTO;

import java.util.List;
import java.util.Optional;

public interface PatientService {
    List<Patient> findAllPatients();
    Optional<Patient> findPatientById(Long id);
    Patient createPatient(PatientDTO patientDTO);
    void deletePatient(Long id);
    Patient updatePatient(PatientDTO patientDTO, Long id);
}
