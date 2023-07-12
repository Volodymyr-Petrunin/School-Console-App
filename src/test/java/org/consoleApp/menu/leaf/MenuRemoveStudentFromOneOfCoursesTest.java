package org.consoleApp.menu.leaf;

import org.consoleApp.dao.jdbc.CourseDAOImpl;
import org.consoleApp.dao.jdbc.GroupsDAOImpl;
import org.consoleApp.dao.jdbc.StudentsDAOImpl;
import org.consoleApp.domin.Course;
import org.consoleApp.domin.Student;
import org.junit.jupiter.api.AfterEach;
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

class MenuRemoveStudentFromOneOfCoursesTest {
    private final static String STUDENT_NAME = "Vova";
    private final static int STUDENT_ID = 44;
    private final static int COURSE_ID = 3;
    private final String dash = "-".repeat(50);
    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    @Mock
    private StudentsDAOImpl studentsDAO;
    @Mock
    private CourseDAOImpl courseDAO;
    @Mock
    private GroupsDAOImpl groupsDAO;
    private MenuRemoveStudentFromOneOfCourses removeStudentFromOneOfCourses;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        removeStudentFromOneOfCourses = new MenuRemoveStudentFromOneOfCourses(studentsDAO, courseDAO, groupsDAO, dash);
    }

    @AfterEach
    void after(){
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    @Test
    void testExecute_ShouldUseCorrectLogic_RemoveStudentFromOneOfCourses(){
        Student student = new Student(STUDENT_ID, null, STUDENT_NAME, "Surname");
        Course course = new Course(7, "IT", "description");

        String input = new StringJoiner(System.lineSeparator())
                .add(STUDENT_NAME)
                .add(String.valueOf(STUDENT_ID))
                .add(String.valueOf(COURSE_ID)).toString();

        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream fakeOutput = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(fakeOutput);

        System.setIn(inputStream);
        System.setOut(printStream);

        when(studentsDAO.findByFirstName(STUDENT_NAME)).thenReturn(Collections.singletonList(student));
        when(courseDAO.findAllCourseByStudentsId(STUDENT_ID)).thenReturn(Collections.singletonList(course));
        when(studentsDAO.removeStudentFromCourse(anyInt(), anyInt())).thenReturn(true);

        removeStudentFromOneOfCourses.execute();

        String expectedOutput = new StringJoiner(System.lineSeparator())
                .add("Enter student name: ")
                .add("I find 1 students:")
                .add("ID:  44 Initial: Vova Surname | Group: No group")
                .add("Now choose which one you need and write its id: ")
                .add("Now select the course id you want to remove from your student: ")
                .add("ID: 7 Course name: IT Course description: description")
                .add("Delete student successfully!")
                .add(dash).toString();

        assertEquals(expectedOutput, fakeOutput.toString());

        verify(studentsDAO, times(1)).findByFirstName(anyString());
        verify(studentsDAO, times(1)).removeStudentFromCourse(anyInt(), anyInt());
        verify(courseDAO, times(1)).findAllCourseByStudentsId(anyInt());
    }

    @Test
    void testGetDescriptionOfCurrentMenu(){
        String expected = "Remove a student from one of their courses";

        String actual = removeStudentFromOneOfCourses.getDescription();

        assertEquals(expected, actual);
    }
}