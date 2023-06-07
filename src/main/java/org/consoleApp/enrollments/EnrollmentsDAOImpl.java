package org.consoleApp.enrollments;

import org.consoleApp.dataBaseSettings.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EnrollmentsDAOImpl implements EnrollmentsDAO{
    private Connection connection;
    public EnrollmentsDAOImpl(DBConnector dbConnector) {
        connection = dbConnector.getConnection();
    }

    @Override
    public void enrollStudentInCourse(int studentId, int courseId) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO enrollments (student_id,course_id) VALUES (?,?)");
            statement.setInt(1,studentId);
            statement.setInt(2,courseId);
            statement.executeUpdate();

            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteStudentById(int studentId) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM enrollments WHERE student_id = ?");
            statement.setInt(1, studentId);
            statement.executeUpdate();

            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
