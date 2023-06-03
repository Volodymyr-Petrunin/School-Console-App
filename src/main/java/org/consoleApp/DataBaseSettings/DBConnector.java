package org.consoleApp.DataBaseSettings;

import java.sql.*;
public class DBConnector {
    private final String HOST = "localhost";
    private final String PORT = "5432";
    private final String DB_NAME = "school-console-app";
    private final String LOGIN = "postgres";
    private final String PASSWORD = "0403";
    private Connection connection;

    private Connection getDBConnection() throws ClassNotFoundException, SQLException {
        String DB_URL = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME;
        Class.forName("org.postgresql.Driver");

        return DriverManager.getConnection(DB_URL,LOGIN,PASSWORD);
    }

    public void isConnected() throws SQLException, ClassNotFoundException {
        connection = getDBConnection();
        System.out.println(connection.isValid(1000));
    }

    public Connection getConnection() {
        try {
            return connection = getDBConnection();

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void closeConnection() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}
