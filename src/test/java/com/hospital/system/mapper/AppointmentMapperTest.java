package com.hospital.system.mapper;

import com.hospital.system.domain.entity.Appointment;
import com.hospital.system.domain.entity.Doctor;
import com.hospital.system.domain.entity.Patient;
import com.hospital.system.web.dto.request.AppointmentRequestDTO;
import com.hospital.system.web.dto.response.AppointmentResponseDTO;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentMapperTest {

    private final AppointmentMapper appointmentMapper = Mappers.getMapper(AppointmentMapper.class);

    @Test
    void testToEntity() {
        AppointmentRequestDTO dto = new AppointmentRequestDTO();
        LocalDateTime dateTime = LocalDateTime.now().plusDays(1);
        dto.setDateTime(dateTime);
        dto.setReasons("Checkup");

        Appointment entity = appointmentMapper.toEntity(dto);

        assertAll(
                () -> assertNull(entity.getPatient(), "Patient debe ser null (se asigna externamente)"),
                () -> assertNull(entity.getDoctor(), "Doctor debe ser null (se asigna externamente)"),
                () -> assertEquals(dateTime, entity.getDateTime()),
                () -> assertEquals("Checkup", entity.getReasons())
        );
    }

    @Test
    void testToResponseDTO() {
        Patient patient = new Patient();
        patient.setId(UUID.randomUUID());
        patient.setName("John Doe");

        Doctor doctor = new Doctor();
        doctor.setId(UUID.randomUUID());
        doctor.setName("Dr. Smith");

        Appointment appointment = new Appointment();
        appointment.setId(UUID.randomUUID());
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setDateTime(LocalDateTime.now().plusDays(1));
        appointment.setReasons("Checkup");

        AppointmentResponseDTO dto = appointmentMapper.toResponseDTO(appointment);

        assertAll(
                () -> assertEquals(patient.getName(), dto.getPatient().getName()),
                () -> assertEquals(doctor.getName(), dto.getDoctor().getName()),
                () -> assertEquals(appointment.getDateTime(), dto.getDateTime()),
                () -> assertEquals(appointment.getReasons(), dto.getReasons())
        );
    }

    @Test
    void testToResponseDTOList() {
        Appointment appointment1 = new Appointment();
        appointment1.setReasons("Checkup");
        Appointment appointment2 = new Appointment();
        appointment2.setReasons("Checkupa");

        List<Appointment> appointments = List.of(appointment1, appointment2);
        List<AppointmentResponseDTO> dtos = appointmentMapper.toResponseDTOList(appointments);

        assertEquals(2, dtos.size());
        assertEquals(appointment1.getReasons(), dtos.get(0).getReasons());
        assertEquals(appointment2.getReasons(), dtos.get(1).getReasons());
    }
}