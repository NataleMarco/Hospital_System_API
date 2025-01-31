package com.hospital.system.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital.system.domain.entity.Patient;
import com.hospital.system.exception.PatientAssociatedAppointmentsException;
import com.hospital.system.mapper.PatientMapper;
import com.hospital.system.service.PatientService;
import com.hospital.system.web.dto.request.PatientRequestDTO;
import com.hospital.system.web.dto.response.PatientResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PatientController.class)
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PatientService patientService;

    @MockitoBean
    private PatientMapper patientMapper;

    private Patient patient;
    private PatientResponseDTO patientResponseDTO;
    private List<Patient> patientList;
    private List<PatientResponseDTO> patientResponseDTOList;

    @BeforeEach
    void setUp() {

        patient = new Patient();
        patient.setId(UUID.randomUUID());
        patient.setName("Jose Luis");
        patient.setAddress("Avenida siempreviva 742");
        patient.setPhone("1112345678");
        patient.setEmail("test@patient.com");
        patient.setDateOfBirth(LocalDate.of(1994, 7,12));



        patient = new Patient();
        patient.setId(UUID.randomUUID());
        patient.setName("John Doe");

        patientResponseDTO = new PatientResponseDTO();
        patientResponseDTO.setId(patient.getId());
        patientResponseDTO.setName(patient.getName());
        patientResponseDTO.setEmail(patient.getEmail());
        patientResponseDTO.setAddress(patient.getAddress());
        patientResponseDTO.setPhone(patient.getPhone());

        patientList = List.of(patient);
        patientResponseDTOList = List.of(patientResponseDTO);
    }

    @Test
    void findAll_whenPatientsExist_thenReturnPatientsList() throws Exception {
        when(patientService.findAllPatients()).thenReturn(patientList);
        when(patientMapper.toResponseDTOs(patientList)).thenReturn(patientResponseDTOList);
        mockMvc.perform(get("/patients")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(patientResponseDTOList)))
                .andDo(print());
    }

    @Test
    void getPatientById_whenPatientExists_thenReturnPatient() throws Exception {
        when(patientService.findPatientById(patient.getId())).thenReturn(patient);
        when(patientMapper.toResponseDTO(patient)).thenReturn(patientResponseDTO);
        mockMvc.perform(get("/patients/{id}", patient.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(patientResponseDTO)));
    }

    @Test
    void deletePatientById_whenPatientxistsOnAppointment_thenThrowException() throws Exception {
        UUID patientId = UUID.randomUUID();
        doThrow(PatientAssociatedAppointmentsException.class).when(patientService).deletePatient(patientId);
        mockMvc.perform(delete("/patients/{id}", patientId )
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict()).andDo(print());
    }

    @Test
    void deletePatientById_whenPatientExists_thenReturnOk() throws Exception {
        mockMvc.perform(delete("/patients/{id}", patient.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void addPatient_whenValidRequest_thenReturnCreatedPatient() throws Exception {
        PatientRequestDTO requestDTO = new PatientRequestDTO();
        requestDTO.setName("Angel di maria");
        requestDTO.setAddress("test address 123");
        requestDTO.setPhone("123456789");
        requestDTO.setEmail("test@patient.com");
        requestDTO.setDateOfBirth(LocalDate.of(1995, 12, 1));



        Patient savedPatient = new Patient();
        savedPatient.setId(UUID.randomUUID());
        savedPatient.setName(requestDTO.getName());
        savedPatient.setAddress(requestDTO.getAddress());
        savedPatient.setPhone(requestDTO.getPhone());
        savedPatient.setEmail(requestDTO.getEmail());
        savedPatient.setDateOfBirth(requestDTO.getDateOfBirth());

        when(patientService.createPatient(any(PatientRequestDTO.class))).thenReturn(savedPatient);

        mockMvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    void updatePatient_whenPatientExists_thenReturnUpdatedPatient() throws Exception {
        PatientRequestDTO requestDTO = new PatientRequestDTO();
        requestDTO.setName("Angel di maria");
        requestDTO.setAddress("test address 123");
        requestDTO.setPhone("123456789");
        requestDTO.setEmail("test@patient.com");
        requestDTO.setDateOfBirth(LocalDate.of(1995, 12, 1));

        Patient updatedPatient = new Patient();
        updatedPatient.setId(patient.getId());
        updatedPatient.setName(requestDTO.getName());
        updatedPatient.setAddress(requestDTO.getAddress());
        updatedPatient.setPhone(requestDTO.getPhone());
        updatedPatient.setEmail(requestDTO.getEmail());
        updatedPatient.setDateOfBirth(requestDTO.getDateOfBirth());

        String json = objectMapper.writeValueAsString(updatedPatient);
        System.out.println(json);

        when(patientService.updatePatient(requestDTO, patient.getId())).thenReturn(updatedPatient);
         mockMvc.perform(put("/patients/{id}", patient.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk()).andDo(print());




    }
}
