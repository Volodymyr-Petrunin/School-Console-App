package org.consoleApp.dao.parametrized;

import org.consoleApp.dao.CourseDAO;
import org.consoleApp.dao.jdbc.AbstractContainerBaseTest;
import org.consoleApp.dao.jdbc.CleanupAndFillData;
import org.consoleApp.dao.jdbc.CourseDAOImpl;
import org.consoleApp.dao.spring_jdbc.CourseRepository;
import org.consoleApp.domin.Course;
import org.consoleApp.domin.Student;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public abstract class CourseBase extends AbstractContainerBaseTest {
    protected final static DataSource dataSource = getDataSource();
    protected CourseDAO courseDAO = createCourseBase();
    protected final CleanupAndFillData cleanupAndFillData = new CleanupAndFillData(dataSource);
    private final List<Course> expectedCourses = List.of(
            new Course(1, "PE", "PE"),
            new Course(2, "IT", "IT"),
            new Course(3, "Music", "Skryabin")
    );
    private List<Course> actual;
    private List<Course> expected;

    @BeforeAll
    static void setup(){
        Student student = new Student(1, null, "Vova", "Petro");
        addStudent(student);
    }

    @BeforeEach
    void cleanupAndFillData(){
        cleanupAndFillData.deleteAll("courses");
        courseDAO.insertBatch(expectedCourses);
    }
    protected abstract CourseDAO createCourseBase();

    @ParameterizedTest
    @MethodSource("courseBases")
    void testFindAll_ShouldFindAllCourses(CourseDAO courseDAO) {
        actual = courseDAO.findAll();

        assertEquals(expectedCourses, actual);
    }

    @ParameterizedTest
    @MethodSource("courseBases")
    void testFindById_ShouldFindCorrectCourseById(CourseDAO courseDAO){
        Optional<Course> findCourse = courseDAO.findById(1);
        Course actual = findCourse.orElseThrow(() -> new RuntimeException("Can't find course"));

        Course expected = expectedCourses.get(0);

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("courseBases")
    void testInsert_ShouldInsertCourse_AndReturnCorrectListOfCourses(){
        expected = new ArrayList<>(expectedCourses);

        Course newCourse = new Course(4, "History", "History course");
        expected.add(newCourse);

        courseDAO.insert(newCourse);
        actual = courseDAO.findAll();

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("courseBases")
    void testInsertBatch_ShouldInsertBatchOfCourse_AndReturnCorrectListOfCourses(){
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
    @MethodSource("courseBases")
    void testUpdate_ShouldUpdateFirstCourseFromExpectedList(){
        Course course = new Course(1, "AVT", "AVT");

        boolean update = courseDAO.update(course);
        actual = courseDAO.findAll();

        expected = new ArrayList<>(expectedCourses);
        expected.set(0, course);

        assertTrue(update);
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("courseBases")
    void testDelete_ShouldRemoveFirstCourseFromDB(){
        boolean remove = courseDAO.delete(expectedCourses.get(0));
        actual = courseDAO.findAll();

        expected = new ArrayList<>(expectedCourses);
        expected.remove(0);

        assertTrue(remove);
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("courseBases")
    void testFindAllCourseByStudentsId(){

        for (Course course : expectedCourses){
            enrollStudentInCourse(1, course.getId());
        }

        actual = courseDAO.findAllCourseByStudentsId(1);

        assertEquals(expectedCourses, actual);
    }

    static Stream<Arguments> courseBases() {
        return Stream.of(
                Arguments.of(new CourseDAOImpl(dataSource)),
                Arguments.of(new CourseRepository(dataSource))
        );
    }



    private static void addStudent(Student student){
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO students (student_id, group_id, first_name, last_name) VALUES (?, ?, ?, ?)")){

            preparedStatement.setInt(1, student.getId());
            preparedStatement.setObject(2, student.getGroupId().orElse(null), Types.INTEGER);
            preparedStatement.setString(3, student.getFirstName());
            preparedStatement.setString(4, student.getLastName());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void enrollStudentInCourse(int studentId, int courseId){
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO enrollments (student_id,course_id) VALUES (?,?)")){
            preparedStatement.setInt(1,studentId);
            preparedStatement.setInt(2,courseId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Can't register", e);
        }
    }
}
