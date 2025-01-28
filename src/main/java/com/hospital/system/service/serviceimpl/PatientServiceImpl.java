package com.hospital.system.service.serviceimpl;

import com.hospital.system.domain.entity.Patient;
import com.hospital.system.domain.repository.PatientRepository;
import com.hospital.system.exception.ResourceNotFoundException;
import com.hospital.system.mapper.PatientMapper;
import com.hospital.system.service.PatientService;
import com.hospital.system.web.dto.request.PatientRequestDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PatientServiceImpl implements PatientService {

    final PatientRepository patientRepository;
    final PatientMapper patientMapper;

    public PatientServiceImpl(PatientRepository patientRepository,
                              PatientMapper patientMapper) {
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
    }

    private String resourceNotFoundMessage(UUID id){
        return "Patient with id: " + id + " not found!";
    }

    @Override
    public List<Patient> findAllPatients() {
        return patientRepository.findAll();
    }

    @Override
    public Patient findPatientById(UUID id) {
        return patientRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(resourceNotFoundMessage(id)));
    }

    public void existsPatientById(UUID id) {
        if(!patientRepository.existsById(id)){
            throw new ResourceNotFoundException(resourceNotFoundMessage(id));
        }
    }

    @Override
    @Transactional
    public Patient createPatient(PatientRequestDTO patientRequestDTO) {
        Patient patient = patientMapper.toEntity(patientRequestDTO);
        patientRepository.save(patient);
        return patient;
    }

    @Override
    @Transactional
    public void deletePatient(UUID id) {
        existsPatientById(id);
        patientRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Patient updatePatient(PatientRequestDTO patientRequestDTO, UUID id) {

        existsPatientById(id);
        Patient updatedPatient = patientMapper.toEntity(patientRequestDTO);
        updatedPatient.setId(id);
        return patientRepository.save(updatedPatient);
    }
}
