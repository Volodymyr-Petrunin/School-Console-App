package org.consoleApp.menu.leaf;

import org.consoleApp.dao.jdbc.StudentsDAOImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.StringJoiner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuDeleteStudentTest {
    private final static int STUDENT_ID = 1;
    private final String dash = "-".repeat(50);
    @Mock
    private StudentsDAOImpl studentsDAO;
    private MenuDeleteStudent deleteStudent;

    @BeforeEach
    void setup() {
        deleteStudent = new MenuDeleteStudent(studentsDAO, dash);
    }

    @AfterEach
    void after(){
        SystemUtils.restoreSystemInputAndOutput();
    }

    @Test
    void testExecute_ShouldUseCorrectLogic_DeleteStudentFromDB(){

        String input = new StringJoiner(System.lineSeparator())
                .add(String.valueOf(STUDENT_ID)).toString();

        SystemUtils.setSystemInput(input);
        SystemUtils.setSystemOutput();

        when(studentsDAO.deleteByStudentId(anyInt())).thenReturn(true);

        deleteStudent.execute();

        String expectedOutput = new StringJoiner(System.lineSeparator())
                .add("Now write the id of the student you wont to delete ;)")
                .add("Delete student successfully!")
                .add(dash)
                .toString();

        assertEquals(expectedOutput, SystemUtils.getSystemOutput());
    }

    @Test
    void testGetDescriptionOfCurrentMenu(){
        String expected = "Delete a student by STUDENT_ID";

        String actual = deleteStudent.getDescription();

        assertEquals(expected, actual);
    }
}