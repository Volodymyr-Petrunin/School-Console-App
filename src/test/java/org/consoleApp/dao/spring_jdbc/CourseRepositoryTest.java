package org.consoleApp.dao.spring_jdbc;

import org.consoleApp.dao.jdbc.AbstractContainerBaseTest;
import org.consoleApp.dao.jdbc.CleanupAndFillData;
import org.consoleApp.domin.Course;
import org.consoleApp.domin.Student;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CourseRepositoryTest extends AbstractContainerBaseTest {
    private final static DataSource dataSource = getDataSource();
    private final CourseRepository courseRepository = new CourseRepository(dataSource);
    private final CleanupAndFillData cleanupAndFillData = new CleanupAndFillData(dataSource);
    private final List<Course> expectedCourses = List.of(
            new Course(1, "PE", "PE"),
            new Course(2, "IT", "IT"),
            new Course(3, "Music", "Skryabin")
    );
    private List<Course> expected;
    private List<Course> actual;

    @BeforeAll
    static void setup(){
        Student student = new Student(1, null, "Vova", "Petro");
        addStudent(student);
    }

    @BeforeEach
    void cleanupAndFillData(){
        cleanupAndFillData.deleteAll("courses");
        courseRepository.insertBatch(expectedCourses);
    }

    @Test
    void testFindAll_ShouldFindAllCourses() {
        actual = courseRepository.findAll();

        assertEquals(expectedCourses, actual);
    }

    @Test
    void testFindById_ShouldFindCorrectCourseById(){
        Optional<Course> findCourse = courseRepository.findById(1);
        Course actual = findCourse.orElseThrow(() -> new RuntimeException("Can't find course"));

        Course expected = expectedCourses.get(0);

        assertEquals(expected, actual);
    }

    @Test
    void testInsert_ShouldInsertCourse_AndReturnCorrectListOfCourses(){
        expected = new ArrayList<>(expectedCourses);

        Course newCourse = new Course(4, "History", "History course");
        expected.add(newCourse);

        courseRepository.insert(newCourse);
        actual = courseRepository.findAll();

        assertEquals(expected, actual);
    }

    @Test
    void testInsertBatch_ShouldInsertBatchOfCourse_AndReturnCorrectListOfCourses(){
        List<Course> coursesBatch = List.of(
                new Course(4, "History", "History course"),
                new Course(5, "Biology", "Biology course"),
                new Course(6, "Mathematics", "Mathematics course")
        );

        courseRepository.insertBatch(coursesBatch);
        actual = courseRepository.findAll();

        expected = new ArrayList<>(expectedCourses);
        expected.addAll(coursesBatch);

        assertEquals(expected, actual);
    }

    @Test
    void testUpdate_ShouldUpdateFirstCourseFromExpectedList(){
        Course course = new Course(1, "AVT", "AVT");

        boolean update = courseRepository.update(course);
        actual = courseRepository.findAll();

        expected = new ArrayList<>(expectedCourses);
        expected.set(0, course);

        assertTrue(update);
        assertEquals(expected, actual);
    }

    @Test
    void testDelete_ShouldRemoveFirstCourseFromDB(){
        boolean remove = courseRepository.delete(expectedCourses.get(0));
        actual = courseRepository.findAll();

        expected = new ArrayList<>(expectedCourses);
        expected.remove(0);

        assertTrue(remove);
        assertEquals(expected, actual);
    }

    @Test
    void testFindAllCourseByStudentsId(){

        for (Course course : expectedCourses){
            enrollStudentInCourse(1, course.getId());
        }

        actual = courseRepository.findAllCourseByStudentsId(1);

        assertEquals(expectedCourses, actual);
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
}