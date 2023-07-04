package org.consoleApp.dao.jdbc;

import org.consoleApp.dataBaseSettings.ScriptRunner;
import org.consoleApp.domin.Course;
import org.consoleApp.domin.Student;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import org.consoleApp.dao.jdbc.abstracts.AbstractContainerBaseTest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class CourseDAOImplTest extends AbstractContainerBaseTest{
    private final static InputStream inputStream = CourseDAOImplTest.class.getResourceAsStream("/SQLScript/create_tables.sql");
    private final static DataSource dataSource = getDataSource();
    private final static ScriptRunner scriptRunner = new ScriptRunner(dataSource);
    private final CourseDAOImpl courseDAO = new CourseDAOImpl(dataSource);
    private final StudentsDAOImpl studentsDAO = new StudentsDAOImpl(dataSource);
    private final List<Course> expectedList = List.of(
            new Course(1, "PE", "PE"),
            new Course(2, "IT", "IT"),
            new Course(3, "Music", "Skryabin")
    );

    @BeforeAll
    static void setup(){
        scriptRunner.runScript(inputStream);
    }

    @BeforeEach
    void cleanupAndFillData(){
        deleteAll();
        courseDAO.insertBatch(expectedList);
    }

    @Test
    void testFindAll_ShouldFindAllCourses() {
        List<Course> actual = courseDAO.findAll();

        assertEquals(expectedList, actual);
    }

    @Test
    void testFindById_ShouldFindCorrectCourseById(){
        Optional<Course> findCourse = courseDAO.findById(1);
        Course actual = findCourse.orElseThrow(() -> new RuntimeException("Can't find course"));

        Course expected = expectedList.get(0);

        assertEquals(expected, actual);
    }

    @Test
    void testInsert_ShouldInsertCourse_AndReturnCorrectListOfCourses(){
        Course newCourse = new Course(4, "History", "History course");

        courseDAO.insert(newCourse);
        List<Course> actual = courseDAO.findAll();

        List<Course> expected = new ArrayList<>(expectedList);
        expected.add(newCourse);

        assertEquals(expected, actual);
    }

    @Test
    void testInsertBatch_ShShouldInsertBatchOfCourse_AndReturnCorrectListOfCourses(){
        List<Course> coursesBatch = List.of(
                new Course(4, "History", "History course"),
                new Course(5, "Biology", "Biology course"),
                new Course(6, "Mathematics", "Mathematics course")
        );

        courseDAO.insertBatch(coursesBatch);
        List<Course> actual = courseDAO.findAll();

        List<Course> expected = new ArrayList<>(expectedList);
        expected.addAll(coursesBatch);

        assertEquals(expected, actual);
    }

    @Test
    void testUpdate_ShouldUpdateFirstCourseFromExpectedList(){
        Course course = new Course(1, "AVT", "AVT");

        boolean update = courseDAO.update(course);
        List<Course> actual = courseDAO.findAll();

        List<Course> expected = new ArrayList<>(expectedList);
        expected.set(0, course);

        assertTrue(update);
        assertThat(actual, containsInAnyOrder(expected.toArray()));
    }

    @Test
    void testDelete_ShouldRemoveFirstCourseFromDB(){
        boolean remove = courseDAO.delete(expectedList.get(0));
        List<Course> actual = courseDAO.findAll();

        List<Course> expected = new ArrayList<>(expectedList);
        expected.remove(0);

        assertTrue(remove);
        assertEquals(expected, actual);
    }

    @Test
    void testFindAllCourseByStudentsId(){
        Student student = new Student(1, null, "Vova", "Petro");
        studentsDAO.insert(student);

        for (Course course : expectedList){
            studentsDAO.enrollStudentInCourse(1, course.getId());
        }

        List<Course> actual = courseDAO.findAllCourseByStudentsId(1);

        assertEquals(expectedList, actual);
    }

    @AfterAll
    static void stopSQLContainer(){
        stopPostgreSQLContainer();
    }

    private void deleteAll(){
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("TRUNCATE TABLE courses RESTART IDENTITY CASCADE ")){

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new IllegalStateException("Can't delete all",e);
        }
    }
}