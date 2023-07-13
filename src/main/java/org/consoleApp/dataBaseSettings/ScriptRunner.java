package org.consoleApp.dataBaseSettings;

import javax.sql.DataSource;
import java.io.*;
import java.sql.SQLException;
import java.sql.Statement;

public class ScriptRunner {
    private DataSource dataSource;

    public ScriptRunner(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void runScript(InputStream inputStream){
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
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
