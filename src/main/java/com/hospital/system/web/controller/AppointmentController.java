package com.hospital.system.web.controller;

import com.hospital.system.domain.entity.Appointment;
import com.hospital.system.service.AppointmentService;
import com.hospital.system.web.dto.request.AppointmentRequestDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    final AppointmentService appointmentService;
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping()
    public ResponseEntity<List<Appointment>> findAll(){
        List<Appointment> appointments = appointmentService.findAllAppointments();
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> findById(@PathVariable UUID id){
        Appointment appointment = appointmentService.findAppointmentById(id);
        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Appointment> createAppointment(@Valid @RequestBody AppointmentRequestDTO appointmentDTO){
        Appointment appointment = appointmentService.saveAppointment(appointmentDTO);
        return new  ResponseEntity<>(appointment, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Appointment> deleteAppointment(@PathVariable UUID id){

        Appointment appointment = appointmentService.findAppointmentById(id);
        appointmentService.deleteAppointment(id);

        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

    @PutMapping("/{appointmentId}")
    public ResponseEntity<Appointment> updateAppointment( @Valid @RequestBody AppointmentRequestDTO appointmentRequestDTO,@PathVariable UUID appointmentId ){

        Appointment appointment = appointmentService.updateAppointment(appointmentRequestDTO, appointmentId);
        return new ResponseEntity<>(appointment, HttpStatus.OK);

    }



}
