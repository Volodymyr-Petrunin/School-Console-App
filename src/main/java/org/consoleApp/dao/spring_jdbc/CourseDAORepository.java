package org.consoleApp.dao.spring_jdbc;

import org.consoleApp.dao.CourseDAO;
import org.consoleApp.domin.Course;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class CourseDAORepository implements CourseDAO {
    private final JdbcTemplate jdbcTemplate;
    private String sql;
    private int rowAffected;

    public CourseDAORepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Course> findAll() {
        sql = "SELECT * FROM courses ORDER BY course_id";
        List<Course> courses = jdbcTemplate.query(sql, courseRowMapper);

        if (courses.isEmpty()){
            throw new IllegalStateException("Can't fetch courses");
        }

        return courses;
    }

    @Override
    public Optional<Course> findById(int id) {
        sql = "SELECT * FROM courses WHERE course_id = ?";
        Course course = jdbcTemplate.queryForObject(sql, new Object[] {id}, courseRowMapper);

        return Optional.ofNullable(course);
    }

    @Override
    public boolean insert(Course course) {
        sql = "INSERT INTO courses (course_name, course_description) VALUES (?, ?)";
        rowAffected = jdbcTemplate.update(sql, course.getName(), course.getDescription());

        return rowAffected > 0;
    }

    @Override
    public void insertBatch(List<Course> courses) {
        sql = "INSERT INTO courses (course_name, course_description) VALUES (?, ?)";

        List<Object[]> batchArgs = courses.stream()
                .map(course -> new Object[] { course.getName(), course.getDescription() })
                .toList();

        int[] rowsAffected = jdbcTemplate.batchUpdate(sql, batchArgs);

        if (rowsAffected.length != courses.size()) {
            throw new IllegalStateException("Not all courses were inserted successfully");
        }
    }

    @Override
    public boolean update(Course course) {
        sql = "UPDATE courses SET course_name = ?, course_description = ? WHERE course_id = ?";
        rowAffected = jdbcTemplate.update(sql, course.getName(), course.getDescription(), course.getId());

        return rowAffected > 0;
    }

    @Override
    public boolean delete(Course course) {
        sql = "DELETE FROM courses WHERE course_id = ?";
        rowAffected = jdbcTemplate.update(sql, course.getId());

        return rowAffected > 0;
    }

    @Override
    public List<Course> findAllCourseByStudentsId(int studentId) {
        sql = "SELECT c.course_id, c.course_name, c.course_description FROM courses c JOIN enrollments e ON c.course_id = e.course_id WHERE e.student_id = ?";

        List<Course> courses = jdbcTemplate.query(sql, new Object[] {studentId}, courseRowMapper);

        if (courses.isEmpty()){
            throw new IllegalStateException("Can't fetch courses");
        }

        return courses;
    }

    private final RowMapper<Course> courseRowMapper = (rs, rowNum) -> {
        int courseId = rs.getInt("course_id");
        String courseName = rs.getString("course_name");
        String courseDescription = rs.getString("course_description");
        return new Course(courseId, courseName, courseDescription);
    };
}
