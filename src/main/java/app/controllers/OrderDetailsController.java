package app.controllers;

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
        List<OrderDetails> orderDetails =  OrderDetailsMapper.getOrderDetailsByOrder();
        ctx.attribute("orderdetails", orderDetails);
        ctx.render("admin.html");
    }
}
