package app.controllers;

import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.CupCakeMapper;
import io.javalin.http.Context;

import java.util.logging.Logger;

public class HomeController {
    private static final Logger LOGGER = Logger.getLogger(HomeController.class.getName());
    private final ConnectionPool connectionPool;
    private User user;

    public HomeController(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public static int signUpUser(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        try {
            String email = ctx.formParam("email");
            String password = ctx.formParam("password");

            User user = new User(email, password);
            boolean userExists = CupCakeMapper.userExists(user, connectionPool);

            if (userExists) {
                ctx.status(400).result("User already exists. Please log in.");
                return 0; // Indikerer at brugeren allerede findes

            } else {
                int result = CupCakeMapper.signUp(email, password, 100, connectionPool);

                if (result == 1) {
                    ctx.status(200).redirect("/shopping");
                    return 1; // Indikerer succesfuld oprettelse
                } else {
                    ctx.status(400).result("Sign-up failed.");
                    return -1; // Indikerer fejl ved sign-up
                }
            }
        } catch (Exception e) {
            ctx.status(500).result("An error occurred: " + e.getMessage());
            return -2;
        }
    }
}
