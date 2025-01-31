package com.hospital.system.service.serviceimpl;

import com.hospital.system.domain.entity.Appointment;
import com.hospital.system.domain.entity.Doctor;
import com.hospital.system.domain.entity.Patient;
import com.hospital.system.domain.repository.AppointmentRepository;
import com.hospital.system.exception.AppointmentNotFoundException;
import com.hospital.system.exception.DoctorAssociatedAppointmentsException;
import com.hospital.system.exception.PatientAssociatedAppointmentsException;
import com.hospital.system.mapper.AppointmentMapper;
import com.hospital.system.service.AppointmentService;
import com.hospital.system.service.DoctorService;
import com.hospital.system.service.PatientService;
import com.hospital.system.web.dto.request.AppointmentRequestDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    @Transactional(readOnly = true)
    @Override
    public List<Appointment> findAllAppointments() {
        return appointmentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Appointment findAppointmentById(UUID appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .orElseThrow(()-> new AppointmentNotFoundException(appointmentId));
    }

    @Override
    @Transactional
    public Appointment saveAppointment(AppointmentRequestDTO appointmentDTO) {

        Doctor doctor = doctorService.findDoctorById(appointmentDTO.getDoctorId());
        Patient patient = patientService.findPatientById(appointmentDTO.getPatientId());

        Appointment appointment = appointmentMapper.toEntity(appointmentDTO);
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);

        return appointmentRepository.save(appointment);
    }

    @Override
    @Transactional
    public void deleteAppointment(UUID id) {
        existsAppointmentById(id);
        appointmentRepository.deleteById(id);
    }


    @Override
    @Transactional
    public Appointment updateAppointment(AppointmentRequestDTO appointmentRequestDTO, UUID appointmentId) {
        existsAppointmentById(appointmentId);

        Patient patient = patientService.findPatientById(appointmentRequestDTO.getPatientId());
        Doctor doctor = doctorService.findDoctorById(appointmentRequestDTO.getDoctorId());

        Appointment updatedAppointment = appointmentMapper.toEntity(appointmentRequestDTO);
        updatedAppointment.setPatient(patient);
        updatedAppointment.setDoctor(doctor);
        updatedAppointment.setId(appointmentId);
        return appointmentRepository.save(updatedAppointment);

    }

    @Override
    public void existAppointmentByDoctorId(UUID doctorId) {
        if(appointmentRepository.existsAppoitmentByDoctorId(doctorId)){
            throw new DoctorAssociatedAppointmentsException(doctorId);
        }
    }

    @Override
    public void existAppointmentByPatientId(UUID patientId) {
        if(appointmentRepository.existsAppoitmentByPatientId(patientId)){
            throw new PatientAssociatedAppointmentsException(patientId);
        }
    }

    private void existsAppointmentById(UUID appointmentId) {
        if (!appointmentRepository.existsById(appointmentId)) {
            throw new AppointmentNotFoundException(appointmentId);
        }
    }
}
