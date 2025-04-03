package app.persistence;

import app.entities.Cupcake;
import app.entities.Order;
import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

public class OrderMapper {
    private static ConnectionPool connectionPool;

    public static void setConnectionPool(ConnectionPool newConnectionPool){
        connectionPool = newConnectionPool;
    }

    public static void addOrder(Order order) throws DatabaseException{
        String sql = "INSERT INTO orders (email, date, price) VALUES(?,?,?)";

        try(Connection connection = connectionPool.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setString(1, order.getEmail());
            ps.setDate(2, Date.valueOf(order.getLocalDate()));
            ps.setDouble(3, order.getCupcakePrice());

            ps.executeUpdate();

        }catch (SQLException e){
            throw new DatabaseException("MEGET FEJL", e.getMessage());
        }

    }

    public static void addOrderDetail(int order_nr, Cupcake cupcake) throws DatabaseException {
        String sql = "INSERT INTO orderdetails (order_nr, topping, bottom) VALUES(?,?,?)";

        try(Connection connection = connectionPool.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setInt(1, order_nr);
            ps.setString(2, cupcake.getTopping().getName());
            ps.setString(3, cupcake.getBottom().getName());

            ps.executeUpdate();
        } catch (SQLException e){
            throw new DatabaseException(e.getMessage());
        }
    }

    public static int getLatestOrderNr() throws DatabaseException{
        String sql = "SELECT order_nr FROM orders ORDER BY order_nr DESC";
        int ordernumber;

        try(Connection connection = connectionPool.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)){
            ResultSet rs = ps.executeQuery();
            rs.next();
            ordernumber = rs.getInt("order_nr");

        }catch (SQLException e){
            throw new RuntimeException();
        }
        return ordernumber;
    }

    public static List<Order> getAllOrders() throws DatabaseException{
        String sql = "SELECT * FROM orders";
        List<Order> ordersList = new ArrayList<>();

        try(Connection connection = connectionPool.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)){
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int id = rs.getInt("order_nr");
                String email = rs.getString("email");
                LocalDate localDate = LocalDate.parse(rs.getString("date"));

                ordersList.add(new Order(id, email, localDate));
            }

        } catch (SQLException e){
            throw new DatabaseException("Fejl i at hente alle ordrene" + e.getMessage());
        }
        return ordersList;
    }

    public static List<Order> getOrdersByEmail(String email) throws DatabaseException{
        String sql = "SELECT * FROM orders WHERE email = ?";
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


