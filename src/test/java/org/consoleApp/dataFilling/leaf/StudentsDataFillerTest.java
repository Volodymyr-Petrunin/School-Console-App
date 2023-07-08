package org.consoleApp.dataFilling.leaf;

import org.consoleApp.dao.jdbc.AbstractContainerBaseTest;
import org.consoleApp.dao.jdbc.GroupsDAOImpl;
import org.consoleApp.dao.jdbc.StudentsDAOImpl;
import org.consoleApp.domin.Group;
import org.consoleApp.domin.Student;
import org.consoleApp.generation.records.InitialAmountGeneration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentsDataFillerTest extends AbstractContainerBaseTest {
    private final static int QUANTITY_GENERATION = 3;
    private final static DataSource dataSource = getDataSource();
    private final static StudentsDAOImpl studentsDAO = new StudentsDAOImpl(dataSource);
    private final static GroupsDAOImpl groupsDAO = new GroupsDAOImpl(dataSource);
    private final static List<Group> expectedGroup = List.of(
            new Group(1, "AA-11"),
            new Group(2, "BB-22"),
            new Group(3, "CC-33")
    );
    private final List<String> names = List.of(
            "Vova", "Stas", "Dima"
    );
    private final List<String> surnames = List.of(
            "Petrunin", "Solyanik", "Dypai"
    );
    private final InitialAmountGeneration amountGeneration = new InitialAmountGeneration(QUANTITY_GENERATION, 3, 0);
    private final StudentsDataFiller dataFiller = new StudentsDataFiller(names, surnames, amountGeneration, studentsDAO, groupsDAO);

    @BeforeAll
    static void before(){
        groupsDAO.insertBatch(expectedGroup);
    }

    @Test
    void testFillData_ShouldInsertBatchOfStudentsInDB(){
        dataFiller.fillData();

        List<Student> actual = studentsDAO.findAll();
        assertNotNull(actual);

        for (Student student : actual){
            assertNotEquals(0, student.getId());

            assertTrue(names.contains(student.getFirstName()));
            assertTrue(surnames.contains(student.getLastName()));
        }
    }
}