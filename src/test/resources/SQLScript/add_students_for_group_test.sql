DROP TABLE IF EXISTS students CASCADE;

CREATE TABLE students(
                         student_id SERIAL PRIMARY KEY,
                         group_id INT,
                         first_name VARCHAR(100),
                         last_name VARCHAR(100),
                         FOREIGN KEY (group_id) REFERENCES groups (group_id)
);

INSERT INTO students (group_id, first_name, last_name) VALUES (1, 'John', 'Doe'),(1, 'Jane', 'Smith'),(1, 'Michael', 'Johnson'),
                                                              (2, 'Emily', 'Williams'),(2, 'Daniel', 'Brown'),(3, 'Zak', 'Brown');