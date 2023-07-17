package org.consoleApp.dao.spring_jdbc;

import org.consoleApp.dao.GroupDAO;
import org.consoleApp.dao.spring_jdbc.rowMappers.GroupRowMapper;
import org.consoleApp.domin.Group;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class GroupRepository implements GroupDAO {
    private static final String FIND_ALL = "SELECT * FROM groups ORDER BY group_id";
    private static final String FIND_BY_ID = "SELECT * FROM groups WHERE group_id = ?";
    private static final String INSERT = "INSERT INTO groups (group_name) VALUES (?)";
    private static final String INSERT_BATCH = "INSERT INTO groups (group_name) VALUES (?)";
    private static final String UPDATE = "UPDATE groups SET group_name = ? WHERE group_id = ?";
    private static final String DELETE = "DELETE FROM groups WHERE group_id = ?";
    private static final String FIND_GROUP_WITH_LESS_OR_EQUAL_STUDENTS = "SELECT * FROM groups WHERE (SELECT COUNT(*) FROM students WHERE students.group_id = groups.group_id) <= ?";
    private final GroupRowMapper groupRowMapper = new GroupRowMapper();
    private final JdbcTemplate jdbcTemplate;

    public GroupRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Group> findAll() {
        return jdbcTemplate.query(FIND_ALL, groupRowMapper);
    }

    @Override
    public Optional<Group> findById(int id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_BY_ID, new Object[] {id}, groupRowMapper));
    }

    @Override
    public boolean insert(Group group) {
       return jdbcTemplate.update(INSERT, group.getName()) > 0;
    }

    @Override
    public void insertBatch(List<Group> groups) {
        List<Object[]> batchArgs = groups.stream()
                .map(group -> new Object[] {group.getName() })
                .toList();

        jdbcTemplate.batchUpdate(INSERT_BATCH, batchArgs);
    }

    @Override
    public boolean update(Group group) {
       return jdbcTemplate.update(UPDATE, group.getName(), group.getId()) > 0;
    }

    @Override
    public boolean delete(Group group) {
        return jdbcTemplate.update(DELETE, group.getId()) > 0;
    }

    @Override
    public List<Group> findGroupsWithLessOrEqualStudents(int maxStudents) {
        return jdbcTemplate.query(FIND_GROUP_WITH_LESS_OR_EQUAL_STUDENTS, new Object[]{maxStudents}, groupRowMapper);
    }
}
