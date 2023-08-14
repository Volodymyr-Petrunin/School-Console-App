package org.consoleApp.dao.spring_jdbc.rowMappers;

import org.consoleApp.domin.Group;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupRowMapper implements RowMapper<Group> {
    @Override
    public Group mapRow(ResultSet rs, int rowNum) throws SQLException {
        int groupId = rs.getInt("group_id");
        String groupName = rs.getString("group_name");

        return new Group(groupId, groupName);
    }
}
