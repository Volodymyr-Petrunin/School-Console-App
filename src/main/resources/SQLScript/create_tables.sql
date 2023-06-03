DROP TABLE IF EXISTS groups CASCADE;
DROP TABLE IF EXISTS students CASCADE;
DROP TABLE IF EXISTS courses;


CREATE TABLE groups(
    group_id SERIAL PRIMARY KEY,
    group_name VARCHAR(100)
);

CREATE TABLE students(
    student_id SERIAL PRIMARY KEY,
    group_id INT,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    FOREIGN KEY (group_id) REFERENCES groups (group_id)
);

CREATE TABLE courses (
    course_id SERIAL PRIMARY KEY,
    course_name VARCHAR(100),
    course_description VARCHAR(200)
);
ALTER SEQUENCE course_id_sequence RESTART WITH 1;