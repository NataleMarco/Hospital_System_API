package com.hospital.system.service;

import com.hospital.system.domain.entity.Appointment;
import com.hospital.system.web.dto.AppointmentDTO;

import java.util.List;
import java.util.Optional;

public interface AppointmentService {
    List<Appointment> findAllAppointments();
    Optional<Appointment> findAppointmentById(Long id);
    Appointment saveAppointment (AppointmentDTO appointmentDTO);
    void deleteAppointment(Long id);
    Appointment updateAppointment(AppointmentDTO appointmentFormDTO, Long id);

}
