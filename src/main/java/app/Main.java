package app;

import app.config.SessionConfig;
import app.config.ThymeleafConfig;
import app.controllers.HomeController;
import app.persistence.ConnectionPool;
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

        // Routing

        app.get("/", ctx -> ctx.redirect("/index"));
        app.get("/index", ctx -> ctx.render("index.html"));


        app.get("order", ctx -> ctx.render("order.html"));
        app.get("createCupcake", ctx -> ctx.render("createcupcake.html"));
        app.get("startpage", ctx -> ctx.render("startpage.html"));


        // Rute til sign-up
        app.post("/signUp", ctx -> HomeController.signUpUser(ctx, connectionPool));

        app.get("/signUp", ctx -> {
            ctx.render("/signUp.html");
        });

        // Rute til login
        app.post("/login", ctx -> HomeController.userLogIn(ctx, connectionPool));

        app.get("/login", ctx -> {
            ctx.render("index.html");
        });
    }
}