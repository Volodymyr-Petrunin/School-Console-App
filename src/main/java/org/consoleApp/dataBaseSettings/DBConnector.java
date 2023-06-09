package org.consoleApp.dataBaseSettings;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.*;
public class DBConnector {
    private final String HOST = "localhost";
    private final String PORT = "5432";
    private final String DB_NAME = "school-console-app";
    private final String LOGIN = "postgres";
    private final String PASSWORD = "0403";
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
