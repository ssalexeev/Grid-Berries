package org.capstone.config.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:5432/mydatabase";
    private static final String USERNAME = "myuser";
    private static final String PASSWORD = "mypassword";

    public static Connection getConnection() {
        try (Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            return con;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
