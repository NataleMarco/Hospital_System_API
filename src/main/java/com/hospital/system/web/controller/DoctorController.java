package com.hospital.system.web.controller;

import com.hospital.system.domain.entity.Doctor;
import com.hospital.system.service.DoctorService;
import com.hospital.system.web.dto.request.DoctorRequestDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/doctors")
//@Api(value = "Gestion de doctores", description = "Operaciones pertinentes a los doctores en el hospital")
public class DoctorController {

    final DoctorService doctorService;
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }


    @GetMapping
    //@ApiOperation(value = "Obtiene la lista de todos los doctores", response = List.class)
    public ResponseEntity<List<Doctor>> findAll(){
        List<Doctor> doctors = doctorService.findAllDoctors();
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Doctor> findDoctorById(@PathVariable UUID id){

        Doctor doctor = doctorService.findDoctorById(id);

        return new ResponseEntity<>(doctor, HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Doctor> deleteDoctorById(@PathVariable UUID id){
        Doctor doctor = doctorService.findDoctorById(id);
        doctorService.deleteDoctor(id);

        return new ResponseEntity<>(doctor, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Doctor> addDoctor(@Valid @RequestBody DoctorRequestDTO doctorRequestDTO){
        Doctor doctor = doctorService.saveDoctor(doctorRequestDTO);
        return new ResponseEntity<>(doctor, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Doctor> updateDoctor(@Valid @RequestBody DoctorRequestDTO doctorRequestDTO, @PathVariable UUID id){
        Doctor doctor = doctorService.updateDoctor(doctorRequestDTO,id);
        return new ResponseEntity<>(doctor, HttpStatus.OK);
    }
}