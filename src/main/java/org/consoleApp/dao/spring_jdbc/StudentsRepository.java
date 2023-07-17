package org.consoleApp.dao.spring_jdbc;

import org.consoleApp.dao.StudentsDAO;
import org.consoleApp.dao.spring_jdbc.rowMappers.StudentRowMapper;
import org.consoleApp.domin.Student;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class StudentsRepository implements StudentsDAO {
    private final StudentRowMapper studentRowMapper = new StudentRowMapper();
    private final JdbcTemplate jdbcTemplate;
    private String sql;
    private int rowAffected;

    public StudentsRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Student> findAll() {
        sql = "SELECT * FROM students ORDER BY student_id";

        return jdbcTemplate.query(sql, studentRowMapper);
    }

    @Override
    public Optional<Student> findById(int id) {
        sql = "SELECT * FROM students WHERE student_id = ?";
        Student student = jdbcTemplate.queryForObject(sql, new Object[]{id}, studentRowMapper);

        return Optional.ofNullable(student);
    }

    @Override
    public boolean insert(Student student) {
        sql = "INSERT INTO students (group_id, first_name, last_name) VALUES (?, ?, ?)";
        rowAffected = jdbcTemplate.update(sql, student);

        return rowAffected > 0;
    }

    @Override
    public void insertBatch(List<Student> students) {
        sql = "INSERT INTO students (group_id, first_name, last_name) VALUES (?, ?, ?)";

        List<Object[]> batchArgs = students.stream()
                .map(student -> new Object[]{student.getGroupId(), student.getFirstName(), student.getLastName()})
                .toList();

        int[] rowAffected = jdbcTemplate.batchUpdate(sql, batchArgs);

        if (rowAffected.length != students.size()){
            throw new IllegalStateException("Not all students were inserted successfully");
        }
    }

    @Override
    public boolean update(Student student) {
       sql = "UPDATE students SET group_id = ?, first_name = ?, last_name = ? WHERE student_id = ?";
       rowAffected = jdbcTemplate.update(sql, student.getGroupId(), student.getFirstName(), student.getLastName(), student.getId());

       return rowAffected > 0;
    }

    @Override
    public boolean delete(Student student) {
        sql = "DELETE FROM students WHERE student_id = ?";
        rowAffected = jdbcTemplate.update(sql, student.getId());

        return rowAffected > 0;
    }

    @Override
    public boolean deleteByStudentId(int studentId) {
        sql = "DELETE FROM students WHERE student_id = ?";
        rowAffected = jdbcTemplate.update(sql, studentId);

        return rowAffected > 0;
    }

    @Override
    public List<Student> findByFirstName(String firstName) {
        sql = "SELECT * FROM students WHERE first_name = ?";

        return jdbcTemplate.query(sql, studentRowMapper, firstName);
    }

    @Override
    public boolean enrollStudentInCourse(int studentId, int courseId) {
        sql = "INSERT INTO enrollments (student_id,course_id) VALUES (?,?)";
        rowAffected = jdbcTemplate.update(sql, studentId, courseId);

        return rowAffected > 0;
    }

    @Override
    public boolean removeStudentFromCourse(int studentId, int courseId) {
        sql = "DELETE FROM enrollments WHERE student_id = ? AND course_id = ?";
        rowAffected = jdbcTemplate.update(sql, studentId, courseId);

        return rowAffected > 0;
    }

    @Override
    public List<Student> findStudentsByCourseName(String courseName) {
        sql = "SELECT s.student_id, s.group_id, s.first_name, s.last_name FROM students s " +
                "JOIN enrollments e ON s.student_id = e.student_id JOIN courses c ON e.course_id = c.course_id " +
                "WHERE c.course_name = ?";

        return jdbcTemplate.query(sql, studentRowMapper, courseName);
    }
}
