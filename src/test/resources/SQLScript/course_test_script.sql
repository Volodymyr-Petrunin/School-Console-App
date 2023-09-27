TRUNCATE TABLE courses RESTART IDENTITY CASCADE;
TRUNCATE TABLE students RESTART IDENTITY CASCADE;

ALTER SEQUENCE courses_seq RESTART WITH 4;

INSERT INTO students (student_id, group_id, first_name, last_name) VALUES (1, null, 'Vova', 'Petro');
INSERT INTO courses (course_name, course_description) VALUES ('PE', 'PE'), ('IT', 'IT'), ('Music', 'Skryabin');

INSERT INTO enrollments (student_id,course_id) VALUES (1,1), (1,2), (1,3);