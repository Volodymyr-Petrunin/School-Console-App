package org.consoleApp.parametrizedDAOTest;

import org.consoleApp.dao.GroupDAO;
import org.consoleApp.domin.Group;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = DaoTestConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles({"spring-jdbc", "native-jdbc"})
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@Sql(value = "classpath:schema.sql")
@Sql(value = "classpath:SQLScript/group_test_script.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class GroupParametrizedTests {
    @Autowired
    private List<GroupDAO> groupDAOList;
    private final static List<Group> expectedGroup = List.of(
            new Group(1, "AA-11"),
            new Group(2, "BB-22"),
            new Group(3, "CC-33")
    );
    private List<Group> expected;
    private List<Group> actual;

    private Stream<GroupDAO> getImpl(){
        return groupDAOList.stream();
    }

    @ParameterizedTest
    @MethodSource("getImpl")
    void testFindAll_ShouldFindAllGroups(GroupDAO groupDAO) {
        actual = groupDAO.findAll();

        assertEquals(expectedGroup, actual);
    }

    @ParameterizedTest
    @MethodSource("getImpl")
    void testFindById_ShouldFindCorrectGroupById(GroupDAO groupDAO) {
        Optional<Group> findGroup = groupDAO.findById(1);
        Group actual = findGroup.orElseThrow(() -> new RuntimeException("Can't find group"));

        Group expected = expectedGroup.get(0);

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("getImpl")
    void testInsert_ShouldInsertGroup_AndReturnCorrectListOfGroups(GroupDAO groupDAO) {
        expected = new ArrayList<>(expectedGroup);

        Group newGroup = new Group(4, "DD-44");
        expected.add(newGroup);

        groupDAO.insert(newGroup);
        actual = groupDAO.findAll();

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("getImpl")
    void testInsertBatch_ShouldInsertBatchOfGroups_AndReturnCorrectListOfGroups(GroupDAO groupDAO) {
        List<Group> groupBatch = List.of(
                new Group(4, "DD-44"),
                new Group(5, "EE-55"),
                new Group(6, "FF-66")
        );

        expected = new ArrayList<>(expectedGroup);
        expected.addAll(groupBatch);

        groupDAO.insertBatch(groupBatch);
        actual = groupDAO.findAll();

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("getImpl")
    void testUpdate_ShouldUpdateFirstGroupFromExpectedList(GroupDAO groupDAO) {
        Group group = new Group(1, "NN-00");

        boolean update = groupDAO.update(group);
        actual = groupDAO.findAll();

        expected = new ArrayList<>(expectedGroup);
        expected.set(0, group);

        assertTrue(update);
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("getImpl")
    void testDelete_ShouldRemoveFirstGroupFromDB(GroupDAO groupDAO) {
        boolean delete = groupDAO.delete(expectedGroup.get(0));
        actual = groupDAO.findAll();

        expected = new ArrayList<>(expectedGroup);
        expected.remove(0);

        assertTrue(delete);
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("getImpl")
    @Sql(value = {"classpath:SQLScript/add_students_for_group_test.sql"})
    void testFindGroupsWithLessOrEqualStudents_ShouldReturnGroupsWithMaxStudentsOrLess(GroupDAO groupDAO){
        int maxStudents = 2;

        expected = new ArrayList<>(expectedGroup);
        expected.remove(0); //remove group 1 because it has 3 students but max 2

        actual = groupDAO.findGroupsWithLessOrEqualStudents(maxStudents);

        assertEquals(expected, actual);
    }
}
