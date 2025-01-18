package com.hospital.system.web.controller;

import com.hospital.system.domain.entity.Patient;
import com.hospital.system.exception.ResourceNotFoundException;
import com.hospital.system.service.PatientService;
import com.hospital.system.web.dto.PatientDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
@Log4j2
public class PatientController {

    final PatientService patientService;

    public PatientController(PatientService patientService ) {
        this.patientService = patientService;
    }


    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients(){
        List<Patient> patients = patientService.findAllPatients();
        if(patients.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id){
        Patient patient = patientService.findPatientById(id).orElseThrow(()-> new ResourceNotFoundException("Patient with id: " + id + " Not found"));
        return new ResponseEntity<>(patient, HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Patient> deletePatientById(@PathVariable Long id){
        Patient patient = patientService.findPatientById(id).orElseThrow(()-> new ResourceNotFoundException("Patient with id: " + id + " Not found"));
        patientService.deletePatient(id);
        return new ResponseEntity<>(patient, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Patient> addPatient(@RequestBody PatientDTO dto){
        Patient patient = patientService.createPatient(dto);
        return new ResponseEntity<>(patient, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(@RequestBody PatientDTO dto, @PathVariable Long id){
        Patient patient = patientService.updatePatient(dto,id);
        return new ResponseEntity<>(patient, HttpStatus.OK);
    }
}