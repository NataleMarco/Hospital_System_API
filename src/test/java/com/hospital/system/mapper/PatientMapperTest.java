package com.hospital.system.mapper;

import com.hospital.system.domain.entity.Patient;
import com.hospital.system.web.dto.request.PatientRequestDTO;
import com.hospital.system.web.dto.response.PatientResponseDTO;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PatientMapperTest {

    private final PatientMapper patientMapper = Mappers.getMapper(PatientMapper.class);

    @Test
    void testToEntity() {
        PatientRequestDTO dto = new PatientRequestDTO();
        dto.setName("John Doe");
        dto.setAddress("123 Main St");
        dto.setPhone("555-1234");
        dto.setEmail("john@example.com");
        dto.setDateOfBirth(LocalDate.of(1990, 1, 1));

        Patient entity = patientMapper.toEntity(dto);

        assertAll(
                () -> assertEquals(dto.getName(), entity.getName()),
                () -> assertEquals(dto.getAddress(), entity.getAddress()),
                () -> assertEquals(dto.getPhone(), entity.getPhone()),
                () -> assertEquals(dto.getEmail(), entity.getEmail()),
                () -> assertEquals(dto.getDateOfBirth(), entity.getDateOfBirth())
        );
    }

    @Test
    void testToResponseDTO() {
        Patient entity = new Patient();
        entity.setId(UUID.randomUUID());
        entity.setName("John Doe");
        entity.setAddress("123 Main St");
        entity.setPhone("555-1234");
        entity.setEmail("john@example.com");
        entity.setDateOfBirth(LocalDate.of(1990, 1, 1));

        PatientResponseDTO dto = patientMapper.toResponseDTO(entity);

        assertAll(
                () -> assertEquals(entity.getId(), dto.getId()),
                () -> assertEquals(entity.getName(), dto.getName()),
                () -> assertEquals(entity.getAddress(), dto.getAddress()),
                () -> assertEquals(entity.getPhone(), dto.getPhone()),
                () -> assertEquals(entity.getEmail(), dto.getEmail()),
                () -> assertEquals(entity.getDateOfBirth(), dto.getDateOfBirth())
        );
    }

    @Test
    void testToResponseDTOs() {
        Patient patient1 = new Patient();
        patient1.setName("John Doe");
        Patient patient2 = new Patient();
        patient2.setName("Jane Doe");

        List<Patient> patients = List.of(patient1, patient2);
        List<PatientResponseDTO> dtos = patientMapper.toResponseDTOs(patients);

        assertEquals(2, dtos.size());
        assertEquals(patient1.getName(), dtos.get(0).getName());
        assertEquals(patient2.getName(), dtos.get(1).getName());
    }
}