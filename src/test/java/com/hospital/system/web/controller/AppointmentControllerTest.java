package com.hospital.system.web.controller;

import com.hospital.system.domain.entity.Appointment;
import com.hospital.system.domain.entity.Doctor;
import com.hospital.system.domain.entity.Patient;
import com.hospital.system.service.AppointmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get; // Import correcto
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(AppointmentController.class)
class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppointmentService appointmentService;
    //FindAll TESTS

    private Doctor existingDoctor1;
    private Patient existingPatient1;
    private Appointment appointment1;


    @BeforeEach
    void setup(){
        existingDoctor1 = new Doctor(
                UUID.randomUUID(),
                "Dr. John Doe",
                "Cardiology",
                "123-456-7890",
                "johndoe@example.com",
                12345678L
        );

        // Initialize Patients
        existingPatient1 = new Patient(
                UUID.randomUUID(),
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
    }

    @Test
    void findAll_whenAppointmentsExists_thenReturnAppointments() throws Exception {
        List<Appointment> mockAppointments = Arrays.asList(appointment1);
        when(appointmentService.findAllAppointments()).thenReturn(mockAppointments);

        mockMvc.perform(get("/appointments")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(mockAppointments.size()))
                .andExpect(jsonPath("$[0].patient.name").value("Emily White"))
                .andExpect(jsonPath("$[0].patient.address").value("123 Maple Street, Springfield"))
                .andExpect(jsonPath("$[0].patient.phone").value("555-123-4567"))
                .andExpect(jsonPath("$[0].patient.email").value("emilywhite@example.com"))
                .andExpect(jsonPath("$[0].patient.dateOfBirth").value("1990-05-15"))
                .andExpect(jsonPath("$[0].doctor.name").value("Dr. John Doe"))
                .andExpect(jsonPath("$[0].doctor.specialty").value("Cardiology"))
                .andExpect(jsonPath("$[0].doctor.phone").value("123-456-7890"))
                .andExpect(jsonPath("$[0].doctor.email").value("johndoe@example.com"))
                .andExpect(jsonPath("$[0].doctor.dni").value(12345678))
                .andExpect(jsonPath("$[0].dateTime").value("2025-03-10T10:30:00"))
                .andExpect(jsonPath("$[0].reasons").value("Routine check-up and blood pressure monitoring."));
    }

    @Test
    void findAll_whenAppointmentsIsEmpty_thenReturnNoContent() throws Exception {
        List<Appointment> mockList = Collections.emptyList();

        when(appointmentService.findAllAppointments()).thenReturn(mockList);
        mockMvc.perform(get("/appointments")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));
    }

    //FINDBY TESTS
    @Test
    void findById() {
    }

    //CreateAppointment TESTS
    @Test
    void createAppointment() {
    }

    //DeleteAppointment TESTS
    @Test
    void deleteAppointment() {
    }

    //UpdateAppointments TESTS
    @Test
    void updateAppointment() {
    }
}