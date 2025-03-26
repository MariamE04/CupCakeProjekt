package app.persistence;

import app.entities.Cupcake;
import app.entities.Order;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class CupcakeMapper {

    public void buyCupcake(Order order, int price, int balance, String email, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "UPDATE users SET balance = ? WHERE email = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)){

            for (Cupcake cupcake : order.getCupcakes()) {
                price += price;
            }

            ps.setInt(1, price - balance);
            ps.setString(2, email);


        } catch (SQLException e){
            throw new DatabaseException("FEEEEJL", e.getMessage());
        }


    }

}







