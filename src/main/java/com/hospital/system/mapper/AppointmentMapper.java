package com.hospital.system.mapper;

import com.hospital.system.domain.entity.Appointment;
import com.hospital.system.web.dto.request.AppointmentRequestDTO;
import com.hospital.system.web.dto.response.AppointmentResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    AppointmentMapper INSTANCE = Mappers.getMapper(AppointmentMapper.class);

    Appointment toEntity(AppointmentRequestDTO appointmentRequestDTO);

    AppointmentResponseDTO toResponseDTO(Appointment appointment);


}
