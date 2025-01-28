package com.hospital.system.domain.repository;

import com.hospital.system.domain.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {

    boolean existsById(UUID id);
    Patient findByEmail(String email);
}
