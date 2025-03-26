package app;

import app.config.SessionConfig;
import app.config.ThymeleafConfig;
<<<<<<< HEAD
import app.exceptions.DatabaseException;
=======
>>>>>>> a79232a515b55f28761a4e17d682f08770b50367
import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;

import java.util.logging.Logger;

import static app.mappers.CupcakeMapper.getBottom;
import static app.mappers.CupcakeMapper.getTopping;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
<<<<<<< HEAD
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

=======
>>>>>>> a79232a515b55f28761a4e17d682f08770b50367
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=public";
    private static final String DB = "cupcake";

    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);
<<<<<<< HEAD

    public static void main(String[] args)
    {
=======
    public static void main(String[] args) {
>>>>>>> a79232a515b55f28761a4e17d682f08770b50367
        // Initializing Javalin and Jetty webserver
        /*
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");
            config.jetty.modifyServletContextHandler(handler ->  handler.setSessionHandler(SessionConfig.sessionConfig()));
            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));
        }).start(7070);

        // Routing

        app.get("/", ctx ->  ctx.render("index.html"));
        */

        try{
            System.out.println(getBottom(connectionPool));
        } catch (DatabaseException e) {
            System.out.println(e.getMessage());
        }

    }
}