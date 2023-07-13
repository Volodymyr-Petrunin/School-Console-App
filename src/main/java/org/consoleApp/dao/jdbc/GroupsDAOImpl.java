package org.consoleApp.dao.jdbc;

import org.consoleApp.dao.GroupDAO;
import org.consoleApp.domin.Group;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GroupsDAOImpl implements GroupDAO {
    private DataSource dataSource;

    public GroupsDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Group> findAll() {
        List<Group> groups = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM groups ORDER BY group_id")) {

            try (ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()){
                    groups.add(mapRow(resultSet));
                }
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Can't find groups", e);
        }

        return groups;
    }

    @Override
    public Optional<Group> findById(int id) {

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM groups WHERE group_id = ?")) {
            preparedStatement.setInt(1,id);

        try (ResultSet resultSet = preparedStatement.executeQuery()){
            if (resultSet.next()){
                return Optional.of(mapRow(resultSet));
            }
        }

        } catch (SQLException e) {
            throw new IllegalStateException("Can't find group", e);
        }

        return Optional.empty();
    }

    @Override
    public boolean insert(Group group) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO groups (group_name) VALUES (?)", Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setString(1, group.getName());

            int rowsAffected = preparedStatement.executeUpdate();

        try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()){
            if (!generatedKeys.next()){
                throw new IllegalStateException("Not enough generated keys returned during group insert");
            }

            group.setId(generatedKeys.getInt(1));

            if (generatedKeys.next()){
                throw new IllegalStateException("Too many generated keys returned during group insert");
            }
        }

        return rowsAffected > 0;

        } catch (SQLException e) {
            throw new IllegalStateException("Can't insert group", e);
        }
    }

    @Override
    public void insertBatch(List<Group> groups) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO groups (group_name) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {

            for (Group group : groups) {
                preparedStatement.setString(1, group.getName());

                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()){
                for (Group group : groups){
                    if (!generatedKeys.next()){
                        throw new IllegalStateException("Not enough generated keys returned during group batch insert");
                    }
                    group.setId(generatedKeys.getInt(1));
                }

                if (generatedKeys.next()){
                    throw new IllegalStateException("Too many generated keys returned during group batch insert");
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Can't insert batch of groups", e);
        }
    }

    @Override
    public boolean update(Group group) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE groups SET group_name = ? WHERE group_id = ?")) {
            preparedStatement.setString(1, group.getName());
            preparedStatement.setInt(2, group.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("Can't update group", e);
        }
    }

    @Override
    public boolean delete(Group group) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM groups WHERE group_id = ?")) {
            preparedStatement.setInt(1, group.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("Can't delete group", e);
        }
    }

    @Override
    public List<Group> findGroupsWithLessOrEqualStudents(int maxStudents) {
        List<Group> groups = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM groups WHERE (SELECT COUNT(*) FROM students WHERE students.group_id = groups.group_id) <= ?")){
            preparedStatement.setInt(1, maxStudents);

        try (ResultSet resultSet = preparedStatement.executeQuery()){
            while (resultSet.next()) {
                groups.add(mapRow(resultSet));
            }
        }

        } catch (SQLException e) {
            throw new IllegalStateException("Can't find groups with less or equal students", e);
        }

        return groups;
    }

    private Group mapRow(ResultSet resultSet) throws SQLException {
        int groupId = resultSet.getInt("group_id");
        String groupName = resultSet.getString("group_name");

        return new Group(groupId, groupName);
    }
}
