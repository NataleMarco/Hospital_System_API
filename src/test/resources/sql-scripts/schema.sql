CREATE TABLE doctors (
                         id UUID  PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         specialty VARCHAR(255) NOT NULL,
                         phone VARCHAR(255) NOT NULL,
                         email VARCHAR(255) NOT NULL UNIQUE ,
                         dni BIGINT NOT NULL UNIQUE
);


CREATE TABLE patients (
                          id UUID  PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          address VARCHAR(255) NOT NULL,
                          phone VARCHAR(255) NOT NULL UNIQUE,
                          email VARCHAR(255) NOT NULL UNIQUE,
                          date_of_birth DATE NOT NULL
);

CREATE TABLE appointments (
                              id UUID  PRIMARY KEY,
                              patient_id UUID NOT NULL,
                              doctor_id UUID NOT NULL,
                              date_time TIMESTAMP NOT NULL,
                              reasons VARCHAR(255),
                              FOREIGN KEY (patient_id) REFERENCES patients(id),
                              FOREIGN KEY (doctor_id) REFERENCES doctors(id)
);

