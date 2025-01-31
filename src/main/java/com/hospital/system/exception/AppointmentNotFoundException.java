package com.hospital.system.exception;

import java.util.UUID;

public class AppointmentNotFoundException extends BusinessException {
    public AppointmentNotFoundException(UUID appointmentId) {
        super(
                "Appointment with ID: " + appointmentId + " not found.",
                "RESOURCE_NOT_FOUND"
        );
    }
}
