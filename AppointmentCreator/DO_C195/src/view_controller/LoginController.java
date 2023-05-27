package view_controller;



import database_access.UsersAccess;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utilities.AlertTracker;
import utilities.AppointmentAlert;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

/**
 * This class controls the login page.
 */
public class LoginController implements Initializable {

    public TextField loginName;
    public TextField password;
    public Label systemRegion;
    public Label userNameTxt;
    public Label userPasswordTxt;
    public Button loginText;
    public Label errorText;

    /**
     * Loads the interface for the Login page. Checks if the user's system language is French or English and
     * sets it to the one found.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ZoneId localID = ZoneId.systemDefault();

        systemRegion.setText("Location: " + localID);


        try {

            ResourceBundle rb = ResourceBundle.getBundle("utilities/Language", Locale.getDefault());

            if (Locale.getDefault().getLanguage().equals("fr")) {

                systemRegion.setText(rb.getString("Location") + " " + localID);
                userNameTxt.setText(rb.getString("Username"));
                userPasswordTxt.setText(rb.getString("Password"));
                loginText.setText(rb.getString("Login"));
                loginName.setPromptText(rb.getString("YourLogin"));
                password.setPromptText(rb.getString("YourPassword"));
            }
        }catch (Exception e) {
        // Language is something other than french so program continues on
        }

    }

    /**
     * Checks the users system for their default language. If French locale is detected, the login is changed to French.
     * Successful and failed logins are recorded in the 'login_report.txt' file in the 'files' folder.
     * @param actionEvent the login button is clicked
     * @throws IOException prevents the program from crashing if no report file is created or found in the 'files' folder.
     */
    public void onLoginButton(ActionEvent actionEvent) throws IOException {

        // Assign file name
        String loginReport = "AppointmentCreator/DO_C195/src/files/login_report.txt", item;

        // Create FileWriter object
        FileWriter loginWriter = new FileWriter(loginReport, true);

        // Create and Open file
        PrintWriter report = new PrintWriter(loginWriter);

        // Set local time
        LocalDateTime theTime = LocalDateTime.now();

        try {

            // Checks if user has French locale
            if (Locale.getDefault().getLanguage().equals("fr")) {

                // Get language resource
                ResourceBundle rb = ResourceBundle.getBundle("utilities/Language", Locale.getDefault());

               // Checks if username field is empty
               if (loginName.getText().isEmpty()) {
                   errorText.setText(rb.getString("FillOutUsername"));
                   return;
               }

               // Checks if password is empty
               if (password.getText().isEmpty()) {
                   errorText.setText(rb.getString("FillOutPassword"));
                   return;
               }

                // Checks is username and password match users in database
                if (!UsersAccess.verifyUserLogin(loginName.getText(), password.getText())) {
                    report.println("Attempted login by user [" + loginName.getText() +"] " + "at [" + theTime + "].");
                    errorText.setText(rb.getString("IncorrectLogin"));
                    System.out.println(loginName.getText() + " " + password.getText() + " not not found!");
                    report.close();
                    return;

                }

           }

            if (!Locale.getDefault().getLanguage().equals("fr")) {

                // Checks if username field is empty
                if (loginName.getText().isEmpty()) {
                    errorText.setText("Username is missing.");
                    return;
                }

                // Checks if password is empty
                if (password.getText().isEmpty()) {
                    errorText.setText("Password is missing");
                    return;
                }

                // Checks is username and password match users in database
                if (!UsersAccess.verifyUserLogin(loginName.getText(), password.getText())) {

                    ZoneId localZoneId = ZoneId.of((TimeZone.getDefault().getID()));
                    report.println("Failed Login: [" + loginName.getText() +"] " + "at [" + theTime + "] " + localZoneId.getDisplayName(TextStyle.FULL, Locale.ENGLISH));
                    errorText.setText("Incorrect Username or Password entered, please try again.");
                    report.close();
                    return;

                }

            }


        } catch (Exception e) {
            // Report if the program crashes
            e.printStackTrace();
        }



        // If login is verified, the 'Home Page' is opened
        if (!loginName.getText().isEmpty() & !password.getText().isEmpty()) {
            if (UsersAccess.verifyUserLogin(loginName.getText(), password.getText())){

                ZoneId localZoneId = ZoneId.of((TimeZone.getDefault().getID()));
                report.println("Successful Login: [" + loginName.getText() + "] logged in at [" + theTime + "] " + localZoneId.getDisplayName(TextStyle.FULL, Locale.ENGLISH));
                report.close();

                AlertTracker.setAppointmentAlert(AppointmentAlert.FirstAlert);

                Parent root = FXMLLoader.load(getClass().getResource("/view_controller/HomePage.fxml"));
                Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage .setTitle("Home");
                stage.setScene(scene);
                stage.show();

                }

            }
        }

}
