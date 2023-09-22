TRUNCATE TABLE courses RESTART IDENTITY CASCADE;
TRUNCATE TABLE students RESTART IDENTITY CASCADE;

ALTER SEQUENCE students_seq RESTART WITH 4 INCREMENT BY 1;

INSERT INTO courses (course_name, course_description) VALUES ('PE', 'PE'), ('IT', 'IT'), ('Music', 'Skryabin');
INSERT INTO students (group_id, first_name, last_name) VALUES (null, 'John', 'Doe'),(null, 'Jane', 'Smith'),(null, 'Michael', 'Johnson');
