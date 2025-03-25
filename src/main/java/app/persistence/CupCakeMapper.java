package app.persistence;


import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CupCakeMapper {

    public static int signup(String email, String password, double balance, ConnectionPool connectionPool ) throws DatabaseException {
        User user = new User(email, password, balance); //objektet bruges senere til at indsætte data i databasen.

        String sql = "INSERT INTO users (email, password , balance) VALUES (?,?,?) ON CONFLICT (email) DO NOtHING"; //hvis emailen allerede findes, sker der ingenting

        try(
                Connection connection = connectionPool.getConnection(); //henter en forbindelse til databasen.
                PreparedStatement ps = connection.prepareStatement(sql); //indsætte værdier sikkert.
                )
        {
            //Disse erstatter ? i SQL'en.
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.setDouble(3,user.getBalance());

            int rowsAffected = ps.executeUpdate(); //kører INSERT-sætningen
            return rowsAffected; //returnerer antal rækker der blev oprettet

        } catch (SQLException e) { //kastet med en fejlbesked.
            String msg = "There was an error during your sign-up for the workout log. Please try again.";
            throw new DatabaseException(msg, e.getMessage());
        }
    }

    public static String logIn(String email, String password, ConnectionPool connectionPool ) throws DatabaseException { //Statisk metode, så den kan kaldes uden at instantiere CupCakeMapper.
        User user = new User(email, password,0);

        String sql = "SELECT email , password FROM users WHERE email = ? AND password = ?";

        try(
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
                )
        {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());

            ResultSet rs = ps.executeQuery(); //eksekverer SELECT-sætningen og får et ResultSet


            if(rs.next()){ //returnerer true, hvis der er en række (bruger findes).
                return rs.getString("email");
            } else {
                throw new DatabaseException("Incorrect email or password. Please try again.");
            }

        } catch (SQLException e) {
            throw new DatabaseException("Login-error: try again", e.getMessage());
        }

    }
}
