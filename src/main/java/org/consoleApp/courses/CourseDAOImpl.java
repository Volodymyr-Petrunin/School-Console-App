package org.consoleApp.courses;

import org.consoleApp.dataBaseSettings.DBConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAOImpl implements CourseDAO{
    private Connection connection;

    public CourseDAOImpl(DBConnector dbConnector) {
        connection = dbConnector.getConnection();
    }

    @Override
    public List<Course> findAll() {
        List<Course> courses = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM courses");

            while (resultSet.next()){
                int courseId = resultSet.getInt("course_id");
                String courseName = resultSet.getString("course_name");
                String courseDescription = resultSet.getString("course_description");

                Course currentCourse = new Course(courseId,courseName, courseDescription);
                courses.add(currentCourse);
            }

            resultSet.close();
            statement.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return courses;
    }

    @Override
    public Course findById(int courseId) {
        Course course = null;

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM courses WHERE course_id = ?");
            statement.setInt(1,courseId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()){
                String courseName = resultSet.getString("course_name");
                String courseDescription = resultSet.getString("course_description");

                course = new Course(courseId, courseName, courseDescription);
            }

            resultSet.close();
            statement.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return course;
    }

    @Override
    public void insertNewCourse(Course course) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO courses (course_name, course_description) VALUES (?, ?)" , Statement.RETURN_GENERATED_KEYS);
            statement.setString(1,course.courseName());
            statement.setString(2,course.courseDescription());
            statement.executeUpdate();

            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateCourse(Course course) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE courses SET course_name = ?, course_description = ? WHERE course_id = ?");
            statement.setString(1,course.courseName());
            statement.setString(2,course.courseDescription());
            statement.setInt(3,course.courseId());
            statement.executeUpdate();

            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteCourse(Course course) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM courses WHERE course_id = ?");
            statement.setInt(1,course.courseId());
            statement.executeUpdate();

            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Course findByCourseName(String courseName) {
        Course course = null;
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM courses WHERE course_name = ?");
            statement.setString(1, courseName);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                int courseId = resultSet.getInt("course_id");
                String courseDescription = resultSet.getString("course_description");

                course = new Course(courseId,courseName, courseDescription);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return course;
    }

    @Override
    public int getNextCourseId() {
        int nextCourseId = 0;

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT MAX(course_id) FROM courses");

            while (resultSet.next()){
                nextCourseId = resultSet.getInt(1) + 1;
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return nextCourseId;
    }
}
