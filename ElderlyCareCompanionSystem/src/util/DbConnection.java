package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class for managing database connections.
 * Keeps your connection code in one place so DAOs can reuse it.
 */
public class DbConnection {
    // Database connection details
    private static final String URL = "jdbc:postgresql://localhost:5432/elderlycare_companion_db";
    private static final String USER = "postgres";   // change if your username differs
    private static final String PASSWORD = "123";   // change if your password differs

    // Method to get a connection object
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
