package utilities;

import java.lang.Class;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class connects to the database using my assigned login details.
 */
public class DatabaseConnection {

    // JDBC URL parts
    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String serverAddress = "//localhost/";
    private static final String dbName = "client_schedule";

    // JDBC URL
    private static final String jdbcURL = protocol + vendorName + serverAddress + dbName + "?connectionTimeZone = SERVER"; // LOCAL

    // Driver reference
    private static final String SQLDriver = "com.mysql.cj.jdbc.Driver";

    // Connection Interface
    private static Connection conn = null;

    // Username
    private static final String username = "sqlUser";
    // Password
    private static final String password = "Passw0rd!";

    /**
     * Connects to the database using the java database URL and my login details.
     * @return sets the <code>Connection - conn</code> variable in this class so other methods can use it to access the
     * database
     */
    public static Connection startConnection() {
        try {
            Class.forName(SQLDriver);
            conn = DriverManager.getConnection(jdbcURL, username, password);

            System.out.println("Successfully connected to the database..");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * Closes the connection to the database.
     */
    public static void closeConnection() {
        try{
            conn.close();
        }
        catch (Exception e){
            // Do nothing
        }
    }

    /**
     * Retrieves the connection information so that other methods of other classes can use it to perform CRUD techniques
     * on the database.
     * @return the connection to the database
     */
    public static Connection getConnection() {
        return conn;
    }
}