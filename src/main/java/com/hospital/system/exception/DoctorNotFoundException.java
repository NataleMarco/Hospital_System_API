package com.hospital.system.exception;

import java.util.UUID;

public class DoctorNotFoundException extends BusinessException {
    public DoctorNotFoundException(UUID doctorId) {
        super(
                "Doctor with ID: " + doctorId + " not found.",
                "RESOURCE_NOT_FOUND"
        );
    }
}
