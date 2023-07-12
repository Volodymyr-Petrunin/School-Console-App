package org.consoleApp.menu.leaf;

import org.consoleApp.dao.jdbc.GroupsDAOImpl;
import org.consoleApp.dao.jdbc.StudentsDAOImpl;
import org.consoleApp.domin.Group;
import org.consoleApp.domin.Student;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.*;
import java.util.Collections;
import java.util.Optional;
import java.util.StringJoiner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MenuAddNewStudentTest {
    private final static String NAME = "Vova";
    private final static String SURNAME = "Petro";
    private final static String GROUP_NAME = "BB-22";
    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private final String dash = "-".repeat(50);
    @Mock
    private GroupsDAOImpl groupsDAO;
    @Mock
    private StudentsDAOImpl studentsDAO;
    private MenuAddNewStudent addNewStudent;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        addNewStudent = new MenuAddNewStudent(groupsDAO, studentsDAO, dash);
    }

    @AfterEach
    void after(){
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    @Test
    void testExecute_ShouldUseCorrectLogic_AddNewStudentInDB(){
        Group group = new Group(1, "BB-22");

        String input = new StringJoiner(System.lineSeparator())
                .add(NAME)
                .add(SURNAME)
                .add(GROUP_NAME).toString();

        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream fakeOutput = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(fakeOutput);

        System.setIn(inputStream);
        System.setOut(printStream);


        when(groupsDAO.findAll()).thenReturn(Collections.singletonList(group));
        when(groupsDAO.findGroupIdByName("BB-22")).thenReturn(Optional.of(group));
        when(studentsDAO.insert(any(Student.class))).thenAnswer(invocation -> {
            Student student = invocation.getArgument(0);
            student.setId(1);
            return true;
        });
        when(studentsDAO.enrollStudentInCourse(anyInt(), anyInt())).thenReturn(true);

        addNewStudent.execute();

        String expectedOutput = new StringJoiner(System.lineSeparator())
                .add("Enter student details =)")
                .add("First Name: ")
                .add("Last Name: ")
                .add("All groups: ")
                .add("Group name: BB-22")
                .add("Choose group name: ")
                .add("New student added successfully! :)")
                .add(dash).toString();

        assertEquals(expectedOutput, fakeOutput.toString());

        verify(studentsDAO, times(1)).insert(any(Student.class));
        verify(studentsDAO, times(1)).enrollStudentInCourse(anyInt(), anyInt());
    }

    @Test
    void testGetDescriptionOfCurrentMenu(){
        String expected = "Add a new student";

        String actual = addNewStudent.getDescription();

        assertEquals(expected, actual);
    }
}