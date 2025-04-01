package app.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class UserMapperTest {
    private Connection connection;
    private UserMapper userMapper;

    @BeforeEach
    void setUp() throws SQLException {
        // Opret forbindelse til en test-database
        String url =  "jdbc:postgresql://localhost:5432/cupcake?currentSchema=test";
        String user = "test_user";
        String password = "test_password";

        connection = DriverManager.getConnection(url, user, password);
        userMapper = new UserMapper(new ConnectionPool(connection));

        // Ryd databasen før hver test
        connection.prepareStatement("DELETE FROM users").executeUpdate();
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