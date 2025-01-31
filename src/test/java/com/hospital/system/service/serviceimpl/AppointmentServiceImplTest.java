package com.hospital.system.service.serviceimpl;

import com.hospital.system.domain.entity.Appointment;
import com.hospital.system.domain.entity.Doctor;
import com.hospital.system.domain.entity.Patient;
import com.hospital.system.domain.repository.AppointmentRepository;
import com.hospital.system.exception.AppointmentNotFoundException;
import com.hospital.system.exception.DoctorAssociatedAppointmentsException;
import com.hospital.system.exception.PatientAssociatedAppointmentsException;
import com.hospital.system.exception.ResourceNotFoundException;
import com.hospital.system.mapper.AppointmentMapperImpl;
import com.hospital.system.service.DoctorService;
import com.hospital.system.service.PatientService;

import com.hospital.system.web.dto.request.AppointmentRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AppointmentServiceImplTest {


    @Mock
    private AppointmentMapperImpl appointmentMapper;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private DoctorService doctorService;

    @Mock
    private PatientService patientService;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    private Doctor existingDoctor1;
    private Patient existingPatient1;
    private Appointment appointment1;
    private Appointment appointment2;

    private AppointmentRequestDTO appointmentDTO;

    @BeforeEach
    void setUp() {
        // Initialize Doctors
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

        appointment2 = new Appointment(
                UUID.randomUUID(),
                existingPatient1,
                existingDoctor1,
                LocalDateTime.of(2025, 2, 10, 10, 30),
                "Routine check-up and blood pressure monitoring."
        );

        // Initialize AppointmentDTO
        appointmentDTO = new AppointmentRequestDTO(
                existingPatient1.getId(),
                existingDoctor1.getId(),
                LocalDateTime.of(2025, 3, 12, 9, 15),
                "Follow-up appointment for migraine treatment."
        );
    }
    @Test
    @DisplayName("findAllAppointments_whenAppointmentsExist_ReturnsListOfAppointments")
    void findAllAppointments_whenAppointmentsExist_ReturnsListOfAppointments() {
        List<Appointment> appointments = Arrays.asList(appointment1, appointment2);
        when(appointmentRepository.findAll()).thenReturn(appointments);

        List<Appointment> result = appointmentService.findAllAppointments();

        assertEquals(2, result.size(), "Should be two appointments");
        verify(appointmentRepository, times(1)).findAll();

    }

    @Test
    @DisplayName("findAllAppointments_whenNoAppointmentsExist_ReturnsEmptyList")
    void findAllAppointments_whenNoAppointmentsExist_ReturnsEmptyList() {
        List<Appointment> appointments = Collections.emptyList();
        when(appointmentRepository.findAll()).thenReturn(appointments);
        List<Appointment> result = appointmentService.findAllAppointments();

        assertEquals(0, result.size(), "Should be zero appointments");
        verify(appointmentRepository, times(1)).findAll();
    }


    @Test
    @DisplayName("findAppointmentById_whenAppointmentExists_ReturnsAppointment")
    void findAppointmentById_whenAppointmentExists_ReturnsAppointment() {
        when(appointmentRepository.findById(appointment1.getId())).thenReturn(Optional.of(appointment1));
        Appointment appointment = appointmentService.findAppointmentById(appointment1.getId());

        assertEquals(appointment1, appointment);

        verify(appointmentRepository,times(1)).findById(appointment1.getId());

    }

    @Test
    @DisplayName("findAppointmentById_whenAppointmentDoesNotExist_ReturnsEmptyOptional")
    void findAppointmentById_whenAppointmentDoesNotExist_ThrowsResourceNotFoundException() {
        UUID appointmentId = UUID.randomUUID();
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.empty());

        assertThrows(AppointmentNotFoundException.class, () -> appointmentService.findAppointmentById(appointmentId));

        verify(appointmentRepository, times(1)).findById(appointmentId);

    }


    @Test
    @DisplayName("saveAppointment_whenValidDTO_ReturnsSavedAppointment")
    void saveAppointment_whenValidDTO_ReturnsSavedAppointment() {
        when(patientService.findPatientById(appointmentDTO.getPatientId())).thenReturn(existingPatient1);
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment1);
        when(appointmentMapper.toEntity(any())).thenReturn(appointment1);
        Appointment savedAppointment = appointmentService.saveAppointment(appointmentDTO);
        assertNotNull(savedAppointment, "Appointment should exist");

    }

    @Test
    @DisplayName("saveAppointment_whenDoctorDoesNotExist_ThrowsIllegalArgumentException")
    void saveAppointment_whenDoctorDoesNotExist_ThrowsIllegalArgumentException() {
        when(doctorService.findDoctorById(appointmentDTO.getDoctorId())).thenThrow(ResourceNotFoundException.class);
        assertThrows(ResourceNotFoundException.class, () -> appointmentService.saveAppointment(appointmentDTO));
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    @DisplayName("saveAppointment_whenPatientDoesNotExist_ThrowsIllegalArgumentException")
    void saveAppointment_whenPatientDoesNotExist_ThrowsIllegalArgumentException() {
        when(patientService.findPatientById(appointmentDTO.getPatientId())).thenThrow(ResourceNotFoundException.class);
        assertThrows(ResourceNotFoundException.class, () -> appointmentService.saveAppointment(appointmentDTO));
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    // **4. deleteAppointment tests**

    @Test
    @DisplayName("deleteAppointment_whenAppointmentExists_DeletesAppointment")
    void deleteAppointment_whenAppointmentExists_DeletesAppointment() {
        UUID appointmentId = appointment1.getId();
        when(appointmentRepository.existsById(appointmentId)).thenReturn(true);
        assertDoesNotThrow(() -> appointmentService.deleteAppointment(appointmentId));
        verify(appointmentRepository, times(1)).deleteById(appointmentId);

    }

    @Test
    @DisplayName("deleteAppointment_whenAppointmentDoesNotExist_ThrowsResourceNotFoundException")
    void deleteAppointment_whenAppointmentDoesNotExist_ThrowsResourceNotFoundException() {

        UUID appointmentId = UUID.randomUUID();
        when(appointmentRepository.existsById(appointmentId)).thenReturn(false);
        assertThrows(AppointmentNotFoundException.class, () -> appointmentService.deleteAppointment(appointmentId));

    }

    // **5. updateAppointment tests**

    @Test
    @DisplayName("updateAppointment_whenValidDTOAndAppointmentExists_ReturnsUpdatedAppointment")
    void updateAppointment_whenValidDTOAndAppointmentExists_ReturnsUpdatedAppointment() {
        UUID appointmentId = appointment1.getId();
        when(appointmentRepository.existsById(appointmentId)).thenReturn(true);
        when(appointmentMapper.toEntity(any())).thenReturn(appointment1);

        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment1);
        Appointment appointment = appointmentService.updateAppointment(appointmentDTO, appointmentId);

        assertNotNull(appointment, "Appointment should exist");

    }

    @Test
    @DisplayName("updateAppointment_whenAppointmentDoesNotExist_ThrowsResourceNotFoundException")
    void updateAppointment_whenAppointmentDoesNotExist_ThrowsResourceNotFoundException() {
        UUID appointmentId = appointment1.getId();
        when(appointmentRepository.existsById(appointmentId)).thenReturn(false);
        assertThrows(AppointmentNotFoundException.class, () -> appointmentService.updateAppointment(appointmentDTO,appointmentId));


    }

    @Test
    @DisplayName("updateAppointment_whenDoctorDoesNotExist_ThrowsResourceNotFoundException")
    void updateAppointment_whenDoctorDoesNotExist_ThrowsResourceNotFoundException() {
        UUID appointmentId = UUID.randomUUID();

        when(appointmentRepository.existsById(appointmentId)).thenReturn(true);
        when(patientService.findPatientById(existingPatient1.getId())).thenReturn(existingPatient1);
        when(doctorService.findDoctorById(existingDoctor1.getId())).thenThrow(new ResourceNotFoundException("Doctor not found"));
        assertThrows(ResourceNotFoundException.class ,() -> appointmentService.updateAppointment(appointmentDTO, appointmentId));

        verify(doctorService).findDoctorById(existingDoctor1.getId());
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    void existsAppointmentByDoctorId_whenDoctorExists_doNothing() {
        UUID doctorId = UUID.randomUUID();
        when(appointmentRepository.existsAppoitmentByDoctorId(doctorId)).thenReturn(false);
        assertDoesNotThrow(() -> appointmentService.existAppointmentByDoctorId(doctorId));
    }

    @Test
    void existsAppointmentByDoctorId_whenDoctorDoesNotExist_thenThrowException() {
        UUID doctorId = UUID.randomUUID();
        when(appointmentRepository.existsAppoitmentByDoctorId(doctorId)).thenReturn(true);
        assertThrows(DoctorAssociatedAppointmentsException.class, () -> appointmentService.existAppointmentByDoctorId(doctorId));
    }

    @Test
    void existsAppointmentByPatientId_whenPatientExists_doNothing() {

        UUID  patientId = UUID.randomUUID();
        when(appointmentRepository.existsAppoitmentByDoctorId(patientId)).thenReturn(false);
        assertDoesNotThrow(() -> appointmentService.existAppointmentByDoctorId(patientId));
    }

    @Test
    void existsAppointmentByPatientId_whenPatientDoesNotExist_thenThrowException() {
        UUID patientId = UUID.randomUUID();
        when(appointmentRepository.existsAppoitmentByPatientId(patientId)).thenReturn(true);
        assertThrows(PatientAssociatedAppointmentsException.class, () -> appointmentService.existAppointmentByPatientId(patientId));
    }
}