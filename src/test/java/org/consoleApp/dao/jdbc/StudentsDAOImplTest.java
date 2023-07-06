package org.consoleApp.dao.jdbc;

import org.consoleApp.domin.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class StudentsDAOImplTest extends AbstractContainerBaseTest {
    private final DataSource dataSource = getDataSource();
    private final StudentsDAOImpl studentsDAO = new StudentsDAOImpl(dataSource);
    private final CleanupAndFillData cleanupAndFillData = new CleanupAndFillData(dataSource);
    private final List<Student> expectedStudents = List.of(
            new Student(1, null, "John", "Doe"),
            new Student(2, null, "Jane", "Smith"),
            new Student(3, null, "Michael", "Johnson")
    );
    private List<Student> expected;
    private List<Student> actual;

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
        expected.set(1, student);

        assertTrue(update);
        assertThat(actual, containsInAnyOrder(expected.toArray()));
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
}