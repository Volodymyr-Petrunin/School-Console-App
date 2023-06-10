package org.consoleApp.enrollments;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentsDAOImpl implements EnrollmentsDAO{
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    public EnrollmentsDAOImpl(DataSource dataSource) {
        try {
            this.connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean enrollStudentInCourse(int studentId, int courseId) {
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO enrollments (student_id,course_id) VALUES (?,?)");
            preparedStatement.setInt(1,studentId);
            preparedStatement.setInt(2,courseId);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("Can't register", e);
        }finally {
            closeResources();
        }
    }

    @Override
    public boolean deleteStudentById(int studentId) {
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM enrollments WHERE student_id = ?");
            preparedStatement.setInt(1, studentId);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("Can't delete student by id", e);
        } finally {
            closeResources();
        }
    }

    @Override
    public List<Integer> findAllStudentsIdByCourseId(int courseId) {
        List<Integer> studentsId = new ArrayList<>();

        try {
            preparedStatement = connection.prepareStatement("SELECT student_id FROM enrollments WHERE course_id = ?");
            preparedStatement.setInt(1, courseId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                int currentId = resultSet.getInt("student_id");
                studentsId.add(currentId);
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Can't find students id by course id", e);
        } finally {
            closeResources();
        }

        return studentsId;
    }
    @Override
    public List<Integer> findAllCourseIdByStudentsId(int studentId) {
        List<Integer> coursesId = new ArrayList<>();

        try {
            preparedStatement = connection.prepareStatement("SELECT course_id FROM enrollments WHERE student_id = ?");
            preparedStatement.setInt(1, studentId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                int currentId = resultSet.getInt("course_id");
                coursesId.add(currentId);
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Can't find course id by student id", e);
        } finally {
            closeResources();
        }

        return coursesId;
    }

    @Override
    public boolean removeStudentFromCourse(int studentId, int courseId) {
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM enrollments WHERE student_id = ? AND course_id = ?");
            preparedStatement.setInt(1, studentId);
            preparedStatement.setInt(2, courseId);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("Can't remove student from course", e);
        } finally {
            closeResources();
        }
    }
    private void closeResources(){
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                throw new RuntimeException("Something wrong with ResultSet in EnrollmentsDAOImpl", e);
            }
        }

        if (preparedStatement != null){
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                throw new RuntimeException("Something wrong with PreparedStatement in EnrollmentsDAOImpl", e);
            }
        }
    }
}
