package org.consoleApp.dao.spring_jdbc;

import org.consoleApp.dao.jdbc.AbstractContainerBaseTest;
import org.consoleApp.dao.jdbc.CleanupAndFillData;
import org.consoleApp.domin.Group;
import org.consoleApp.domin.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GroupRepositoryTest extends AbstractContainerBaseTest {
    private final DataSource dataSource = getDataSource();
    private final CleanupAndFillData cleanupAndFillData = new CleanupAndFillData(dataSource);
    private final GroupRepository groupRepository = new GroupRepository(dataSource);
    private static final List<Group> expectedGroup = List.of(
            new Group(1, "AA-11"),
            new Group(2, "BB-22"),
            new Group(3, "CC-33")
    );
    private List<Group> expected;
    private List<Group> actual;

    @BeforeEach
    void cleanupAndFillData(){
        cleanupAndFillData.deleteAll("groups");
        groupRepository.insertBatch(expectedGroup);
    }

    @Test
    void testFindAll_ShouldFindAllGroups() {
        actual = groupRepository.findAll();

        assertEquals(expectedGroup, actual);
    }

    @Test
    void testFindById_ShouldFindCorrectGroupById() {
        Optional<Group> findGroup = groupRepository.findById(1);
        Group actual = findGroup.orElseThrow(() -> new RuntimeException("Can't find group"));

        Group expected = expectedGroup.get(0);

        assertEquals(expected, actual);
    }

    @Test
    void testInsert_ShouldInsertGroup_AndReturnCorrectListOfGroups() {
        expected = new ArrayList<>(expectedGroup);

        Group newGroup = new Group(4, "DD-44");
        expected.add(newGroup);

        boolean insert = groupRepository.insert(newGroup);
        actual = groupRepository.findAll();

        assertTrue(insert);
        assertEquals(expected, actual);
    }

    @Test
    void testInsertBatch_ShouldInsertBatchOfGroups_AndReturnCorrectListOfGroups() {
        List<Group> groupBatch = List.of(
                new Group(4, "DD-44"),
                new Group(5, "EE-55"),
                new Group(6, "FF-66")
        );

        expected = new ArrayList<>(expectedGroup);
        expected.addAll(groupBatch);

        groupRepository.insertBatch(groupBatch);
        actual = groupRepository.findAll();

        assertEquals(expected, actual);
    }

    @Test
    void testUpdate_ShouldUpdateFirstGroupFromExpectedList() {
        Group group = new Group(1, "NN-00");

        boolean update = groupRepository.update(group);
        actual = groupRepository.findAll();

        expected = new ArrayList<>(expectedGroup);
        expected.set(0, group);

        assertTrue(update);
        assertEquals(expected, actual);
    }

    @Test
    void testDelete_ShouldRemoveFirstGroupFromDB() {
        boolean delete = groupRepository.delete(expectedGroup.get(0));
        actual = groupRepository.findAll();

        expected = new ArrayList<>(expectedGroup);
        expected.remove(0);

        assertTrue(delete);
        assertEquals(expected, actual);
    }

    @Test
    void testFindGroupsWithLessOrEqualStudents_ShouldReturnGroupsWithMaxStudentsOrLess(){
        addStudent();
        int maxStudents = 2;

        expected = new ArrayList<>(expectedGroup);
        expected.remove(0); //remove group 1 because it has 3 students but max 2

        actual = groupRepository.findGroupsWithLessOrEqualStudents(maxStudents);

        assertEquals(expected, actual);
    }

    private void addStudent(){
        List<Student> students = List.of(
                new Student(1, 1, "John", "Doe"),
                new Student(2, 1, "Jane", "Smith"),
                new Student(3, 1, "Michael", "Johnson"),
                new Student(4, 2, "Emily", "Williams"),
                new Student(5, 2, "Daniel", "Brown"),
                new Student(6, 3, "Zak", "Brown")
        );

        cleanupAndFillData.addStudentsBatch(students);
    }
}