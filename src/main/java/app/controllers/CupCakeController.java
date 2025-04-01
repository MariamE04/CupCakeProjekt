package app.controllers;

import app.entities.*;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.CupcakeMapper;
import app.persistence.OrderMapper;
import io.javalin.http.Context;

import java.time.LocalDate;
import java.util.List;


public class CupCakeController {
    private static ConnectionPool connectionPool;

    public static void setConnectionPool(ConnectionPool newConnectionPool){
        connectionPool = newConnectionPool;
    }

    public static Order createCart(Context ctx){
        User user = ctx.sessionAttribute("currentUser");
        Order cart = new Order(user.getEmail(), LocalDate.now());

        ctx.sessionAttribute("cart", cart);
        return cart;
    }

    public static void addToCart(Context ctx){
        Order cart = ctx.sessionAttribute("cart");
        if (cart == null) {
            cart = createCart(ctx);
        }
        try {
            Topping topping = CupcakeMapper.getChosenTopping(ctx.formParam("topping"));
            Bottom bottom = CupcakeMapper.getChosenBottom(ctx.formParam("bottom"));
            cart.getCupcakes().add(new Cupcake(topping, bottom));
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
        ctx.render("startpage.html");
    }

    public static void purchaseCart(Context ctx){
        User user = ctx.sessionAttribute("currentUser");
        Order cart = ctx.sessionAttribute("cart");

        try {
            CupcakeMapper.payForOrder(cart, user.getBalance(), user.getEmail());
            OrderMapper.addOrder(cart);
            for (Cupcake cupcake : cart.getCupcakes()){
                //Mangler måde at afgøre kurvens order_nr
                OrderMapper.addOrderDetail(0, cupcake);
            }
            ctx.attribute("message", "Købet er gennemført");
            ctx.render("startpage.html");
        } catch (DatabaseException e) {
            ctx.attribute("message", "Fejl ved køb af vare " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static void showTopping(Context ctx){
        try {
            List<Topping> toppings = CupcakeMapper.getTopping();
            ctx.attribute("toppings", toppings);
            ctx.render("createcupcake.html");
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }
    public static void showBottoms(Context ctx){
        try {
            List<Bottom> bottoms = CupcakeMapper.getBottom();
            ctx.attribute("bottoms", bottoms);
            ctx.render("createcupcake.html");
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }
}
