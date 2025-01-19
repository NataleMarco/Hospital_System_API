package com.hospital.system.service.serviceimpl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.hospital.system.domain.entity.Doctor;
import com.hospital.system.domain.repository.DoctorRepository;
import com.hospital.system.web.dto.DoctorDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class DoctorServiceImplTest {

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private DoctorServiceImpl doctorService;

    private Doctor doctor;
    private DoctorDTO doctorDTO;

    @BeforeEach
    void setUp() {
        // Initialize your test data
        doctorDTO = new DoctorDTO();
        doctorDTO.setName("John Doe");
        doctorDTO.setPhone("1234567890");
        doctorDTO.setEmail("john.doe@example.com");
        doctorDTO.setSpecialty("Cardiology");

        doctor = new Doctor();
        doctor.setName(doctorDTO.getName());
        doctor.setPhone(doctorDTO.getPhone());
        doctor.setEmail(doctorDTO.getEmail());
        doctor.setSpecialty(doctorDTO.getSpecialty());
    }

    @Test
    void testFindAllDoctors() {
        // Setup mock behavior
        when(doctorRepository.findAll()).thenReturn(Collections.singletonList(doctor));

        // Call the method to test
        List<Doctor> doctors = doctorService.findAllDoctors();

        // Assertions
        assertNotNull(doctors);
        assertFalse(doctors.isEmpty());
        assertEquals(1, doctors.size());
        assertEquals("John Doe", doctors.get(0).getName());
    }

    @Test
    void testFindDoctorById() {
        // Setup mock behavior
        when(doctorRepository.findById(anyLong())).thenReturn(Optional.of(doctor));

        // Call the method to test
        Optional<Doctor> result = doctorService.findDoctorById(1L);

        // Assertions
        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getName());
    }

    @Test
    void testSaveDoctor() {
        // Setup mock behavior
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);

        // Call the method to test
        Doctor savedDoctor = doctorService.saveDoctor(doctorDTO);

        // Assertions
        assertNotNull(savedDoctor);
        assertEquals("John Doe", savedDoctor.getName());
    }

    @Test
    void testDeleteDoctor() {
        // Setup mock to do nothing
        doNothing().when(doctorRepository).deleteById(anyLong());

        // Call the method to test
        doctorService.deleteDoctor(1L);

        // Verify the interaction with the mock
        verify(doctorRepository).deleteById(1L);
    }

    @Test
    void testUpdateDoctor() {
        // Setup mock behavior
        when(doctorRepository.findById(anyLong())).thenReturn(Optional.of(doctor));
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);

        // Call the method to test
        Doctor updatedDoctor = doctorService.updateDoctor(doctorDTO, 1L);

        // Assertions
        assertEquals("John Doe", updatedDoctor.getName());
    }
}
