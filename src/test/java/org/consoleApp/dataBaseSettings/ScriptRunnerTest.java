package org.consoleApp.dataBaseSettings;

import org.consoleApp.parametrizedDAOTest.AbstractContainerBaseTest;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ScriptRunnerTest extends AbstractContainerBaseTest {
    private final DataSource dataSource = getDataSource();
    private ScriptRunner scriptRunner;
    private InputStream inputStream;

    @Test
    void testRunScript_ShouldExecuteScriptSuccessfully(){
        String scriptContent = "INSERT INTO groups (group_id, group_name) VALUES " + "(1, 'AA-11')," + "(2, 'BB-22')";
        inputStream = new ByteArrayInputStream(scriptContent.getBytes(StandardCharsets.UTF_8));

        scriptRunner = new ScriptRunner(dataSource, inputStream);

        scriptRunner.runScript();

        int expected = 2;
        int actual = countAllGroups();

        assertEquals(expected, actual);
    }

    public int countAllGroups() {
        int count = 0;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM groups")) {

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    count = resultSet.getInt(1);
                }
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Can't count groups", e);
        }

        return count;
    }
}