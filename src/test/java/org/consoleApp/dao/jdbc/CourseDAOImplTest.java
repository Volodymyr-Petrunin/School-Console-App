package org.consoleApp.dao.jdbc;

import org.consoleApp.dataBaseSettings.DBConnector;
import org.consoleApp.dataBaseSettings.ScriptRunner;
import org.consoleApp.domin.Course;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.List;

class CourseDAOImplTest {
    private static CourseDAOImpl courseDAO;
    private static PostgreSQLContainer<?> postgreSQLContainer;
    private static  ScriptRunner scriptRunner;
    private static InputStream inputStream;

    @BeforeAll
    static void setup(){
        postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
                .withDatabaseName("school-console-app")
                .withUsername("postgres")
                .withPassword("0403");

        postgreSQLContainer.start();

        DBConnector dbConnector = new DBConnector();
        DataSource dataSource = dbConnector.getDBConnection();

        scriptRunner = new ScriptRunner(dataSource);
        inputStream = CourseDAOImplTest.class.getResourceAsStream("/SQLScript/create_tables.sql");
        courseDAO = new CourseDAOImpl(dataSource);
    }

    @BeforeEach
    void cleanup() {
        scriptRunner.runScript(inputStream);
    }

    @AfterAll
    static void teardown(){
        postgreSQLContainer.stop();
    }

    @Test
    void findAll() {

        courseDAO.insert(new Course(null, "PE", "PE"));
        courseDAO.insert(new Course(null, "IT", "IT"));

        List<Course> expected = List.of(
                new Course(1, "PE", "PE"),
                new Course(2, "IT", "IT")
        );

        List<Course> actual = courseDAO.findAll();

        assertEquals(expected, actual);
    }
}