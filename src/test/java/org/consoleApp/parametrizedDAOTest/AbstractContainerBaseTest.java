package org.consoleApp.parametrizedDAOTest;

import org.postgresql.ds.PGSimpleDataSource;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;

public abstract class AbstractContainerBaseTest {
    static final PostgreSQLContainer<?> postgreSQLContainer;

    static {
        postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
                .withDatabaseName("school-console-app")
                .withUsername("postgres")
                .withPassword("0403")
                .withInitScript("SQLScript/create_tables.sql");

        postgreSQLContainer.start();
    }

    protected static DataSource getDataSource(){
        PGSimpleDataSource dataSource = new PGSimpleDataSource();

        dataSource.setUrl(postgreSQLContainer.getJdbcUrl());
        dataSource.setUser(postgreSQLContainer.getUsername());
        dataSource.setPassword(postgreSQLContainer.getPassword());

        return dataSource;
    }
}
