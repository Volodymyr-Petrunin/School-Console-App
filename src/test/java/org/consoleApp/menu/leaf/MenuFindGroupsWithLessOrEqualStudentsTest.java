package org.consoleApp.menu.leaf;

import org.consoleApp.dao.jdbc.GroupsDAOImpl;
import org.consoleApp.domin.Group;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.StringJoiner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuFindGroupsWithLessOrEqualStudentsTest {
    private final static int EQUAL_NUMBER = 13;
    private final String dash = "-".repeat(50);
    @Mock
    private GroupsDAOImpl groupsDAO;
    private MenuFindGroupsWithLessOrEqualStudents lessOrEqualStudents;

    @BeforeEach
    void setup() {
        lessOrEqualStudents = new MenuFindGroupsWithLessOrEqualStudents(groupsDAO, dash);
    }

    @AfterEach
    void after(){
        SystemUtils.restoreSystemInputAndOutput();
    }

    @Test
    void testExecute_ShouldUseCorrectLogic_FindGroupsWithLessOrEqualStudents(){
        List<Group> expectedGroup = List.of(
                new Group(1, "AA-11"),
                new Group(2, "BB-22"),
                new Group(3, "CC-33")
        );

        String input = new StringJoiner(System.lineSeparator())
                .add(String.valueOf(EQUAL_NUMBER)).toString();

        SystemUtils.setSystemInput(input);
        SystemUtils.setSystemOutput();

        when(groupsDAO.findGroupsWithLessOrEqualStudents(EQUAL_NUMBER)).thenReturn(expectedGroup);
        lessOrEqualStudents.execute();

        String expectedOutput = new StringJoiner(System.lineSeparator())
                .add("Now write how many students should be in one group at least and I will try to find such groups :)")
                .add("All Group:")
                .add("Group name: AA-11 and group id 1")
                .add("Group name: BB-22 and group id 2")
                .add("Group name: CC-33 and group id 3")
                .add(dash).toString();

        assertEquals(expectedOutput, SystemUtils.getSystemOutput());

        verify(groupsDAO, times(1)).findGroupsWithLessOrEqualStudents(anyInt());
    }

    @Test
    void testGetDescriptionOfCurrentMenu(){
        String expected = "Find all groups with less or equal number of students";

        String actual = lessOrEqualStudents.getDescription();

        assertEquals(expected, actual);
    }
}