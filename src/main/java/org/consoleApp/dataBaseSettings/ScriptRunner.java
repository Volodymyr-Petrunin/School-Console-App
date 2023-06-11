package org.consoleApp.dataBaseSettings;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;

public class ScriptRunner {
    private DataSource dataSource;

    public ScriptRunner(DataSource dataSource) {
        this.dataSource = dataSource;
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
        try (Statement statement = dataSource.getConnection().createStatement()) {
            statement.execute(script);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
