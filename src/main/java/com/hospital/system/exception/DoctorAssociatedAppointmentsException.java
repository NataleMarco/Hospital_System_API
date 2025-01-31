package com.hospital.system.exception;

import java.util.UUID;

public class DoctorAssociatedAppointmentsException extends BusinessException {
    public DoctorAssociatedAppointmentsException(UUID doctorId) {
        super(
                "Doctor with ID " + doctorId + " cannot be deleted because they have associated appointments.",
                "DOCTOR_HAS_APPOINTMENTS"
        );
    }
}