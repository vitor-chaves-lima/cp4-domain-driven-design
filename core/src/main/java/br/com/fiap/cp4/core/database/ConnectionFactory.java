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
    private static boolean schemaInitialized = false;

    static {
        loadDatabaseConfig();
        initializeDataSource();
        ensureSchemaExists();
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

    private static void ensureSchemaExists() {
        if (!schemaInitialized) {
            try {
                if (!tableExists("games")) {
                    System.out.println("Table 'games' not found. Initializing schema...");
                    executeSchemaInitialization();
                } else if (!columnExists("games", "is_favorite")) {
                    System.out.println("Column 'is_favorite' not found. Running migration...");
                    addFavoriteColumn();
                }
                schemaInitialized = true;
            } catch (Exception e) {
                System.err.println("Error ensuring schema exists: " + e.getMessage());
            }
        }
    }

    public static boolean tableExists(String tableName) {
        try (Connection conn = getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();

            try (ResultSet tables = metaData.getTables(null, null, tableName, new String[]{"TABLE"})) {
                boolean exists = tables.next();

                if (exists) {
                    System.out.println("Table '" + tableName + "' already exists.");
                }

                return exists;
            }

        } catch (SQLException e) {
            System.err.println("Error checking table existence: " + e.getMessage());
            return false;
        }
    }

    public static boolean columnExists(String tableName, String columnName) {
        try (Connection conn = getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();

            try (ResultSet columns = metaData.getColumns(null, null, tableName, columnName)) {
                boolean exists = columns.next();

                if (exists) {
                    System.out.println("Column '" + columnName + "' exists in table '" + tableName + "'.");
                } else {
                    System.out.println("Column '" + columnName + "' not found in table '" + tableName + "'.");
                }

                return exists;
            }

        } catch (SQLException e) {
            System.err.println("Error checking column existence: " + e.getMessage());
            return false;
        }
    }

    private static void addFavoriteColumn() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            String sql = "ALTER TABLE games ADD COLUMN is_favorite BOOLEAN DEFAULT FALSE";
            stmt.executeUpdate(sql);

            String indexSql = "CREATE INDEX IF NOT EXISTS idx_games_is_favorite ON games(is_favorite)";
            stmt.executeUpdate(indexSql);

            System.out.println("Added 'is_favorite' column and index successfully.");

        } catch (SQLException e) {
            System.err.println("Error adding favorite column: " + e.getMessage());
            throw new RuntimeException("Failed to add favorite column", e);
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