package com.hospital.system.service.serviceimpl;

import com.hospital.system.domain.entity.Doctor;
import com.hospital.system.domain.repository.AppointmentRepository;
import com.hospital.system.domain.repository.DoctorRepository;
import com.hospital.system.service.DoctorService;
import com.hospital.system.web.dto.DoctorDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorServiceImpl implements DoctorService {

    final DoctorRepository doctorRepository;

    public DoctorServiceImpl (DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @Override
    public List<Doctor> findAllDoctors() {
        return doctorRepository.findAll();
         
    }

    @Override
    public Optional<Doctor> findDoctorById(Long id) {
        return doctorRepository.findById(id);
    }

    @Override
    @Transactional
    public Doctor saveDoctor(DoctorDTO doctorDTO) {
        Doctor doctor = convertFromDTO(doctorDTO);
        return doctorRepository.save(doctor);
    }

    @Override
    @Transactional
    public void deleteDoctor(Long id) {
        doctorRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Doctor updateDoctor(DoctorDTO doctorDTO, Long id) {

        findDoctorById(id).orElseThrow(() -> new RuntimeException("Doctor with id " + id + " not found ")) ;

        Doctor doctor = convertFromDTO(doctorDTO);
        doctor.setId(id);
        doctorRepository.save(doctor);
        return doctor;
    }

    private Doctor convertFromDTO(DoctorDTO dto){
        Doctor doctor = new Doctor();
        doctor.setName(dto.getName());
        doctor.setPhone(dto.getPhone());
        doctor.setEmail(dto.getEmail());
        doctor.setSpecialty(dto.getSpecialty());
        return doctor;
    }
}
