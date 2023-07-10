package org.consoleApp.menu.leaf;

import org.consoleApp.dao.jdbc.CourseDAOImpl;
import org.consoleApp.dao.jdbc.GroupsDAOImpl;
import org.consoleApp.dao.jdbc.StudentsDAOImpl;
import org.consoleApp.domin.Course;
import org.consoleApp.domin.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.StringJoiner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MenuAddStudentToCourseTest {
    private final static int STUDENT_ID = 1;
    private final static int COURSE_ID = 1;
    private final String dash = "-".repeat(50);
    @Mock
    private StudentsDAOImpl studentsDAO;
    @Mock
    private CourseDAOImpl courseDAO;
    @Mock
    private GroupsDAOImpl groupsDAO;
    private  MenuAddStudentToCourse addStudentToCourse;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        addStudentToCourse = new MenuAddStudentToCourse(studentsDAO, courseDAO, groupsDAO, dash);
    }

    @Test
    void testExecute_ShouldUseCorrectLogic_AddNewStudentToCourse(){
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;

        Student student = new Student(1, 1, "Volodymyr", "Velykyi");
        Course course = new Course(1, "IT", "IT");

        try {
            String input = String.format("%s%s%s%s", STUDENT_ID, System.lineSeparator(), COURSE_ID, System.lineSeparator());

            InputStream inputStream = new ByteArrayInputStream(input.getBytes());
            ByteArrayOutputStream fakeOutput = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(fakeOutput);

            System.setIn(inputStream);
            System.setOut(printStream);

            when(studentsDAO.findAll()).thenReturn(Collections.singletonList(student));
            when(courseDAO.findAll()).thenReturn(Collections.singletonList(course));
            when(studentsDAO.enrollStudentInCourse(anyInt(), anyInt())).thenReturn(true);

            addStudentToCourse.execute();

            String expectedOutput = new StringJoiner(System.lineSeparator())
                    .add("All students:")
                    .add("ID:   1 Initial: Volodymyr Velykyi | Group: No group")
                    .add("Now choose student id: ")
                    .add("All courses:")
                    .add("ID: 1 Course name: IT Course description: IT")
                    .add("Now choose courses id: ")
                    .add("Add student successfully!")
                    .add(dash).toString();

            assertEquals(expectedOutput, fakeOutput.toString());

            verify(studentsDAO, times(1)).findAll();
            verify(courseDAO, times(1)).findAll();
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }

    }

    @Test
    void testGetDescriptionOfCurrentMenu(){
        String expected = "Add a student to a course";

        String actual = addStudentToCourse.getDescription();

        assertEquals(expected, actual);
    }
}