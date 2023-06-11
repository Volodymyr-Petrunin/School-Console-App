package org.consoleApp.dataBaseSettings;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.consoleApp.parser.impl.DBSettingsParser;
import org.consoleApp.readers.ResourcesFileReader;
import org.consoleApp.records.DBSettings;

import javax.sql.DataSource;
import java.sql.*;

public class DBConnector {
    private final ResourcesFileReader reader = new ResourcesFileReader("DBsettings.txt");
    private final DBSettingsParser parser = new DBSettingsParser();
    private final DBSettings settings = parser.parse(reader.read().get(0)); // get 0 because only one line in DBsettings file.
    private final String HOST = settings.host();
    private final String PORT = settings.port();
    private final String DB_NAME = settings.dbName();
    private final String LOGIN = settings.login();
    private final String PASSWORD = settings.password();
    private int maxPoolSize;
    private DataSource dataSource;

    public DBConnector(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    private DataSource getDBConnection() throws ClassNotFoundException, SQLException {
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl("jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME);
        config.setUsername(LOGIN);
        config.setPassword(PASSWORD);
        config.setMaximumPoolSize(maxPoolSize);

        HikariDataSource dataSource = new HikariDataSource(config);
        return dataSource;
    }

    public DataSource getConnection() {
        try {
            return dataSource = getDBConnection();

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
