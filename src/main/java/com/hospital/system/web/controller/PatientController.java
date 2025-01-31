package com.hospital.system.web.controller;

import com.hospital.system.domain.entity.Patient;
import com.hospital.system.mapper.PatientMapper;
import com.hospital.system.service.PatientService;
import com.hospital.system.web.dto.request.PatientRequestDTO;
import com.hospital.system.web.dto.response.PatientResponseDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patients")
@Log4j2
public class PatientController {

    final PatientService patientService;
    final PatientMapper patientMapper;

    public PatientController(PatientService patientService,
                             PatientMapper patientMapper) {
        this.patientService = patientService;
        this.patientMapper = patientMapper;
    }


    @GetMapping
    public ResponseEntity<List<PatientResponseDTO>> findAll(){
        List<Patient> patients = patientService.findAllPatients();
        List<PatientResponseDTO> responsePatients = patientMapper.toResponseDTOs(patients);

        return new ResponseEntity<>(responsePatients, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> getPatientById(@PathVariable UUID id){
        Patient patient = patientService.findPatientById(id);
        PatientResponseDTO patientResponseDTO = patientMapper.toResponseDTO(patient);

        return new ResponseEntity<>(patientResponseDTO, HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> deletePatientById(@PathVariable UUID id){
        patientService.deletePatient(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<PatientResponseDTO> addPatient(@RequestBody PatientRequestDTO patientRequestDTO){
        Patient patient = patientService.createPatient(patientRequestDTO);
        PatientResponseDTO responseDTO = patientMapper.toResponseDTO(patient);

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{patientId}")
    public ResponseEntity<PatientResponseDTO> updatePatient(@RequestBody PatientRequestDTO patientRequestDTO, @PathVariable UUID patientId){
        Patient patient = patientService.updatePatient(patientRequestDTO, patientId);
        PatientResponseDTO patientResponseDTO = patientMapper.toResponseDTO(patient);

        return new ResponseEntity<>(patientResponseDTO, HttpStatus.OK);
    }
}