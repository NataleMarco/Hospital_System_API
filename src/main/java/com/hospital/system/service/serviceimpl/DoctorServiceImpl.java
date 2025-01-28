package com.hospital.system.service.serviceimpl;

import com.hospital.system.domain.entity.Doctor;
import com.hospital.system.domain.repository.DoctorRepository;
import com.hospital.system.exception.ResourceNotFoundException;
import com.hospital.system.mapper.DoctorMapper;
import com.hospital.system.service.DoctorService;
import com.hospital.system.web.dto.request.DoctorRequestDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DoctorServiceImpl implements DoctorService {

    final DoctorRepository doctorRepository;
    final DoctorMapper doctorMapper;


    public DoctorServiceImpl (DoctorRepository doctorRepository,
                              DoctorMapper doctorMapper) {
        this.doctorRepository = doctorRepository;
        this.doctorMapper = doctorMapper;
    }

    public String resourceNotFoundMessage(UUID id){
        return "Doctor with id: " + id + " not found!";
    }

    @Override
    public List<Doctor> findAllDoctors() {
        return doctorRepository.findAll();
         
    }


    @Override
    public Doctor findDoctorById(UUID id) {
        return doctorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(resourceNotFoundMessage(id)));
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

    private void existsDoctorById(UUID id) {
        if (!doctorRepository.existsById(id)) {
            throw new ResourceNotFoundException(resourceNotFoundMessage(id));
        }
    }
}
