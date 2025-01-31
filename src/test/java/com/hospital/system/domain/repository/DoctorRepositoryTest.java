package com.hospital.system.domain.repository;

import com.hospital.system.domain.entity.Doctor;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class DoctorRepositoryTest {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private EntityManager entityManager;


    @Test
    void findAll_whenDoctorExist_ReturnDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();

        assertEquals(5, doctors.size());
    }

    @Test
    void saveDoctor_withoutId_ShouldGenerateId(){
        Doctor doctor = new Doctor(null, "test","test","test","test@test.com",1234567L);
        Doctor doctor1 = doctorRepository.save(doctor);
        assertNotNull(doctor1.getId());
    }

    @Test
    void countEntities_ShouldReturnCorrectCount() {
        long initialCount = doctorRepository.count();
        Doctor doctor = new Doctor(null, "test","test","test","test@test.com",1234567L);
        doctorRepository.saveAndFlush(doctor);

        long updatedCount = doctorRepository.count();
        assertEquals(initialCount + 1, updatedCount );
    }


    @Test
    void FindById_whenDoctorExist_ReturnDoctor() {
        Optional<Doctor> doctor = doctorRepository.findById(UUID.fromString("123e4567-e89b-12d3-a456-426614174004"));
        assertTrue(doctor.isPresent());
    }

    @Test
    void findById_whenDoctorNotExist_ReturnEmpty() {

        Optional<Doctor> doctor = doctorRepository.findById(UUID.randomUUID());
        assertEquals(Optional.empty(), doctor);

    }

    @Test
    void saveDoctor_whenDoctorNotExist_ReturnDoctor() {
        Doctor doctorTest = new Doctor(null,"test" ,"test","123456789","test@test.com", 123456789L);
        Doctor doctorSaved = doctorRepository.save(doctorTest);
        assertNotNull(doctorSaved);

        Optional<Doctor> doctor = doctorRepository.findById(doctorSaved.getId());
        assertTrue(doctor.isPresent());
        assertEquals("test", doctor.get().getName());

    }

    @Test
    void saveDoctor_whenDoctorExist_ThrowException() {
        Doctor doctorTest1 = new Doctor(null,"test" ,"test","123456789","test@test.com", 123L );
        doctorRepository.saveAndFlush(doctorTest1);

        Doctor doctorTest2 = new Doctor(null,"test" ,"test","123456789","test@test.com", 123L );
        assertThrows(DataIntegrityViolationException.class, () -> doctorRepository.saveAndFlush(doctorTest2));
    }

    @Test
    void updateDoctor_whenDoctorExist_ReturnDoctor() {
        UUID doctorId = UUID.fromString("123e4567-e89b-12d3-a456-426614174004");
        Doctor doctor = new Doctor(doctorId, "test", "test","123456789","test@test.com", 123456789L );
        doctorRepository.saveAndFlush(doctor);
        Optional<Doctor> doctorUpdated = doctorRepository.findById(doctorId);
        assertTrue(doctorUpdated.isPresent());

        assertEquals("test",doctorUpdated.get().getName(),"The name of the doctor should be updated");
        assertEquals("test",doctorUpdated.get().getSpecialty(),"The specialty of the doctor should be updated");
        assertEquals("123456789",doctorUpdated.get().getPhone(),"The phone number of the doctor should be updated");
        assertEquals("test@test.com",doctorUpdated.get().getEmail(),"The email of the doctor should be updated");
        assertEquals(123456789L,doctorUpdated.get().getDni(),"The DNI of the doctor should be updated");
    }

    @Test
    void testDeleteDoctor() {
        UUID doctorId = UUID.fromString("123e4567-e89b-12d3-a456-426614174004");
        doctorRepository.deleteById(doctorId);
        Optional<Doctor> doctor = doctorRepository.findById(doctorId);

        assertFalse(doctor.isPresent());
    }

    @Test
    void saveDoctor_withNullName_ShouldThrowException() {
        Doctor doctor = new Doctor(null, null, "test", "123456789", "test@test.com", 123456789L);
        assertThrows(DataIntegrityViolationException.class, () -> doctorRepository.saveAndFlush(doctor), "Saving a null name should throw an exception");
    }
    @Test
    void saveDoctor_withNullSpecialty_ShouldThrowException() {
        Doctor doctor = new Doctor(null, "Dr. Test", null, "123456789", "test@test.com", 123456789L);
        assertThrows(DataIntegrityViolationException.class, () -> doctorRepository.saveAndFlush(doctor), "Saving a null specialty should throw an exception");
    }

    @Test
    void saveDoctor_withNullPhone_ShouldThrowException() {
        Doctor doctor = new Doctor(null, "Dr. Test", "test", null, "test@test.com", 123456789L);
        assertThrows(DataIntegrityViolationException.class, () -> doctorRepository.saveAndFlush(doctor), "Saving a null phone should throw an exception");
    }

    @Test
    void saveDoctor_withNullEmail_ShouldThrowException() {
        Doctor doctor = new Doctor(null, "Dr. Test", "test", "123456789", null, 123456789L);
        assertThrows(DataIntegrityViolationException.class, () -> doctorRepository.saveAndFlush(doctor), "Saving a null email should throw an exception");
    }

    @Test
    void saveDoctor_withNullDNI_ShouldThrowException() {
        Doctor doctor = new Doctor(null, "Dr. Test", "test", "123456789", "test@test.com", null);
        assertThrows(DataIntegrityViolationException.class, () -> doctorRepository.saveAndFlush(doctor), "Saving a null DNI should throw an exception");
    }

    @Test
    void deleteDoctor_whenDoctorDoesNotExist_ShouldNotThrowException() {
        UUID nonExistentId = UUID.randomUUID();
        assertDoesNotThrow(() -> doctorRepository.deleteById(nonExistentId), "Trying to delete a non-existent doctor should throw an exception");
    }

    @Test
    void transactionalSave_whenExceptionThrown_ShouldRollback() {
        Doctor doctor1 = new Doctor(null, "Doctor1", "Specialty1", "1111111111", "doctor1@domain.com", 111111111L);
        doctorRepository.saveAndFlush(doctor1);

        Doctor doctor2 = new Doctor(null, "Doctor2", "Specialty2", "2222222222", "doctor1@domain.com", 222222222L); // Email duplicado

        assertThrows(DataIntegrityViolationException.class, () -> doctorRepository.saveAndFlush(doctor2), "Guardar una entidad con email duplicado debería lanzar una excepción.");
        entityManager.clear();

        // Verificar que doctor2 no se ha guardado debido al rollback
        Optional<Doctor> fetchedDoctor2 = doctorRepository.findByEmail("doctor2@domain.com");
        assertFalse(fetchedDoctor2.isPresent(), "La segunda entidad no debería haberse guardado debido al rollback.");
    }

}
