package app;

import app.config.SessionConfig;
import app.config.ThymeleafConfig;
import app.controllers.CupCakeController;
import app.controllers.HomeController;
import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;

public class Main {
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=public";
    private static final String DB = "cupcake";

    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);
    static HomeController homeController = new HomeController(connectionPool);

    public static void main(String[] args) {
        // Initializing Javalin and Jetty webserver

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");
            config.jetty.modifyServletContextHandler(handler ->  handler.setSessionHandler(SessionConfig.sessionConfig()));
            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));
        }).start(7070);

        CupCakeController cupCakeController = new CupCakeController(connectionPool);

        // Routing
        app.get("/", ctx -> ctx.redirect("/index"));
        app.get("/index", ctx -> ctx.render("index.html"));


        app.get("order", ctx -> ctx.render("admin.html"));
        app.get("createCupcake", ctx ->{
            cupCakeController.showBottoms(ctx);
            cupCakeController.showTopping(ctx);
        });

        app.get("startpage", ctx -> ctx.render("startpage.html"));
        app.get("cart", ctx -> ctx.render("cart.html"));


        // Rute til sign-up
        app.post("/signUp", ctx -> homeController.signUpUser(ctx));

        app.get("/signUp", ctx -> ctx.render("/signUp.html"));

        // Rute til login
        app.post("/login", ctx -> homeController.userLogIn(ctx));

        app.get("/login", ctx -> ctx.render("index.html"));
    }
}