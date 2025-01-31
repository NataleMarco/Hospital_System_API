package com.hospital.system.service;

import com.hospital.system.domain.entity.Appointment;
import com.hospital.system.web.dto.request.AppointmentRequestDTO;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

public interface AppointmentService {
    List<Appointment> findAllAppointments();
    Appointment findAppointmentById(UUID id);
    Appointment saveAppointment (@Valid AppointmentRequestDTO appointmentDTO);
    void deleteAppointment(UUID id);
    Appointment updateAppointment(AppointmentRequestDTO appointmentRequestDTO, UUID id);
    void existAppointmentByDoctorId(UUID doctorId);
    void existAppointmentByPatientId(UUID patientId);

}
