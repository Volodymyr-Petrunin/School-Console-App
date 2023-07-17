package org.consoleApp.dao.spring_jdbc.rowMappers;

import org.consoleApp.domin.Course;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CourseRowMapper implements RowMapper<Course> {
    @Override
    public Course mapRow(ResultSet rs, int rowNum) throws SQLException {
        int courseId = rs.getInt("course_id");
        String courseName = rs.getString("course_name");
        String courseDescription = rs.getString("course_description");

        return new Course(courseId, courseName, courseDescription);
    }
}
