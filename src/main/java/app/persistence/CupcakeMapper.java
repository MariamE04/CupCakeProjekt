package app.persistence;

import app.entities.Bottom;
import app.entities.Cupcake;
import app.entities.Order;
import app.entities.Topping;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CupcakeMapper {
    private static ConnectionPool connectionPool;

    public static void setConnectionPool(ConnectionPool newConnectionPool){
        connectionPool = newConnectionPool;
    }

    public static void payForOrder(Order order, double balance, String email) throws DatabaseException {
        double totalCost = 0;
        String sql = "UPDATE users SET balance = ? WHERE email = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)){

            for (Cupcake cupcake : order.getCupcakes()) {
                totalCost += cupcake.getPrice();
            }

            ps.setDouble(1, balance - totalCost);
            ps.setString(2, email);
            ps.executeUpdate();

        } catch (SQLException e){
            throw new DatabaseException("FEEEEJL", e.getMessage());
        }
    }

    public static List<Topping> getTopping() throws DatabaseException {
        List<Topping> toppings = new ArrayList<>(); // List to hold topping records.
        String sql = "SELECT * FROM public.toppings"; // SQL query to fetch all records.

        try (
                Connection connection = connectionPool.getConnection(); // Acquire a database connection.
                PreparedStatement ps = connection.prepareStatement(sql) // Prepare the SQL statement.
        ) {
            ResultSet rs = ps.executeQuery(); // Execute the query and fetch results.
            while (rs.next()) { // Iterate over each row in the result set.
                String name = rs.getString("topping"); // Extract the 'topping' field from the row.
                double price = rs.getDouble("price"); // Extract the 'price' field.

                // Create a Topping object and add it to the list.
                toppings.add(new Topping(name, price));
            }
            //Catch exceptions
        } catch (SQLException e) {
            throw new DatabaseException("Error", e.getMessage());
        }
        return toppings;  // Return the list of topping records
    }

    public static Topping getChosenTopping(String choice) throws DatabaseException {
        Topping topping = null; // Object to hold topping record.
        String sql = "SELECT * FROM toppings WHERE topping = ?"; // SQL query to fetch all records.

        try (
                Connection connection = connectionPool.getConnection(); // Acquire a database connection.
                PreparedStatement ps = connection.prepareStatement(sql) // Prepare the SQL statement.
        ) {
            ps.setString(1, choice);
            ResultSet rs = ps.executeQuery(); // Execute the query and fetch results.
            while (rs.next()) { // Iterate over each row in the result set.
                String name = rs.getString("topping"); // Extract the 'topping' field from the row.
                double price = rs.getDouble("price"); // Extract the 'price' field.

                // Create a Topping object and add it to the list.
                topping = new Topping(name, price);
            }
            //Catch exceptions
        } catch (SQLException e) {
            throw new DatabaseException("Error", e.getMessage());
        }
        return topping;  // Return the list of topping records
    }

    public static Bottom getChosenBottom(String choice) throws DatabaseException {
        Bottom bottom = null; // Object to hold bottom record.
        String sql = "SELECT * FROM bottoms WHERE bottom = ?"; // SQL query to fetch all records.

        try (
                Connection connection = connectionPool.getConnection(); // Acquire a database connection.
                PreparedStatement ps = connection.prepareStatement(sql) // Prepare the SQL statement.
        ) {
            ps.setString(1, choice);
            ResultSet rs = ps.executeQuery(); // Execute the query and fetch results.
            while (rs.next()) { // Iterate over each row in the result set.
                String name = rs.getString("bottom"); // Extract the 'bottom' field from the row.
                double price = rs.getDouble("price"); // Extract the 'price' field.

                // Create a Bottom object and add it to the list.
                bottom = new Bottom(name, price);
            }
            //Catch exceptions
        } catch (SQLException e) {
            throw new DatabaseException("Error", e.getMessage());
        }
        return bottom;  // Return the list of bottom records
    }

    public static List<Bottom> getBottom() throws DatabaseException {
        List<Bottom> bottoms = new ArrayList<>(); // List to hold bottom records.
        String sql = "SELECT * FROM public.bottoms"; // SQL query to fetch all records.

        try (
                Connection connection = connectionPool.getConnection(); // Acquire a database connection.
                PreparedStatement ps = connection.prepareStatement(sql) // Prepare the SQL statement.
        ) {
            ResultSet rs = ps.executeQuery(); // Execute the query and fetch results.
            while (rs.next()) { // Iterate over each row in the result set.
                String name = rs.getString("bottom"); // Extract the 'bottom' field from the row.
                double price = rs.getDouble("price"); // Extract the 'price' field.

                // Create a Bottom object and add it to the list.
                bottoms.add(new Bottom(name, price));
            }
            //Catch exceptions
        } catch (SQLException e) {
            throw new DatabaseException("Error", e.getMessage());
        }
        return bottoms;  // Return the list of bottom records
    }

}







