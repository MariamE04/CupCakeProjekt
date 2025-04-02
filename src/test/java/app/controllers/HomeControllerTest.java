package app.controllers;

import app.persistence.ConnectionPool;
import app.persistence.UserMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class HomeControllerTest {
    private static ConnectionPool connector;
    private HomeController homeController;

    @BeforeAll
    public static void setUpClass() {
        connector = ConnectionPool.getInstance(
                "postgres", "postgres",
                "jdbc:postgresql://localhost:5432/cupcake?currentSchema=test",
                "cupcake"
        );

        // Sæt connection pool i HomeController
        HomeController.setConnectionPool(connector);
    }

    @BeforeEach
    void setUp() {
        try (Connection testConnection = connector.getConnection(); Statement stmt = testConnection.createStatement()) {
            // Clear the tables to ensure a clean state before each test
            stmt.execute("DELETE FROM test.orderdetails CASCADE");
            stmt.execute("DELETE FROM test.orders CASCADE");
            stmt.execute("DELETE FROM test.users CASCADE");
            stmt.execute("DELETE FROM test.bottoms CASCADE");
            stmt.execute("DELETE FROM test.toppings CASCADE");

            // Optionally reset sequences to make sure IDs start at 1 each time
            stmt.execute("SELECT setval('test.bottoms_bottom_seq', 1, false)");
            stmt.execute("SELECT setval('test.orderdetails_id_seq', 1, false)");

            // Insert test data into your tables
            stmt.execute("INSERT INTO test.bottoms (bottom, price) VALUES ('Chocolate', 5.0), ('Vanilla', 5.0)");
            stmt.execute("INSERT INTO test.toppings (topping, price) VALUES ('Blueberry', 1.5), ('Lemon', 2.0)");
            stmt.execute("INSERT INTO test.users (email, password, balance) VALUES ('testuser@example.com', 'password123', 50.0)");
            stmt.execute("INSERT INTO test.orders (email, date) VALUES ('testuser@example.com', '2025-01-01')");
            stmt.execute("INSERT INTO test.orderdetails (order_nr, topping, bottom) VALUES (1, 'Blueberry', 'Chocolate')");

        } catch (SQLException e) {
            Assertions.fail("Failed to set up test data: " + e.getMessage());
        }
    }
    @Test
    void testConnection() throws SQLException {
        Assertions.assertNotNull(connector.getConnection(), "Database connection should not be null");
    }

    @Test
    void signUpUser() {
    }

    @Test
    void userLogIn() {
    }
}