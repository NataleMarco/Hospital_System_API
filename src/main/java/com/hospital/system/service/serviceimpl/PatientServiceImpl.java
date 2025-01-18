package com.hospital.system.service.serviceimpl;

import com.hospital.system.domain.entity.Patient;
import com.hospital.system.domain.repository.PatientRepository;
import com.hospital.system.exception.ResourceNotFoundException;
import com.hospital.system.service.PatientService;
import com.hospital.system.web.dto.PatientDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {

    final PatientRepository patientRepository;
    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public List<Patient> findAllPatients() {
        return patientRepository.findAll();
    }

    @Override
    public Optional<Patient> findPatientById(Long id) {
        return patientRepository.findById(id);
    }

    @Override
    @Transactional
    public Patient createPatient(PatientDTO patientDTO) {
        Patient patient = convertFromDTO(patientDTO);
        patientRepository.save(patient);
        return patient;
    }

    @Override
    @Transactional
    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Patient updatePatient(PatientDTO patientDTO, Long id) {

        patientRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Patient with id: " + id + " Not found"));

        Patient patient = convertFromDTO(patientDTO);
        patient.setId(id);
        patientRepository.save(patient);
        return patient;
    }

    private Patient convertFromDTO(PatientDTO patientDTO) {
        Patient patient = new Patient();
        patient.setAddress(patientDTO.getAddress());
        patient.setPhone(patientDTO.getPhone());
        patient.setName(patientDTO.getName());
        patient.setDateOfBirth(patientDTO.getDateOfBirth());
        patient.setEmail(patientDTO.getEmail());
        return patient;
    }
}
