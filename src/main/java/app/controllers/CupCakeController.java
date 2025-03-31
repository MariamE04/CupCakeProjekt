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

    public CupCakeController(ConnectionPool connectionPool){
        this.connectionPool = connectionPool;
    }

    public void createCart(Context ctx){
        User user = ctx.sessionAttribute("currentUser");
        Order cart = new Order(user.getEmail(), LocalDate.now());

        ctx.attribute("cart", cart);
    }

    public void addToCart(Context ctx){
        Order cart = ctx.sessionAttribute("cart");
        if (cart == null)
            createCart(ctx);
        Topping topping = ctx.sessionAttribute("topping");
        Bottom bottom = ctx.sessionAttribute("bottom");

        cart.getCupcakes().add(new Cupcake(topping, bottom));
    }

    public void purchaseCart(Context ctx){
        User user = ctx.sessionAttribute("currentUser");
        Order cart = ctx.sessionAttribute("cart");

        try {
            CupcakeMapper.payForOrder(cart, user.getBalance(), user.getEmail(), connectionPool);
            OrderMapper.addOrder(cart, connectionPool);
            for (Cupcake cupcake : cart.getCupcakes()){
                //Mangler måde at afgøre kurvens order_nr
                OrderMapper.addOrderDetail(0, cupcake, connectionPool);
            }
            ctx.attribute("message", "Købet er gennemført");
            ctx.render("Indtast HTML side");
        } catch (DatabaseException e) {
            ctx.attribute("message", "Fejl ved køb af vare " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void showTopping(Context ctx){
        try {
            List<Topping> toppings = CupcakeMapper.getTopping(connectionPool);
            ctx.attribute("toppings", toppings);
            ctx.render("createcupcake.html");
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }
    public void showBottoms(Context ctx){
        try {
            List<Bottom> bottoms = CupcakeMapper.getBottom(connectionPool);
            ctx.attribute("bottoms", bottoms);
            ctx.render("createcupcake.html");
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }
}
