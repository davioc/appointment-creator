package database_access;

import database_objects.AppointmentsObj;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import utilities.DatabaseConnection;
import utilities.DatabaseQuery;
import java.sql.*;
import java.time.*;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.TimeZone;

/**
 * This class is used to interact with the Appointments table in the MySQL database. It provides other classes and methods
 * access to the Appointment objects stored locally in the appointment <code>ObservableList</code>. It can add, update
 * and delete Appointment objects.
 */
public class AppointmentsAccess {

    static AppointmentsObj appointmentsObj;
    static ObservableList<AppointmentsObj> appointmentlist = FXCollections.observableArrayList();

    /**
     * Uses a <code>SELECT</code> SQL call to retrieve all objects from the appointment table in the MySQL database and
     * adds them to an <code>ObservableList</code>. It also
     * converts <code>TIMESTAMP</code> values to <code>LocalDateTime</code> and customer/contact IDs to <code>int</code>.
     */
    public static void getAllAppointments() {

        try {
            String sql = "SELECT * FROM appointments";

            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                int appointmentID = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String location = rs.getString("Location");
                String type = rs.getString("Type");

                // Converts start/end sql timestamp to local date time
                Timestamp startTS = rs.getTimestamp("Start");
                LocalDateTime startLDT = startTS.toLocalDateTime();

                Timestamp endTS = rs.getTimestamp("End");
                LocalDateTime endLDT = endTS.toLocalDateTime();

                String description = rs.getString("Description");
                int customerID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");
                int contactID = rs.getInt("Contact_ID");


                AppointmentsObj A = new AppointmentsObj(appointmentID, title, location, type, startLDT, endLDT, description, customerID, userID, contactID);
                appointmentlist.add(A);

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    /**
     * Returns a copy of the <code>ObservableList</code> "appoinmentlist" so that it can be viewed in the UI and modified
     * using CRUD techniques.
     * @return a copy of the <code>ObservableList</code>
     */
    public static ObservableList<AppointmentsObj> getAppointmentlist() {

        return appointmentlist;
    }

    //DISABLING NOT USED?
//    /**
//     * Used to return the
//     * @param appointmentID
//     * @return
//     */
//    public AppointmentsObj getAppointmentID(int appointmentID) {
//
//        return appointmentlist.get(appointmentID);
//    }

    /**
     * Takes in input from the Add Appointment form, turns it into a <code>INSERT</code> SQL statement, and adds it to the
     * Appointments table in the mySQL database.
     * @param contactID the contact ID
     * @param customerID the customer ID
     * @param title name of the appointment
     * @param description brief summary of the appointment
     * @param location the place of the appointment
     * @param type category of the appointment
     * @param startDate the Start Date day/month of the appointment (combined with startTime in LocalDateTime method)
     * @param startTime the Start Date hour/minutes of the appointment (combined with startDate in LocalDateTime method)
     * @param endDate the End Date day/month of the appointment (combined with endTime in LocalDateTime method)
     * @param endTime the End Date hour/minutes of the appointment (combined with endDate in LocalDateTime method)
     * @throws SQLException prevents app from crashing when incorrect SQL syntax is used
     */
    public static void addAppointment(int contactID, int customerID, String title, String description, String location, String type, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) throws SQLException {

        Connection conn = DatabaseConnection.getConnection();

        DatabaseQuery.setStatement(conn);

        Statement statement = DatabaseQuery.getStatement();

        // Convert startTime and endTime to UTC

        // Create UTC Zone ID
        ZoneId utcZoneID = ZoneId.of("UTC");

        // Create local zone id
        ZoneId localZoneId = ZoneId.of((TimeZone.getDefault().getID()));

        // Set local ZonedDateTime
        ZonedDateTime localStartTime = ZonedDateTime.of(startDate, startTime, localZoneId);
        ZonedDateTime localEndTime = ZonedDateTime.of(endDate, endTime, localZoneId);

        // To convert Start time to UTC
        ZonedDateTime localToStartUTC = localStartTime.withZoneSameInstant(utcZoneID);

        // To convert End time to UTC
        ZonedDateTime localToEndUTC = localEndTime.withZoneSameInstant(utcZoneID);

        // ZonedDateTime converted to LocalDateTime
        LocalDateTime startLDT = localToStartUTC.toLocalDateTime();

        // Set Timestamp for start based on the value of startLDT (UTC)
        Timestamp startTS = Timestamp.valueOf(startLDT);

        // ZonedDateTime converted to LocalDateTime
        LocalDateTime endLDT = localToEndUTC.toLocalDateTime();

        // Set Timestamp for end based on the value of endLDT (UTC)
        Timestamp endTS = Timestamp.valueOf(endLDT);

        String insertSQL = "INSERT INTO appointments(Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) " +
                "VALUES('" + title + "', '" +
                        description + "', '" +
                        location + "', '" +
                        type + "', '" +
                        startTS + "', '" +
                        endTS + "', '" +
                        customerID + "', '" +
                        UsersAccess.usersObj.getId() + "', '" +
                        contactID + "')";

        // EXECUTE statement
        statement.execute(insertSQL);

        // Confirm rows affected
        if(statement.getUpdateCount() > 0) {
            System.out.println(statement.getUpdateCount() + " rows(s) affected!");
            appointmentlist.clear();
            getAllAppointments();

        }
        else
            System.out.println("Nothing changed..");

    }

    /**
     * Updates the selected Appointment in the database. Also converts the users local time to UTC when updating.
     * @param appointmentID the appointment ID
     * @param contactID the contact ID
     * @param customerID the customer ID
     * @param title name of the appointment
     * @param description brief summary of the appointment
     * @param location the place of the appointment
     * @param type category of the appointment
     * @param startDate the Start Date day/month of the appointment (combined with startTime in LocalDateTime method)
     * @param startTime the Start Date hour/minutes of the appointment (combined with startDate in LocalDateTime method)
     * @param endDate the End Date day/month of the appointment (combined with endTime in LocalDateTime method)
     * @param endTime the End Date hour/minutes of the appointment (combined with endDate in LocalDateTime method)
     * @throws SQLException prevents app from crashing when incorrect SQL syntax is used
     */
    public static void updateAppointment(int appointmentID, int contactID, int customerID, String title, String description, String location, String type, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) throws SQLException {

        Connection conn = DatabaseConnection.getConnection();

        DatabaseQuery.setStatement(conn);

        Statement statement = DatabaseQuery.getStatement();

        // Convert startTime and endTime to UTC

        // Create UTC Zone ID
        ZoneId utcZoneID = ZoneId.of("UTC");

        // Create local zone id
        ZoneId localZoneId = ZoneId.of((TimeZone.getDefault().getID()));

        // Set local ZonedDateTime
        ZonedDateTime localStartTime = ZonedDateTime.of(startDate, startTime, localZoneId);
        ZonedDateTime localEndTime = ZonedDateTime.of(endDate, endTime, localZoneId);

        // To convert Start time to UTC
        ZonedDateTime localToStartUTC = localStartTime.withZoneSameInstant(utcZoneID);

        // To convert End time to UTC
        ZonedDateTime localToEndUTC = localEndTime.withZoneSameInstant(utcZoneID);

        // ZonedDateTime converted to LocalDateTime
        LocalDateTime startLDT = localToStartUTC.toLocalDateTime();

        // Set Timestamp for start based on the value of startLDT (UTC)
        Timestamp startTS = Timestamp.valueOf(startLDT);

        // ZonedDateTime converted to LocalDateTime
        LocalDateTime endLDT = localToEndUTC.toLocalDateTime();

        // Set Timestamp for end based on the value of endLDT (UTC)
        Timestamp endTS = Timestamp.valueOf(endLDT);

        // Update SQL statement for the appointments table
        String updateSQL = "UPDATE appointments " +
                "SET Title = '" + title + "', " +
                "Description = '" + description + "', " +
                "Location = '" + location + "', " +
                "Type = '" + type + "', " +
                "Start = '" + startTS + "', " +
                "End = '" + endTS + "', " +
                "Customer_ID = '" + customerID + "', " +
                "Contact_ID = '" + contactID + "' " +
                "WHERE Appointment_ID = " + appointmentID;

        // EXECUTE statement

        statement.execute(updateSQL);

        // Confirm rows affected
        if(statement.getUpdateCount() > 0) {
            System.out.println(statement.getUpdateCount() + " rows(s) affected!");
            appointmentlist.clear();
            getAllAppointments();

        }
        else
            System.out.println("Nothing changed..");

    }

    /**
     * Used to create a temporary static placeholder of an appointment object that is selected to be or was modified.
     * @param appointment the selected appointment object
     */
    public static void setAppointmentsObj(AppointmentsObj appointment) {
        appointmentsObj = appointment;
    }

    /**
     * Retrieves a copy of the Appointment object stored in this class. It's used as a placeholder when navigating the
     * other JavaFXML scenes.
     * @return appointment object to be loaded in new <code>scene</code>
     */
    public static AppointmentsObj getAppointmentObj() {
        return appointmentsObj;
    }

    /**
     * Uses a <code>DELETE</code> SQL call to delete selected object from the appointment table in the MySQL database by
     * appointment ID. If rows are updated in the MySQL database then the current appointment list is cleared and an updated
     * one is retrieved from the database.
     * @param appointmentsObj the selected appointment object to be deleted
     * @throws SQLException prevents crashes if incorrect SQL syntax is used
     */
    public static void deleteAppointment(AppointmentsObj appointmentsObj) throws SQLException {

        int found = appointmentsObj.getAppointmentID();

        Connection conn = DatabaseConnection.getConnection();

        DatabaseQuery.setStatement(conn);

        Statement statement = DatabaseQuery.getStatement();

        String deleteSQL = "DELETE FROM appointments WHERE Appointment_ID = " + found;

        statement.execute(deleteSQL);

        // Confirm rows affected
        if(statement.getUpdateCount() > 0) {
            System.out.println(statement.getUpdateCount() + " rows(s) affected!");
            appointmentlist.clear();
            getAllAppointments();

        }
        else
            System.out.println("Nothing changed..");

        }

    /**
     * Creates a temporary observable list based on the current months value. It compares it with the <code>ObservableList</code>
     * in this class.
     * @return a temporary, modified, copy of the <code>ObservableList</code> sorted by month/year
     */
    public static ObservableList<AppointmentsObj> findMonth() {

        ObservableList<AppointmentsObj> monthlist = FXCollections.observableArrayList();
        LocalDateTime month = LocalDateTime.now();
        int monthNumber = month.getMonthValue();
        int yearNumber = month.getYear();

        // Try statement in case there haven't been any appointments scheduled
        try {
            for (int i = 0; i < appointmentlist.size(); i++) {
                if (monthNumber == appointmentlist.get(i).getStart().getMonthValue())
                {
                    if (yearNumber == appointmentlist.get(i).getStart().getYear()){
                        monthlist.add(appointmentlist.get(i));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("No appointments have been scheduled, please try adding an appointment and try again");
        }

        return monthlist;
    }

    /**
     * Creates a temporary observable list based on the users system's <code>LocalDateTime.now()</code> value to find
     * appointments 7 days from now.
     * @return a temporary, modified, copy of the <code>ObservableList</code> filtered by appointments starting in 7 days
     */
    public static ObservableList<AppointmentsObj> findWeek() {

        LocalDateTime localWeekNow = LocalDateTime.now();
        LocalDateTime sevenDaysLater = localWeekNow.plusDays(7);

        ObservableList<AppointmentsObj> weeklist = FXCollections.observableArrayList();

        try {
            for (int i = 0; i < appointmentlist.size(); i++) {

                // if current week is before the listed appointment or is 7 days after, add to the list
                if (localWeekNow.isBefore(appointmentlist.get(i).getStart()) & sevenDaysLater.isAfter(appointmentlist.get(i).getStart())) {
                    weeklist.add(appointmentlist.get(i));
                }

            }
        } catch (Exception e) {
            System.out.println("No appointments have been scheduled, please try adding an appointment and try again");
        }

        return weeklist;

    }

    /**
     * [LAMBDA expression #1] - This lambda expression uses the <code>interface Consumer T</code> to shorten the method body. The Consumer
     * interface looks for an action. Clicking the <code>OK</code> button is the action and the parameter is
     * represented by the "response" value. If this Lambda wasn't here, more code would be needed in the body to define the alert
     * and the actions.
     * <p></p>
     *  Checks for upcoming appointments by using LocalDateTime values upon successful login by user.
     *  The appointment alert notifies the user if there is no upcoming appointment or if there is an upcoming appointment
     *  starting in the next 15 minutes.
     *  Two values are set, the time of now, and the time of now plus 15 minutes. LAMBDA is used for the alert button.
     */
    public static void checkForAppointment() {

        LocalDateTime timeOfNow = LocalDateTime.now();
        LocalDateTime checkForAppt = timeOfNow.plusMinutes(15);

        ObservableList<AppointmentsObj> weeklist = FXCollections.observableArrayList();

        Alert appointment = new Alert(Alert.AlertType.INFORMATION);

        try {
            for (int i = 0; i < appointmentlist.size(); i++) {
                if (appointmentlist.get(i).getStart().isAfter(timeOfNow) && appointmentlist.get(i).getStart().isBefore(checkForAppt)) {
//                    System.out.println("AppointmentID - " + appointmentlist.get(i).getAppointmentID() + " , has an appointment starting within 15 minutes " + " [" + appointmentlist.get(i).getStart() + "]");
                    weeklist.add(appointmentlist.get(i));

                    ZoneId localZoneId = ZoneId.of((TimeZone.getDefault().getID()));

                    appointment.setTitle("APPOINTMENT ALERT");
                    appointment.setHeaderText("Upcoming Appointment in 15 minutes:");
                    appointment.setContentText("Appointment ID: [" + appointmentlist.get(i).getAppointmentID() + "]\n" + "Scheduled time: [" + appointmentlist.get(i).getStart() + "] " + localZoneId.getDisplayName(TextStyle.FULL, Locale.ENGLISH));

                    appointment.showAndWait().ifPresent((response -> {
                        if (response == ButtonType.OK) {
                            // The Ok button is clicked [LAMBDA]
                        }
                    }));

                }
            }
            if (weeklist.isEmpty()) {

                appointment.setTitle("APPOINTMENT ALERT");
                appointment.setHeaderText("There are no upcoming appointments in the next 15 minutes");

                appointment.showAndWait().ifPresent((response -> {
                    if (response == ButtonType.OK) {
                        // button is clicked
                    }
                }));

            }
        }catch (Exception e) {
            System.out.println("No Appointments have been set");
        }

    }

    /**
     * Creates a <code>String</code> observable list of Type values from the appointmentlist variable in this class. It
     * goes through the list and adds only unique types to the list so it can be used in a search for Reports on the Home page.
     * @return <code>String ObservableList</code> of unique Types
     */
    public static ObservableList<String> findAppointmentType() {

        // String list to add to the Reports - Type combo box
        ObservableList<String> typeList = FXCollections.observableArrayList();

        // Copy of the appointmentlist so it's easier to look at in the For statement
        ObservableList<AppointmentsObj> type = appointmentlist;

        // assigns
        String newType;

        for (int i = 0; i < type.size(); i++) {
            newType = type.get(i).getType();
            if (!typeList.contains(newType)) {
                typeList.add(newType);
            }

        }

        return typeList;

    }

    /**
     * Creates a <code>ObservableList</code> of months from values of the <code>Month</code> class so it can viewed in the
     * UI on the home page in the reports section.
     * @return <code>ObservableList</code> of months
     */
    public static ObservableList<Month> getMonths() {

        ObservableList<Month> months = FXCollections.observableArrayList();
        months.add(Month.JANUARY);
        months.add(Month.FEBRUARY);
        months.add(Month.MARCH);
        months.add(Month.APRIL);
        months.add(Month.MAY);
        months.add(Month.JUNE);
        months.add(Month.JULY);
        months.add(Month.AUGUST);
        months.add(Month.SEPTEMBER);
        months.add(Month.OCTOBER);
        months.add(Month.NOVEMBER);
        months.add(Month.DECEMBER);

        return months;

    }

    /**
     * Creates a <code>ObservableList</code> of <code>String</code> values, "Yes"/"No to be selections in the reports
     * section - "Customer Scheduled" of the home page.
     * @return <code>ObservableList</code> of "Yes"/"No" values for users to select on the home page
     */
    public static ObservableList<String> isScheduled() {

        ObservableList<String> checkList = FXCollections.observableArrayList();
        checkList.add("YES");
        checkList.add("NO");

        return checkList;
    }
}
