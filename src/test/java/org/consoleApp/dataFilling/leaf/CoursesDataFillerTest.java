package org.consoleApp.dataFilling.leaf;

import org.consoleApp.dao.jdbc.AbstractContainerBaseTest;
import org.consoleApp.dao.jdbc.CourseDAOImpl;
import org.consoleApp.domin.Course;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CoursesDataFillerTest extends AbstractContainerBaseTest {
    private final DataSource dataSource = getDataSource();
    private final CourseDAOImpl courseDAO = new CourseDAOImpl(dataSource);
    private final List<Course> expected = List.of(
            new Course(1, "PE", "PE"),
            new Course(2, "IT", "IT"),
            new Course(3, "Music", "Skryabin")
    );
    private final CoursesDataFiller dataFiller = new CoursesDataFiller(expected, courseDAO);


    @Test
    void testFillData_ShouldInsertBatchInDB(){
        dataFiller.fillData();
        List<Course> actual = courseDAO.findAll();

        assertEquals(expected, actual);
        assertNotNull(actual);
    }
}