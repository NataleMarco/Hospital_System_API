package com.hospital.system.domain.repository;

import com.hospital.system.domain.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    List<Appointment> findAppointmentsByDoctorId(UUID doctorId);
    boolean existsAppoitmentByDoctorId(UUID doctorId);
    boolean existsAppoitmentByPatientId(UUID doctorId);

    List<Appointment> findAppointmentsByPatientId(UUID patientId);

    @Override
    boolean existsById(UUID uuid);
}
