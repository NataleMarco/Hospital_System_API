package com.hospital.system.service.serviceimpl;

import com.hospital.system.domain.entity.Doctor;
import com.hospital.system.domain.repository.DoctorRepository;
import com.hospital.system.exception.ResourceNotFoundException;

import com.hospital.system.web.dto.request.DoctorRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoctorServiceImplTest {

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private DoctorServiceImpl doctorService;

    private Doctor existingDoctor1;
    private Doctor existingDoctor2;
    private DoctorRequestDTO validDoctorDTO;

    @BeforeEach
    void setUp() {
        existingDoctor1 = new Doctor(
                UUID.randomUUID(),
                "Dr. John Doe",
                "Cardiology",
                "1234567890",
                "johndoe@example.com",
                12345678L
        );

        existingDoctor2 = new Doctor(
                UUID.randomUUID(),
                "Dr. Jane Smith",
                "Neurology",
                "0987654321",
                "janesmith@example.com",
                87654321L
        );

        validDoctorDTO = new DoctorRequestDTO(
                "Dr. Alice Brown",
                "Pediatrics",
                "1122334455",
                "alicebrown@example.com",
                11223344L
        );
    }

    // 1. Tests for findAllDoctors()

    @Test
    void findAllDoctors_whenDoctorsExist_ReturnsListOfDoctors() {
        List<Doctor> doctors = Arrays.asList(existingDoctor1, existingDoctor2);
        when(doctorRepository.findAll()).thenReturn(doctors);

        List<Doctor> result = doctorService.findAllDoctors();

        assertEquals(2, result.size(), "Should return two doctors");
        assertTrue(result.contains(existingDoctor1), "Result should contain existingDoctor1");
        assertTrue(result.contains(existingDoctor2), "Result should contain existingDoctor2");
        verify(doctorRepository, times(1)).findAll();
    }

    @Test
    void findAllDoctors_whenNoDoctorsExist_ReturnsEmptyList() {
        when(doctorRepository.findAll()).thenReturn(Collections.emptyList());

        List<Doctor> result = doctorService.findAllDoctors();

        assertTrue(result.isEmpty(), "Result should be an empty list");
        verify(doctorRepository, times(1)).findAll();
    }

    @Test
    void findAllDoctors_whenRepositoryThrowsException_ThrowsRuntimeException() {
        when(doctorRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            doctorService.findAllDoctors();
        }, "Should throw RuntimeException when repository fails");

        assertEquals("Database error", exception.getMessage(), "Exception message should match");
        verify(doctorRepository, times(1)).findAll();
    }

    // 2. Tests for findDoctorById(UUID id)

    @Test
    void findDoctorById_whenDoctorExists_ReturnsDoctor() {
        UUID id = existingDoctor1.getId();
        when(doctorRepository.findById(id)).thenReturn(Optional.of(existingDoctor1));

        Doctor resultDoctor = doctorService.findDoctorById(id);

        assertEquals(existingDoctor1, resultDoctor, "Returned doctor should match existingDoctor1");
        verify(doctorRepository, times(1)).findById(id);
    }

    @Test
    void findDoctorById_whenDoctorDoesNotExist_throwsException() {
        UUID id = UUID.randomUUID();
        when(doctorRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,()-> doctorService.findDoctorById(id), "Should throw ResourceNotFoundException");
        verify(doctorRepository, times(1)).findById(id);
    }

    @Test
    void findDoctorById_whenRepositoryThrowsException_ThrowsRuntimeException() {
        UUID id = existingDoctor1.getId();
        when(doctorRepository.findById(id)).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            doctorService.findDoctorById(id);
        }, "Should throw RuntimeException when repository fails");

        assertEquals("Database error", exception.getMessage(), "Exception message should match");
        verify(doctorRepository, times(1)).findById(id);
    }

    // 3. Tests for saveDoctor(DoctorDTO doctorDTO)

    @Test
    void saveDoctor_whenValidDoctorDTO_ReturnsSavedDoctor() {
        Doctor doctorToSave = new Doctor();
        doctorToSave.setName(validDoctorDTO.getName());
        doctorToSave.setSpecialty(validDoctorDTO.getSpecialty());
        doctorToSave.setPhone(validDoctorDTO.getPhone());
        doctorToSave.setEmail(validDoctorDTO.getEmail());
        doctorToSave.setDNI(validDoctorDTO.getDni());

        Doctor savedDoctor = new Doctor(
                UUID.randomUUID(),
                validDoctorDTO.getName(),
                validDoctorDTO.getSpecialty(),
                validDoctorDTO.getPhone(),
                validDoctorDTO.getEmail(),
                validDoctorDTO.getDni()
        );

        when(doctorRepository.save(any(Doctor.class))).thenReturn(savedDoctor);

        Doctor result = doctorService.saveDoctor(validDoctorDTO);

        assertNotNull(result.getId(), "Saved doctor should have an ID");
        assertEquals(validDoctorDTO.getName(), result.getName(), "Names should match");
        assertEquals(validDoctorDTO.getSpecialty(), result.getSpecialty(), "Specialties should match");
        assertEquals(validDoctorDTO.getPhone(), result.getPhone(), "Phones should match");
        assertEquals(validDoctorDTO.getEmail(), result.getEmail(), "Emails should match");
        assertEquals(validDoctorDTO.getDni(), result.getDNI(), "DNIs should match");

        verify(doctorRepository, times(1)).save(any(Doctor.class));
    }

    @Test
    void saveDoctor_whenRepositoryThrowsException_ThrowsRuntimeException() {
        when(doctorRepository.save(any(Doctor.class))).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            doctorService.saveDoctor(validDoctorDTO);
        }, "Should throw RuntimeException when repository fails during save");

        assertEquals("Database error", exception.getMessage(), "Exception message should match");
        verify(doctorRepository, times(1)).save(any(Doctor.class));
    }

    // 4. Tests for deleteDoctor(UUID id)

    @Test
    void deleteDoctor_whenDoctorExists_DeletesDoctor() {
        UUID id = existingDoctor1.getId();
        doNothing().when(doctorRepository).deleteById(id);
        when(doctorRepository.existsById(id)).thenReturn(true);

        assertDoesNotThrow(() -> doctorService.deleteDoctor(id), "Deleting existing doctor should not throw exception");

        verify(doctorRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteDoctor_whenDoctorDoesNotExist_thenThrowsException() {
        UUID id = UUID.randomUUID();
        when(doctorRepository.existsById(id)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, ()-> doctorService.deleteDoctor(id));

        verify(doctorRepository, times(0)).deleteById(id);
    }

    // 5. Tests for updateDoctor(DoctorDTO doctorDTO, UUID id)

    @Test
    void updateDoctor_whenDoctorExists_ReturnsUpdatedDoctor() {
        UUID id = existingDoctor1.getId();
        when(doctorRepository.existsById(id)).thenReturn(true);

        Doctor updatedDoctor = new Doctor(
                id,
                validDoctorDTO.getName(),
                validDoctorDTO.getSpecialty(),
                validDoctorDTO.getPhone(),
                validDoctorDTO.getEmail(),
                validDoctorDTO.getDni()
        );

        when(doctorRepository.save(any(Doctor.class))).thenReturn(updatedDoctor);

        Doctor result = doctorService.updateDoctor(validDoctorDTO, id);

        assertEquals(id, result.getId(), "IDs should match");
        assertEquals(validDoctorDTO.getName(), result.getName(), "Names should match");
        assertEquals(validDoctorDTO.getSpecialty(), result.getSpecialty(), "Specialties should match");
        assertEquals(validDoctorDTO.getPhone(), result.getPhone(), "Phones should match");
        assertEquals(validDoctorDTO.getEmail(), result.getEmail(), "Emails should match");
        assertEquals(validDoctorDTO.getDni(), result.getDNI(), "DNIs should match");

        verify(doctorRepository, times(1)).save(any(Doctor.class));
    }

    @Test
    void updateDoctor_whenDoctorDoesNotExist_thenThrowsException() {
        UUID id = UUID.randomUUID();
        when(doctorRepository.existsById(id)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, ()-> doctorService.updateDoctor(validDoctorDTO, id));

        verify(doctorRepository, times(1)).existsById(id);
        verify(doctorRepository, times(0)).save(any(Doctor.class));
    }

}

