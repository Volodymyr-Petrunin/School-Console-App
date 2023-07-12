package org.consoleApp.dataFilling.leaf;

import org.consoleApp.dao.jdbc.CourseDAOImpl;
import org.consoleApp.dao.jdbc.StudentsDAOImpl;
import org.consoleApp.domin.Course;
import org.consoleApp.domin.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnrollmentsDataFillerTest {
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
    @Mock private StudentsDAOImpl studentsDAO;
    @Mock private CourseDAOImpl courseDAO;
    private EnrollmentsDataFiller dataFiller;

    @BeforeEach
    void before(){
        dataFiller = new EnrollmentsDataFiller(2, studentsDAO, courseDAO);
    }

    @Test
    void testFillData_ShouldInsertBatchOfStudentsInDB(){
        when(studentsDAO.findAll()).thenReturn(students);
        when(courseDAO.findAll()).thenReturn(courses);

        dataFiller.fillData();

        for (Student student : students){
            verify(studentsDAO, atLeastOnce()).enrollStudentInCourse(eq(student.getId()), anyInt());
        }
    }
}