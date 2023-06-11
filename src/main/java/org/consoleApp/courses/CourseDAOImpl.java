package org.consoleApp.courses;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CourseDAOImpl implements CourseDAO{
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public CourseDAOImpl(DataSource dataSource) {
        try {
            this.connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to establish a database connection", e);
        }
    }

    @Override
    public List<Course> findAll() {
        List<Course> courses = new ArrayList<>();

        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM courses");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                int courseId = resultSet.getInt("course_id");
                String courseName = resultSet.getString("course_name");
                String courseDescription = resultSet.getString("course_description");

                Course currentCourse = new Course(courseId,courseName, courseDescription);
                courses.add(currentCourse);
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Can't fetch courses", e);
        } finally {
            closeResources();
        }

        return courses;
    }

    @Override
    public Optional<Course> findById(int courseId) {
        Optional<Course> course = Optional.empty();

        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM courses WHERE course_id = ?");
            preparedStatement.setInt(1,courseId);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                String courseName = resultSet.getString("course_name");
                String courseDescription = resultSet.getString("course_description");

                course = Optional.of(new Course(courseId, courseName, courseDescription));
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Can't fetch courses", e);
        }finally {
            closeResources();
        }

        return course;
    }

    @Override
    public void insert(Course course) {
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO courses (course_name, course_description) VALUES (?, ?)");
            preparedStatement.setString(1,course.courseName());
            preparedStatement.setString(2,course.courseDescription());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Can't insert course", e);
        }finally {
            closeResources();
        }
    }

    @Override
    public void update(Course course) {
        try {
            preparedStatement = connection.prepareStatement("UPDATE courses SET course_name = ?, course_description = ? WHERE course_id = ?");
            preparedStatement.setString(1,course.courseName());
            preparedStatement.setString(2,course.courseDescription());
            preparedStatement.setInt(3,course.courseId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Can't update courses", e);
        }finally {
            closeResources();
        }
    }

    @Override
    public void delete(Course course) {
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM courses WHERE course_id = ?");
            preparedStatement.setInt(1,course.courseId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Can't delete courses", e);
        }finally {
            closeResources();
        }
    }

    @Override
    public Optional<Course> findByCourseName(String courseName) {
        Optional<Course> course = Optional.empty();

        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM courses WHERE course_name = ?");
            preparedStatement.setString(1, courseName);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                int courseId = resultSet.getInt("course_id");
                String courseDescription = resultSet.getString("course_description");

                course = Optional.of(new Course(courseId,courseName, courseDescription));
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Can't find by course name", e);
        }finally {
            closeResources();
        }

        return course;
    }

    public int getNextId() {
        int nextCourseId = 0;

        try {
            preparedStatement = connection.prepareStatement("SELECT MAX(course_id) FROM courses");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                nextCourseId = resultSet.getInt(1) + 1;
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new IllegalStateException("Can't get next course id", e);
        }finally {
            closeResources();
        }

        return nextCourseId;
    }

    private void closeResources(){
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                throw new RuntimeException("Something wrong with ResultSet in CourseDAOImpl", e);
            }
        }

        if (preparedStatement != null){
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                throw new RuntimeException("Something wrong with PreparedStatement in CourseDAOImpl", e);
            }
        }
    }
}
