package org.consoleApp.parametrizedDAOTest;

import org.consoleApp.dao.CourseDAO;
import org.consoleApp.domin.Course;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles({"spring-jdbc", "native-jdbc"})
@Sql(scripts = "classpath:schema.sql")
@Sql(scripts = "classpath:SQLScript/course_test_script.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class CourseParametrizedTests {

    @Autowired
    private List<CourseDAO> courseDAOList;
    private final List<Course> expectedCourses = List.of(
            new Course(1, "PE", "PE"),
            new Course(2, "IT", "IT"),
            new Course(3, "Music", "Skryabin")
    );
    private List<Course> actual;
    private List<Course> expected;

    private Stream<CourseDAO> getImpls() {
        return courseDAOList.stream();
    }

    @ParameterizedTest
    @MethodSource("getImpls")
    void testFindAll_ShouldFindAllCourses(CourseDAO courseDAO) {
        actual = courseDAO.findAll();

        assertEquals(expectedCourses, actual);
    }

    @ParameterizedTest
    @MethodSource("getImpls")
    void testFindById_ShouldFindCorrectCourseById(CourseDAO courseDAO){
        Optional<Course> findCourse = courseDAO.findById(1);
        Course actual = findCourse.orElseThrow(() -> new RuntimeException("Can't find course"));

        Course expected = expectedCourses.get(0);

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("getImpls")
    void testInsert_ShouldInsertCourse_AndReturnCorrectListOfCourses(CourseDAO courseDAO){
        expected = new ArrayList<>(expectedCourses);

        Course newCourse = new Course(4, "History", "History course");
        expected.add(newCourse);

        courseDAO.insert(newCourse);
        actual = courseDAO.findAll();

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("getImpls")
    void testInsertBatch_ShouldInsertBatchOfCourse_AndReturnCorrectListOfCourses(CourseDAO courseDAO){
        List<Course> coursesBatch = List.of(
                new Course(4, "History", "History course"),
                new Course(5, "Biology", "Biology course"),
                new Course(6, "Mathematics", "Mathematics course")
        );

        courseDAO.insertBatch(coursesBatch);
        actual = courseDAO.findAll();

        expected = new ArrayList<>(expectedCourses);
        expected.addAll(coursesBatch);

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("getImpls")
    void testUpdate_ShouldUpdateFirstCourseFromExpectedList(CourseDAO courseDAO){
        Course course = new Course(1, "AVT", "AVT");

        boolean update = courseDAO.update(course);
        actual = courseDAO.findAll();

        expected = new ArrayList<>(expectedCourses);
        expected.set(0, course);

        assertTrue(update);
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("getImpls")
    void testDelete_ShouldRemoveFirstCourseFromDB(CourseDAO courseDAO){
        boolean remove = courseDAO.delete(expectedCourses.get(0));
        actual = courseDAO.findAll();

        expected = new ArrayList<>(expectedCourses);
        expected.remove(0);

        assertTrue(remove);
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("getImpls")
    void testFindAllCourseByStudentsId(CourseDAO courseDAO){
        actual = courseDAO.findAllCourseByStudentsId(1);

        assertEquals(expectedCourses, actual);
    }
}
