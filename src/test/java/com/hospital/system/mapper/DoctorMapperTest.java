package com.hospital.system.mapper;

import com.hospital.system.domain.entity.Doctor;
import com.hospital.system.web.dto.request.DoctorRequestDTO;
import com.hospital.system.web.dto.response.DoctorResponseDTO;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DoctorMapperTest {

    private final DoctorMapper doctorMapper = Mappers.getMapper(DoctorMapper.class);

    @Test
    void testToEntity() {
        DoctorRequestDTO dto = new DoctorRequestDTO();
        dto.setName("Dr. Smith");
        dto.setSpecialty("Cardiology");
        dto.setPhone("1234567890");
        dto.setEmail("dr.smith@example.com");
        dto.setDni(12345678L);

        Doctor entity = doctorMapper.toEntity(dto);

        assertAll(
                () -> assertEquals(dto.getName(), entity.getName()),
                () -> assertEquals(dto.getSpecialty(), entity.getSpecialty()),
                () -> assertEquals(dto.getPhone(), entity.getPhone()),
                () -> assertEquals(dto.getEmail(), entity.getEmail()),
                () -> assertEquals(dto.getDni(), entity.getDni())
        );
    }

    @Test
    void testToResponseDTO() {
        Doctor entity = new Doctor();
        entity.setId(UUID.randomUUID());
        entity.setName("Dr. Smith");
        entity.setSpecialty("Cardiology");
        entity.setPhone("1234567890");
        entity.setEmail("dr.smith@example.com");
        entity.setDni(12345678L);

        DoctorRequestDTO dto = doctorMapper.toResponseDTO(entity);

        assertAll(
                () -> assertEquals(entity.getName(), dto.getName()),
                () -> assertEquals(entity.getSpecialty(), dto.getSpecialty()),
                () -> assertEquals(entity.getPhone(), dto.getPhone()),
                () -> assertEquals(entity.getEmail(), dto.getEmail()),
                () -> assertEquals(entity.getDni(), dto.getDni())
        );
    }

    @Test
    void testToResponseDTOs() {
        Doctor doctor1 = new Doctor();
        doctor1.setName("Dr. Smith");
        Doctor doctor2 = new Doctor();
        doctor2.setName("Dr. Jones");

        List<Doctor> doctors = List.of(doctor1, doctor2);
        List<DoctorResponseDTO> dtos = doctorMapper.toResponseDTOs(doctors);

        assertEquals(2, dtos.size());
        assertEquals(doctor1.getName(), dtos.get(0).getName());
        assertEquals(doctor2.getName(), dtos.get(1).getName());
    }
}