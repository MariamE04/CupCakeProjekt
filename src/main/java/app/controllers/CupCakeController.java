package app.controllers;

import app.entities.*;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.CupcakeMapper;
import io.javalin.http.Context;

import java.util.List;


public class CupCakeController {
    private static ConnectionPool connectionPool;

    public CupCakeController(ConnectionPool connectionPool){
        this.connectionPool = connectionPool;
    }

    public void purchaseCupcake(Context ctx){
        User user = ctx.sessionAttribute("currentUser");
        Order order = ctx.sessionAttribute("currentOrder");

        try {
            CupcakeMapper.buyCupcake(order, user.getBalance(),user.getEmail(), connectionPool);
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
            ctx.render("Indtast HTML side");
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }
    public void showBottoms(Context ctx){
        try {
            List<Bottom> bottoms = CupcakeMapper.getBottom(connectionPool);
            ctx.attribute("bottoms", bottoms);
            ctx.render("Indtast HTML side");
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }
}
