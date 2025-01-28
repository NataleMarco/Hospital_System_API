INSERT INTO doctors (id, name, specialty, phone, email, dni)
SELECT t.id, t.name, t.specialty, t.phone, t.email, t.dni
FROM (
         VALUES
             ('123e4567-e89b-12d3-a456-426614174010', 'Dr. Elena Torres',   'Endocrinología', '9876543210', 'elena.torres@hospital.com', 9876543210),
             ('123e4567-e89b-12d3-a456-426614174011', 'Dr. Jorge Rivera',   'Oncología',      '8765432109', 'jorge.rivera@hospital.com', 8765432109),
             ('123e4567-e89b-12d3-a456-426614174012', 'Dr. Isabel Méndez',  'Psiquiatría',    '7654321098', 'isabel.mendez@hospital.com', 7654321098),
             ('123e4567-e89b-12d3-a456-426614174013', 'Dr. Mario Vargas',   'Ortopedia',      '6543210987', 'mario.vargas@hospital.com', 6543210987),
             ('123e4567-e89b-12d3-a456-426614174014', 'Dr. Laura Espinoza', 'Reumatología',   '5432109876', 'laura.espinoza@hospital.com', 5432109876)
         ) AS t (id, name, specialty, phone, email, dni)
WHERE NOT EXISTS (
    SELECT 1 FROM doctors d
    WHERE d.id = t.id
);

INSERT INTO patients (id, name, address, phone, email, date_of_birth)
SELECT t.id, t.name, t.address, t.phone, t.email, t.date_of_birth
FROM (
         VALUES
             ('123e4567-e89b-12d3-a456-426614174015', 'Carlos Sánchez',   '987 Vía Láctea',         '3213213210', 'carlos.sanchez@ejemplo.com', '1978-12-20'),
             ('123e4567-e89b-12d3-a456-426614174016', 'Sara Ortiz',       '654 Plaza Central',      '4324324321', 'sara.ortiz@ejemplo.com',     '1985-04-08'),
             ('123e4567-e89b-12d3-a456-426614174017', 'Ricardo Núñez',    '321 Avenida del Sol',    '5435435432', 'ricardo.nunez@ejemplo.com',  '1969-03-15'),
             ('123e4567-e89b-12d3-a456-426614174018', 'Claudia Miranda',  '654 Calle del Bosque',   '6546546543', 'claudia.miranda@ejemplo.com','1990-07-22'),
             ('123e4567-e89b-12d3-a456-426614174019', 'Antonio Camacho',  '789 Calle de la Estación','7657657654', 'antonio.camacho@ejemplo.com','1982-11-11')
         ) AS t (id, name, address, phone, email, date_of_birth)
WHERE NOT EXISTS (
    SELECT 1 FROM patients p
    WHERE p.id = t.id
);

INSERT INTO appointments (id, patient_id, doctor_id, date_time, reasons)
SELECT t.id, t.patient_id, t.doctor_id, t.date_time, t.reasons
FROM (
         VALUES
             ('123e4567-e89b-12d3-a456-426614174020', '123e4567-e89b-12d3-a456-426614174015', '123e4567-e89b-12d3-a456-426614174010', '2023-02-15 10:00:00', 'Consulta endocrinológica'),
             ('123e4567-e89b-12d3-a456-426614174021', '123e4567-e89b-12d3-a456-426614174016', '123e4567-e89b-12d3-a456-426614174011', '2023-02-16 11:00:00', 'Revisión por tratamiento oncológico'),
             ('123e4567-e89b-12d3-a456-426614174022', '123e4567-e89b-12d3-a456-426614174017', '123e4567-e89b-12d3-a456-426614174012', '2023-02-17 09:30:00', 'Evaluación psiquiátrica rutinaria'),
             ('123e4567-e89b-12d3-a456-426614174023', '123e4567-e89b-12d3-a456-426614174018', '123e4567-e89b-12d3-a456-426614174013', '2023-02-18 08:30:00', 'Control ortopédico de seguimiento'),
             ('123e4567-e89b-12d3-a456-426614174024', '123e4567-e89b-12d3-a456-426614174019', '123e4567-e89b-12d3-a456-426614174014', '2023-02-19 15:00:00', 'Consulta para manejo de artritis')
         ) AS t (id, patient_id, doctor_id, date_time, reasons)
WHERE NOT EXISTS (
    SELECT 1 FROM appointments a
    WHERE a.id = t.id
);
