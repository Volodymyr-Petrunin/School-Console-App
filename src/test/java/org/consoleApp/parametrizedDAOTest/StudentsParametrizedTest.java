package org.consoleApp.parametrizedDAOTest;

import org.consoleApp.dao.StudentsDAO;
import org.consoleApp.domin.Course;
import org.consoleApp.domin.Student;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = DaoTestConfig.class)
@ActiveProfiles({"spring-jdbc", "native-jdbc"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Sql(value = "classpath:SQLScript/students_test_script.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
 class StudentsParametrizedTest {
    @Autowired
    private List<StudentsDAO> studentsDAOList;
    @Autowired
    private DataSource dataSource;
    private final List<Student> expectedStudents = List.of(
            new Student(1, null, "John", "Doe"),
            new Student(2, null, "Jane", "Smith"),
            new Student(3, null, "Michael", "Johnson")
    );
    private List<Student> expected;
    private List<Student> actual;

    private Stream<StudentsDAO> getImpl(){
        return studentsDAOList.stream();
    }

    @ParameterizedTest
    @MethodSource("getImpl")
    void testFindAll_ShouldFindAllStudents(StudentsDAO studentsDAO) {
        actual = studentsDAO.findAll();

        assertEquals(expectedStudents, actual);
    }

    @ParameterizedTest
    @MethodSource("getImpl")
    void testFindById_ShouldFindCorrectStudentById(StudentsDAO studentsDAO) {
        Optional<Student> findStudent = studentsDAO.findById(1);
        Student actual = findStudent.orElseThrow(() -> new RuntimeException("Can't get student"));

        Student expected = expectedStudents.get(0);

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("getImpl")
    void testInsert_ShouldInsertStudent_AndReturnCorrectListOfStudents(StudentsDAO studentsDAO) {
        expected = new ArrayList<>(expectedStudents);

        Student newStudent = new Student(4, null, "Vova", "Petro");
        expected.add(newStudent);

        studentsDAO.insert(newStudent);
        actual = studentsDAO.findAll();

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("getImpl")
    void testInsertBatch_ShouldInsertBatchOfStudents_AndReturnCorrectListOfStudents(StudentsDAO studentsDAO) {
        List<Student> studentsBatch = List.of(
                new Student(4, null, "Vova", "Petro"),
                new Student(5, null, "Max", "Kozak"),
                new Student(6, null, "Lando", "Brown")
        );

        expected = new ArrayList<>(expectedStudents);
        expected.addAll(studentsBatch);

        studentsDAO.insertBatch(studentsBatch);
        actual = studentsDAO.findAll();

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("getImpl")
    void testUpdate_ShouldUpdateFirstStudentsFromExpectedList(StudentsDAO studentsDAO) {
        Student student = new Student(1, null, "Lando", "Brown");

        boolean update = studentsDAO.update(student);
        actual = studentsDAO.findAll();

        expected = new ArrayList<>(expectedStudents);
        expected.set(0, student);

        assertTrue(update);
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("getImpl")
    void testDelete_ShouldRemoveFirstStudentFromDB(StudentsDAO studentsDAO) {
        boolean delete = studentsDAO.delete(expectedStudents.get(0));
        actual = studentsDAO.findAll();

        expected = new ArrayList<>(expectedStudents);
        expected.remove(0);

        assertTrue(delete);
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("getImpl")
    void testDeleteByStudentId_ShouldRemoveStudentFromDBbyID_AndReturnCorrectList(StudentsDAO studentsDAO){
        boolean delete = studentsDAO.deleteByStudentId(1);

        actual = studentsDAO.findAll();

        expected = new ArrayList<>(expectedStudents);
        expected.remove(0);

        assertTrue(delete);
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("getImpl")
    void testFindByFirstName_ShouldFindCorrectStudentByName_AndReturnCorrectList(StudentsDAO studentsDAO){
        String firstName = expectedStudents.get(0).getFirstName();
        actual = studentsDAO.findByFirstName(firstName);

        assertThat(actual, containsInAnyOrder(hasProperty("firstName", equalTo(firstName))));
        assertThat(actual, hasSize(1));
    }

    @ParameterizedTest
    @MethodSource("getImpl")
    void testEnrollStudentInCourse_ShouldEnrollStudentToCourse_AndReturnCorrectCourseList(StudentsDAO studentsDAO){
        int courseID = 1;

        List<Course> expectedCourses = new ArrayList<>();

        for (Student student : expectedStudents){
            boolean enroll = studentsDAO.enrollStudentInCourse(student.getId(), courseID);
            assertTrue(enroll);

            List<Course> studentCourses = findAllCourseByStudentsId(student.getId());
            expectedCourses.add(studentCourses.get(studentCourses.size() - 1));

            courseID++;
        }

        List<Course> actualCourses = findAllCourses();
        assertThat(actualCourses, containsInAnyOrder(expectedCourses.toArray()));
    }

    @ParameterizedTest
    @MethodSource("getImpl")
    void testRemoveStudentFromCourse_ShouldRemoveStudentFromCourse(StudentsDAO studentsDAO){
        int studentID = 1;
        int courseID = 1;

        for (Course course : findAllCourses()){
            studentsDAO.enrollStudentInCourse(studentID, course.getId());
        }

        boolean remove = studentsDAO.removeStudentFromCourse(studentID, courseID);
        assertTrue(remove);

        List<Course> expected = findAllCourses();
        expected.remove(0);

        List<Course> actual = findAllCourseByStudentsId(studentID);

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("getImpl")
    void testFindStudentsByCourseName_ShouldFindCorrectStudentsByCourseName_AndReturnCorrectStudentsList(StudentsDAO studentsDAO){
        String courseName = "PE";

        expected = new ArrayList<>(expectedStudents);
        expected.remove(0);

        for (Student student : expected){
            studentsDAO.enrollStudentInCourse(student.getId(), 1);
        }

        actual = studentsDAO.findStudentsByCourseName(courseName);

        assertEquals(expected, actual);
    }

    private List<Course> findAllCourseByStudentsId(int studentId) {
        List<Course> courses = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT c.course_id, c.course_name, c.course_description FROM courses c JOIN enrollments e ON c.course_id = e.course_id WHERE e.student_id = ?")){
            preparedStatement.setInt(1, studentId);

            try (ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()){
                    int courseId = resultSet.getInt("course_id");
                    String courseName = resultSet.getString("course_name");
                    String courseDescription = resultSet.getString("course_description");

                    courses.add(new Course(courseId, courseName, courseDescription));
                }
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Can't fetch courses", e);
        }
        return courses;
    }

    public List<Course> findAllCourses() {
        List<Course> courses = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM courses");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()){
                int courseId = resultSet.getInt("course_id");
                String courseName = resultSet.getString("course_name");
                String courseDescription = resultSet.getString("course_description");

                courses.add(new Course(courseId, courseName, courseDescription));
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Can't fetch courses", e);
        }

        return courses;
    }
}
