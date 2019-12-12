DROP DATABASE hospital_db;
CREATE DATABASE hospital_db;
USE hospital_db;
set sql_mode=STRICT_TRANS_TABLES;
CREATE Table Patient(
    patient_id smallint AUTO_INCREMENT,
    name varchar(50) NOT NULL,
    email varchar(50),
    address varchar(100),
    age smallint NOT NULL,
    gender boolean NOT NULL,
    phone_number bigint NOT NULL,
    last_visits varchar(200),
    PRIMARY KEY (patient_id),
    CHECK ((gender = 0) OR (gender = 1) )
);
CREATE Table Medical_Bills( 
    bill_id smallint AUTO_INCREMENT, 
    bill_date date, 
    amount float(10,2), 
    patient_id smallint NOT NULL,
    PRIMARY KEY (bill_id),
    constraint bill_fk FOREIGN KEY (patient_id) references Patient (patient_id)
);

CREATE Table Prescriptions( 
    prescription_id smallint AUTO_INCREMENT, 
    prescription_date date, 
    doctors_name varchar(50),
    description varchar(50), 
    patient_id smallint NOT NULL,
    PRIMARY KEY (prescription_id),
    constraint pres_fk FOREIGN KEY (patient_id) references Patient (patient_id)
);

CREATE Table Patient_Medical_History(
    medical_history_id smallint AUTO_INCREMENT,
    name varchar(50),
    past_health_issues varchar(200),
    family_history_diseases varchar(200),
    patient_id smallint NOT NULL,
    PRIMARY KEY (medical_history_id),
    constraint history_fk FOREIGN KEY (patient_id) references Patient (patient_id)
);


