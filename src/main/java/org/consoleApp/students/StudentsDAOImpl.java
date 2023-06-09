package org.consoleApp.students;


import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentsDAOImpl implements StudentsDAO{
    private DataSource dataSource;

    public StudentsDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Student> findAll() {
        List<Student> students = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()){
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM students");

            while (resultSet.next()){
                int studentId = resultSet.getInt("student_id");
                int groupId = resultSet.getInt("group_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");

                Student student = new Student(studentId, groupId, firstName, lastName);
                students.add(student);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return students;
    }

    @Override
    public Student findById(int studentId) {
        Student student = null;

        try (Connection connection = dataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM students WHERE student_id = ?");
            statement.setInt(1,studentId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()){
                int groupId = resultSet.getInt("group_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");

                student = new Student(studentId,groupId,firstName,lastName);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return student;
    }

    @Override
    public boolean insert(Student student) {
        try (Connection connection = dataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement("INSERT INTO students (group_id, first_name, last_name) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1,student.group_id());
            statement.setString(2,student.first_name());
            statement.setString(3,student.last_name());
            int rowsAffected = statement.executeUpdate();


            statement.close();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateStudent(Student student) {
        try (Connection connection = dataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement("UPDATE students SET group_id = ?, first_name = ?, last_name = ? WHERE student_id = ?");
            statement.setInt(1, student.group_id());
            statement.setString(2, student.first_name());
            statement.setString(3, student.last_name());
            statement.executeUpdate();

            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteStudentById(int studentId) {
        try (Connection connection = dataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement("DELETE FROM students WHERE student_id = ?");
            statement.setInt(1, studentId);
            int rowsAffected = statement.executeUpdate();

            statement.close();

            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getGroupSize(int groupId) {
        try (Connection connection = dataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM students WHERE group_id = ?");
            statement.setInt(1,groupId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()){
                return resultSet.getInt("count");
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    @Override
    public int getNextStudentId() {
        int nextStudentId = 0;

        try (Connection connection = dataSource.getConnection()){
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT MAX(student_id) FROM students");

            while (resultSet.next()){
                nextStudentId = resultSet.getInt(1) + 1;
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return nextStudentId;
    }

    @Override
    public List<Student> findByFirstName(String firstName) {
        List<Student> students = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM students WHERE first_name = ?");
            statement.setString(1, firstName);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                int studentId = resultSet.getInt("student_id");
                int groupId = resultSet.getInt("group_id");
                String lastName = resultSet.getString("last_name");

                Student student = new Student(studentId,groupId,firstName,lastName);
                students.add(student);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return students;
    }
}
