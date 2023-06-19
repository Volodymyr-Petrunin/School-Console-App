package org.consoleApp.dao.jdbc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentsDAO {
    private DataSource dataSource;

    public EnrollmentsDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean deleteStudentById(int studentId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM enrollments WHERE student_id = ?")){
            preparedStatement.setInt(1, studentId);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("Can't delete student by id", e);
        }
    }

    public List<Integer> findAllStudentsIdByCourseId(int courseId) {
        List<Integer> studentsId = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT student_id FROM enrollments WHERE course_id = ?")){
            preparedStatement.setInt(1, courseId);

        try (ResultSet resultSet = preparedStatement.executeQuery()){
            while (resultSet.next()){
                int currentId = resultSet.getInt("student_id");
                studentsId.add(currentId);
            }
        }

        } catch (SQLException e) {
            throw new IllegalStateException("Can't find students id by course id", e);
        }

        return studentsId;
    }

    public List<Integer> findAllCourseIdByStudentsId(int studentId) {
        List<Integer> coursesId = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT course_id FROM enrollments WHERE student_id = ?")){
            preparedStatement.setInt(1, studentId);

        try (ResultSet resultSet = preparedStatement.executeQuery()){
            while (resultSet.next()){
                int currentId = resultSet.getInt("course_id");
                coursesId.add(currentId);
            }
        }

        } catch (SQLException e) {
            throw new IllegalStateException("Can't find course id by student id", e);
        }

        return coursesId;
    }

    public boolean removeStudentFromCourse(int studentId, int courseId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM enrollments WHERE student_id = ? AND course_id = ?")){
            preparedStatement.setInt(1, studentId);
            preparedStatement.setInt(2, courseId);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("Can't remove student from course", e);
        }
    }
}
