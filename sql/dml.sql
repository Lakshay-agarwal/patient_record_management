USE hospital_db;

INSERT INTO Patient (patient_id, name, email, address, age, gender, phone_number, last_visits) 
values(NULL, 'Blah', 'gg@gmail.com', 'sector 49 chd', 20, 1, 987654321, '24/9/19 - Arm Fracture' );


INSERT INTO Patient (patient_id, name, email, address, age, gender, phone_number, last_visits) 
values(NULL, 'Meenal', 'msm@gmail.com', 'bangalore', 55, 1, 9992328931, '28/10/19 - Fever, 17/10/19 - Severe Body Rash' );


INSERT INTO Patient (patient_id, name, email, address, age, gender, phone_number, last_visits) 
values(NULL, 'Dodo', 'ddsharma@gmail.com', 'bangalore', 45, 4, 4849333221, '5/12/19 - Back Pain' );

INSERT INTO Patient_Medical_History (medical_history_id, name, past_health_issues, family_history_diseases, patient_id)
values (NULL, 'Blah', 'high blood pressure', 'Father had heart issues', 1) ;

INSERT INTO Patient_Medical_History (medical_history_id, name, past_health_issues, family_history_diseases, patient_id)
values (NULL, 'Meenal', 'diabetes, cancer', 'Mother had breast cancer', 2) ;

INSERT INTO Patient_Medical_History (medical_history_id, name, past_health_issues, family_history_diseases, patient_id)
values (NULL, 'Dodo', 'diabetes', 'Mother had diabetes', 3) ;


