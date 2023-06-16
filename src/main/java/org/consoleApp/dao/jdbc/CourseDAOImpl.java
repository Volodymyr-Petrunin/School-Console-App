package org.consoleApp.dao.jdbc;

import org.consoleApp.dao.CourseDAO;
import org.consoleApp.domin.Course;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        Optional<Course> course = Optional.empty();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM courses WHERE course_id = ?")) {
            preparedStatement.setInt(1, courseId);


        try (ResultSet resultSet = preparedStatement.executeQuery()){
            if (resultSet.next()){
                String courseName = resultSet.getString("course_name");
                String courseDescription = resultSet.getString("course_description");

                course = Optional.of(new Course(courseId, courseName, courseDescription));
            }
        }

        } catch (SQLException e) {
            throw new IllegalStateException("Can't fetch courses", e);
        }

        return course;
    }

    @Override
    public boolean insert(Course course) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO courses (course_name, course_description) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1,course.getCourseName());
            preparedStatement.setString(2,course.getCourseDescription());

            int rowsAffected = preparedStatement.executeUpdate();

        try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()){

            while (generatedKeys.next()){
                int courseId = generatedKeys.getInt(1);
                course.setCourseId(courseId);
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

            connection.setAutoCommit(false);

            for (Course course : courses){
                preparedStatement.setString(1, course.getCourseName());
                preparedStatement.setString(2, course.getCourseDescription());

                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()){
                int index = 0;

                while (generatedKeys.next()) {
                    int courseId = generatedKeys.getInt(1);
                    courses.get(index).setCourseId(courseId);
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
    public boolean update(Course course) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE courses SET course_name = ?, course_description = ? WHERE course_id = ?")) {

            preparedStatement.setString(1,course.getCourseName());
            preparedStatement.setString(2,course.getCourseDescription());
            preparedStatement.setInt(3,course.getCourseId());

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
            preparedStatement.setInt(1,course.getCourseId());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("Can't delete courses", e);
        }
    }

    @Override
    public Optional<Course> findByCourseName(String courseName) {
        Optional<Course> course = Optional.empty();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM courses WHERE course_name = ?")) {
            preparedStatement.setString(1, courseName);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int courseId = resultSet.getInt("course_id");
                    String courseDescription = resultSet.getString("course_description");

                    course = Optional.of(new Course(courseId, courseName, courseDescription));
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Can't find by course name", e);
        }

        return course;
    }
}
