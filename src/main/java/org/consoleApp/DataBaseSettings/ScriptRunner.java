package org.consoleApp.DataBaseSettings;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ScriptRunner {
    private Connection connection;

    public ScriptRunner(DBConnector dbConnector) {
        connection = dbConnector.getConnection();
    }

    public void runScript(String filePath){
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            StringBuilder scriptContent = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                scriptContent.append(line);
                scriptContent.append("\n");
            }

            executeScript(scriptContent.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void executeScript(String script) {
        try (Statement statement = connection.createStatement()) {
            statement.execute(script);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
