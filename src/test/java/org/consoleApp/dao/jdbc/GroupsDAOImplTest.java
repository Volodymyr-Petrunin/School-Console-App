package org.consoleApp.dao.jdbc;

import org.consoleApp.domin.Course;
import org.consoleApp.domin.Group;
import org.consoleApp.domin.Student;
import org.junit.jupiter.api.BeforeAll;
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
    private final GroupsDAOImpl groupsDAO = new GroupsDAOImpl(dataSource);
    private final List<Group> expectedGroup = List.of(
      new Group(1, "AA-11"),
      new Group(2, "BB-22"),
      new Group(3, "CC-33")
    );
    private final  Cleanup cleanup = new Cleanup(dataSource);
    private List<Group> expected;
    private List<Group> actual;

    @BeforeAll
    static void setup(){
        Student student1 = new Student(1, 1, "John", "Doe");
        Student student2 = new Student(2, 1, "Jane", "Smith");
        Student student3 = new Student(3, 1, "Michael", "Johnson");
        Student student4 = new Student(4, 2, "Emily", "Williams");
        Student student5 = new Student(6, 2, "Daniel", "Brown");
        Student student6 = new Student(7, 3, "Zak", "Brown");
    }

    @BeforeEach
    void cleanupAndFillData(){
        cleanup.deleteAll("groups");
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
}