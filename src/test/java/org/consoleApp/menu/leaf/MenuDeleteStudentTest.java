package org.consoleApp.menu.leaf;

import org.consoleApp.dao.jdbc.StudentsDAOImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.StringJoiner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MenuDeleteStudentTest {
    private final static int STUDENT_ID = 1;
    private final String dash = "-".repeat(50);
    @Mock
    private StudentsDAOImpl studentsDAO;
    private MenuDeleteStudent deleteStudent;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        deleteStudent = new MenuDeleteStudent(studentsDAO, dash);
    }

    @Test
    void testExecute_ShouldUseCorrectLogic_DeleteStudentFromDB(){
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;

        try {
            String input = String.format("%s%s", STUDENT_ID, System.lineSeparator());

            InputStream inputStream = new ByteArrayInputStream(input.getBytes());
            ByteArrayOutputStream fakeOutput = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(fakeOutput);

            System.setIn(inputStream);
            System.setOut(printStream);

            when(studentsDAO.deleteByStudentId(anyInt())).thenReturn(true);

            deleteStudent.execute();

            String expectedOutput = new StringJoiner(System.lineSeparator())
                    .add("Now write the id of the student you wont to delete ;)")
                    .add("Delete student successfully!")
                    .add(dash)
                    .toString();

            assertEquals(expectedOutput, fakeOutput.toString());
        }finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
    }

    @Test
    void testGetDescriptionOfCurrentMenu(){
        String expected = "Delete a student by STUDENT_ID";

        String actual = deleteStudent.getDescription();

        assertEquals(expected, actual);
    }
}