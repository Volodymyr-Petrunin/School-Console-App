package org.consoleApp.generation.impl;

import org.consoleApp.dao.CourseDAO;
import org.consoleApp.dao.StudentsDAO;
import org.consoleApp.domin.Course;
import org.consoleApp.domin.Student;
import org.consoleApp.generation.records.EnrollInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = EnrollmentsDataGeneration.class)
@TestPropertySource(properties="numberOfStudentInOneCourse=3")
class EnrollmentsDataGenerationTest {
    private final static List<Student> students = List.of(
            new Student(1, null, "John", "Doe"),
            new Student(2, null, "Jane", "Smith"),
            new Student(3, null, "Michael", "Johnson"),
            new Student(4, null, "Vova", "Petro"),
            new Student(5, null, "Max", "Kozak"),
            new Student(6, null, "Lando", "Brown")
    );
    private final static List<Course> courses = List.of(
            new Course(1, "PE", "PE"),
            new Course(2, "IT", "IT"),
            new Course(3, "Music", "Skryabin")
    );
    @MockBean
    private StudentsDAO studentsDAO;
    @MockBean
    private CourseDAO courseDAO;
    @Autowired
    private EnrollmentsDataGeneration enrollmentsDataGeneration;

    @Test
    void testFillData_ShouldReturnListEnrollInfo(){
        when(studentsDAO.findAll()).thenReturn(students);
        when(courseDAO.findAll()).thenReturn(courses);

        List<EnrollInfo> enrollInfo = enrollmentsDataGeneration.generateData();


        for (EnrollInfo enrollment : enrollInfo) {
            assertTrue(enrollment.studentId() >= 1);
            assertTrue(enrollment.studentId() <= students.size());
            assertTrue(enrollment.courseId() >= 1);
            assertTrue(enrollment.courseId() <= courses.size());
        }
    }
}