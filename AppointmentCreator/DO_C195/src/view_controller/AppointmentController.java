package view_controller;


import database_access.AppointmentsAccess;
import database_access.ContactsAccess;
import database_access.CustomersAccess;
import database_objects.AppointmentsObj;
import database_objects.ContactsObj;
import database_objects.CustomersObj;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import utilities.AddUpdate;
import utilities.UtilityMessage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

/**
 * This class controls the Appointment Form.
 */
public class AppointmentController implements Initializable {

    public TextField appointmentID;
    public ComboBox<ContactsObj> assignContact;
    public ComboBox<CustomersObj> assignCustomer;
    public TextField apptType;
    public TextField apptLocation;
    public TextField apptDescription;
    public TextField apptTitle;

    public DatePicker pickerSD;
    public ComboBox startTime;
    public ComboBox endTime;
    public DatePicker pickerED;
    public Label formTitle;
    public Label timeZoneLabel;
    public Label timeZoneLabel1;
    public Label errorMsg;

    /**
     * Initializes the data of the Appointment Form so that the user can update or add an appointment object and converts
     * the time from EST to the users local time if it's different.
     * It uses <code>ENUM</code> values ADD or UPDATE to determine which actions to take.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Retrieves contacts list for the contacts combo box
        assignContact.setItems(ContactsAccess.getContactsList());

        // Retrieves customer list for the customer combo box
        assignCustomer.setItems(CustomersAccess.getCustomerlist());

        // Creating local EST business hours from 8AM - 10PM
        LocalDate easternDate = LocalDate.of(2021, 9, 01);
        LocalTime easternTimeStart = LocalTime.of(8, 00);
        LocalTime easternTimeEnd = LocalTime.of(22,00);

        // Create EST Zone ID
        ZoneId easternZoneID = ZoneId.of("America/New_York");

        // Create UTC Zone ID
        ZoneId utcZoneID = ZoneId.of("UTC");

        // Create Local Zone ID based on the users system time
        ZoneId localZoneId = ZoneId.of((TimeZone.getDefault().getID()));

        // Reminds the user what timezone they are setting it in
        timeZoneLabel.setText(localZoneId.getDisplayName(TextStyle.FULL, Locale.ENGLISH));
        timeZoneLabel1.setText(localZoneId.getDisplayName(TextStyle.FULL, Locale.ENGLISH));

        // Set EST ZoneDateTimes for Start and End times using the EST Business hours
        ZonedDateTime easternZDT = ZonedDateTime.of(easternDate, easternTimeStart, easternZoneID);
        ZonedDateTime easternZDTend = ZonedDateTime.of(easternDate, easternTimeEnd, easternZoneID);

        // To convert EST Start time to users local zone
        ZonedDateTime easternToLocalZDT = easternZDT.withZoneSameInstant(localZoneId);

        // To convert EST End time to users local zone
        ZonedDateTime easternToLocalZDTEnd = easternZDTend.withZoneSameInstant(localZoneId);

        // To convert local user time to UTC
//        ZonedDateTime localTimeToUtcS = easternToLocalZDT.withZoneSameInstant(utcZoneID);
//        ZonedDateTime localTimeToUtcE = easternToLocalZDTEnd.withZoneSameInstant(utcZoneID);

        // Substring the Start/End times so they can be displayed as the users time in the UI
        String parseTime = String.valueOf(easternToLocalZDT.toLocalTime());
        String parseTimeEnd = String.valueOf(easternToLocalZDTEnd.toLocalTime());

        // Substring of the start hour and converts to int
        String hourS = parseTime.substring(0, 2);
        int startHour = Integer.parseInt(hourS);

        // Substring of the end hour and converts to int
        String hourE = parseTimeEnd.substring(0, 2);
        int endHour = Integer.parseInt(hourE);

        // Setting time parameters for Start/End times

        LocalTime start = LocalTime.of(startHour,0);
        LocalTime end = LocalTime.of(endHour,0);

        // Initializes the start/end time combo boxes
        while (start.isBefore(end.plusSeconds(1))) {
            startTime.getItems().add(start);
            endTime.getItems().add(start);
            start = start.plusMinutes(15);
        }

        startTime.getSelectionModel().select(LocalTime.of(startHour, 0));
        endTime.getSelectionModel().select(LocalTime.of(startHour, 0));

        // checks if Add or Update was clicked
        switch (AddUpdate.getAddOrUpdate()) {
            case ADD:
                formTitle.setText("ADD");
                break;
            case UPDATE:
                formTitle.setText("UPDATE");

                // Converts appointment ID to string so it can be viewed in form
                String stringID = Integer.toString(AppointmentsAccess.getAppointmentObj().getAppointmentID());
                appointmentID.setText(stringID);

                // Grabs the selected contact object's Contact and Customer IDs found in the Customer Tableview and sets it as pre-selected in the combo box
                assignContact.getSelectionModel().select(ContactsAccess.getContactsObj());
                assignCustomer.getSelectionModel().select(CustomersAccess.getCustomersObj());

                // Sets the text fields of the selected object
                apptTitle.setText(AppointmentsAccess.getAppointmentObj().getTitle());
                apptDescription.setText(AppointmentsAccess.getAppointmentObj().getDescription());
                apptLocation.setText(AppointmentsAccess.getAppointmentObj().getLocation());
                apptType.setText(AppointmentsAccess.getAppointmentObj().getType());

                pickerSD.setValue(AppointmentsAccess.getAppointmentObj().getStart().toLocalDate());
                startTime.setValue(AppointmentsAccess.getAppointmentObj().getStart().toLocalTime());

                pickerED.setValue(AppointmentsAccess.getAppointmentObj().getEnd().toLocalDate());
                endTime.setValue(AppointmentsAccess.getAppointmentObj().getEnd().toLocalTime());

                break;
        }
    }

    /**
     * Checks if all fields of the Appointment form are entered correctly. If fields are correct, the information is
     * passed to the AppointmentAccess class and are added to the mySQL database. The user is returned to the Home page
     * if successful.
     * <p></p>
     * [LAMBDA expression #2] Is used in this class to reduce redundant string text when an error message is displayed to the user. For
     * example, each field in this class will have an error displaying, "is missing" along with the name of the field. To
     * prevent a clutter of String values repeating the same thing, used a lambda so that only new String needed is
     * the name of the field. Another example: "Contact"[new string passed in] + "is missing"[from lambda].
     * @param actionEvent the Save button is clicked
     * @throws SQLException prevents app from crashing if incorrect SQL syntax is used
     * @throws IOException prevents app from crashing if the Home Page fxml file isn't found
     */
    public void onApptSave(ActionEvent actionEvent) throws SQLException, IOException {

        // Passes the name of the field as a parameter and adds "is missing" to the end of the string
        UtilityMessage msg = k -> k + " is missing.";

        // Passes the name of the field as a parameter and adds "overlaps.." to the end of the string
        UtilityMessage aMsg = s -> s + " overlaps with appointment ID: ";

        if (assignContact.getSelectionModel().isEmpty()){
            errorMsg.setText(msg.genMsg("Contact"));
            return;
        }

        if (assignCustomer.getSelectionModel().isEmpty()){
            errorMsg.setText(msg.genMsg("Customer"));
            return;
        }

        if (apptTitle.getText().isEmpty()){
            errorMsg.setText(msg.genMsg("Title"));
            return;
        }
        if (apptDescription.getText().isEmpty()){
            errorMsg.setText(msg.genMsg("Description"));
            return;
        }
        if (apptLocation.getText().isEmpty()){
            errorMsg.setText(msg.genMsg("Location"));
            return;
        }
        if (apptType.getText().isEmpty()){
            errorMsg.setText(msg.genMsg("Type"));
            return;
        }

        try {

            String startdate = pickerSD.getValue().toString();
            if (!startdate.isEmpty()){
                // continue
            }

        }catch (Exception e) {
            errorMsg.setText(msg.genMsg("Start Date"));
            return;
        }

        try {

            String enddate = pickerED.getValue().toString();
            if (enddate.isEmpty()) {
                // continue
            }
        }catch (Exception e) {
            errorMsg.setText(msg.genMsg("End Date"));
            return;
        }

        if (pickerSD.getValue().isAfter(pickerED.getValue())) {
            errorMsg.setText("Start date can't be set after the End date");
            return;
        }

        // converts the DateTime start/end to string to compare end time to start time
        try {

            String startdate = pickerSD.getValue().toString();
            String enddate = pickerED.getValue().toString();

            String starttime = startTime.getValue().toString().replace(":","");
            int st = Integer.parseInt(starttime);

            String endtime = endTime.getValue().toString().replace(":", "");
            int et = Integer.parseInt(endtime);

            if (startdate.equals(enddate) & et <= st) {
                errorMsg.setText("End time can't be the same or before the Start time when scheduled on the same day");
                return;
            }

            if (!startdate.equals(enddate) & et <= st) {
                // continue
            }

        }catch (Exception e) {
            // Out of bounds error happened
            errorMsg.setText("something went wrong");
            e.printStackTrace();
            return;
        }


        switch (AddUpdate.getAddOrUpdate()) {


            case ADD:
            // Check for overlapping schedules
            try {

                // Create local zone id
                ZoneId localZoneId = ZoneId.of((TimeZone.getDefault().getID()));

                // Set local ZonedDateTime
                ZonedDateTime localStartTime = ZonedDateTime.of(pickerSD.getValue(), (LocalTime) startTime.getValue(), localZoneId);
                ZonedDateTime localEndTime = ZonedDateTime.of(pickerED.getValue(), (LocalTime) endTime.getValue(), localZoneId);

                LocalDateTime ldtStart = localStartTime.toLocalDateTime();
                LocalDateTime ldtEnd = localEndTime.toLocalDateTime();



                for (int i = 0; i < AppointmentsAccess.getAppointmentlist().size(); i++) {
                    AppointmentsObj apptObj = AppointmentsAccess.getAppointmentlist().get(i);

                    if (apptObj.getStart().equals(ldtStart)) {
                        errorMsg.setText(aMsg.genMsg("Start Date/Time") + apptObj.getAppointmentID());

                        return;
                    }

                    if (apptObj.getStart().isAfter(ldtStart) && apptObj.getStart().isBefore(ldtEnd)) {
                        errorMsg.setText(aMsg.genMsg("End Date/Time") + apptObj.getAppointmentID());

                        return;
                    }


                }


            } catch (Exception e) {

            }

            break;

            case UPDATE:

                try {

                    // Create local zone id
                    ZoneId localZoneId = ZoneId.of((TimeZone.getDefault().getID()));

                    // Set local ZonedDateTime
                    ZonedDateTime localStartTime = ZonedDateTime.of(pickerSD.getValue(), (LocalTime) startTime.getValue(), localZoneId);
                    ZonedDateTime localEndTime = ZonedDateTime.of(pickerED.getValue(), (LocalTime) endTime.getValue(), localZoneId);

                    LocalDateTime ldtStart = localStartTime.toLocalDateTime();
                    LocalDateTime ldtEnd = localEndTime.toLocalDateTime();

                    for (int i = 0; i < AppointmentsAccess.getAppointmentlist().size(); i++) {
                        AppointmentsObj apptObj = AppointmentsAccess.getAppointmentlist().get(i);


                        if (apptObj.getAppointmentID() == Integer.parseInt(appointmentID.getText())) {
                            // Allows appointment id to be updated if the date and time are the same
//                            System.out.println("It's fine to update if it's the same appointment id: " + apptObj.getAppointmentID());

                        }

                        else  if (apptObj.getAppointmentID() != Integer.parseInt(appointmentID.getText())) {

                            if (apptObj.getStart().equals(ldtStart)) {
                                errorMsg.setText(aMsg.genMsg("Start Date/Time") + apptObj.getAppointmentID());

                                return;
                            }

                            if (apptObj.getStart().isAfter(ldtStart) && apptObj.getStart().isBefore(ldtEnd)) {
                                errorMsg.setText(aMsg.genMsg("End Date/Time") + apptObj.getAppointmentID());

                                return;
                            }

                        }




                    }


                } catch (Exception e) {

                }
        }

        switch (AddUpdate.getAddOrUpdate()) {
            case ADD:
                    AppointmentsAccess.addAppointment(assignContact.getSelectionModel().getSelectedItem().getContactID(),
                    assignCustomer.getSelectionModel().getSelectedItem().getCustomerID(),
                    apptTitle.getText(), apptDescription.getText(), apptLocation.getText(),
                    apptType.getText(), pickerSD.getValue(), (LocalTime) startTime.getValue(),
                    pickerED.getValue(), (LocalTime) endTime.getValue());
                    break;

            case UPDATE:
                        AppointmentsAccess.updateAppointment(AppointmentsAccess.getAppointmentObj().getAppointmentID(),
                        assignContact.getSelectionModel().getSelectedItem().getContactID(), assignCustomer.getSelectionModel().getSelectedItem().getCustomerID(),
                        apptTitle.getText(), apptDescription.getText(), apptLocation.getText(),
                        apptType.getText(), pickerSD.getValue(), (LocalTime) startTime.getValue(),
                        pickerED.getValue(), (LocalTime) endTime.getValue());
                        break;
        }
        Parent root = FXMLLoader.load(getClass().getResource("/view_controller/HomePage.fxml"));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage .setTitle("Home");
        stage.setScene(scene);
        stage.show();


    }

    /**
     * Quits the appointment form and returns the user back to the Home Page.
     * @param actionEvent the Cancel button is clicked.
     * @throws IOException prevents the app from crashing if the Home Page fxml file isn't found
     */
    public void onApptCancel(ActionEvent actionEvent) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/view_controller/HomePage.fxml"));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage .setTitle("Home");
        stage.setScene(scene);
        stage.show();

    }
}
