package org.consoleApp.menu.leaf;

import org.consoleApp.dao.jdbc.GroupsDAOImpl;
import org.consoleApp.domin.Group;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.StringJoiner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MenuFindGroupsWithLessOrEqualStudentsTest {
    private final static int EQUAL_NUMBER = 13;
    private final String dash = "-".repeat(50);
    @Mock
    private GroupsDAOImpl groupsDAO;
    private MenuFindGroupsWithLessOrEqualStudents lessOrEqualStudents;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        lessOrEqualStudents = new MenuFindGroupsWithLessOrEqualStudents(groupsDAO, dash);
    }

    @Test
    void testExecute_ShouldUseCorrectLogic_FindGroupsWithLessOrEqualStudents(){
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;

        List<Group> expectedGroup = List.of(
                new Group(1, "AA-11"),
                new Group(2, "BB-22"),
                new Group(3, "CC-33")
        );

        try {
            String input = String.format("%s%s", EQUAL_NUMBER, System.lineSeparator());

            InputStream inputStream = new ByteArrayInputStream(input.getBytes());
            ByteArrayOutputStream fakeOutput = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(fakeOutput);

            System.setIn(inputStream);
            System.setOut(printStream);

            when(groupsDAO.findGroupsWithLessOrEqualStudents(EQUAL_NUMBER)).thenReturn(expectedGroup);
            lessOrEqualStudents.execute();

            String expectedOutput = new StringJoiner(System.lineSeparator())
                    .add("Now write how many students should be in one group at least and I will try to find such groups :)")
                    .add("All Group:")
                    .add("Group name: AA-11 and group id 1")
                    .add("Group name: BB-22 and group id 2")
                    .add("Group name: CC-33 and group id 3")
                    .add(dash).toString();

            assertEquals(expectedOutput, fakeOutput.toString());

            verify(groupsDAO, times(1)).findGroupsWithLessOrEqualStudents(anyInt());
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
    }

    @Test
    void testGetDescriptionOfCurrentMenu(){
        String expected = "Find all groups with less or equal number of students";

        String actual = lessOrEqualStudents.getDescription();

        assertEquals(expected, actual);
    }
}