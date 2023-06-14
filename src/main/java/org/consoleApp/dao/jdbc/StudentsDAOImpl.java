package org.consoleApp.dao.jdbc;


import org.consoleApp.dao.StudentsDAO;
import org.consoleApp.domin.Student;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentsDAOImpl implements StudentsDAO {
    private DataSource dataSource;

    public StudentsDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Student> findAll() {
        List<Student> students = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM students");
             ResultSet resultSet = preparedStatement.executeQuery()){

            while (resultSet.next()){
                int studentId = resultSet.getInt("student_id");
                int groupId = resultSet.getInt("group_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");

                Student student = new Student(studentId, groupId, firstName, lastName);
                students.add(student);
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Can't fetch students", e);
        }

        return students;
    }

    @Override
    public Optional<Student> findById(int studentId) {
        Optional<Student> student = Optional.empty();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM students WHERE student_id = ?")){
            preparedStatement.setInt(1,studentId);

        try (ResultSet resultSet = preparedStatement.executeQuery()){
            if (resultSet.next()){
                int groupId = resultSet.getInt("group_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");

                student = Optional.of(new Student(studentId,groupId,firstName,lastName));
            }
        }

        } catch (SQLException e) {
            throw new IllegalStateException("Can't fetch students by id", e);
        }

        return student;
    }

    @Override
    public boolean insert(Student student) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO students (group_id, first_name, last_name) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setInt(1,student.getGroupId());
            preparedStatement.setString(2,student.getFirstName());
            preparedStatement.setString(3,student.getLastName());

            int rowsAffected = preparedStatement.executeUpdate();

        try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()){
            while (generatedKeys.next()){
                int studentId = generatedKeys.getInt(1);
                student.setStudentId(studentId);
            }
        }

            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("Can't insert students", e);
        }
    }

    @Override
    public void insertBatch(List<Student> students) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO students (group_id, first_name, last_name) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)){

            connection.setAutoCommit(false);

            for (Student student : students){
                preparedStatement.setInt(1, student.getGroupId());
                preparedStatement.setString(2, student.getFirstName());
                preparedStatement.setString(3, student.getLastName());

                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();

        try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys();){
            int index = 0;

            while (generatedKeys.next()){
                int courseId = generatedKeys.getInt(1);
                students.get(index).setStudentId(courseId);
                index++;
            }
        }
            connection.commit();
        } catch (SQLException e) {
            try (Connection connection = dataSource.getConnection()){
                connection.rollback();
            } catch (SQLException rollbackException) {
                throw new IllegalStateException("Can't insert batch of courses", e);
            }
            throw new IllegalStateException("Can't insert batch of courses", e);
        } finally {
            try (Connection connection = dataSource.getConnection()){
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException("Can't set auto commit", e);
            }
        }
    }

    @Override
    public boolean update(Student student) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE students SET group_id = ?, first_name = ?, last_name = ? WHERE student_id = ?")){
            preparedStatement.setInt(1, student.getGroupId());
            preparedStatement.setString(2, student.getFirstName());
            preparedStatement.setString(3, student.getLastName());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(Student student) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM students WHERE student_id = ?")){
            preparedStatement.setInt(1, student.getStudentId());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("Can't delete students", e);
        }
    }

    public boolean deleteByStudentId(int studentId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM students WHERE student_id = ?")){
            preparedStatement.setInt(1, studentId);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("Can't delete students", e);
        }
    }

    @Override
    public int getGroupSize(int groupId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM students WHERE group_id = ?")){
            preparedStatement.setInt(1,groupId);

        try (ResultSet resultSet = preparedStatement.executeQuery()){
            if (resultSet.next()){
                return resultSet.getInt("count");
            }
        }

        } catch (SQLException e) {
            throw new IllegalStateException("Can't get group size", e);
        }

        return 0;
    }

    @Override
    public List<Student> findByFirstName(String firstName) {
        List<Student> students = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM students WHERE first_name = ?")){
            preparedStatement.setString(1, firstName);

        try (ResultSet resultSet = preparedStatement.executeQuery()){
            while (resultSet.next()){
                int studentId = resultSet.getInt("student_id");
                int groupId = resultSet.getInt("group_id");
                String lastName = resultSet.getString("last_name");

                Student student = new Student(studentId,groupId,firstName,lastName);
                students.add(student);
            }
        }

        } catch (SQLException e) {
            throw new IllegalStateException("Can't find by student firs name", e);
        }

        return students;
    }

    @Override
    public boolean enrollStudentInCourse(int studentId, int courseId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO enrollments (student_id,course_id) VALUES (?,?)")){
            preparedStatement.setInt(1,studentId);
            preparedStatement.setInt(2,courseId);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("Can't register", e);
        }
    }
}
