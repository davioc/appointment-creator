package main;

import database_access.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utilities.DatabaseConnection;
import java.sql.SQLException;

/**
 * The program starts with this class. It pre-loads the information from the MySQL database so when the user logs in
 * there is no delay in loading the information.
 */
public class Main extends Application {
    /**
     * Launches the program and opens the Login Form. Pulls data from the MySQL database using SQL statements and sends
     * them to the classes within the "database_access" folder.
     * @param primaryStage loads the Login Form
     * @throws Exception prevents the app from crashing if the LoginPage fxml isn't found
     */
    @Override
    public void start(Stage primaryStage) throws Exception{

        // Connection to Database
        DatabaseConnection.startConnection();

        // Retrieve users table
        UsersAccess.getAllUsers();

        // Retrieve appointment table
        AppointmentsAccess.getAllAppointments();

        // Retrieve customers table
        CustomersAccess.getAllCustomers();

        // Retrieve contacts table
        ContactsAccess.getAllContacts();

        // Retrieve first level divisions table
        FirstLevelAccess.getAllFirstLevel();

        // Retrieve countries
        CountriesAccess.getAllCountries();

        // Opens login page
        Parent root = FXMLLoader.load(getClass().getResource("/view_controller/LoginPage.fxml"));
        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }

    /**
     * Takes in arguments from the <code>start()</code> method in this class to enable the Login Form to launch.
     * @param args the functions of the <code>start()</code> method
     * @throws SQLException prevents the app from crashing if incorrect syntax is used in the <code>start</code> method.
     */
    public static void main(String[] args) throws SQLException {
        launch(args);
    }
  }
