package com.hospital.system.service;

import com.hospital.system.domain.entity.Patient;
import com.hospital.system.web.dto.request.PatientRequestDTO;

import java.util.List;
import java.util.UUID;

public interface PatientService {
    List<Patient> findAllPatients();
    Patient findPatientById(UUID id);
    Patient createPatient(PatientRequestDTO patientRequestDTO);
    void deletePatient(UUID id);
    Patient updatePatient(PatientRequestDTO patientRequestDTO, UUID id);
}
