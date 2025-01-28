package com.hospital.system.service.serviceimpl;

import com.hospital.system.domain.entity.Patient;
import com.hospital.system.domain.repository.PatientRepository;
import com.hospital.system.exception.ResourceNotFoundException;
import com.hospital.system.web.dto.request.PatientRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceImplTest {

    @Mock
    PatientRepository patientRepository;

    @InjectMocks
    PatientServiceImpl patientService;

    Patient existingPatient1;
    Patient existingPatient2;
    PatientRequestDTO validPatientDTO;

    @BeforeEach
    void setUp() {

        existingPatient1 = new Patient(
                UUID.randomUUID(),
                "Jose Luis",
                "Avenida siempre viva 123",
                "1109893243",
                "test@patient.com",
                LocalDate.of(1994, 7,12)
        );

        existingPatient2 = new Patient(
                UUID.randomUUID(),
                "Maria jose",
                "Calle falsa 123",
                "1109893243",
                "test@patient2.com",
                LocalDate.of(1995, 12,1)
        );

        validPatientDTO = new PatientRequestDTO(
                "Angel di maria",
                "test address 123",
                "123456789",
                "test@patientdto.com",
               LocalDate.of(1995, 12, 1)
        );


    }


    @Test
    void findAllPatients_whenPatientsExist_thenReturnPatientList() {
        List<Patient> patients = Arrays.asList(existingPatient1, existingPatient2);
        when(patientRepository.findAll()).thenReturn(patients);


        List<Patient> result = patientService.findAllPatients();
        assertEquals(2, result.size());

    }
    @Test

    void findAllPatients_whenPatientsDontExist_thenReturnEmptyList() {
        when(patientRepository.findAll()).thenReturn(Collections.emptyList());


        List<Patient> result = patientService.findAllPatients();
        assertTrue(result.isEmpty());

    }

    //FindById Tests

    @Test
    void findById_whenPatientExists_thenReturnPatient() {
        UUID patientId = existingPatient1.getId();
        when(patientRepository.findById(patientId)).thenReturn(Optional.ofNullable(existingPatient1));
        Patient patient = patientService.findPatientById(patientId);
        assertEquals(patient, existingPatient1);

    }

    @Test
    void findById_whenPatientDontExist_thenThrowException() {

        UUID patientId = existingPatient1.getId();
        when(patientRepository.findById(patientId)).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> patientService.findPatientById(patientId));

    }

    // Delete Tests
    @Test
    void deletePatient_whenPatientExists_thenReturnPatient() {
        UUID patientId = existingPatient1.getId();
        when(patientRepository.existsById(patientId)).thenReturn(true);
        assertDoesNotThrow(() -> patientService.deletePatient(patientId));
    }

    @Test
    void deletePatient_whenPatientDoesntExist_thenThrowException() {
        UUID patientId = existingPatient1.getId();
        when(patientRepository.existsById(patientId)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> patientService.deletePatient(patientId));
    }


    //Exists Tests
    @Test
    void existsById_whenPatientExists_thenDoesNotThrowException() {
        UUID patientId = UUID.randomUUID();
        when(patientRepository.existsById(patientId)).thenReturn(true);
        assertDoesNotThrow(() -> patientService.deletePatient(patientId));
    }

    @Test
    void existsById_whenPatientDoesntExists_thenThrowException() {
        UUID patientId = UUID.randomUUID();
        when(patientRepository.existsById(patientId)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> patientService.deletePatient(patientId));
    }


    //Update Tests
    @Test
    void updatePatient_whenPatientExists_thenReturnPatient() {
        UUID patientId = existingPatient1.getId();
        when(patientRepository.existsById(patientId)).thenReturn(true);

        Patient updatedPatient = new Patient(
                patientId,
                validPatientDTO.getName(),
                validPatientDTO.getAddress(),
                validPatientDTO.getPhone(),
                validPatientDTO.getEmail(),
                validPatientDTO.getDateOfBirth()
        );

        when(patientRepository.save(any(Patient.class))).thenReturn(updatedPatient);
        Patient patient = patientService.updatePatient(validPatientDTO, patientId);
        assertEquals(patient, updatedPatient);
    }

    @Test
    void updatePatient_whenPatientDoesntExist_thenThrowException() {
        UUID patientId = existingPatient1.getId();
        when(patientRepository.existsById(patientId)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> patientService.updatePatient(validPatientDTO, patientId));
    }

    @Test
    void createPatient_whenPatientDoesntExists_thenReturnPatient() {
        when(patientRepository.save(any(Patient.class))).thenReturn(existingPatient1);
        Patient patient = patientService.createPatient(validPatientDTO);

        assertEquals(patient.getName(), validPatientDTO.getName());
        assertEquals(patient.getAddress(), validPatientDTO.getAddress());
        assertEquals(patient.getPhone(), validPatientDTO.getPhone());
        assertEquals(patient.getEmail(), validPatientDTO.getEmail());
        assertEquals(patient.getDateOfBirth(), validPatientDTO.getDateOfBirth());

    }

    void createPatient_whenPatientExists_thenThrowException() {
        Patient patient = patientService.createPatient(validPatientDTO);

        assertEquals(patient.getName(), validPatientDTO.getName());
        assertEquals(patient.getAddress(), validPatientDTO.getAddress());
        assertEquals(patient.getPhone(), validPatientDTO.getPhone());
        assertEquals(patient.getEmail(), validPatientDTO.getEmail());
        assertEquals(patient.getDateOfBirth(), validPatientDTO.getDateOfBirth());

    }
}