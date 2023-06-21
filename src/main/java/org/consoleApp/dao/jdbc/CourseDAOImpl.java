package org.consoleApp.dao.jdbc;

import org.consoleApp.dao.CourseDAO;
import org.consoleApp.domin.Course;
import org.consoleApp.domin.Student;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

public class CourseDAOImpl implements CourseDAO {
    private DataSource dataSource;

    public CourseDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Course> findAll() {
        List<Course> courses = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM courses");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()){
                int courseId = resultSet.getInt("course_id");
                String courseName = resultSet.getString("course_name");
                String courseDescription = resultSet.getString("course_description");

                Course currentCourse = new Course(courseId,courseName, courseDescription);
                courses.add(currentCourse);
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Can't fetch courses", e);
        }

        return courses;
    }

    @Override
    public Optional<Course> findById(int courseId) {

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM courses WHERE course_id = ?")) {
            preparedStatement.setInt(1, courseId);


        try (ResultSet resultSet = preparedStatement.executeQuery()){
            if (resultSet.next()){
                String courseName = resultSet.getString("course_name");
                String courseDescription = resultSet.getString("course_description");

               return Optional.of(new Course(courseId, courseName, courseDescription));
            }
        }

        } catch (SQLException e) {
            throw new IllegalStateException("Can't fetch courses", e);
        }

        return Optional.empty();
    }

    @Override
    public boolean insert(Course course) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO courses (course_name, course_description) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1,course.getName());
            preparedStatement.setString(2,course.getDescription());

            int rowsAffected = preparedStatement.executeUpdate();

        try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()){

            if (!generatedKeys.next()){
                throw new IllegalStateException("Not enough generated keys returned during courses insert");
            }

            course.setId(generatedKeys.getInt(1));

            if (generatedKeys.next()){
                throw new IllegalStateException("Too many generated keys returned during courses insert");
            }
        }

          return rowsAffected > 0;

        } catch (SQLException e) {
            throw new IllegalStateException("Can't insert course", e);
        }
    }

    @Override
    public void insertBatch(List<Course> courses) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO courses (course_name, course_description) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {


            for (Course course : courses){
                preparedStatement.setString(1, course.getName());
                preparedStatement.setString(2, course.getDescription());

                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()){
                for (Course course : courses){

                    if (!generatedKeys.next()){
                        throw new IllegalStateException("Not enough generated keys returned during courses batch insert");
                    }

                    course.setId(generatedKeys.getInt(1));
                }

                if (generatedKeys.next()){
                    throw new IllegalStateException("Too many generated keys returned during courses batch insert");
                }
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Can't insert batch of courses", e);
        }
    }

    @Override
    public boolean update(Course course) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE courses SET course_name = ?, course_description = ? WHERE course_id = ?")) {

            preparedStatement.setString(1,course.getName());
            preparedStatement.setString(2,course.getDescription());
            preparedStatement.setInt(3,course.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("Can't update courses", e);
        }
    }

    @Override
    public boolean delete(Course course) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM courses WHERE course_id = ?")) {
            preparedStatement.setInt(1,course.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("Can't delete courses", e);
        }
    }

    @Override
    public Optional<Course> findByCourseName(String courseName) {

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM courses WHERE course_name = ?")) {
            preparedStatement.setString(1, courseName);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int courseId = resultSet.getInt("course_id");
                    String courseDescription = resultSet.getString("course_description");

                   return Optional.of(new Course(courseId, courseName, courseDescription));
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Can't find by course name", e);
        }

        return Optional.empty();
    }

    @Override
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

    @Override
    public List<Student> findStudentsByCourseName(String courseName){
        List<Student> students = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT s.student_id, s.group_id, s.first_name, s.last_name FROM students s " +
                     "JOIN enrollments e ON s.student_id = e.student_id JOIN courses c ON e.course_id = c.course_id WHERE c.course_name = ?")) {

            preparedStatement.setString(1, courseName);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("student_id");
                    int groupId = resultSet.getInt("group_id");
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");

                    students.add(new Student(id, groupId, firstName, lastName));
                }
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Can't find batch by student id", e);
        }

        return students;
    }
}
