package com.hospital.system.service.serviceimpl;

import com.hospital.system.domain.entity.Appointment;
import com.hospital.system.domain.repository.AppointmentRepository;
import com.hospital.system.exception.ResourceNotFoundException;
import com.hospital.system.mapper.AppointmentMapper;
import com.hospital.system.service.AppointmentService;
import com.hospital.system.service.DoctorService;
import com.hospital.system.service.PatientService;
import com.hospital.system.web.dto.request.AppointmentRequestDTO;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    final AppointmentMapper appointmentMapper;
    final AppointmentRepository appointmentRepository;
    final DoctorService doctorService;
    final PatientService patientService;

    public AppointmentServiceImpl(AppointmentMapper appointmentMapper,
                                  AppointmentRepository appointmentRepository,
                                  DoctorService doctorService,
                                  PatientService patientService) {
        this.appointmentMapper = appointmentMapper;
        this.appointmentRepository = appointmentRepository;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    @Override
    public List<Appointment> findAllAppointments() {
        return appointmentRepository.findAll();
    }

    @Override
    public Appointment findAppointmentById(UUID id) {
        return appointmentRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Appointment with id: " + id + " not found"));
    }

    @Override
    @Transactional
    public Appointment saveAppointment(@Valid AppointmentRequestDTO appointmentDTO) {
        Appointment appointment = appointmentMapper.toEntity(appointmentDTO);
        return appointmentRepository.save(appointment);

    }

    @Override
    @Transactional
    public void deleteAppointment(UUID id) {
        existsAppointmentById(id);
        appointmentRepository.deleteById(id);
    }

    private void existsAppointmentById(UUID id) {
        if (!appointmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Appointment with id: " + id + " not found");
        }
    }

    @Override
    @Transactional
    public Appointment updateAppointment(AppointmentRequestDTO appointmentRequestDTO, UUID appointmentId) {
        existsAppointmentById(appointmentId);
        Appointment updatedAppointment = appointmentMapper.toEntity(appointmentRequestDTO);
        updatedAppointment.setId(appointmentId);

        return appointmentRepository.save(updatedAppointment);


    }

}
