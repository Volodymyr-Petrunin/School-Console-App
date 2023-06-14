package org.consoleApp.dataBaseSettings;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class DBConnector {
    private int maxPoolSize;
    private DataSource dataSource;

    public DBConnector(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    private DataSource getDBConnection() {
        HikariConfig config = new HikariConfig("src\\main\\resources\\hikari.properties");
        return new HikariDataSource(config);
    }

    public DataSource getConnection() {
        return dataSource = getDBConnection();
    }
}
