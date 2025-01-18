package com.hospital.system.service.serviceimpl;

import com.hospital.system.domain.entity.Appointment;
import com.hospital.system.domain.entity.Doctor;
import com.hospital.system.domain.entity.Patient;
import com.hospital.system.domain.repository.AppointmentRepository;
import com.hospital.system.exception.ResourceNotFoundException;
import com.hospital.system.service.AppointmentService;
import com.hospital.system.service.DoctorService;
import com.hospital.system.service.PatientService;
import com.hospital.system.web.dto.AppointmentDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    final AppointmentRepository appointmentRepository;
    final DoctorService doctorService;
    final PatientService patientService;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, DoctorService doctorService, PatientService patientService) {
        this.appointmentRepository = appointmentRepository;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    @Override
    public List<Appointment> findAllAppointments() {
        return appointmentRepository.findAll();
    }

    @Override
    public Optional<Appointment> findAppointmentById(Long id) {
        return appointmentRepository.findById(id);
    }

    @Override
    @Transactional
    public Appointment saveAppointment(AppointmentDTO appointmentDTO) {
        Appointment appointment = new Appointment();

        Doctor doctor = doctorService.findDoctorById(appointmentDTO.getDoctorId()).orElseThrow(() -> new IllegalArgumentException("Doctor not found"));
        Patient patient = patientService.findPatientById(appointmentDTO.getPatientId()).orElseThrow(() -> new IllegalArgumentException("Patient not found"));


        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setDateTime(appointmentDTO.getDateTime());
        appointment.setReasons(appointmentDTO.getReasons());

        return appointmentRepository.save(appointment);

    }

    @Override
    @Transactional
    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Appointment updateAppointment(AppointmentDTO appointmentFormDTO, Long id) {

        Appointment appointment = findAppointmentById(id).orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
        Appointment updatedAppointment = convertFromDTO(appointmentFormDTO);
        updatedAppointment.setId(appointment.getId());

        return appointmentRepository.save(updatedAppointment);


    }

    private Appointment convertFromDTO(AppointmentDTO appointmentDTO) {

        Doctor doctor = doctorService.findDoctorById(appointmentDTO.getDoctorId()).orElseThrow(() -> new ResourceNotFoundException("Doctor with id: " + appointmentDTO.getDoctorId() + " not found"));
        Patient patient = patientService.findPatientById(appointmentDTO.getPatientId()).orElseThrow(() -> new ResourceNotFoundException("Patient with id: " + appointmentDTO.getPatientId() + " not found"));

        Appointment appointment = new Appointment();
        appointment.setDateTime(appointmentDTO.getDateTime());
        appointment.setReasons(appointmentDTO.getReasons());
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);

        return appointment;
    }
}
