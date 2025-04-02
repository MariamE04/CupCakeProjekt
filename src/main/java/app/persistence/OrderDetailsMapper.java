package app.persistence;

import app.entities.Order;
import app.entities.OrderDetails;
import app.exceptions.DatabaseException;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailsMapper {
private static ConnectionPool connectionPool;

public static void setConnectionPool(ConnectionPool newConnectionPool){
    connectionPool = newConnectionPool;

}

    public static List<OrderDetails> getOrderDetailsByOrder(int orderNumber) throws DatabaseException{
        List<OrderDetails>orderDetails = new ArrayList<>();
        String sql = "SELECT * FROM orderdetails JOIN orders ON orderdetails.order_nr = orders.order_nr WHERE orders.order_nr = ?";

        try(Connection connection = connectionPool.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setInt(1, orderNumber);

            ResultSet rs = ps.executeQuery();



           while(rs.next()){
               int id = rs.getInt("id");
               String topping = rs.getString("topping");
               String bottom = rs.getString("bottom");
               orderDetails.add(new OrderDetails(id, orderNumber, topping, bottom));
           }


        }catch (SQLException e){
            throw new DatabaseException("MISTAKE", e.getMessage());
        }
        return orderDetails;

    }
}
