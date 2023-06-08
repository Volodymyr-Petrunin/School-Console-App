package org.consoleApp.enrollments;

import org.consoleApp.dataBaseSettings.DBConnector;
import org.consoleApp.students.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<Integer> findAllStudentsIdByCourseId(int courseId) {
        List<Integer> studentsId = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT student_id FROM enrollments WHERE course_id = ?");
            statement.setInt(1, courseId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                int currentId = resultSet.getInt("student_id");
                studentsId.add(currentId);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return studentsId;
    }
}
