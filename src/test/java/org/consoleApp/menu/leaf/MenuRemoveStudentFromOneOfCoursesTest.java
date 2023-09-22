package org.consoleApp.menu.leaf;

import org.consoleApp.dao.jdbc.CourseDAOImpl;
import org.consoleApp.dao.jdbc.GroupsDAOImpl;
import org.consoleApp.dao.jdbc.StudentsDAOImpl;
import org.consoleApp.domin.Course;
import org.consoleApp.domin.Group;
import org.consoleApp.domin.Student;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.StringJoiner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuRemoveStudentFromOneOfCoursesTest {
    private final static String STUDENT_NAME = "Vova";
    private final static int STUDENT_ID = 44;
    private final static int COURSE_ID = 3;
    private final String dash = "-".repeat(50);
    @Mock
    private StudentsDAOImpl studentsDAO;
    @Mock
    private CourseDAOImpl courseDAO;
    @Mock
    private GroupsDAOImpl groupsDAO;
    private MenuRemoveStudentFromOneOfCourses removeStudentFromOneOfCourses;

    @BeforeEach
    void setup() {
        removeStudentFromOneOfCourses = new MenuRemoveStudentFromOneOfCourses(studentsDAO, courseDAO, groupsDAO, dash);
    }

    @AfterEach
    void after(){
        SystemUtils.restoreSystemInputAndOutput();
    }

    @Test
    void testExecute_ShouldUseCorrectLogic_RemoveStudentFromOneOfCourses(){
        Student student = new Student(STUDENT_ID, (Group)null, STUDENT_NAME, "Surname");
        Course course = new Course(7, "IT", "description");

        String input = new StringJoiner(System.lineSeparator())
                .add(STUDENT_NAME)
                .add(String.valueOf(STUDENT_ID))
                .add(String.valueOf(COURSE_ID)).toString();

        SystemUtils.setSystemInput(input);
        SystemUtils.setSystemOutput();

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

        assertEquals(expectedOutput, SystemUtils.getSystemOutput());

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