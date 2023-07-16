package org.consoleApp.dao.spring_jdbc;

import org.consoleApp.dao.GroupDAO;
import org.consoleApp.domin.Group;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class GroupDAORepository implements GroupDAO {
    private final JdbcTemplate jdbcTemplate;
    private String sql;
    private int rowAffected;

    public GroupDAORepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Group> findAll() {
        sql = "SELECT * FROM groups ORDER BY group_id";
        List<Group> groups = jdbcTemplate.query(sql, groupRowMapper);

        if (groups.isEmpty()){
            throw new IllegalStateException("Can't find groups");
        }

        return groups;
    }

    @Override
    public Optional<Group> findById(int id) {
        sql = "SELECT * FROM groups WHERE group_id = ?";
        Group group = jdbcTemplate.queryForObject(sql, new Object[] {id}, groupRowMapper);

        return Optional.ofNullable(group);
    }

    @Override
    public boolean insert(Group group) {
        sql = "INSERT INTO groups (group_name) VALUES (?)";

        rowAffected = jdbcTemplate.update(sql, group.getName());
        return rowAffected > 0;
    }

    @Override
    public void insertBatch(List<Group> groups) {
        sql = "INSERT INTO groups (group_name) VALUES (?)";

        List<Object[]> batchArgs = groups.stream()
                .map(group -> new Object[] {group.getName() })
                .toList();

        int[] rowAffected = jdbcTemplate.batchUpdate(sql, batchArgs);

        if (rowAffected.length != groups.size()){
            throw new IllegalStateException("Not all groups were inserted successfully");
        }
    }

    @Override
    public boolean update(Group group) {
       sql = "UPDATE groups SET group_name = ? WHERE group_id = ?";
       rowAffected = jdbcTemplate.update(sql, group.getName(), group.getId());

       return rowAffected > 0;
    }

    @Override
    public boolean delete(Group group) {
        sql = "DELETE FROM groups WHERE group_id = ?";
        rowAffected = jdbcTemplate.update(sql, group.getId());

        return rowAffected > 0;
    }

    @Override
    public List<Group> findGroupsWithLessOrEqualStudents(int maxStudents) {
        sql = "SELECT * FROM groups WHERE (SELECT COUNT(*) FROM students WHERE students.group_id = groups.group_id) <= ?";
        List<Group> groups = jdbcTemplate.query(sql, new Object[]{maxStudents}, groupRowMapper);

        if (groups.isEmpty()){
            throw new IllegalStateException("Can't find groups with less or equal students");
        }

        return groups;
    }

    private final RowMapper<Group> groupRowMapper = ((rs, rowNum) -> {
        int groupId = rs.getInt("group_id");
        String groupName = rs.getString("group_name");

        return new Group(groupId, groupName);
    });
}
