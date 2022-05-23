package utilities;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class stores and retrieves SQL statements for methods in other classes to access the database.
 */
public class DatabaseQuery {

    private static Statement statement; // Statement reference

    /**
     * Creates a Statement object in this class and assigns an SQL statement to it.
     * @param connection to the database
     * @throws SQLException prevents app from crashing if incorrect SQL syntax is used
     */
    public static void setStatement(Connection connection) throws SQLException {
        statement = connection.createStatement();
    }

    /**
     * Retrieves the Statement object from this class
     * @return the Statement object
     */
    public static Statement getStatement() {
        return statement;
    }
}

