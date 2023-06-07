package org.consoleApp.students;

import org.consoleApp.dataBaseSettings.DBConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentsDAOImpl implements StudentsDAO{
    private Connection connection;

    public StudentsDAOImpl(DBConnector dbConnector) {
        connection = dbConnector.getConnection();
    }

    @Override
    public List<Student> findAll() {
        List<Student> students = new ArrayList<>();

        try {
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

        try {
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
    public void insert(Student student) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO students (group_id, first_name, last_name) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1,student.group_id());
            statement.setString(2,student.first_name());
            statement.setString(3,student.last_name());
            statement.executeUpdate();


            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Student student) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE students SET group_id = ?, first_name = ?, last_name = ? WHERE student_id = ?");
            statement.setInt(1, student.group_id());
            statement.setString(2, student.first_name());
            statement.setString(3, student.last_name());
            statement.setInt(4, student.student_id());
            statement.executeUpdate();

            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int studentId) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM students WHERE student_id = ?");
            statement.setInt(1, studentId);
            statement.executeUpdate();

            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getGroupSize(int groupId) {
        try {
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
}
