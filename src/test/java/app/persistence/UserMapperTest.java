package app.persistence;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import app.exceptions.DatabaseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

class userMapperTest {
    private static ConnectionPool connector;
    UserMapper userMapper = new UserMapper();

    @BeforeAll
    public static void setUpClass() {
        try {
            // Initialize the database connector with the credentials and URL
            connector = ConnectionPool.getInstance("postgres", "postgres", "jdbc:postgresql://localhost:5432/cupcake?currentSchema=test", "cupcake");

            // Create a connection to set up the test environment
            try (Connection testConnection = connector.getConnection(); Statement stmt = testConnection.createStatement()) {
                // Drop the existing tables (if any)
                stmt.execute("DROP TABLE IF EXISTS test.orderdetails CASCADE");
                stmt.execute("DROP TABLE IF EXISTS test.orders CASCADE");
                stmt.execute("DROP TABLE IF EXISTS test.users CASCADE");
                stmt.execute("DROP TABLE IF EXISTS test.bottoms CASCADE");
                stmt.execute("DROP TABLE IF EXISTS test.toppings CASCADE");

                // Create tables in the correct order
                String sql =
                        "CREATE TABLE IF NOT EXISTS test.users (" +
                                "email character varying(255) COLLATE pg_catalog.\"default\" NOT NULL, " +
                                "password character varying(255) COLLATE pg_catalog.\"default\" NOT NULL, " +
                                "balance double precision NOT NULL, " +
                                "CONSTRAINT users_pkey PRIMARY KEY (email));" +

                                "CREATE TABLE IF NOT EXISTS test.bottoms (" +
                                "bottom character varying(255) COLLATE pg_catalog.\"default\" NOT NULL, " +
                                "price double precision NOT NULL, " +
                                "CONSTRAINT bottoms_pkey PRIMARY KEY (bottom));" +

                                "CREATE TABLE IF NOT EXISTS test.toppings (" +
                                "topping character varying(255) COLLATE pg_catalog.\"default\" NOT NULL, " +
                                "price double precision NOT NULL, " +
                                "CONSTRAINT toppings_pkey PRIMARY KEY (topping));" +

                                "CREATE TABLE IF NOT EXISTS test.orders (" +
                                "order_nr bigserial NOT NULL, " +
                                "\"user\" character varying(255) COLLATE pg_catalog.\"default\" NOT NULL, " +
                                "date date NOT NULL, " +
                                "CONSTRAINT orders_pkey PRIMARY KEY (order_nr));" +

                                "CREATE TABLE IF NOT EXISTS test.orderdetails (" +
                                "id bigserial NOT NULL, " +
                                "order_nr bigint NOT NULL, " +
                                "topping character varying(255) COLLATE pg_catalog.\"default\" NOT NULL, " +
                                "bottom character varying(255) COLLATE pg_catalog.\"default\" NOT NULL, " +
                                "CONSTRAINT orderdetails_pkey PRIMARY KEY (id));" +

                                // Add the foreign key constraints after tables are created
                                "ALTER TABLE IF EXISTS test.orderdetails " +
                                "ADD CONSTRAINT orderdetails_bottom_fkey FOREIGN KEY (bottom) " +
                                "REFERENCES test.bottoms (bottom) MATCH SIMPLE " +
                                "ON UPDATE NO ACTION " +
                                "ON DELETE NO ACTION;" +

                                "ALTER TABLE IF EXISTS test.orderdetails " +
                                "ADD CONSTRAINT orderdetails_order_nr_fkey FOREIGN KEY (order_nr) " +
                                "REFERENCES test.orders (order_nr) MATCH SIMPLE " +
                                "ON UPDATE NO ACTION " +
                                "ON DELETE NO ACTION;" +

                                "ALTER TABLE IF EXISTS test.orderdetails " +
                                "ADD CONSTRAINT orderdetails_topping_fkey FOREIGN KEY (topping) " +
                                "REFERENCES test.toppings (topping) MATCH SIMPLE " +
                                "ON UPDATE NO ACTION " +
                                "ON DELETE NO ACTION;" +

                                "ALTER TABLE IF EXISTS test.orders " +
                                "ADD CONSTRAINT orders_user_fkey FOREIGN KEY (\"user\") " +
                                "REFERENCES test.users (email) MATCH SIMPLE " +
                                "ON UPDATE NO ACTION " +
                                "ON DELETE NO ACTION;";

                stmt.execute(sql);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                Assertions.fail("Database setup failed: " + e.getMessage());
            }
        } catch (Exception e) {
            Assertions.fail("Database connection failed: " + e.getMessage());
        }
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
            stmt.execute("INSERT INTO test.bottoms (bottom, price) VALUES ('Cheese', 5.0), ('Pepperoni', 6.0)");
            stmt.execute("INSERT INTO test.toppings (topping, price) VALUES ('Olives', 1.5), ('Mushrooms', 2.0)");
            stmt.execute("INSERT INTO test.users (email, password, balance) VALUES ('testuser@example.com', 'password123', 50.0)");
            stmt.execute("INSERT INTO test.orders (\"user\", date) VALUES ('testuser@example.com', '2025-01-01')");
            stmt.execute("INSERT INTO test.orderdetails (order_nr, topping, bottom) VALUES (1, 'Olives', 'Cheese')");

        } catch (SQLException e) {
            Assertions.fail("Failed to set up test data: " + e.getMessage());
        }
    }

    @Test
    void testConnection() throws SQLException {
        // Test the database connection
        Assertions.assertNotNull(connector.getConnection(), "Database connection should not be null");
    }

    @Test
    void signUp() {
        try {
            // Testdata
            String email = "newuser@example.com";
            String password = "password123";

            // Kald signUp-metoden
            int rowsAffected = userMapper.signUp(email, password);

            // Kontroller, om én række blev oprettet (1 betyder, at brugeren blev oprettet)
            Assertions.assertEquals(1, rowsAffected, "En bruger skal være blevet oprettet.");

        } catch (DatabaseException e) {
            Assertions.fail("SignUp-metoden fejlede: " + e.getMessage());
        }
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