package br.com.fiap.cp4.core.database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {
    private static final String CONFIG_FILE = "/database.properties";
    private static String url;
    private static String user;
    private static String password;

    static {
        loadDatabaseConfig();
    }

    private static void loadDatabaseConfig() {
        try (InputStream input = ConnectionFactory.class.getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new RuntimeException("Configuration file not found: " + CONFIG_FILE);
            }

            Properties props = new Properties();
            props.load(input);

            url = props.getProperty("db.url");
            user = props.getProperty("db.user");
            password = props.getProperty("db.password");

            String driverClass = props.getProperty("db.driver");
            if (driverClass != null) {
                Class.forName(driverClass);
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error loading database configuration: " + e.getMessage(), e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Connection error: " + e.getMessage());
            return false;
        }
    }
}