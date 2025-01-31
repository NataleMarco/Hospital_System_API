package com.hospital.system.exception;

import java.util.UUID;

public class PatientAssociatedAppointmentsException extends BusinessException {
    public PatientAssociatedAppointmentsException(UUID patientId) {
        super(
                "Patient with ID " + patientId + " cannot be deleted because they have associated appointments.",
                "PATIENT_HAS_APPOINTMENTS"
        );
    }
}
