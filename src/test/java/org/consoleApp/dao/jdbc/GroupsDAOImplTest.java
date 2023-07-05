package org.consoleApp.dao.jdbc;

import org.consoleApp.domin.Group;
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

class GroupsDAOImplTest extends AbstractContainerBaseTest {
    private final static DataSource dataSource = getDataSource();
    private final static GroupsDAOImpl groupsDAO = new GroupsDAOImpl(dataSource);
    private final static List<Group> expectedGroup = List.of(
      new Group(1, "AA-11"),
      new Group(2, "BB-22"),
      new Group(3, "CC-33")
    );
    private final CleanupAndFillData cleanupAndFillData = new CleanupAndFillData(dataSource);
    private List<Group> expected;
    private List<Group> actual;

    @BeforeEach
    void cleanupAndFillData(){
        cleanupAndFillData.deleteAll("groups");
        groupsDAO.insertBatch(expectedGroup);
    }

    @Test
    void testFindAll_ShouldFindAllGroups() {
        actual = groupsDAO.findAll();

        assertEquals(expectedGroup, actual);
    }

    @Test
    void testFindById_ShouldFindCorrectGroupById() {
        Optional<Group> findGroup = groupsDAO.findById(1);
        Group actual = findGroup.orElseThrow(() -> new RuntimeException("Can't find group"));

        Group expected = expectedGroup.get(0);

        assertEquals(expected, actual);
    }

    @Test
    void testInsert_ShouldInsertGroup_AndReturnCorrectListOfGroups() {
        expected = new ArrayList<>(expectedGroup);

        Group newGroup = new Group(4, "DD-44");
        expected.add(newGroup);

        groupsDAO.insert(newGroup);
        actual = groupsDAO.findAll();

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

        groupsDAO.insertBatch(groupBatch);
        actual = groupsDAO.findAll();

        assertEquals(expected, actual);
    }

    @Test
    void testUpdate_ShouldUpdateFirstGroupFromExpectedList() {
        Group group = new Group(1, "NN-00");

        boolean update = groupsDAO.update(group);
        actual = groupsDAO.findAll();

        expected = new ArrayList<>(expectedGroup);
        expected.set(0, group);

        assertTrue(update);
        assertThat(actual, containsInAnyOrder(expected.toArray()));
    }

    @Test
    void testDelete_ShouldRemoveFirstGroupFromDB() {
        boolean delete = groupsDAO.delete(expectedGroup.get(0));
        actual = groupsDAO.findAll();

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

        actual = groupsDAO.findGroupsWithLessOrEqualStudents(maxStudents);

        assertEquals(expected, actual);
    }

    @Test
    void testFindGroupIdByName_ShouldReturnFirstGroupFromExpectedGroupList(){
        String name = expectedGroup.get(0).getName();

        Optional<Group> group = groupsDAO.findGroupIdByName(name);
        Group actual = group.orElseThrow(() -> new RuntimeException("Can't get group"));

        Group expected = expectedGroup.get(0);

        assertEquals(expected, actual);
    }

    private void addStudent(){ // we need this method because when we clean groups table we use cascade delete so all students removed after test
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