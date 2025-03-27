package app.persistence;

import app.entities.Order;
import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderMapper {

    public static Order addOrder(Order order, ConnectionPool connectionPool) throws DatabaseException{
        String sql = "INSERT INTO orders (user, date) VALUE(?,?)";

        try(Connection connection = connectionPool.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setString(1, order.getEmail());
            ps.setObject(2, order.getLocalDate());

            try (var rs = ps.executeQuery()) { // Use executeQuery() since we're returning a result
                if (rs.next()) {
                    // Return a new Order object including the generated ID
                    return new Order(order.getEmail(), order.getLocalDate());
                } else {
                    throw new DatabaseException("Fejl ved tilføjelse af order: ingen ID returneret.");
                }
            }

        }catch (SQLException e){
            throw new DatabaseException("MEGET FEJL", e.getMessage());
        }

    }

    public static List<Order> getAllOrders(ConnectionPool connectionPool) throws DatabaseException{
        String sql = "SELECT * FROM orders";
        List<Order> ordersList = new ArrayList<>();

        try(Connection connection = connectionPool.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)){
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int id = rs.getInt("order_nr");
                String email = rs.getString("user");
                LocalDate localDate = LocalDate.parse(rs.getString("date"));

                ordersList.add(new Order(id, email, localDate));
            }

        } catch (SQLException e){
            throw new DatabaseException("Fejl i at hente alle ordrene" + e.getMessage());
        }
        return ordersList;
    }

    public static List<Order> getOrdersByEmail(String email, ConnectionPool connectionPool) throws DatabaseException{
        String sql = "SELECT * FROM orders WHERE user = ?";
        List<Order> ordersList = new ArrayList<>();

        try(Connection connection = connectionPool.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)){

           ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int id = rs.getInt("order_nr");
                LocalDate localDate = LocalDate.parse(rs.getString("date"));

                ordersList.add(new Order(id, email, localDate));
            }

        } catch (SQLException e){
            throw new DatabaseException("Fejl i at hente ordrene fra "+ email + e.getMessage());
        }
        return ordersList;
    }


}


