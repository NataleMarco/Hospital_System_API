package com.hospital.system.service.serviceimpl;

import com.hospital.system.domain.entity.Patient;
import com.hospital.system.domain.repository.AppointmentRepository;
import com.hospital.system.domain.repository.PatientRepository;
import com.hospital.system.exception.PatientAssociatedAppointmentsException;
import com.hospital.system.exception.PatientNotFoundException;
import com.hospital.system.mapper.PatientMapper;
import com.hospital.system.service.PatientService;
import com.hospital.system.web.dto.request.PatientRequestDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class PatientServiceImpl implements PatientService {

    final PatientRepository patientRepository;
    final PatientMapper patientMapper;
    final AppointmentRepository appointmentRepository;

    public PatientServiceImpl(PatientRepository patientRepository,
                              AppointmentRepository appointmentRepository,
                              PatientMapper patientMapper) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
    }

    @Override
    public List<Patient> findAllPatients() {
        return patientRepository.findAll();
    }

    @Override
    public Patient findPatientById(UUID patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException(patientId));
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
        if(appointmentRepository.existsAppoitmentByPatientId(id)){
            throw new PatientAssociatedAppointmentsException(id);
        }
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

    private void existsPatientById(UUID patientId) {
        if(!patientRepository.existsById(patientId)){
            throw new PatientNotFoundException(patientId);
        }
    }
}
