package org.consoleApp.dao.spring_jdbc;

import org.consoleApp.dao.StudentsDAO;
import org.consoleApp.dao.spring_jdbc.rowMappers.StudentRowMapper;
import org.consoleApp.domin.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.List;
import java.util.Optional;

@Profile("spring-jdbc")
@Repository
public class StudentsRepository implements StudentsDAO {
    private static final String FIND_ALL = "SELECT * FROM students ORDER BY student_id";
    private static final String FIND_BY_ID = "SELECT * FROM students WHERE student_id = ?";
    private static final String INSERT = "INSERT INTO students (group_id, first_name, last_name) VALUES (?, ?, ?)";
    private static final String INSERT_BATCH = "INSERT INTO students (group_id, first_name, last_name) VALUES (?, ?, ?)";
    private static final String UPDATE = "UPDATE students SET group_id = ?, first_name = ?, last_name = ? WHERE student_id = ?";
    private static final String DELETE = "DELETE FROM students WHERE student_id = ?";
    private static final String DELETE_BY_STUDENT_ID = "DELETE FROM students WHERE student_id = ?";
    private static final String FIND_BY_FIRST_NAME = "SELECT * FROM students WHERE first_name = ?";
    private static final String ENROLL_STUDENT_IN_COURSE = "INSERT INTO enrollments (student_id,course_id) VALUES (?,?)";
    private static final String REMOVE_STUDENT_FROM_COURSE = "DELETE FROM enrollments WHERE student_id = ? AND course_id = ?";
    private static final String FIND_STUDENT_BY_COURSE_NAME = "SELECT s.student_id, s.group_id, s.first_name, s.last_name FROM students s " +
            "JOIN enrollments e ON s.student_id = e.student_id JOIN courses c ON e.course_id = c.course_id " +
            "WHERE c.course_name = ?";
    private final StudentRowMapper studentRowMapper = new StudentRowMapper();
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public StudentsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Student> findAll() {
        return jdbcTemplate.query(FIND_ALL, studentRowMapper);
    }

    @Override
    public Optional<Student> findById(int id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_BY_ID, new Object[]{id}, studentRowMapper));
    }

    @Override
    public boolean insert(Student student) {
        return jdbcTemplate.update(INSERT, new SqlParameterValue(Types.INTEGER, student.getGroupId().orElse(null)), student.getFirstName(), student.getLastName()) > 0;
    }

    @Override
    public void insertBatch(List<Student> students) {
        List<Object[]> batchArgs = students.stream()
                .map(student -> new Object[]{new SqlParameterValue(Types.INTEGER, student.getGroupId().orElse(null)), student.getFirstName(), student.getLastName()})
                .toList();

        jdbcTemplate.batchUpdate(INSERT_BATCH, batchArgs);
    }

    @Override
    public boolean update(Student student) {
        return jdbcTemplate.update(UPDATE, new SqlParameterValue(Types.INTEGER, student.getGroupId().orElse(null)), student.getFirstName(), student.getLastName(), student.getId()) > 0;
    }

    @Override
    public boolean delete(Student student) {
        return jdbcTemplate.update(DELETE, student.getId()) > 0;
    }

    @Override
    public boolean deleteByStudentId(int studentId) {
        return jdbcTemplate.update(DELETE_BY_STUDENT_ID, studentId) > 0;
    }

    @Override
    public List<Student> findByFirstName(String firstName) {
        return jdbcTemplate.query(FIND_BY_FIRST_NAME, studentRowMapper, firstName);
    }

    @Override
    public boolean enrollStudentInCourse(int studentId, int courseId) {
        return jdbcTemplate.update(ENROLL_STUDENT_IN_COURSE, studentId, courseId) > 0;
    }

    @Override
    public boolean removeStudentFromCourse(int studentId, int courseId) {
        return jdbcTemplate.update(REMOVE_STUDENT_FROM_COURSE, studentId, courseId) > 0;
    }

    @Override
    public List<Student> findStudentsByCourseName(String courseName) {
        return jdbcTemplate.query(FIND_STUDENT_BY_COURSE_NAME, studentRowMapper, courseName);
    }
}
