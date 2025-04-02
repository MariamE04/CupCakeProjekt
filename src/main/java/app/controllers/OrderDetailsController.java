package app.controllers;

import app.entities.Order;
import app.entities.OrderDetails;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderDetailsMapper;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailsController {
    private static ConnectionPool connectionPool;

    public static void setConnectionPool(ConnectionPool newConnectionPool){
        connectionPool = newConnectionPool;
    }

    public static void getOrderDetailsByOrderNumber(Context ctx) throws DatabaseException{

        int orderNumber = Integer.parseInt(ctx.formParam("orderNumber"));
        List<OrderDetails> orderDetails =  OrderDetailsMapper.getOrderDetailsByOrder(orderNumber);
        ctx.sessionAttribute("orderDetails", orderDetails);
        ctx.render("orderdetails");
        for (OrderDetails orderd: orderDetails){
            System.out.println(orderd);
        }
    }
}
