package org.consoleApp.dao.jdbc;

import org.consoleApp.domin.Student;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CleanupAndFillData {
    private DataSource dataSource;

    public CleanupAndFillData(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void deleteAll(String tableName){
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("TRUNCATE TABLE " + tableName + " RESTART IDENTITY CASCADE ")){

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new IllegalStateException("Can't delete all",e);
        }
    }

    public void addStudentsBatch(List<Student> students) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO students (group_id, first_name, last_name) VALUES (?, ?, ?)")) {

            for (Student student : students) {
                Optional<Integer> groupIdOptional = student.getGroupId();
                Integer groupId = groupIdOptional.orElse(null);

                preparedStatement.setObject(1, groupId, java.sql.Types.INTEGER);
                preparedStatement.setString(2, student.getFirstName());
                preparedStatement.setString(3, student.getLastName());

                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
