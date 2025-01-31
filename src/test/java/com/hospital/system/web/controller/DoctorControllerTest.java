package com.hospital.system.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital.system.domain.entity.Doctor;
import com.hospital.system.exception.DoctorAssociatedAppointmentsException;
import com.hospital.system.exception.DoctorNotFoundException;
import com.hospital.system.exception.ResourceNotFoundException;
import com.hospital.system.service.DoctorService;
import com.hospital.system.web.dto.request.DoctorRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DoctorController.class)
class DoctorControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DoctorService doctorService;


    private DoctorRequestDTO validDoctorDTO;
    private Doctor existingDoctor1;
    private Doctor existingDoctor2;


    public static Stream<Arguments> provideInvalidDoctorRequests() {
        return Stream.of(
                Arguments.of("name", null, "Name is required"),
                Arguments.of("name", "A", "Name must be between 2 and 100 characters"),
                Arguments.of("name", "ThisNameIsWayTooLongForTheGivenConstraintsWhichExceedsOneHundredCharactersAndShouldTriggerAValidationErrorInTheControllerLayer", "Name must be between 2 and 100 characters"),
                Arguments.of("specialty", null, "Specialty is required"),
                Arguments.of("phone", null, "Phone is required"),
                Arguments.of("email", null, "Email is required"),
                Arguments.of("email", "invalidemail", "Email has to be valid"),
                Arguments.of("dni", null, "DNI is required"),
                Arguments.of("dni", -12345678L, "DNI must be a positive number")
        );
    }

    private DoctorRequestDTO createInvalidDoctorDTO(String fieldName, Object fieldValue) {
        DoctorRequestDTO invalidRequestDTO = createValidDoctorDTO();

        switch (fieldName) {
            case "name":
                invalidRequestDTO.setName((String) fieldValue);
                break;
            case "specialty":
                invalidRequestDTO.setSpecialty((String) fieldValue);
                break;
            case "phone":
                invalidRequestDTO.setPhone((String) fieldValue);
                break;
            case "email":
                invalidRequestDTO.setEmail((String) fieldValue);
                break;
            case "dni":
                invalidRequestDTO.setDni((Long) fieldValue);
                break;
            default:
                throw new IllegalArgumentException("Campo no soportado: " + fieldName);
        }

        return invalidRequestDTO;
    }


    @BeforeEach
    void setUp() {
        existingDoctor1 = new Doctor(
                UUID.randomUUID(),
                "Dr. John Doe",
                "Cardiology",
                "1234567890",
                "johndoe@example.com",
                12345678L
        );

        existingDoctor2 = new Doctor(
                UUID.randomUUID(),
                "Dr. Jane Smith",
                "Neurology",
                "0987654321",
                "janesmith@example.com",
                87654321L
        );

        validDoctorDTO = createValidDoctorDTO();
    }

    private DoctorRequestDTO createValidDoctorDTO() {
        return new DoctorRequestDTO(
                "Dr. Alice Brown",
                "Pediatrics",
                "1122334455",
                "alicebrown@example.com",
                11223344L
        );
    }

    @Test
    void findAll_whenDoctorsExists_thenReturnDoctorsList() throws Exception {
        List<Doctor> doctors = Arrays.asList(existingDoctor1,existingDoctor2);

        when(doctorService.findAllDoctors()).thenReturn(doctors);

        mockMvc.perform(get("/doctors")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(doctors)));

    }
    @Test
    void findAll_whenDoctorsEmpty_thenReturnEmptyList() throws Exception {
        List<Doctor> doctors = Collections.emptyList();
        when(doctorService.findAllDoctors()).thenReturn(doctors);
        mockMvc.perform(get("/doctors")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));

    }

    @Test
    void findDoctorById_whenDoctorExists_thenReturnDoctor() throws Exception {
        UUID doctorId = existingDoctor1.getId();
        when(doctorService.findDoctorById(any(UUID.class))).thenReturn(existingDoctor1);
        mockMvc.perform(get("/doctors/{doctorId}", doctorId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(existingDoctor1)));


    }

    @Test
    void findDoctorById_whenDoctorNotExists_thenThrowException() throws Exception {

        UUID doctorId = UUID.randomUUID();
        when(doctorService.findDoctorById(any(UUID.class))).thenThrow(DoctorNotFoundException.class);

        mockMvc.perform(get("/doctors/{doctorId}", doctorId))
                .andExpect(status().isNotFound());


    }

    @Test
    void deleteDoctorById_whenDoctorExists_thenReturnOK() throws Exception {
        UUID doctorId = existingDoctor1.getId();
        doNothing().when(doctorService).deleteDoctor(any(UUID.class));

        mockMvc.perform(delete("/doctors/{doctorId}", doctorId))
                .andExpect(status().isOk());

    }

    @Test
    void deleteDoctorById_whenDoctorExistsWithAppointments_thenThrowException() throws Exception {
        UUID doctorId = UUID.randomUUID();
        doThrow(DoctorAssociatedAppointmentsException.class).when(doctorService).deleteDoctor(any(UUID.class));
        mockMvc.perform(delete("/doctors/{doctorId}", doctorId))
                .andExpect(status().isConflict()).andDo(print());
    }

    @Test
    void deleteDoctorById_whenDoctorNotExists_thenThrowException() throws Exception {
        UUID doctorId = UUID.randomUUID();
        doThrow(DoctorNotFoundException.class).when(doctorService).deleteDoctor(any(UUID.class));
        mockMvc.perform(delete("/doctors/{doctorId}", doctorId))
                .andExpect(status().isNotFound());
    }

    @ParameterizedTest(name = "{index} => field={0}, value={1}, expectedError={2}")
    @MethodSource("provideInvalidDoctorRequests")
    void addDoctor_whenInvalidData_thenReturnCreated(String fieldName, Object fielValue, String expectedErrorMessage) throws Exception {

        DoctorRequestDTO invalidRequestDTO = createInvalidDoctorDTO(fieldName, fielValue);
        String requestJson = objectMapper.writeValueAsString(invalidRequestDTO);

        mockMvc.perform(post("/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors." + fieldName).value(expectedErrorMessage));

    }


    @Test
    void addDoctor_whenMissmatchedData_thenThrowException() throws Exception {

        String json = """
               {"id":"74b7068d-37fa-4f81-b06c-03fb67a1b2ae",
               "name":"Dr. John Doe",
               "specialty":"Cardiology",
               "phone":"1234567890",
               "email":"johndoe@example.com",
               "dni":"asjdfjas"}""";
        when(doctorService.saveDoctor(any(DoctorRequestDTO.class))).thenReturn(existingDoctor1);


        mockMvc.perform(post("/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest()).andDo(print());



    }
    @Test
    void addDoctor_whenValidData_thenReturnOk() throws Exception {

        when(doctorService.saveDoctor(any(DoctorRequestDTO.class))).thenReturn(existingDoctor1);

        String requestJson = objectMapper.writeValueAsString(existingDoctor1);

        mockMvc.perform(post("/doctors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(existingDoctor1)));



    }

    @Test
    void updateDoctor_whenValidData_thenReturnOK() throws Exception {


        UUID doctorId = existingDoctor1.getId();
        String requestJson = objectMapper.writeValueAsString(validDoctorDTO);

        when(doctorService.updateDoctor(validDoctorDTO, doctorId )).thenReturn(existingDoctor2);

        mockMvc.perform(put("/doctors/{doctorId}", doctorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

    }

    @ParameterizedTest(name = "{index} => field={0}, value={1}, expectedError={2}")
    @MethodSource("provideInvalidDoctorRequests")
    void updateDoctor_whenInvalidData_thenReturnBadRequest(String fieldName, Object fielValue, String expectedErrorMessage) throws Exception {

        UUID doctorId = existingDoctor1.getId();
        DoctorRequestDTO invalidRequestDTO = createInvalidDoctorDTO(fieldName, fielValue);
        String requestJson = objectMapper.writeValueAsString(invalidRequestDTO);


        when(doctorService.updateDoctor(invalidRequestDTO, doctorId )).thenReturn(existingDoctor2);
        mockMvc.perform(put("/doctors/{doctorId}", doctorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors." + fieldName).value(expectedErrorMessage));

    }
}