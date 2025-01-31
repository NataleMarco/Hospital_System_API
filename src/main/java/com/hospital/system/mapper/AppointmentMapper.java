package com.hospital.system.mapper;

import com.hospital.system.domain.entity.Appointment;
import com.hospital.system.web.dto.request.AppointmentRequestDTO;
import com.hospital.system.web.dto.response.AppointmentResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {
    // Campos que SI se mapean automáticamente
    @Mapping(target = "dateTime", source = "dateTime")
    @Mapping(target = "reasons", source = "reasons")
    // Campos que NO se mapean automáticamente
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "id", ignore = true)
    Appointment toEntity(AppointmentRequestDTO appointmentRequestDTO);

    AppointmentResponseDTO toResponseDTO(Appointment appointment);
    List<AppointmentResponseDTO> toResponseDTOList(List<Appointment> appointments);


}
