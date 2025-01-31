package com.hospital.system.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital.system.domain.entity.Appointment;
import com.hospital.system.domain.entity.Doctor;
import com.hospital.system.domain.entity.Patient;
import com.hospital.system.exception.AppointmentNotFoundException;
import com.hospital.system.exception.ResourceNotFoundException;
import com.hospital.system.service.AppointmentService;
import com.hospital.system.web.dto.request.AppointmentRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AppointmentController.class)
class AppointmentControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AppointmentService appointmentService;

    private Patient existingPatient1;
    private Doctor existingDoctor1;
    private Appointment appointment1;
    private AppointmentRequestDTO appointmentRequestDTO;

    private static final UUID FIXED_PATIENT_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    private static final UUID FIXED_DOCTOR_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");

    @BeforeEach
    void setup(){
        existingDoctor1 = new Doctor(
                FIXED_DOCTOR_ID,
                "Dr. John Doe",
                "Cardiology",
                "123-456-7890",
                "johndoe@example.com",
                12345678L
        );

        // Initialize Patients
        existingPatient1 = new Patient(
                FIXED_PATIENT_ID,
                "Emily White",
                "123 Maple Street, Springfield",
                "555-123-4567",
                "emilywhite@example.com",
                LocalDate.of(1990, 5, 15)
        );

        // Initialize Appointments
        appointment1 = new Appointment(
                UUID.randomUUID(),
                existingPatient1,
                existingDoctor1,
                LocalDateTime.of(2025, 3, 10, 10, 30),
                "Routine check-up and blood pressure monitoring."
        );

        appointmentRequestDTO = new AppointmentRequestDTO(
                existingPatient1.getId(),
                existingDoctor1.getId(),
                LocalDateTime.of(2025, 3, 12, 9, 15),
                "Follow-up appointment for migraine treatment."
        );
    }

    @Test
    @DisplayName("Find All Appointments When They Exist")
    void findAll_whenAppointmentsExists_thenReturnAppointments() throws Exception {
        List<Appointment> mockAppointments = Collections.singletonList(appointment1);
        when(appointmentService.findAllAppointments()).thenReturn(mockAppointments);

        mockMvc.perform(get("/appointments")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(mockAppointments)));
    }

    @Test
    @DisplayName("Find All Appointments When List Is Empty")
    void findAll_whenAppointmentsIsEmpty_thenReturnEmptyOk() throws Exception {
        List<Appointment> mockList = Collections.emptyList();

        when(appointmentService.findAllAppointments()).thenReturn(mockList);
        mockMvc.perform(get("/appointments")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));
    }

    // FINDBY TESTS
    @Test
    @DisplayName("Find Appointment By ID When It Exists")
    void findById_whenAppointmentExists_thenReturnAppointment() throws Exception {

        UUID appointmentId = appointment1.getId();
        when(appointmentService.findAppointmentById(appointmentId)).thenReturn(appointment1);
        mockMvc.perform(get("/appointments/{appointmentId}", appointmentId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(appointment1)));
    }

    @Test
    @DisplayName("Find Appointment By ID When It Does Not Exist")
    void findById_whenAppointmentDoesNotExist_thenReturnNotFound() throws Exception {

        UUID appointmentId = appointment1.getId();
        when(appointmentService.findAppointmentById(appointmentId)).thenThrow(AppointmentNotFoundException.class);
        mockMvc.perform(get("/appointments/{appointmentId}", appointmentId))
                .andExpect(status().isNotFound());
    }

    // CreateAppointment TESTS - Refactorizados a Pruebas Parametrizadas
    @ParameterizedTest(name = "{index} => field={0}, value={1}, expectedError={2}")
    @MethodSource("provideInvalidAppointmentRequests")
    @DisplayName("Create Appointment with Invalid Data Should Return Bad Request")
    void createAppointment_withInvalidData_shouldReturnBadRequest(String fieldName, Object fieldValue, String expectedErrorMessage) throws Exception {

        AppointmentRequestDTO validRequest = new AppointmentRequestDTO(
                existingPatient1.getId(),
                existingDoctor1.getId(),
                LocalDateTime.of(2025, 3, 12, 9, 15),
                "Follow-up appointment for migraine treatment."
        );

        AppointmentRequestDTO invalidRequest = new AppointmentRequestDTO(
                validRequest.getPatientId(),
                validRequest.getDoctorId(),
                validRequest.getDateTime(),
                validRequest.getReasons()
        );

        switch (fieldName) {
            case "doctorId":
                invalidRequest.setDoctorId((UUID) fieldValue);
                break;
            case "patientId":
                invalidRequest.setPatientId((UUID) fieldValue);
                break;
            case "dateTime":
                invalidRequest.setDateTime((LocalDateTime) fieldValue);
                break;
            case "reasons":
                invalidRequest.setReasons((String) fieldValue);
                break;
            default:
                throw new IllegalArgumentException("Field not supported: " + fieldName);
        }

        String requestJson = objectMapper.writeValueAsString(invalidRequest);

        mockMvc.perform(post("/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors." + fieldName).value(expectedErrorMessage));
    }

    // Fuente de datos para las pruebas parametrizadas
    private static Stream<Arguments> provideInvalidAppointmentRequests() {
        return Stream.of(
                Arguments.of("doctorId", null, "Doctor ID is required"),
                Arguments.of("patientId", null, "Patient ID is required"),
                Arguments.of("dateTime", null, "Date and time are required"),
                Arguments.of("dateTime", LocalDateTime.of(2001, 3, 12, 9, 15), "Date must be in the future"),
                Arguments.of("reasons", "", "Reasons are required"),
                Arguments.of("reasons", null, "Reasons are required")
        );
    }

    @Test
    @DisplayName("Create Appointment When Valid Data Should Return Created")
    void createAppointment_whenValidData_thenReturnCreated() throws Exception {
        Appointment createdAppointment = new Appointment(
                UUID.randomUUID(),
                existingPatient1,
                existingDoctor1,
                appointmentRequestDTO.getDateTime(),
                appointmentRequestDTO.getReasons()
        );
        String requestJson = objectMapper.writeValueAsString(appointmentRequestDTO);
        when(appointmentService.saveAppointment(any(AppointmentRequestDTO.class))).thenReturn(createdAppointment);

        mockMvc.perform(post("/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.reasons").value(appointmentRequestDTO.getReasons()));

        verify(appointmentService).saveAppointment(any(AppointmentRequestDTO.class));
    }

    @Test
    @DisplayName("Delete Appointment When It Exists Should Return OK")
    void deleteAppointment_whenAppointmentExists_returnOk() throws Exception {
        UUID appointmentId = appointment1.getId();
        doNothing().when(appointmentService).deleteAppointment(appointmentId);

        mockMvc.perform(delete("/appointments/{appointmentId}", appointmentId))
                .andExpect(status().isOk());

        verify(appointmentService).deleteAppointment(appointmentId);
    }

    @Test
    @DisplayName("Delete Appointment When It Does Not Exist Should Return Not Found")
    void deleteAppointment_whenAppointmentDoesNotExist_thenReturnNotFound() throws Exception {
        UUID appointmentId = UUID.randomUUID();

        doThrow(AppointmentNotFoundException.class).when(appointmentService).deleteAppointment(appointmentId);

        mockMvc.perform(delete("/appointments/{appointmentId}", appointmentId))
                .andExpect(status().isNotFound());
    }

    // UpdateAppointments TESTS
    @Test
    @DisplayName("Update Appointment When It Exists Should Return Updated Appointment")
    void updateAppointment_whenreturnAppointment() throws Exception {
        UUID appointmentId = appointment1.getId();

        Appointment updatedAppointment = new Appointment(
                appointmentId,
                existingPatient1,
                existingDoctor1,
                appointmentRequestDTO.getDateTime(),
                appointmentRequestDTO.getReasons()
        );

        when(appointmentService.updateAppointment(any(AppointmentRequestDTO.class), eq(appointmentId))).thenReturn(updatedAppointment);

        String bodyJson = objectMapper.writeValueAsString(appointmentRequestDTO);

        mockMvc.perform(put("/appointments/{appointmentId}", appointmentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(appointmentId.toString()))
                .andExpect(jsonPath("$.reasons").value(appointmentRequestDTO.getReasons()));
    }
}
