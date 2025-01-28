package com.hospital.system.domain.repository;

import com.hospital.system.domain.entity.Patient;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PatientRepositoryTest {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void findAll_whenPatientsExist_ReturnPatients() {
        List<Patient> patients = patientRepository.findAll();

        assertEquals(5, patients.size());
    }

    @Test
    void savePatient_withoutId_ShouldGenerateId(){
        Patient patient = new Patient();
        patient.setName("Juan Pérez");
        patient.setAddress("Calle Falsa 123");
        patient.setPhone("5551234567");
        patient.setEmail("juan.perez@example.com");
        patient.setDateOfBirth(LocalDate.of(1990, 1, 1));

        Patient savedPatient = patientRepository.save(patient);
        assertNotNull(savedPatient.getId());
    }

    @Test
    void countEntities_ShouldReturnCorrectCount() {
        long initialCount = patientRepository.count();
        Patient patient = new Patient();
        patient.setName("María López");
        patient.setAddress("Avenida Siempre Viva 742");
        patient.setPhone("5557654321");
        patient.setEmail("maria.lopez@example.com");
        patient.setDateOfBirth(LocalDate.of(1985, 5, 15));

        patientRepository.saveAndFlush(patient);

        long updatedCount = patientRepository.count();
        assertEquals(initialCount + 1, updatedCount);
    }

    @Test
    void findById_whenPatientExist_ReturnPatient() {
        UUID patientId = UUID.fromString("123e4567-e89b-12d3-a456-426614174005"); // Asegúrate de que este ID exista en tus datos de prueba
        Optional<Patient> patient = patientRepository.findById(patientId);
        assertTrue(patient.isPresent());
    }

    @Test
    void findById_whenPatientNotExist_ReturnEmpty() {
        Optional<Patient> patient = patientRepository.findById(UUID.randomUUID());
        assertEquals(Optional.empty(), patient);
    }

    @Test
    void savePatient_whenPatientNotExist_ReturnPatient() {
        Patient patientTest = new Patient();
        patientTest.setName("Carlos Gómez");
        patientTest.setAddress("Boulevard de los Sueños Rotos 456");
        patientTest.setPhone("5559876543");
        patientTest.setEmail("carlos.gomez@example.com");
        patientTest.setDateOfBirth(LocalDate.of(1975, 12, 30));

        Patient patientSaved = patientRepository.save(patientTest);
        assertNotNull(patientSaved);

        Optional<Patient> fetchedPatient = patientRepository.findById(patientSaved.getId());
        assertTrue(fetchedPatient.isPresent());
        assertEquals("Carlos Gómez", fetchedPatient.get().getName());
    }

    @Test
    void savePatient_whenPatientExist_ThrowException() {
        Patient patientTest1 = new Patient();
        patientTest1.setName("Ana María");
        patientTest1.setAddress("Camino Real 789");
        patientTest1.setPhone("5551122334");
        patientTest1.setEmail("ana.maria@example.com");
        patientTest1.setDateOfBirth(LocalDate.of(1992, 7, 20));
        patientRepository.saveAndFlush(patientTest1);

        Patient patientTest2 = new Patient();
        patientTest2.setName("Ana María");
        patientTest2.setAddress("Camino Real 789");
        patientTest2.setPhone("5551122334");
        patientTest2.setEmail("ana.maria@example.com");
        patientTest2.setDateOfBirth(LocalDate.of(1992, 7, 20));

        assertThrows(DataIntegrityViolationException.class, () -> patientRepository.saveAndFlush(patientTest2));
    }

    @Test
    void updatePatient_whenPatientExist_ReturnPatient() {
        UUID patientId = UUID.fromString("123e4567-e89b-12d3-a456-426614174005");
        Optional<Patient> existingPatientOpt = patientRepository.findById(patientId);
        assertTrue(existingPatientOpt.isPresent());

        Patient existingPatient = existingPatientOpt.get();
        existingPatient.setName("Updated Name");
        existingPatient.setAddress("Nueva Dirección 321");
        existingPatient.setPhone("5550001111");
        existingPatient.setEmail("updated.email@example.com");
        existingPatient.setDateOfBirth(LocalDate.of(1991, 2, 2));

        patientRepository.saveAndFlush(existingPatient);

        Optional<Patient> updatedPatientOpt = patientRepository.findById(patientId);
        assertTrue(updatedPatientOpt.isPresent());

        Patient updatedPatient = updatedPatientOpt.get();
        assertEquals("Updated Name", updatedPatient.getName(), "El nombre del paciente debería estar actualizado");
        assertEquals("Nueva Dirección 321", updatedPatient.getAddress(), "La dirección del paciente debería estar actualizada");
        assertEquals("5550001111", updatedPatient.getPhone(), "El teléfono del paciente debería estar actualizado");
        assertEquals("updated.email@example.com", updatedPatient.getEmail(), "El email del paciente debería estar actualizado");
        assertEquals(LocalDate.of(1991, 2, 2), updatedPatient.getDateOfBirth(), "La fecha de nacimiento del paciente debería estar actualizada");
    }

    @Test
    void testDeletePatient() {
        UUID patientId = UUID.fromString("123e4567-e89b-12d3-a456-426614174005"); // Asegúrate de que este ID exista en tus datos de prueba
        patientRepository.deleteById(patientId);
        Optional<Patient> patient = patientRepository.findById(patientId);

        assertFalse(patient.isPresent());
    }

    @Test
    void savePatient_withNullName_ShouldThrowException() {
        Patient patient = new Patient();
        patient.setName(null);
        patient.setAddress("Calle Sin Nombre 456");
        patient.setPhone("5553332222");
        patient.setEmail("noname@example.com");
        patient.setDateOfBirth(LocalDate.of(2000, 1, 1));

        assertThrows(DataIntegrityViolationException.class, () -> patientRepository.saveAndFlush(patient), "Guardar un nombre nulo debería lanzar una excepción");
    }

    @Test
    void savePatient_withNullAddress_ShouldThrowException() {
        Patient patient = new Patient();
        patient.setName("Nombre Valido");
        patient.setAddress(null);
        patient.setPhone("5553332222");
        patient.setEmail("noaddress@example.com");
        patient.setDateOfBirth(LocalDate.of(2000, 1, 1));

        assertThrows(DataIntegrityViolationException.class, () -> patientRepository.saveAndFlush(patient), "Guardar una dirección nula debería lanzar una excepción");
    }

    @Test
    void savePatient_withNullPhone_ShouldThrowException() {
        Patient patient = new Patient();
        patient.setName("Nombre Valido");
        patient.setAddress("Calle Valida 789");
        patient.setPhone(null);
        patient.setEmail("nophone@example.com");
        patient.setDateOfBirth(LocalDate.of(2000, 1, 1));

        assertThrows(DataIntegrityViolationException.class, () -> patientRepository.saveAndFlush(patient), "Guardar un teléfono nulo debería lanzar una excepción");
    }

    @Test
    void savePatient_withNullEmail_ShouldThrowException() {
        Patient patient = new Patient();
        patient.setName("Nombre Valido");
        patient.setAddress("Calle Valida 789");
        patient.setPhone("5553332222");
        patient.setEmail(null);
        patient.setDateOfBirth(LocalDate.of(2000, 1, 1));

        assertThrows(DataIntegrityViolationException.class, () -> patientRepository.saveAndFlush(patient), "Guardar un email nulo debería lanzar una excepción");
    }

    @Test
    void savePatient_withNullDateOfBirth_ShouldThrowException() {
        Patient patient = new Patient();
        patient.setName("Nombre Valido");
        patient.setAddress("Calle Valida 789");
        patient.setPhone("5553332222");
        patient.setEmail("validemail@example.com");
        patient.setDateOfBirth(null);

        assertThrows(DataIntegrityViolationException.class, () -> patientRepository.saveAndFlush(patient), "Guardar una fecha de nacimiento nula debería lanzar una excepción");
    }

    @Test
    void deletePatient_whenPatientDoesNotExist_ShouldNotThrowException() {
        UUID nonExistentId = UUID.randomUUID();
        assertDoesNotThrow(() -> patientRepository.deleteById(nonExistentId), "Intentar borrar un paciente inexistente no debería lanzar una excepción");
    }

    @Test
    void transactionalSave_whenExceptionThrown_ShouldRollback() {
        Patient patient1 = new Patient();
        patient1.setName("Paciente Uno");
        patient1.setAddress("Dirección Uno");
        patient1.setPhone("5551112222");
        patient1.setEmail("paciente1@example.com");
        patient1.setDateOfBirth(LocalDate.of(1995, 3, 10));
        patientRepository.saveAndFlush(patient1);

        Patient patient2 = new Patient();
        patient2.setName("Paciente Dos");
        patient2.setAddress("Dirección Dos");
        patient2.setPhone("5553334444");
        patient2.setEmail("paciente1@example.com"); // Email duplicado para provocar excepción
        patient2.setDateOfBirth(LocalDate.of(1996, 4, 20));

        assertThrows(DataIntegrityViolationException.class, () -> patientRepository.saveAndFlush(patient2), "Guardar una entidad con email duplicado debería lanzar una excepción.");
        entityManager.clear();

        // Verificar que patient2 no se ha guardado debido al rollback
        Patient fetchedPatient2 = patientRepository.findByEmail("paciente2@example.com");
        assertNull(fetchedPatient2);
    }
}
