package org.consoleApp.dataBaseSettings;

import org.consoleApp.dao.jdbc.AbstractContainerBaseTest;
import org.consoleApp.domin.Group;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScriptRunnerTest extends AbstractContainerBaseTest {
    private final DataSource dataSource = getDataSource();
    private final ScriptRunner scriptRunner = new ScriptRunner(dataSource);
    private InputStream inputStream;

    @Test
    void testRunScript_ShouldExecuteScriptSuccessfully(){
        String scriptContent = "INSERT INTO groups (group_id, group_name) VALUES " + "(1, 'AA-11')," + "(2, 'BB-22')";

        inputStream = new ByteArrayInputStream(scriptContent.getBytes(StandardCharsets.UTF_8));
        scriptRunner.runScript(inputStream);

        List<Group> expected = List.of(
          new Group(1, "AA-11"),
          new Group(2, "BB-22")
        );

        List<Group> actual = findAll();

        assertEquals(expected, actual);
    }

    public List<Group> findAll() {
        List<Group> groups = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM groups")) {

            try (ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()){
                    int groupId = resultSet.getInt("group_id");
                    String groupName = resultSet.getString("group_name");

                    groups.add(new Group(groupId, groupName));
                }
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Can't find groups", e);
        }

        return groups;
    }
}