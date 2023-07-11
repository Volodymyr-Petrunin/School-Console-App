package org.consoleApp.menu.leaf;

import org.consoleApp.dao.jdbc.GroupsDAOImpl;
import org.consoleApp.dao.jdbc.StudentsDAOImpl;
import org.consoleApp.domin.Student;
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

class MenuFindAllStudentsRelatedToCourseTest {
    private final static String NAME_COURSE = "IT";
    private final String dash = "-".repeat(50);
    @Mock
    private StudentsDAOImpl studentsDAO;
    @Mock
    private GroupsDAOImpl groupsDAO;
    private MenuFindAllStudentsRelatedToCourse menuFindAllStudentsRelatedToCourse;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        menuFindAllStudentsRelatedToCourse = new MenuFindAllStudentsRelatedToCourse(studentsDAO, groupsDAO, dash);
    }

    @Test
    void testExecute_ShouldUseCorrectLogic_FindAllStudentsRelatedToCourse(){
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;

        List<Student> students = List.of(
                new Student(1, null, "Vova", "IDK"),
                new Student(2, null, "", "IDK"),
                new Student(3, null, "WHY", "IDK")
        );

        try {
            String input = String.format("%s%s", NAME_COURSE, System.lineSeparator());

            InputStream inputStream = new ByteArrayInputStream(input.getBytes());
            ByteArrayOutputStream fakeOutput = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(fakeOutput);

            System.setIn(inputStream);
            System.setOut(printStream);

            when(studentsDAO.findStudentsByCourseName(anyString())).thenReturn(students);
            menuFindAllStudentsRelatedToCourse.execute();

            String expectedOutput = new StringJoiner(System.lineSeparator())
                    .add("Now write the name of course 0_0")
                    .add("All Students: ")
                    .add("ID:   1 Initial: Vova IDK | Group: No group")
                    .add("ID:   2 Initial:      IDK | Group: No group")
                    .add("ID:   3 Initial: WHY  IDK | Group: No group")
                    .add(dash).toString();

            assertEquals(expectedOutput, fakeOutput.toString());
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
    }

    @Test
    void testGetDescriptionOfCurrentMenu(){
        String expected = "Find all students related to a course with a specified name";

        String actual = menuFindAllStudentsRelatedToCourse.getDescription();

        assertEquals(expected, actual);
    }
}