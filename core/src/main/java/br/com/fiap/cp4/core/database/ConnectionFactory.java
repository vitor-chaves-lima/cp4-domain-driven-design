package br.com.fiap.cp4.core.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class ConnectionFactory {
    private static final String CONFIG_FILE = "/database.properties";
    private static final String SCHEMA_INITIALIZATION_FILE = "/schema.sql";
    private static HikariDataSource dataSource;
    private static String url;
    private static String user;
    private static String password;

    static {
        loadDatabaseConfig();
        initializeDataSource();
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

    private static void initializeDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);
        config.setAutoCommit(true);

        dataSource = new HikariDataSource(config);
    }

    public static boolean tableExists(String tableName) {
        try (Connection conn = getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();

            try (ResultSet tables = metaData.getTables(null, null, tableName, new String[]{"TABLE"})) {
                boolean exists = tables.next();

                if (exists) {
                    System.out.println("Table '" + tableName + "' already exists. Skipping creation.");
                }

                return exists;
            }

        } catch (SQLException e) {
            System.err.println("Error checking table existence: " + e.getMessage());
            return false;
        }
    }

    public static void executeSchemaInitialization() {
        try (Connection conn = getConnection();
             InputStream inputStream = ConnectionFactory.class.getResourceAsStream(SCHEMA_INITIALIZATION_FILE)) {

            if (inputStream == null) {
                throw new RuntimeException("Schema file not found: " + SCHEMA_INITIALIZATION_FILE);
            }

            try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8)) {
                scanner.useDelimiter(";");

                try (Statement stmt = conn.createStatement()) {
                    while (scanner.hasNext()) {
                        String command = scanner.next().trim();
                        if (!command.isEmpty() && !command.startsWith("--")) {
                            stmt.executeUpdate(command);
                        }
                    }
                }

                System.out.println("Tables created successfully from: " + SCHEMA_INITIALIZATION_FILE);

            }
        } catch (SQLException | IOException e) {
            throw new RuntimeException("Error creating tables: " + e.getMessage(), e);
        }
    }

    public static Connection getConnection() throws SQLException {
        if (dataSource == null || dataSource.isClosed()) {
            initializeDataSource();
        }
        return dataSource.getConnection();
    }

    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && conn.isValid(5);
        } catch (SQLException e) {
            System.err.println("Connection error: " + e.getMessage());
            return false;
        }
    }

    public static void shutdown() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }

    public static boolean isHealthy() {
        return dataSource != null && !dataSource.isClosed() && testConnection();
    }
}