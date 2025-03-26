package app.mappers;

import app.entities.Bottom;
import app.entities.Topping;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CupcakeMapper {

    public static List<Topping> getTopping(ConnectionPool connectionPool) throws DatabaseException {
        List<Topping> toppings = new ArrayList<>(); // List to hold conversion records.
        String sql = "SELECT * FROM toppings"; // SQL query to fetch all records.

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

    public static List<Bottom> getBottom(ConnectionPool connectionPool) throws DatabaseException {
        List<Bottom> bottoms = new ArrayList<>(); // List to hold conversion records.
        String sql = "SELECT * FROM bottoms"; // SQL query to fetch all records.

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
