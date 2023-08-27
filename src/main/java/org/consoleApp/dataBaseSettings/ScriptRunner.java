package org.consoleApp.dataBaseSettings;

import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.io.*;
import java.sql.SQLException;
import java.sql.Statement;

public class ScriptRunner {
    private final DataSource dataSource;
    private final InputStream inputStream;

    @Autowired
    public ScriptRunner(DataSource dataSource, InputStream inputStream) {
        this.dataSource = dataSource;
        this.inputStream = inputStream;
    }

    public void runScript(){
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
