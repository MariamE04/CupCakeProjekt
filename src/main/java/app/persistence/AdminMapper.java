package app.persistence;

import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminMapper {
    public static boolean checkAdmin(User user, ConnectionPool connectionPool) throws DatabaseException {
        if (user.getEmail().equals("olskeradmin@gmail.com") && (user.getPassword().equals("olsker123"))) {
            return user.setIsAdmin(true);
        } else return user.setIsAdmin(false);
    }
}

/* TODO LISTE | DANIEL |
*  Nr. 1 Lav currentUser i UserMapper (Hvis ikke Mariam har lavet det)
* Nr. 2 Lav en getAllUsers
* Nr. 3 Lav en getAllUserAndTheirOrders
* Nr.4 Indsæt  if-sætning i thymeleaf med currentUser.isAdmin så kun den person kan se ordre
*  Samt kunder + kundernes ordre
*     */
