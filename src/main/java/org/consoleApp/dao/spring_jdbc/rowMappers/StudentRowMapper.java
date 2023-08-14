package org.consoleApp.dao.spring_jdbc.rowMappers;

import org.consoleApp.domin.Student;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentRowMapper implements RowMapper<Student> {
    @Override
    public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("student_id");
        Integer groupId = rs.getInt("group_id");
        String firstName = rs.getString("first_name");
        String lastName = rs.getString("last_name");

        return new Student(id, groupId, firstName, lastName);
    }
}
