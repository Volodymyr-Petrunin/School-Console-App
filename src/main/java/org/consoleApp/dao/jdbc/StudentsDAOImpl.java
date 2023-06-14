package org.consoleApp.dao.jdbc;


import org.consoleApp.dao.StudentsDAO;
import org.consoleApp.domin.Student;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentsDAOImpl implements StudentsDAO {
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private DataSource dataSource;

    public StudentsDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Student> findAll() {
        List<Student> students = new ArrayList<>();

        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM students");
            resultSet = preparedStatement.executeQuery();

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
        } finally {
            closeResources();
        }

        return students;
    }

    @Override
    public Optional<Student> findById(int studentId) {
        Optional<Student> student = Optional.empty();

        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM students WHERE student_id = ?");
            preparedStatement.setInt(1,studentId);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                int groupId = resultSet.getInt("group_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");

                student = Optional.of(new Student(studentId,groupId,firstName,lastName));
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Can't fetch students by id", e);
        } finally {
            closeResources();
        }

        return student;
    }

    @Override
    public boolean insert(Student student) {
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO students (group_id, first_name, last_name) VALUES (?, ?, ?)");
            preparedStatement.setInt(1,student.getGroupId());
            preparedStatement.setString(2,student.getFirstName());
            preparedStatement.setString(3,student.getLastName());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("Can't insert students", e);
        } finally {
            closeResources();
        }
    }

    @Override
    public void insertBatch(List<Student> students) {
        try {
            connection.setAutoCommit(false);

            preparedStatement = connection.prepareStatement("INSERT INTO students (group_id, first_name, last_name) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            for (Student student : students){
                preparedStatement.setInt(1, student.getGroupId());
                preparedStatement.setString(2, student.getFirstName());
                preparedStatement.setString(3, student.getLastName());

                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            int index = 0;

            while (generatedKeys.next()){
                int courseId = generatedKeys.getInt(1);
                students.get(index).setStudentId(courseId);
                index++;
            }

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackException) {
                throw new IllegalStateException("Can't insert batch of courses", e);
            }
            throw new IllegalStateException("Can't insert batch of courses", e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException("Can't set auto commit", e);
            } finally {
                closeResources();
            }
        }
    }

    @Override
    public boolean update(Student student) {
        try {
            preparedStatement = connection.prepareStatement("UPDATE students SET group_id = ?, first_name = ?, last_name = ? WHERE student_id = ?");
            preparedStatement.setInt(1, student.getGroupId());
            preparedStatement.setString(2, student.getFirstName());
            preparedStatement.setString(3, student.getLastName());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeResources();
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
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM students WHERE student_id = ?");
            preparedStatement.setInt(1, studentId);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("Can't delete students", e);
        } finally {
            closeResources();
        }
    }

    @Override
    public int getGroupSize(int groupId) {
        try {
            preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM students WHERE group_id = ?");
            preparedStatement.setInt(1,groupId);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                return resultSet.getInt("count");
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Can't get group size", e);
        } finally {
            closeResources();
        }

        return 0;
    }


    public int getNextId() {
        int nextStudentId = 0;

        try {
            preparedStatement = connection.prepareStatement("SELECT MAX(student_id) FROM students");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                nextStudentId = resultSet.getInt(1) + 1;
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Can't get next student id", e);
        } finally {
            closeResources();
        }

        return nextStudentId;
    }

    @Override
    public List<Student> findByFirstName(String firstName) {
        List<Student> students = new ArrayList<>();

        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM students WHERE first_name = ?");
            preparedStatement.setString(1, firstName);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                int studentId = resultSet.getInt("student_id");
                int groupId = resultSet.getInt("group_id");
                String lastName = resultSet.getString("last_name");

                Student student = new Student(studentId,groupId,firstName,lastName);
                students.add(student);
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Can't find by student firs name", e);
        } finally {
            closeResources();
        }

        return students;
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

    private void closeResources(){
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                throw new RuntimeException("Something wrong with ResultSet in StudentsDAOImpl", e);
            }
        }

        if (preparedStatement != null){
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                throw new RuntimeException("Something wrong with PreparedStatement in StudentsDAOImpl", e);
            }
        }
    }
}
