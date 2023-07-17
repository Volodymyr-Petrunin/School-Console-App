package org.consoleApp.dao.spring_jdbc;

import org.consoleApp.dao.CourseDAO;
import org.consoleApp.dao.spring_jdbc.rowMappers.CourseRowMapper;
import org.consoleApp.domin.Course;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class CourseRepository implements CourseDAO {
    private static final String FIND_ALL = "SELECT * FROM courses ORDER BY course_id";
    private static final String FIND_BY_ID = "SELECT * FROM courses WHERE course_id = ?";
    private static final String INSERT = "INSERT INTO courses (course_name, course_description) VALUES (?, ?)";
    private static final String INSERT_BATCH = "INSERT INTO courses (course_name, course_description) VALUES (?, ?)";
    private static final String UPDATE = "UPDATE courses SET course_name = ?, course_description = ? WHERE course_id = ?";
    private static final String DELETE = "DELETE FROM courses WHERE course_id = ?";
    private static final String FIND_ALL_COURSE_BY_STUDENT_ID = "SELECT c.course_id, c.course_name, c.course_description FROM courses c JOIN enrollments e ON c.course_id = e.course_id WHERE e.student_id = ?";
    private final CourseRowMapper courseRowMapper = new CourseRowMapper();
    private final JdbcTemplate jdbcTemplate;

    public CourseRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Course> findAll() {
        return jdbcTemplate.query(FIND_ALL, courseRowMapper);
    }

    @Override
    public Optional<Course> findById(int id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_BY_ID, new Object[] {id}, courseRowMapper));
    }

    @Override
    public boolean insert(Course course) {
        return jdbcTemplate.update(INSERT, course.getName(), course.getDescription()) > 0;
    }

    @Override
    public void insertBatch(List<Course> courses) {
        List<Object[]> batchArgs = courses.stream()
                .map(course -> new Object[] { course.getName(), course.getDescription() })
                .toList();

        jdbcTemplate.batchUpdate(INSERT_BATCH, batchArgs);
    }

    @Override
    public boolean update(Course course) {
        return jdbcTemplate.update(UPDATE, course.getName(), course.getDescription(), course.getId()) > 0;
    }

    @Override
    public boolean delete(Course course) {
        return jdbcTemplate.update(DELETE, course.getId()) > 0;
    }

    @Override
    public List<Course> findAllCourseByStudentsId(int studentId) {
        return jdbcTemplate.query(FIND_ALL_COURSE_BY_STUDENT_ID, new Object[] {studentId}, courseRowMapper);
    }
}
