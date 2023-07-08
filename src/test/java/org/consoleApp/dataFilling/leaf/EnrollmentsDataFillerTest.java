package org.consoleApp.dataFilling.leaf;

import org.consoleApp.dao.jdbc.AbstractContainerBaseTest;
import org.consoleApp.dao.jdbc.CourseDAOImpl;
import org.consoleApp.dao.jdbc.StudentsDAOImpl;
import org.consoleApp.domin.Course;
import org.consoleApp.domin.Student;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EnrollmentsDataFillerTest extends AbstractContainerBaseTest {
    private final static DataSource dataSource = getDataSource();
    private final static StudentsDAOImpl studentsDAO = new StudentsDAOImpl(dataSource);
    private final static CourseDAOImpl courseDAO = new CourseDAOImpl(dataSource);
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
    private final EnrollmentsDataFiller dataFiller = new EnrollmentsDataFiller(2, studentsDAO, courseDAO);

    @BeforeAll
    static void before(){
        courseDAO.insertBatch(courses);
        studentsDAO.insertBatch(students);
    }

    @Test
    void testFillData_ShouldInsertBatchOfStudentsInDB(){
        dataFiller.fillData();
        List<Student> actual = studentsDAO.findAll();
        assertNotNull(actual);

        for (Student student : actual){
            List<Course> studentCourse = courseDAO.findAllCourseByStudentsId(student.getId());

            assertTrue(courses.containsAll(studentCourse));
        }
    }
}