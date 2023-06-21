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

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM students WHERE student_id = ?")){
            preparedStatement.setInt(1,studentId);

        try (ResultSet resultSet = preparedStatement.executeQuery()){
            if (resultSet.next()){
                int groupId = resultSet.getInt("group_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");

                return Optional.of(new Student(studentId,groupId,firstName,lastName));
            }
        }

        } catch (SQLException e) {
            throw new IllegalStateException("Can't fetch students by id", e);
        }

        return Optional.empty();
    }

    @Override
    public boolean insert(Student student) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO students (group_id, first_name, last_name) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)){

            Optional<Integer> groupIdOptional = student.getGroupId();
            Integer groupId = groupIdOptional.orElse(null);

            preparedStatement.setObject(1, groupId, java.sql.Types.INTEGER);
            preparedStatement.setString(2,student.getFirstName());
            preparedStatement.setString(3,student.getLastName());

            int rowsAffected = preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()){
                if (!generatedKeys.next()){
                    throw new IllegalStateException("Not enough generated keys returned during students insert");
                }

                student.setId(generatedKeys.getInt(1));

                if (generatedKeys.next()){
                    throw new IllegalStateException("Too many generated keys returned during students insert");
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

            for (Student student : students){
                Optional<Integer> groupIdOptional = student.getGroupId();
                Integer groupId = groupIdOptional.orElse(null);

                preparedStatement.setObject(1, groupId, java.sql.Types.INTEGER);
                preparedStatement.setString(2, student.getFirstName());
                preparedStatement.setString(3, student.getLastName());

                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()){
                for (Student student : students){
                    if (!generatedKeys.next()){
                        throw new IllegalStateException("Not enough generated keys returned during students batch insert");
                    }
                    student.setId(generatedKeys.getInt(1));
                }

                if (generatedKeys.next()){
                    throw new IllegalStateException("Too many generated keys returned during students batch insert");
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Can't insert batch of courses", e);
        }
    }

    @Override
    public boolean update(Student student) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE students SET group_id = ?, first_name = ?, last_name = ? WHERE student_id = ?")){

            Optional<Integer> groupIdOptional = student.getGroupId();
            Integer groupId = groupIdOptional.orElse(null);

            preparedStatement.setObject(1, groupId, java.sql.Types.INTEGER);
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
            preparedStatement.setInt(1, student.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("Can't delete students", e);
        }
    }

    @Override
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

    @Override
    public boolean deleteStudentByIdFromEnrollments(int studentId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM enrollments WHERE student_id = ?")){
            preparedStatement.setInt(1, studentId);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("Can't delete student by id", e);
        }
    }

    @Override
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

    @Override
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

    @Override
    public List<Student> findByIdBatch(List<Integer> studentsId){
        List<Student> students = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM students WHERE student_id = ?")) {

            for (Integer id : studentsId) {
                preparedStatement.setInt(1, id);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int groupId = resultSet.getInt("group_id");
                        String firstName = resultSet.getString("first_name");
                        String lastName = resultSet.getString("last_name");

                        students.add(new Student(id, groupId, firstName, lastName));
                    }
                }
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Can't find batch by student id", e);
        }

        return students;
    }
}
