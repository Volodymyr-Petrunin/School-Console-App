package org.consoleApp.dao.jdbc;

import org.consoleApp.domin.Course;
import org.consoleApp.domin.Student;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class StudentsDAOImplTest extends AbstractContainerBaseTest {
    private static final DataSource dataSource = getDataSource();
    private final StudentsDAOImpl studentsDAO = new StudentsDAOImpl(dataSource);
    private final CleanupAndFillData cleanupAndFillData = new CleanupAndFillData(dataSource);
    private final List<Student> expectedStudents = List.of(
            new Student(1, null, "John", "Doe"),
            new Student(2, null, "Jane", "Smith"),
            new Student(3, null, "Michael", "Johnson")
    );
    private List<Student> expected;
    private List<Student> actual;

    @BeforeAll
    static void beforeAll(){
        addCourse();
    }

    @BeforeEach
    void cleanupAndFillData(){
        cleanupAndFillData.deleteAll("students");
        studentsDAO.insertBatch(expectedStudents);
    }

    @Test
    void testFindAll_ShouldFindAllStudents() {
        actual = studentsDAO.findAll();

        assertEquals(expectedStudents, actual);
    }

    @Test
    void testFindById_ShouldFindCorrectStudentById() {
        Optional<Student> findStudent = studentsDAO.findById(1);
        Student actual = findStudent.orElseThrow(() -> new RuntimeException("Can't get student"));

        Student expected = expectedStudents.get(0);

        assertEquals(expected, actual);
    }

    @Test
    void testInsert_ShouldInsertStudent_AndReturnCorrectListOfStudents() {
        expected = new ArrayList<>(expectedStudents);

        Student newStudent = new Student(4, null, "Vova", "Petro");
        expected.add(newStudent);

        studentsDAO.insert(newStudent);
        actual = studentsDAO.findAll();

        assertEquals(expected, actual);
    }

    @Test
    void testInsertBatch_ShouldInsertBatchOfStudents_AndReturnCorrectListOfStudents() {
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

    @Test
    void testUpdate_ShouldUpdateFirstStudentsFromExpectedList() {
        Student student = new Student(1, null, "Lando", "Brown");

        boolean update = studentsDAO.update(student);
        actual = studentsDAO.findAll();

        expected = new ArrayList<>(expectedStudents);
        expected.set(0, student);

        assertTrue(update);
        assertThat(expected, containsInAnyOrder(actual.toArray()));
    }

    @Test
    void testDelete_ShouldRemoveFirstStudentFromDB() {
        boolean delete = studentsDAO.delete(expectedStudents.get(0));
        actual = studentsDAO.findAll();

        expected = new ArrayList<>(expectedStudents);
        expected.remove(0);

        assertTrue(delete);
        assertEquals(expected, actual);
    }

    @Test
    void testDeleteByStudentId_ShouldRemoveStudentFromDBbyID_AndReturnCorrectList(){
        boolean delete = studentsDAO.deleteByStudentId(1);

        actual = studentsDAO.findAll();

        expected = new ArrayList<>(expectedStudents);
        expected.remove(0);

        assertTrue(delete);
        assertEquals(expected, actual);
    }

    @Test
    void testFindByFirstName_ShouldFindCorrectStudentByName_AndReturnCorrectList(){
        String firstName = expectedStudents.get(0).getFirstName();
        actual = studentsDAO.findByFirstName(firstName);

        assertThat(actual, containsInAnyOrder(hasProperty("firstName", equalTo(firstName))));
        assertThat(actual, hasSize(1));
    }

    @Test
    void testEnrollStudentInCourse_ShouldEnrollStudentToCourse_AndReturnCorrectCourseList(){
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

    @Test
    void testRemoveStudentFromCourse_ShouldRemoveStudentFromCourse(){
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

    @Test
    void soon(){
        String courseName = "PE";

        expected = new ArrayList<>(expectedStudents);
        expected.remove(0);

        for (Student student : expected){
            studentsDAO.enrollStudentInCourse(student.getId(), 1);
        }

        actual = studentsDAO.findStudentsByCourseName(courseName);

        assertEquals(expected, actual);
    }


    private static void insertCourseBatch(List<Course> courses) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO courses (course_name, course_description) VALUES (?, ?)")) {


            for (Course course : courses) {
                preparedStatement.setString(1, course.getName());
                preparedStatement.setString(2, course.getDescription());

                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

    private static void addCourse(){
        List<Course> courses = List.of(
                new Course(1, "PE", "PE"),
                new Course(2, "IT", "IT"),
                new Course(3, "Music", "Skryabin")
        );

        insertCourseBatch(courses);
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