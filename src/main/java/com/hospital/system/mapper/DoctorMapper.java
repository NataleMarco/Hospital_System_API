package com.hospital.system.mapper;

import com.hospital.system.domain.entity.Doctor;
import com.hospital.system.web.dto.request.DoctorRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
    DoctorMapper INSTANCE = Mappers.getMapper(DoctorMapper.class);

    Doctor toEntity(DoctorRequestDTO doctorRequestDTO);

    DoctorRequestDTO toResponseDTO(Doctor doctor);

}
