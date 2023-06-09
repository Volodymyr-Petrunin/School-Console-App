package org.consoleApp.enrollments;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentsDAOImpl implements EnrollmentsDAO{
    private DataSource dataSource;
    public EnrollmentsDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public boolean enrollStudentInCourse(int studentId, int courseId) {
        try (Connection connection = dataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement("INSERT INTO enrollments (student_id,course_id) VALUES (?,?)");
            statement.setInt(1,studentId);
            statement.setInt(2,courseId);
            int rowsAffected = statement.executeUpdate();

            statement.close();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteStudentById(int studentId) {
        try (Connection connection = dataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement("DELETE FROM enrollments WHERE student_id = ?");
            statement.setInt(1, studentId);
            int rowsAffected = statement.executeUpdate();

            statement.close();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Integer> findAllStudentsIdByCourseId(int courseId) {
        List<Integer> studentsId = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()){
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
    @Override
    public List<Integer> findAllCourseIdByStudentsId(int studentId) {
        List<Integer> coursesId = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement("SELECT course_id FROM enrollments WHERE student_id = ?");
            statement.setInt(1, studentId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                int currentId = resultSet.getInt("course_id");
                coursesId.add(currentId);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return coursesId;
    }

    @Override
    public boolean removeStudentFromCourse(int studentId, int courseId) {
        try (Connection connection = dataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement("DELETE FROM enrollments WHERE student_id = ? AND course_id = ?");
            statement.setInt(1, studentId);
            statement.setInt(2, courseId);
            int rowsAffected = statement.executeUpdate();

            statement.close();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
