package app.controllers;

import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.UserMapper;
import io.javalin.http.Context;

import java.util.logging.Logger;

public class HomeController {
    private static final Logger LOGGER = Logger.getLogger(HomeController.class.getName());
    private final ConnectionPool connectionPool;

    public HomeController(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public static int signUpUser(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        try {
            String email = ctx.formParam("email");
            String password = ctx.formParam("password");

            User user = new User(email, password);
            boolean userExists = UserMapper.userExists(user, connectionPool);

            if (userExists) {
                ctx.status(400).result("User already exists. Please log in.");
                return 0; // Indikerer at brugeren allerede findes

            } else {
                int result = UserMapper.signUp(email, password, 0, connectionPool);

                if (result == 1) {
                    User newUser = new User(email, password);
                    ctx.sessionAttribute("currentUser", newUser); // Gemmer hele User-objektet
                    ctx.attribute("message", "You have now been registered");
                    ctx.status(200).render("startpage.html");
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

    public static void userLogIn(Context ctx, ConnectionPool connectionPool) {
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");

        try {
            String user = UserMapper.logIn(email, password, connectionPool);

            if (user != null) {
                User loggedInUser = new User(email, password);
                ctx.sessionAttribute("currentUser", loggedInUser); // Gem bruger i session

                if (email.equals("admin@gmail.com")) {
                    ctx.sessionAttribute("admin", loggedInUser);
                    ctx.render("/adminSite.html");
                } else {
                    ctx.render("startpage.html");
                }
            } else {
                ctx.status(400).result("Incorrect email or password.");
            }
        } catch (DatabaseException e) {
            LOGGER.severe("Error during login: " + e.getMessage());
            ctx.status(500).result("Error during login.");
        }
    }
}
