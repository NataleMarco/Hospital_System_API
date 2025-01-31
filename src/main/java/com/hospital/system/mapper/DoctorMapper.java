package com.hospital.system.mapper;

import com.hospital.system.domain.entity.Doctor;
import com.hospital.system.web.dto.request.DoctorRequestDTO;
import com.hospital.system.web.dto.response.DoctorResponseDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DoctorMapper {

    Doctor toEntity(DoctorRequestDTO doctorRequestDTO);
    DoctorRequestDTO toResponseDTO(Doctor doctor);
    List<DoctorResponseDTO> toResponseDTOs(List<Doctor> doctors);

}
