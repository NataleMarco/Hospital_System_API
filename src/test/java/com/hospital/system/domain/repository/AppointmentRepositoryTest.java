package com.hospital.system.domain.repository;

import com.hospital.system.domain.entity.Appointment;
import com.hospital.system.domain.entity.Doctor;
import com.hospital.system.domain.entity.Patient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AppointmentRepositoryTest {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    // Preexisting UUIDs of doctors
    private static final UUID DOCTOR_ANARUIZ_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    private static final UUID DOCTOR_LUISFERNANDEZ_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");
    private static final UUID DOCTOR_CARLOSJIMENEZ_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174002");
    private static final UUID DOCTOR_SOFIACASTRO_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174004");

    // Preexisting UUIDs of patients
    private static final UUID PATIENT_JUANPEREZ_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174005");
    private static final UUID PATIENT_MARIALOPEZ_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174006");
    private static final UUID PATIENT_PEDROMARTINEZ_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174007");

    // Preexisting UUIDs of appointments
    private static final UUID APPOINTMENT1_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174010");
    private static final UUID APPOINTMENT2_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174011");
    private static final UUID APPOINTMENT3_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174012");

    @Test
    void findAll_whenAppointmentsExist_ReturnAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();
        assertEquals(5, appointments.size(), "There should be 5 appointments in the database");
    }

    @Test
    void saveAppointment_withoutId_ShouldGenerateId() {
        // Retrieve existing doctor and patient
        Optional<Doctor> doctorOpt = doctorRepository.findById(DOCTOR_ANARUIZ_ID);
        Optional<Patient> patientOpt = patientRepository.findById(PATIENT_JUANPEREZ_ID);

        assertTrue(doctorOpt.isPresent(), "The doctor must exist");
        assertTrue(patientOpt.isPresent(), "The patient must exist");

        Doctor doctor = doctorOpt.get();
        Patient patient = patientOpt.get();

        // Create new appointment
        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setDateTime(LocalDateTime.now().plusDays(1));
        appointment.setReasons("New test appointment");

        Appointment savedAppointment = appointmentRepository.saveAndFlush(appointment);
        assertNotNull(savedAppointment.getId(), "The appointment ID should be generated automatically");
    }

    @Test
    void countEntities_ShouldReturnCorrectCount() {
        long initialCount = appointmentRepository.count();

        // Retrieve existing doctor and patient
        Optional<Doctor> doctorOpt = doctorRepository.findById(DOCTOR_LUISFERNANDEZ_ID);
        Optional<Patient> patientOpt = patientRepository.findById(PATIENT_MARIALOPEZ_ID);

        assertTrue(doctorOpt.isPresent(), "The doctor must exist");
        assertTrue(patientOpt.isPresent(), "The patient must exist");

        Doctor doctor = doctorOpt.get();
        Patient patient = patientOpt.get();

        // Create and save new appointment
        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setDateTime(LocalDateTime.now().plusDays(2));
        appointment.setReasons("Count test");

        appointmentRepository.saveAndFlush(appointment);

        long updatedCount = appointmentRepository.count();
        assertEquals(initialCount + 1, updatedCount, "The appointment count should increase by 1");
    }

    @Test
    void findById_whenAppointmentExists_ReturnAppointment() {
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(APPOINTMENT1_ID);
        assertTrue(appointmentOpt.isPresent(), "The appointment must exist");
        Appointment appointment = appointmentOpt.get();
        assertEquals("Revisión cardíaca regular", appointment.getReasons(), "The reason for the appointment must match");
    }

    @Test
    void findById_whenAppointmentNotExist_ReturnEmpty() {
        UUID nonExistentId = UUID.randomUUID();
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(nonExistentId);
        assertFalse(appointmentOpt.isPresent(), "The appointment must not exist");
    }

    @Test
    void saveAppointment_whenAppointmentNotExist_ReturnAppointment() {
        // Retrieve existing doctor and patient
        Optional<Doctor> doctorOpt = doctorRepository.findById(DOCTOR_CARLOSJIMENEZ_ID);
        Optional<Patient> patientOpt = patientRepository.findById(PATIENT_PEDROMARTINEZ_ID);

        assertTrue(doctorOpt.isPresent(), "The doctor must exist");
        assertTrue(patientOpt.isPresent(), "The patient must exist");

        Doctor doctor = doctorOpt.get();
        Patient patient = patientOpt.get();

        // Create new appointment
        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setDateTime(LocalDateTime.now().plusDays(3));
        appointment.setReasons("Save new appointment");

        Appointment savedAppointment = appointmentRepository.saveAndFlush(appointment);
        assertNotNull(savedAppointment, "The saved appointment must not be null");
        assertNotNull(savedAppointment.getId(), "The appointment ID should be generated");

        Optional<Appointment> fetchedAppointment = appointmentRepository.findById(savedAppointment.getId());
        assertTrue(fetchedAppointment.isPresent(), "The saved appointment must exist in the database");
        assertEquals("Save new appointment", fetchedAppointment.get().getReasons(), "The reason for the appointment must match");
    }

    @Test
    void updateAppointment_whenAppointmentExists_ShouldUpdateSuccessfully() {
        // Retrieve an existing appointment
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(APPOINTMENT2_ID);
        assertTrue(appointmentOpt.isPresent(), "The appointment must exist");
        Appointment appointment = appointmentOpt.get();

        // Update fields
        String newReasons = "Updated consultation";
        LocalDateTime newDateTime = LocalDateTime.now().plusDays(5);
        appointment.setReasons(newReasons);
        appointment.setDateTime(newDateTime);

        // Save the changes
        appointmentRepository.saveAndFlush(appointment);

        // Verify that the changes have been applied
        Optional<Appointment> updatedOpt = appointmentRepository.findById(APPOINTMENT2_ID);
        assertTrue(updatedOpt.isPresent(), "The updated appointment must exist");
        Appointment updated = updatedOpt.get();
        assertEquals(newReasons, updated.getReasons(), "The reason for the appointment must be updated");
        assertEquals(newDateTime, updated.getDateTime(), "The date and time of the appointment must be updated");
    }

    @Test
    void deleteAppointment_whenAppointmentExists_ShouldDeleteSuccessfully() {
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(APPOINTMENT3_ID);
        assertTrue(appointmentOpt.isPresent(), "The appointment must exist");

        appointmentRepository.deleteById(APPOINTMENT3_ID);

        Optional<Appointment> deletedAppointmentOpt = appointmentRepository.findById(APPOINTMENT3_ID);
        assertFalse(deletedAppointmentOpt.isPresent(), "The appointment must have been deleted");
    }

    @Test
    void saveAppointment_withNullDoctor_ShouldThrowException() {
        Optional<Patient> patientOpt = patientRepository.findById(PATIENT_JUANPEREZ_ID);
        assertTrue(patientOpt.isPresent(), "The patient must exist");

        Patient patient = patientOpt.get();

        Appointment appointment = new Appointment();
        appointment.setDoctor(null); // Doctor is mandatory
        appointment.setPatient(patient);
        appointment.setDateTime(LocalDateTime.now().plusDays(6));
        appointment.setReasons("Appointment without doctor");

        assertThrows(DataIntegrityViolationException.class, () -> appointmentRepository.saveAndFlush(appointment),
                "Saving an appointment without doctor should throw an exception");
    }

    @Test
    void saveAppointment_withNullPatient_ShouldThrowException() {
        Optional<Doctor> doctorOpt = doctorRepository.findById(DOCTOR_SOFIACASTRO_ID);
        assertTrue(doctorOpt.isPresent(), "The doctor must exist");

        Doctor doctor = doctorOpt.get();

        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(null); // Patient is mandatory
        appointment.setDateTime(LocalDateTime.now().plusDays(7));
        appointment.setReasons("Appointment without patient");

        assertThrows(DataIntegrityViolationException.class, () -> appointmentRepository.saveAndFlush(appointment),
                "Saving an appointment without patient should throw an exception");
    }

    @Test
    void saveAppointment_withNullDateTime_ShouldThrowException() {
        Optional<Doctor> doctorOpt = doctorRepository.findById(DOCTOR_CARLOSJIMENEZ_ID);
        Optional<Patient> patientOpt = patientRepository.findById(PATIENT_PEDROMARTINEZ_ID);

        assertTrue(doctorOpt.isPresent(), "The doctor must exist");
        assertTrue(patientOpt.isPresent(), "The patient must exist");

        Doctor doctor = doctorOpt.get();
        Patient patient = patientOpt.get();

        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setDateTime(null); // DateTime is mandatory
        appointment.setReasons("Appointment without DateTime");

        assertThrows(DataIntegrityViolationException.class, () -> appointmentRepository.saveAndFlush(appointment),
                "Saving an appointment without DateTime should throw an exception");
    }

    @Test
    void deleteAppointment_whenAppointmentDoesNotExist_ShouldNotThrowException() {
        UUID nonExistentId = UUID.randomUUID();
        assertDoesNotThrow(() -> appointmentRepository.deleteById(nonExistentId),
                "Removing a non-existent appointment should not throw an exception");
    }

}
