package org.consoleApp.menu.leaf;

import org.consoleApp.dao.jdbc.CourseDAOImpl;
import org.consoleApp.dao.jdbc.GroupsDAOImpl;
import org.consoleApp.dao.jdbc.StudentsDAOImpl;
import org.consoleApp.domin.Course;
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
        addStudentToCourse = new MenuAddStudentToCourse(studentsDAO, courseDAO, groupsDAO, dash);
    }

    @AfterEach
    void after(){
        SystemUtils.restoreSystemInputAndOutput();
    }

    @Test
    void testExecute_ShouldUseCorrectLogic_AddNewStudentToCourse(){

        Student student = new Student(1, 1, "Volodymyr", "Velykyi");
        Course course = new Course(1, "IT", "IT");

        String input = new StringJoiner(System.lineSeparator())
                .add(String.valueOf(STUDENT_ID))
                .add(String.valueOf(COURSE_ID)).toString();

        SystemUtils.setSystemInput(input);
        SystemUtils.setSystemOutput();

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

        assertEquals(expectedOutput, SystemUtils.getSystemOutput());

        verify(studentsDAO, times(1)).findAll();
        verify(courseDAO, times(1)).findAll();
    }

    @Test
    void testGetDescriptionOfCurrentMenu(){
        String expected = "Add a student to a course";

        String actual = addStudentToCourse.getDescription();

        assertEquals(expected, actual);
    }
}