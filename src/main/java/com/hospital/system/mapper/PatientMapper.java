package com.hospital.system.mapper;

import com.hospital.system.domain.entity.Patient;
import com.hospital.system.web.dto.request.PatientRequestDTO;
import com.hospital.system.web.dto.response.PatientResponseDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    Patient toEntity(PatientRequestDTO patientRequestDTO);
    PatientResponseDTO toResponseDTO(Patient patient);
    List<PatientResponseDTO> toResponseDTOs(List<Patient> patients);



}
