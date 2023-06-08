package org.consoleApp.groups;

import org.consoleApp.dataBaseSettings.DBConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GroupsDAOImpl implements GroupDAO{

    private Connection connection;

    public GroupsDAOImpl(DBConnector dbConnector) {
        connection = dbConnector.getConnection();
    }

    @Override
    public List<Group> findAll() {
        List<Group> groups = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM groups");

            getCurrentList(groups, statement, resultSet);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return groups;
    }

    @Override
    public Group findGroupById(int id) {
        Group group = null;

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM groups WHERE group_id = ?");
            statement.setInt(1,id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()){
                String groupName = resultSet.getString("group_name");
                group = new Group(id, groupName);
            }

            resultSet.close();
            statement.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return group;
    }

    @Override
    public void insertNewGroup(Group group) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO groups (group_name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, group.groupName());
            statement.executeUpdate();

            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateGroup(Group group) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE groups SET group_name = ? WHERE group_id = ?");
            statement.setString(1, group.groupName());
            statement.setInt(2, group.groupId());
            statement.executeUpdate();

            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteGroup(Group group) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM groups WHERE group_id = ?");
            statement.setInt(1, group.groupId());
            statement.executeUpdate();

            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Group> findGroupsWithLessOrEqualStudents(int maxStudents) {
        List<Group> groups = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM groups WHERE (SELECT COUNT(*) FROM students WHERE students.group_id = groups.group_id) <= ?");
            statement.setInt(1, maxStudents);
            ResultSet resultSet = statement.executeQuery();

            getCurrentList(groups, statement, resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return groups;
    }

    @Override
    public Group findGroupIdByName(String groupName) {
        Group group = null;
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT group_id FROM groups WHERE group_name = ?");
            statement.setString(1, groupName);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                int groupId = resultSet.getInt("group_id");
                group = new Group(groupId, groupName);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return group;
    }

    private void getCurrentList(List<Group> groups, Statement statement, ResultSet resultSet) throws SQLException {
        while (resultSet.next()){
            int groupId = resultSet.getInt("group_id");
            String groupName = resultSet.getString("group_name");

            Group group = new Group(groupId, groupName);
            groups.add(group);
        }

        resultSet.close();
        statement.close();
    }
}
