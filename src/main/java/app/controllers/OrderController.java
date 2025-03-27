package app.controllers;

import app.entities.Order;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import io.javalin.http.Context;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class OrderController {
    private static ConnectionPool connectionPool;

    public OrderController(ConnectionPool connectionPool){
        this.connectionPool = connectionPool;
    }

    public void getOrdersByUser(Context ctx){
        //String email = ctx.sessionAttribute("currentUser");
        User user = ctx.sessionAttribute("currentUser");

        try {
            List<Order> orders = OrderMapper.getOrdersByEmail(user.getEmail(), connectionPool);
            ctx.attribute("orders", orders);
            ctx.render("Indtast HTML side");
        } catch (DatabaseException e) {
            ctx.attribute("message", "Fejl ved hentning af ordre til bruger: " + user.getEmail() + e.getMessage());
            throw new RuntimeException(e);
        }




    }

    public void getAllOrders(Context ctx) throws DatabaseException{
        List<Order> orders = new OrderMapper().getAllOrders(connectionPool);

        ctx.attribute("orders", orders);
        ctx.render("Indtast HTML side");

    }

    public void addOrderToUser(Context ctx){
        User user = ctx.sessionAttribute("currentUser");
        Order order = new Order(user.getEmail(), LocalDate.now());

        OrderMapper orderMapper = new OrderMapper();

        try {
            orderMapper.addOrder(order, connectionPool);
            ctx.attribute("message", "Ordrer tilføjet til bruger: " + user.getEmail());

        } catch (DatabaseException e) {
            ctx.attribute("message", "Fejl ved hentning af alle ordre " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}

