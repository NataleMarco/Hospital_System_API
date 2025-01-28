INSERT INTO doctors (id, name, specialty, phone, email, dni) VALUES
                                                                 ('123e4567-e89b-12d3-a456-426614174000', 'Dr. Ana Ruiz', 'Cardiología', '1234567890', 'ana.ruiz@hospital.com', 4815162342),
                                                                 ('123e4567-e89b-12d3-a456-426614174001', 'Dr. Luis Fernández', 'Dermatología', '0987654321', 'luis.fernandez@hospital.com', 2345678901),
                                                                 ('123e4567-e89b-12d3-a456-426614174002', 'Dr. Carlos Jiménez', 'Neurología', '2345678901', 'carlos.jimenez@hospital.com', 3456789012),
                                                                 ('123e4567-e89b-12d3-a456-426614174003', 'Dr. Marta González', 'Pediatría', '3456789012', 'marta.gonzalez@hospital.com', 4567890123),
                                                                 ('123e4567-e89b-12d3-a456-426614174004', 'Dr. Sofia Castro', 'Gastroenterología', '4567890123', 'sofia.castro@hospital.com', 5678901234);

INSERT INTO patients (id, name, address, phone, email, date_of_birth) VALUES
                                                                          ('123e4567-e89b-12d3-a456-426614174005', 'Juan Pérez', '123 Calle Principal', '1231231234', 'juan.perez@ejemplo.com', '1980-05-15'),
                                                                          ('123e4567-e89b-12d3-a456-426614174006', 'María López', '456 Calle Secundaria', '4564564567', 'maria.lopez@ejemplo.com', '1992-08-24'),
                                                                          ('123e4567-e89b-12d3-a456-426614174007', 'Pedro Martínez', '789 Calle Terciaria', '5675675678', 'pedro.martinez@ejemplo.com', '1975-11-30'),
                                                                          ('123e4567-e89b-12d3-a456-426614174008', 'Lucía Hernández', '321 Calle Cuarta', '6786786789', 'lucia.hernandez@ejemplo.com', '1989-02-03'),
                                                                          ('123e4567-e89b-12d3-a456-426614174009', 'Tomás Morales', '654 Calle Quinta', '7897897890', 'tomas.morales@ejemplo.com', '1996-07-12');

INSERT INTO appointments (id, patient_id, doctor_id, date_time, reasons) VALUES
                                                                             ('123e4567-e89b-12d3-a456-426614174010', '123e4567-e89b-12d3-a456-426614174005', '123e4567-e89b-12d3-a456-426614174000', '2023-01-15 09:00:00', 'Revisión cardíaca regular'),
                                                                             ('123e4567-e89b-12d3-a456-426614174011', '123e4567-e89b-12d3-a456-426614174006', '123e4567-e89b-12d3-a456-426614174001', '2023-01-16 14:00:00', 'Consulta por problemas de piel'),
                                                                             ('123e4567-e89b-12d3-a456-426614174012', '123e4567-e89b-12d3-a456-426614174007', '123e4567-e89b-12d3-a456-426614174002', '2023-01-17 10:30:00', 'Chequeo neurológico'),
                                                                             ('123e4567-e89b-12d3-a456-426614174013', '123e4567-e89b-12d3-a456-426614174008', '123e4567-e89b-12d3-a456-426614174003', '2023-01-18 08:00:00', 'Control pediátrico anual'),
                                                                             ('123e4567-e89b-12d3-a456-426614174014', '123e4567-e89b-12d3-a456-426614174009', '123e4567-e89b-12d3-a456-426614174004', '2023-01-19 16:00:00', 'Evaluación gastroenterológica');
