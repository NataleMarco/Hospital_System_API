package com.hospital.system.web.controller;

import com.hospital.system.domain.entity.Appointment;
import com.hospital.system.exception.ResourceNotFoundException;
import com.hospital.system.service.AppointmentService;
import com.hospital.system.web.dto.AppointmentDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        if(!appointments.isEmpty()){
            return new ResponseEntity<>(appointments, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> findById(@PathVariable Long id){

        Appointment appointment = appointmentService.findAppointmentById(id).orElseThrow(()->new ResourceNotFoundException("Appointment with id: " + id + " Not Found"));

        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Appointment> createAppointment(@RequestBody AppointmentDTO appointmentDTO){
        Appointment appointment = appointmentService.saveAppointment(appointmentDTO);
        return new  ResponseEntity<>(appointment, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Appointment> deleteAppointment(@PathVariable Long id){

        Appointment appointment = appointmentService.findAppointmentById(id).orElseThrow(() -> new ResourceNotFoundException("Appointment with id: " + id + " Not found"));
        appointmentService.deleteAppointment(id);

        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Appointment> updateAppointment(@PathVariable Long id, @RequestBody AppointmentDTO appointmentDTO){

        Appointment appointment = appointmentService.updateAppointment(appointmentDTO, id);
        return new ResponseEntity<>(appointment, HttpStatus.OK);

    }



}
