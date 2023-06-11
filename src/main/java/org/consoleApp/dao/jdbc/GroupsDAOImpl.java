package org.consoleApp.dao.jdbc;

import org.consoleApp.dao.GroupDAO;
import org.consoleApp.domin.Group;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GroupsDAOImpl implements GroupDAO {

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public GroupsDAOImpl(DataSource dataSource) {
        try {
            this.connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to establish a database connection", e);
        } finally {
            closeResources();
        }
    }

    @Override
    public List<Group> findAll() {
        List<Group> groups = new ArrayList<>();

        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM groups");
            resultSet = preparedStatement.executeQuery();

            getCurrentList(groups, resultSet);

        } catch (SQLException e) {
            throw new IllegalStateException("Can't find groups", e);
        } finally {
            closeResources();
        }

        return groups;
    }

    @Override
    public Optional<Group> findById(int id) {
        Optional<Group> group = Optional.empty();

        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM groups WHERE group_id = ?");
            preparedStatement.setInt(1,id);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                String groupName = resultSet.getString("group_name");
                group = Optional.of(new Group(id, groupName));
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Can't find group", e);
        } finally {
            closeResources();
        }

        return group;
    }

    @Override
    public void insert(Group group) {
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO groups (group_name) VALUES (?)");
            preparedStatement.setString(1, group.groupName());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new IllegalStateException("Can't insert group", e);
        } finally {
            closeResources();
        }
    }

    @Override
    public void updateGroup(Group group) {
        try {
            preparedStatement = connection.prepareStatement("UPDATE groups SET group_name = ? WHERE group_id = ?");
            preparedStatement.setString(1, group.groupName());
            preparedStatement.setInt(2, group.groupId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Can't update group", e);
        } finally {
            closeResources();
        }
    }

    @Override
    public void deleteGroup(Group group) {
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM groups WHERE group_id = ?");
            preparedStatement.setInt(1, group.groupId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Can't delete group", e);
        }  finally {
            closeResources();
        }
    }

    @Override
    public List<Group> findGroupsWithLessOrEqualStudents(int maxStudents) {
        List<Group> groups = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM groups WHERE (SELECT COUNT(*) FROM students WHERE students.group_id = groups.group_id) <= ?");
            preparedStatement.setInt(1, maxStudents);
            resultSet = preparedStatement.executeQuery();

            getCurrentList(groups, resultSet);
        } catch (SQLException e) {
            throw new IllegalStateException("Can't find groups with less or equal students", e);
        } finally {
            closeResources();
        }

        return groups;
    }

    @Override
    public Optional<Group> findGroupIdByName(String groupName) {
        Optional<Group> group = Optional.empty();
        try {
            preparedStatement = connection.prepareStatement("SELECT group_id FROM groups WHERE group_name = ?");
            preparedStatement.setString(1, groupName);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                int groupId = resultSet.getInt("group_id");
                group = Optional.of(new Group(groupId, groupName));
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Can't find groups id by name", e);
        } finally {
            closeResources();
        }

        return group;
    }

    public int getNextId() {
        int nextGroupId = 0;

        try {
            preparedStatement = connection.prepareStatement("SELECT MAX(group_id) FROM groups");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                nextGroupId = resultSet.getInt(1) + 1;
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Can't find next id", e);
        } finally {
            closeResources();
        }

        return nextGroupId;
    }

    private void getCurrentList(List<Group> groups, ResultSet resultSet) throws SQLException {
        while (resultSet.next()){
            int groupId = resultSet.getInt("group_id");
            String groupName = resultSet.getString("group_name");

            Group group = new Group(groupId, groupName);
            groups.add(group);
        }
    }

    private void closeResources(){
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                throw new RuntimeException("Something wrong with ResultSet in GroupsDAOImpl", e);
            }
        }

        if (preparedStatement != null){
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                throw new RuntimeException("Something wrong with PreparedStatement in GroupsDAOImpl", e);
            }
        }
    }
}
