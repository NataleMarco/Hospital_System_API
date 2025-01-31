package com.hospital.system.service.serviceimpl;

import com.hospital.system.domain.entity.Doctor;
import com.hospital.system.domain.repository.AppointmentRepository;
import com.hospital.system.domain.repository.DoctorRepository;
import com.hospital.system.exception.DoctorAssociatedAppointmentsException;
import com.hospital.system.exception.DoctorNotFoundException;
import com.hospital.system.mapper.DoctorMapper;
import com.hospital.system.service.AppointmentService;
import com.hospital.system.service.DoctorService;
import com.hospital.system.web.dto.request.DoctorRequestDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class DoctorServiceImpl implements DoctorService {

    final DoctorRepository doctorRepository;
    final DoctorMapper doctorMapper;
    final AppointmentRepository appointmentRepository  ;


    public DoctorServiceImpl (DoctorRepository doctorRepository,
                              DoctorMapper doctorMapper,
                              AppointmentRepository appointmentRepository1) {

        this.doctorRepository = doctorRepository;
        this.doctorMapper = doctorMapper;
        this.appointmentRepository = appointmentRepository1;
    }


    @Transactional(readOnly = true)
    @Override
    public List<Doctor> findAllDoctors() {
        return doctorRepository.findAll();
         
    }


    @Transactional(readOnly = true)
    @Override
    public Doctor findDoctorById(UUID doctorId) {
        return doctorRepository.findById(doctorId).orElseThrow(() -> new DoctorNotFoundException(doctorId));
    }

    @Override
    @Transactional
    public Doctor saveDoctor(DoctorRequestDTO doctorDTO) {

        Doctor doctor = doctorMapper.toEntity(doctorDTO);
        return doctorRepository.save(doctor);
    }

    @Override
    @Transactional
    public void deleteDoctor(UUID id) {
        existsDoctorById(id);
        if (appointmentRepository.existsAppoitmentByDoctorId(id)){
            throw new DoctorAssociatedAppointmentsException(id);
        }
        doctorRepository.deleteById(id);
    }


    @Override
    @Transactional
    public Doctor updateDoctor(DoctorRequestDTO doctorRequestDTO, UUID id) {
        existsDoctorById(id);
        Doctor updatedDoctor = doctorMapper.toEntity(doctorRequestDTO);
        updatedDoctor.setId(id);

        return doctorRepository.save(updatedDoctor);
    }

    private void existsDoctorById(UUID doctorId) {
        if (!doctorRepository.existsById(doctorId)) {
            throw new DoctorNotFoundException(doctorId);
        }
    }
}
