package com.hospital.system.exception;

import java.util.UUID;

public class PatientNotFoundException extends BusinessException {
    public PatientNotFoundException(UUID patientId) {
        super(
                "Patient with ID: " + patientId + " not found.",
                "RESOURCE_NOT_FOUND"
        );
    }
}
