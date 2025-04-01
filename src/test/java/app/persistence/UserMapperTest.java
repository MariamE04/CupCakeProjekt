package app.persistence;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {
    private static ConnectionPool connectionPool;

    @BeforeAll
    public static void setUp() {
        // Angiv testparametre til ConnectionPool
        String user = "postgres";
        String password = "postgres";
        String url = "jdbc:postgresql://localhost:5432/cupcake?currentSchema=test";
        String db = "cupcake";

        // Initialiser ConnectionPool med testparametre
        connectionPool = ConnectionPool.getInstance(user, password, url, db);
        UserMapper.setConnectionPool(connectionPool);
    }


    @BeforeEach
    void clearDatabase() {
        try (Connection connection = connectionPool.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM users"); // Sletter alle brugere i tabellen
        } catch (SQLException e) {
            fail("Kunne ikke rydde databasen før test: " + e.getMessage());
        }
    }


    @AfterEach
    void resetDatabaseState() {
        try (Connection connection = connectionPool.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM users"); // Sletter alle brugere
            stmt.execute("ALTER TABLE users ALTER COLUMN email RESTART WITH 1"); // Hvis du bruger en sekvens til email, kan du nulstille den
        } catch (SQLException e) {
            fail("Kunne ikke nulstille databasen efter test: " + e.getMessage());
        }
    }


    @Test
    void setConnectionPool() {
    }

    @Test
    void signUp() {
    }

    @Test
    void logIn() {
    }

    @Test
    void userExists() {
    }

    @Test
    void getAllUsers() {
    }
}