package app;

import app.config.SessionConfig;
import app.config.ThymeleafConfig;
import app.controllers.CupCakeController;
import app.controllers.HomeController;
import app.controllers.OrderController;
import app.controllers.OrderDetailsController;
import app.entities.Order;
import app.persistence.*;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=public";
    private static final String DB = "cupcake";

    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);

    public static void main(String[] args) {
        // Initializing Javalin and Jetty webserver

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");
            config.jetty.modifyServletContextHandler(handler ->  handler.setSessionHandler(SessionConfig.sessionConfig()));
            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));
        }).start(7070);

        CupCakeController.setConnectionPool(connectionPool);
        CupcakeMapper.setConnectionPool(connectionPool);
        HomeController.setConnectionPool(connectionPool);
        UserMapper.setConnectionPool(connectionPool);
        OrderController.setConnectionPool(connectionPool);
        OrderMapper.setConnectionPool(connectionPool);
        OrderDetailsMapper.setConnectionPool(connectionPool);
        OrderDetailsController.setConnectionPool(connectionPool);

        // Routing
        app.get("/", ctx -> ctx.redirect("/index"));
        app.get("/index", ctx -> ctx.render("index.html"));



        //Rute til ordre
        app.get("admin", ctx -> OrderController.getAllOrders(ctx));

        //Rute til ordre-detaljer
        app.post("orderdetails", ctx -> OrderDetailsController.getOrderDetailsByOrderNumber(ctx));
        app.get("orderdetails", ctx -> ctx.render("orderdetails"));



        app.get("createCupcake", ctx ->{
            CupCakeController.showBottoms(ctx);
            CupCakeController.showTopping(ctx);
        });

        app.get("startpage", ctx -> ctx.render("startpage.html"));
        app.get("cart", ctx -> ctx.render("cart.html"));


        // Rute til sign-up
        app.post("/signUp", ctx -> HomeController.signUpUser(ctx));

        app.get("/signUp", ctx -> ctx.render("/signUp.html"));

        // Rute til login
        app.post("/login", ctx -> HomeController.userLogIn(ctx));



        app.get("/login", ctx -> ctx.render("index.html"));
    }
}