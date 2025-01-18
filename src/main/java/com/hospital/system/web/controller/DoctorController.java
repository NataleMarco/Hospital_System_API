package com.hospital.system.web.controller;

import com.hospital.system.domain.entity.Doctor;
import com.hospital.system.exception.ResourceNotFoundException;
import com.hospital.system.service.DoctorService;
import com.hospital.system.web.dto.DoctorDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctors")
@Log4j2
public class DoctorController {

    final DoctorService doctorService;
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }


    @GetMapping
    public ResponseEntity<List<Doctor>> getAllDoctors(){
        List<Doctor> doctors = doctorService.findAllDoctors();

        if(doctors.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable Long id){

        Doctor doctor = doctorService.findDoctorById(id).orElseThrow(()-> new ResourceNotFoundException("Doctor with id: " + id + " Not found"));

        return new ResponseEntity<>(doctor, HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Doctor> deleteDoctorById(@PathVariable Long id){
        Doctor doctor = doctorService.findDoctorById(id).orElseThrow(()-> new ResourceNotFoundException("Doctor with id: " + id + " Not found"));
        doctorService.deleteDoctor(id);

        return new ResponseEntity<>(doctor, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Doctor> addDoctor(@RequestBody DoctorDTO dto){
        Doctor doctor = doctorService.saveDoctor(dto);
        return new ResponseEntity<>(doctor, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Doctor> updateDoctor(@RequestBody DoctorDTO dto, @PathVariable Long id){
        Doctor doctor = doctorService.updateDoctor(dto,id);
        return new ResponseEntity<>(doctor, HttpStatus.OK);
    }
}