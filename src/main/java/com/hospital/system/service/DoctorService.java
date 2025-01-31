package com.hospital.system.service;

import com.hospital.system.domain.entity.Doctor;
import com.hospital.system.web.dto.request.DoctorRequestDTO;

import java.util.List;
import java.util.UUID;

public interface DoctorService {
    List<Doctor> findAllDoctors();
    Doctor findDoctorById(UUID id);
    Doctor saveDoctor(DoctorRequestDTO doctorRequestDTO);
    void deleteDoctor(UUID id);
    Doctor updateDoctor(DoctorRequestDTO doctorRequestDTO, UUID id);
}