package org.consoleApp.dao.jdbc;

import org.consoleApp.dataBaseSettings.ScriptRunner;
import org.consoleApp.domin.Course;
import org.junit.After;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import org.consoleApp.dao.jdbc.abstracts.AbstractContainerBaseTest;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.*;
import java.util.List;
import java.util.Optional;

class CourseDAOImplTest extends AbstractContainerBaseTest{
    private final static InputStream inputStream = CourseDAOImplTest.class.getResourceAsStream("/SQLScript/create_tables.sql");
    private final static DataSource dataSource = getDataSource();
    private final static ScriptRunner scriptRunner = new ScriptRunner(dataSource);
    private final CourseDAOImpl courseDAO = new CourseDAOImpl(dataSource);
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
    void cleanup(){
        deleteAll();
        fillData(expectedList);
    }

    @Test
    void testFindAll() {
        List<Course> actual = courseDAO.findAll();

        assertEquals(expectedList, actual);
    }

    @Test
    void testFindById(){
        Optional<Course> findCourse = courseDAO.findById(1);
        Course actual = findCourse.orElseThrow(() -> new RuntimeException("Can't find course"));

        Course expected = expectedList.get(0);

        assertEquals(expected, actual);
    }

    private void deleteAll(){
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("TRUNCATE TABLE courses RESTART IDENTITY CASCADE ")){

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new IllegalStateException("Can't delete all",e);
        }
    }

    private void fillData(List<Course> courses){
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO courses (course_name, course_description) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {


            for (Course course : courses){
                preparedStatement.setString(1, course.getName());
                preparedStatement.setString(2, course.getDescription());

                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()){
                for (Course course : courses){

                    if (!generatedKeys.next()){
                        throw new IllegalStateException("Not enough generated keys returned during courses batch insert");
                    }

                    course.setId(generatedKeys.getInt(1));
                }

                if (generatedKeys.next()){
                    throw new IllegalStateException("Too many generated keys returned during courses batch insert");
                }
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Can't insert batch of courses", e);
        }
    }
}