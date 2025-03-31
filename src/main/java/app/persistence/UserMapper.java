package app.persistence;


import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserMapper {

    //Dependency injection
    private static ConnectionPool connectionPool;

    public UserMapper(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public static int signUp(String email, String password, double balance) throws DatabaseException { //Statisk metode, så den kan kaldes uden at instantiere CupCakeMapper.
        User user = new User(email, password, balance); //objektet bruges senere til at indsætte data i databasen.

        String sql = "INSERT INTO users (email, password , balance) VALUES (?,?,?) ON CONFLICT (email) DO NOtHING"; //hvis emailen allerede findes, sker der ingenting

        try (
                Connection connection = connectionPool.getConnection(); //henter en forbindelse til databasen.
                PreparedStatement ps = connection.prepareStatement(sql); //indsætte værdier sikkert.
        ) {
            //Disse erstatter ? i SQL'en.
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.setDouble(3, user.getBalance());

            int rowsAffected = ps.executeUpdate(); //kører INSERT-sætningen
            return rowsAffected; //returnerer antal rækker der blev oprettet

        } catch (SQLException e) { //kastet med en fejlbesked.
            String msg = "There was an error during your sign-up for the workout log. Please try again.";
            throw new DatabaseException(msg, e.getMessage());
        }
    }

    public static String logIn(String email, String password) throws DatabaseException { //Statisk metode, så den kan kaldes uden at instantiere CupCakeMapper.
        User user = new User(email, password);

        String sql = "SELECT email , password FROM users WHERE email = ? AND password = ?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());

            ResultSet rs = ps.executeQuery(); //eksekverer SELECT-sætningen og får et ResultSet


            if (rs.next()) { //returnerer true, hvis der er en række (bruger findes).
                return rs.getString("email");
            } else {
                return null; // Forkert email eller kode -> returner null
            }

        } catch (SQLException e) {
            throw new DatabaseException("Login-error: try again", e.getMessage());
        }
    }

    public static boolean userExists(User user) throws DatabaseException {
        String sql = "SELECT 1 FROM users WHERE email = ?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ps.setString(1, user.getEmail());
            ResultSet rs = ps.executeQuery();
            return rs.next(); // Returnerer true hvis brugeren findes, ellers false

        } catch (SQLException e) {
            throw new DatabaseException("Error checking if user exists.", e.getMessage());
        }
    }


    public static ArrayList<User> getAllUsers(User user) throws DatabaseException {

        ArrayList<User> allUsers = new ArrayList<>();
        String sql = "SELECT * FROM public.users";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)) {
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                   String email = rs.getString("email");
                    String password = rs.getString("password");
                    int balance = rs.getInt("balance");

                    allUsers.add(new User(email, password, balance));
                }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl, kunne ikke hente alle kunderne |"+e.getMessage());
        }
        return allUsers;
    }



}
