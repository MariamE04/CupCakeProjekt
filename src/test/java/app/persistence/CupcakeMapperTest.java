package app.persistence;

import app.entities.*;
import app.exceptions.DatabaseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CupcakeMapperTest {
    private static ConnectionPool connector;
    CupcakeMapper cupcakeMapper = new CupcakeMapper();

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
                                "email character varying(255) COLLATE pg_catalog.\"default\" NOT NULL, " +
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
                                "ADD CONSTRAINT orders_email_fkey FOREIGN KEY (email) " +
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
    void payForOrder() throws DatabaseException {
        CupcakeMapper.setConnectionPool(connector);

        String email = "testuser@example.com";
        double initialBalance = 50.0; // Brugerens startsaldo

        //cupcakes med kendte priser
        Cupcake cupcake1 = new Cupcake(new Topping("Blueberry", 1.5), new Bottom("Chocolate", 5.0));
        Cupcake cupcake2 = new Cupcake( new Topping("Lemon", 2.0), new Bottom("Vanilla", 5.0));

        // Opret ordre
        List<Cupcake> cupcakes = new ArrayList<>();
        cupcakes.add(cupcake1);
        cupcakes.add(cupcake2);
        Order order = new Order("", LocalDate.now());
        order.setCupcakes(cupcakes);

        // Beregn forventet ny saldo
        double totalCost = cupcake1.getPrice()  + cupcake2.getPrice();
        double expectedBalance = initialBalance - totalCost;

        // Betal for ordren
        CupcakeMapper.payForOrder(order, initialBalance, email);

        //hent den opdaterede saldo fra databasen
        double balance =  0.0;


    }

    @Test
    void getTopping() throws DatabaseException {
        CupcakeMapper.setConnectionPool(connector);

        List<Topping> topping = cupcakeMapper.getTopping();

        Assertions.assertEquals(2, topping.size(), "There should be exactly 2 topping");
    }

    @Test
    void getChosenTopping() throws DatabaseException {
        CupcakeMapper.setConnectionPool(connector);

        Topping excpected = new Topping("Blueberry",1.5);
        String choice = "Blueberry";

        Topping actual = cupcakeMapper.getChosenTopping(choice);
        Assertions.assertEquals(excpected.getName(), actual.getName());
        Assertions.assertEquals(excpected.getPrice(), actual.getPrice());
    }

    @Test
    void getChosenBottom() throws DatabaseException {
        CupcakeMapper.setConnectionPool(connector);

        Bottom expected = new Bottom("Vanilla",5.00);
        String choice = "Vanilla";

        Bottom actual = cupcakeMapper.getChosenBottom(choice);
        Assertions.assertEquals(expected.getName(), actual.getName());
        Assertions.assertEquals(expected.getPrice(), actual.getPrice());
    }

    @Test
    void getBottom() throws DatabaseException {
        CupcakeMapper.setConnectionPool(connector);

        List<Bottom> bottoms = cupcakeMapper.getBottom();

        Assertions.assertEquals(2, bottoms.size(), "There should be exactly 2 bottoms");
    }
}