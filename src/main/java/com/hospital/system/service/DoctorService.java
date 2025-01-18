package com.hospital.system.service;

import com.hospital.system.domain.entity.Doctor;
import com.hospital.system.web.dto.DoctorDTO;

import java.util.List;
import java.util.Optional;

public interface DoctorService {
    List<Doctor> findAllDoctors();
    Optional<Doctor> findDoctorById(Long id);
    Doctor saveDoctor(DoctorDTO doctorDTO);
    void deleteDoctor(Long id);
    Doctor updateDoctor(DoctorDTO doctorDTO, Long id);
}
