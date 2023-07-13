package org.consoleApp.menu.leaf;

import org.consoleApp.dao.jdbc.GroupsDAOImpl;
import org.consoleApp.dao.jdbc.StudentsDAOImpl;
import org.consoleApp.domin.Student;
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
        menuFindAllStudentsRelatedToCourse = new MenuFindAllStudentsRelatedToCourse(studentsDAO, groupsDAO, dash);
    }

    @AfterEach
    void after(){
        SystemUtils.restoreSystemInputAndOutput();
    }

    @Test
    void testExecute_ShouldUseCorrectLogic_FindAllStudentsRelatedToCourse(){

        List<Student> students = List.of(
                new Student(1, null, "Vova", "IDK"),
                new Student(2, null, "", "IDK"),
                new Student(3, null, "WHY", "IDK")
        );

        String input = new StringJoiner(System.lineSeparator())
                .add(NAME_COURSE).toString();

        SystemUtils.setSystemInput(input);
        SystemUtils.setSystemOutput();

        when(studentsDAO.findStudentsByCourseName(anyString())).thenReturn(students);
        menuFindAllStudentsRelatedToCourse.execute();

        String expectedOutput = new StringJoiner(System.lineSeparator())
                .add("Now write the name of course 0_0")
                .add("All Students: ")
                .add("ID:   1 Initial: Vova IDK | Group: No group")
                .add("ID:   2 Initial:      IDK | Group: No group")
                .add("ID:   3 Initial: WHY  IDK | Group: No group")
                .add(dash).toString();

        assertEquals(expectedOutput, SystemUtils.getSystemOutput());
    }

    @Test
    void testGetDescriptionOfCurrentMenu(){
        String expected = "Find all students related to a course with a specified name";

        String actual = menuFindAllStudentsRelatedToCourse.getDescription();

        assertEquals(expected, actual);
    }
}