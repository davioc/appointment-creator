package view_controller;
import javafx.scene.control.Alert;
import database_access.*;
import database_objects.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import utilities.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.Month;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This class controls the Customer Form.
 */
public class HomeController implements Initializable {

    // Appointment Tableview
    //////////////////////////////////
    public TableView appointmentTable;
    public TableColumn appointmentIdColumn;
    public TableColumn titleCol;
    public TableColumn descriptionCol;
    public TableColumn locationCol;
    public TableColumn contactCol;
    public TableColumn typeCol;
    public TableColumn startCol;
    public TableColumn endCol;
    public TableColumn custIdCol;
    //////////////////////////////////

    // Customer Tableview
    /////////////////////////////////
    public TableView customerTable;
    public TableColumn customerCol;
    public TableColumn nameCol;
    public TableColumn addressCol;
    public TableColumn postalCol;
    public TableColumn phoneCol;
    public TableColumn countryCol;
    /////////////////////////////////

    // Reports
    /////////////////////////////////
    public ComboBox apptTypeBox;
    public ComboBox apptMonthBox;
    public ComboBox contacttBox;
    public ComboBox customersBox;
    public TextArea reportsField;
    public RadioButton apptRadioButton;
    public RadioButton cusRadioButton;
    public RadioButton conRadioButton;
    public Label alertLabel;
    /////////////////////////////////

    /**
     * Initializes the UI of the Home Page. It sets the tableview for Appointments and Customers. It also provides an
     * additional report section for users to view information on if a contact has an appointment.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Initializes Appointments tableview

        appointmentTable.setItems(AppointmentsAccess.getAppointmentlist());
        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("AppointmentID"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("Title"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("Location"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("Type"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("Start"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("End"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("Description"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("ContactID"));
        custIdCol.setCellValueFactory(new PropertyValueFactory<>("CustomerID"));

        // Initializes Customers tableview

        customerTable.setItems(CustomersAccess.getCustomerlist());
        customerCol.setCellValueFactory(new PropertyValueFactory<>("CustomerID"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("Address"));
        postalCol.setCellValueFactory(new PropertyValueFactory<>("PostalCode"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("Phone"));
        countryCol.setCellValueFactory(new PropertyValueFactory<>("DivisionID"));

        // Checks if there is an appointment within the next 15 min
        // To stop Appointment Spam alerts when navigating the rest of the app
        switch (AlertTracker.getAppointmentAlert()) {
            case FirstAlert:

                AlertTracker.setAppointmentAlert(AppointmentAlert.AlreadyAlerted);

                AppointmentsAccess.checkForAppointment();

                break;

            case AlreadyAlerted:

                // No more alerts triggered

                break;

        }


    }

    /**
     * Exits the program and closes the database connection.
     * @param actionEvent the Exit button is clicked
     */
    public void onExit(ActionEvent actionEvent) {
        DatabaseConnection.closeConnection();
        System.exit(0);
    }

    /**
     * Sets the <code>ENUM</code> Value ADD in the AddOrUpdate class so the Appointment Form loads empty fields for a new
     * appointment object.
     * @param actionEvent the Add button is clicked
     * @throws IOException prevents the app from crashing if the appointment fxml file isn't found.
     */
    public void onAddAppt(ActionEvent actionEvent) throws IOException {

        AddUpdate.setAddOrUpdate(AddOrUpdate.ADD);

        Parent root = FXMLLoader.load(getClass().getResource("/view_controller/AppointmentForm.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Add Appointment");
        stage.setScene(scene);
        stage.show();

    }

    /**
     * Deletes the selected appointment object in the Appointment Tableview on the Home Page.
     * @param actionEvent the Delete button is clicked
     */
    public void onDeleteAppt(ActionEvent actionEvent) {
        // Clear previous alert text
        alertLabel.setText("");
        // Checks if the users selected an appointment
        if (appointmentTable.getSelectionModel().isEmpty()) {
            alertLabel.setText("No appointment is selected to Delete");
//            System.out.println("No appointment is selected to Delete");
            return;
        }

        AppointmentsObj appointment = (AppointmentsObj) appointmentTable.getSelectionModel().getSelectedItem();
        // Lambda for quick message
        UtilityMessage msg = d -> d + appointment.getAppointmentID();


        Alert delete = new Alert(Alert.AlertType.CONFIRMATION);
        delete.setTitle("Appointments");
        delete.setHeaderText(msg.genMsg("Request to delete appointment: "));
        delete.setContentText("Click OK to delete or Cancel to go back.");

        delete.showAndWait().ifPresent((response -> {
            if (response == ButtonType.OK) {
                // button is clicked
                try {
                    AppointmentsAccess.deleteAppointment(appointment);
                    alertLabel.setText(msg.genMsg("Successfully deleted appointment ID: "));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }));



//        AppointmentsAccess.deleteAppointment((AppointmentsObj) appointmentTable.getSelectionModel().getSelectedItem());

    }

    /**
     * Grabs the selected Appointment Object and prepares it for the Appointment Form view. Temporary Contact/Customer
     * objects are created based on the IDs via their respective Access classes.
     *
     * @param actionEvent Update button is clicked.
     * @throws IOException prevents the app from crashing if incorrect SQL syntax is used.
     */
    public void onUpdateAppt(ActionEvent actionEvent) throws IOException {
        alertLabel.setText("");
        // Checks if the users selected an appointment
        if (appointmentTable.getSelectionModel().isEmpty()) {
            alertLabel.setText("No appointment is selected to Update");
            return;
        }

        // Changes the AddUpdate class variable to Update so the Appointment form knows to run the Update actions
        AddUpdate.setAddOrUpdate(AddOrUpdate.UPDATE);

        // Grabs the selected Appointment object, sets it in the Appointment class variable so the information can be pulled in the update form
        AppointmentsAccess.setAppointmentsObj((AppointmentsObj) appointmentTable.getSelectionModel().getSelectedItem());

        // Grabs the Contact and Customer IDs of the Appointment object and sends them to static methods to act as a temporary holder for the found objects
        ContactsAccess.findContact(((AppointmentsObj) appointmentTable.getSelectionModel().getSelectedItem()).getContactID());
        CustomersAccess.findCustomer(((AppointmentsObj) appointmentTable.getSelectionModel().getSelectedItem()).getCustomerID());


        Parent root = FXMLLoader.load(getClass().getResource("/view_controller/AppointmentForm.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Update Appointment");
        stage.setScene(scene);
        stage.show();

    }

    /**
     * Sets the <code>ENUM</code> Value ADD in the AddOrUpdate class so the Customer Form loads empty fields for a new
     * customer object.
     * @param actionEvent the Add button is clicked
     * @throws IOException prevents the app from crashing if the appointment fxml file isn't found.
     */
    public void onAddCus(ActionEvent actionEvent) throws IOException {

        AddUpdate.setAddOrUpdate(AddOrUpdate.ADD);

        Parent root = FXMLLoader.load(getClass().getResource("/view_controller/CustomerForm.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Add Customer");
        stage.setScene(scene);
        stage.show();

    }

    /**
     * Grabs the selected Customer Object and prepares it for the Customer Form view. Copy of the selection is sent to the
     * CustomerAccess class so that the information is displayed in the update form. The customer's Division ID is also looked
     * up in the FirstLevelAccess so that it's pre-selected when user loads the Update form.
     *
     * @param actionEvent Update button is clicked.
     * @throws IOException prevents the app from crashing if incorrect SQL syntax is used.
     */
    public void onUpdateCus(ActionEvent actionEvent) throws IOException {
        alertLabel.setText("");
        // Checks if the users selected a customer
        if (customerTable.getSelectionModel().isEmpty()) {
            alertLabel.setText("No customer is selected to Update");
            return;
        }

        AddUpdate.setAddOrUpdate(AddOrUpdate.UPDATE);

        // Grabs the selected Customer object, sets it in the Customer class variable so the information can be pulled in the update form
        CustomersAccess.setCustomersObj((CustomersObj) customerTable.getSelectionModel().getSelectedItem());

        // Grabs the selected Customer object's Division ID, and calls a method in the FirstLevelAccess Class to find the Country ID associated with it
        FirstLevelAccess.setFindFirst(((CustomersObj) customerTable.getSelectionModel().getSelectedItem()).getDivisionID());

        Parent root = FXMLLoader.load(getClass().getResource("/view_controller/CustomerForm.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Update Customer");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The selected customer object's ID is compared to the customer ID set in the Appointment Tableview. If the ID isn't
     * found then the customer object is deleted from the list and removed the mySQL database.
     *
     * @param actionEvent the Delete button is clicked.
     * @throws SQLException
     */
    public void onDeleteCus(ActionEvent actionEvent) throws SQLException {
        alertLabel.setText("");
        // Checks if the users selected an appointment
        if (customerTable.getSelectionModel().isEmpty()) {
            alertLabel.setText("No customer is selected to Delete");
            return;
        }

        // Creates a temporary Customer object to find the Customer ID
        CustomersObj customer = (CustomersObj) customerTable.getSelectionModel().getSelectedItem();
        int findCustomer = customer.getCustomerID();

        // Lambda for quick message
        UtilityMessage msg = d -> d + customer.getName();

        // Compares the customer ID with the ones in Appointment list, if found the customer isn't deleted
        for (int i = 0; i < AppointmentsAccess.getAppointmentlist().size(); i++) {
            if (findCustomer == AppointmentsAccess.getAppointmentlist().get(i).getCustomerID()) {

                alertLabel.setText(msg.genMsg("Failed to delete customer ") + " , they still have an appointment.");
//                System.out.println("Can't delete customer, they still have an appointment");
                return;
            }
        }

        Alert delete = new Alert(Alert.AlertType.CONFIRMATION);
        delete.setTitle("Customers");
        delete.setHeaderText(msg.genMsg("Request to delete customer: "));
        delete.setContentText("Click OK to delete or Cancel to go back.");

        delete.showAndWait().ifPresent((response -> {
            if (response == ButtonType.OK) {
                // button is clicked
                try {
                    CustomersAccess.deleteCustomer(customer);
                    alertLabel.setText(msg.genMsg("Successfully deleted customer: "));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }));

//        Optional<ButtonType> result = alert.showAndWait();
//        if (result.get() == ButtonType.OK) {
            // Selected Part is deleted
//            Inventory.deletePart(selection); // User selection is deleted
//            allPartsTable.setItems(Inventory.getAllParts()); // reset the table
//            searchPartText.clear(); // refresh search
//            userFeedBack.setText("Selected Part was successfully deleted.");
//            userFeedBack.setStyle("-fx-border-color: black; -fx-border-radius: 30px;");
//        } else {
//            // user chose CANCEL or closed the box
//        }

//        UtilityMessage msg = d -> "Customer: " + customer.getCustomerID() + d ;




//        System.out.println("Deletion successful");

    }

    /**
     * Returns the user back to the Login Page.
     * @param actionEvent the Logout button is clicked
     * @throws IOException prevents the app from closing if the login page fxml isn't found
     */
    public void onLogOut(ActionEvent actionEvent) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/view_controller/LoginPage.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();

    }

    /**
     * Returns the observableList by current week from the AppoinmentAccess class in the Appointment tableview if the Week radio button is
     * clicked.
     * @param actionEvent WEEK radio button is clicked.
     */
    public void onWeek(ActionEvent actionEvent) {

        appointmentTable.setItems(AppointmentsAccess.findWeek());
    }

    /**
     * Returns the observableList by current Month from the AppoinmentAccess class in the Appointment tableview if the Month radio button is
     * clicked.
     * @param actionEvent MONTH radio button is clicked.
     */
    public void onMonth(ActionEvent actionEvent) {

        appointmentTable.setItems(AppointmentsAccess.findMonth());
    }

    /**
     * Returns all objects from the AppoinmentAccess class in the Appointment tableview if the All radio button is
     * clicked.
     * @param actionEvent ALL radio button is clicked.
     */
    public void onAll(ActionEvent actionEvent) {
        appointmentTable.setItems(AppointmentsAccess.getAppointmentlist());

    }

    /**
     * Initializes the values of the combo boxes for Appointment Type - Reports. Calls the methods in the
     * AppointmentsAccess class to get the lists for the combo boxes.
     *
     * @param actionEvent Appointment Type radio button is clicked
     */
    public void onApptRadio(ActionEvent actionEvent) {

        // Set the type list into the combo box
        apptTypeBox.setItems(AppointmentsAccess.findAppointmentType());

        // set the month to a 12 month list in the combo box
        apptMonthBox.setItems(AppointmentsAccess.getMonths());
    }

    /**
     * Initializes the String values of "YES" or "NO"  to the combo boxes for Customers Scheduled - Reports. Calls the methods in the
     * AppointmentsAccess class to get the list for the combo boxes.
     *
     * @param actionEvent Customers Scheduled radio button is clicked
     */
    public void onCustomerRadio(ActionEvent actionEvent) {

        // Select true or false in the customer schedule box
        customersBox.setItems(AppointmentsAccess.isScheduled());

    }

    /**
     * Initializes the combo box with current Contact objects in the Contact Appointments - Reports. Calls the methods in the
     * ContactAccess class to get the list of contacts for the combo boxes.
     *
     * @param actionEvent Contact Appointments radio button is clicked
     */
    public void onContactRadio(ActionEvent actionEvent) {

        // Set status options in the customer combo box
        contacttBox.setItems(ContactsAccess.getContactsList());

    }

    /**
     * Looks in the Reports section for a selected radio button and their respective combo-box selections. If the radio and
     * respective combo-box(s) are selected, it calls methods in the AppointmentAccess class to search for the information and
     * return it as a report.
     * @param actionEvent the Generate button is clicked
     */
    public void onGenerate(ActionEvent actionEvent) throws SQLException {

        // List for finding an Appointment object and its values
        ObservableList<AppointmentsObj> aList = AppointmentsAccess.getAppointmentlist();

        // List for finding customers
        ObservableList<CustomersObj> cList = CustomersAccess.getCustomerlist();

        if (!apptRadioButton.isSelected() && !conRadioButton.isSelected() && !cusRadioButton.isSelected()) {
            reportsField.setText("A Report option hasn't been selected yet. Please select a Report option and try again.");
        }

        // Checks if Appointment Type radio button is selected and Type box is selected
        if (apptRadioButton.isSelected() && apptTypeBox.getSelectionModel().isEmpty()) {
//            System.out.println("Appointment Type selection is missing");
            reportsField.setText("Appointment Type selection is missing");
            return;
        }

        // Checks if Appointment Type radio button is selected and Month box is selected
        if (apptRadioButton.isSelected() && apptMonthBox.getSelectionModel().isEmpty()) {
//            System.out.println("Appointment Month selection is missing");
            reportsField.setText("Appointment Month selection is missing");

            return;
        }

        // Checks if radio button for type is selected and its respective combo boxes are chosen
        if (apptRadioButton.isSelected() && !apptTypeBox.getSelectionModel().isEmpty() && !apptMonthBox.getSelectionModel().isEmpty()) {
            String type = apptTypeBox.getSelectionModel().getSelectedItem().toString();
            Month month = (Month) apptMonthBox.getSelectionModel().getSelectedItem();

            // Sets value of selected month to int so it can be compared to Start time of the object
            int monthValue = month.getValue();

            int counter = 0;
            for (int i = 0; i < aList.size(); i++) {
                if (aList.get(i).getType().equals(type) && monthValue == aList.get(i).getStart().getMonthValue()) {
                    counter++;
                }
            }
            // Outputs the report information in the the Text Field box
            reportsField.setText("There is [" + counter + "] appointment(s) scheduled by type [" + type + "] for the month of [" + apptMonthBox.getSelectionModel().getSelectedItem().toString() + "].");

        }

        // Checks if Contacts radio button is selected and Contacts box is selected
        if (conRadioButton.isSelected() && contacttBox.getSelectionModel().isEmpty()) {
            reportsField.setText("Contact selection is missing");
            return;
        }

        // If Contacts radio button is selected and a contact is selected in the box , it looks
        if (conRadioButton.isSelected() && !contacttBox.getSelectionModel().isEmpty()) {
            ContactsObj contact = (ContactsObj) contacttBox.getSelectionModel().getSelectedItem();
            reportsField.setText("Contact [" + contact.getName() + "] has the following scheduled: \n");
            for (int i = 0; i < aList.size(); i++) {
                if (contact.getContactID() == aList.get(i).getContactID()) {
                    System.out.println(aList.get(i));
                    reportsField.appendText((aList.get(i) + "\n"));
                }


            }
        }

        // Checks if combo box for customers is empty
        if (cusRadioButton.isSelected() && customersBox.getSelectionModel().isEmpty()) {
            reportsField.setText("Customers selection is missing");
        }

        // Checks if the combo box for customers has data
        if (cusRadioButton.isSelected() && !customersBox.getSelectionModel().isEmpty()) {

            // Selection box is set to YES
            if (customersBox.getSelectionModel().getSelectedItem().equals("YES")) {
                reportsField.setText("The following customers have appointments scheduled: \n");
                for (int i = 0; i < cList.size(); i++) {
                   int cusID = cList.get(i).getCustomerID();
                   for (int k = 0; k < aList.size(); k++) {

                       if (cusID == aList.get(k).getCustomerID()) {

                           reportsField.appendText(cList.get(i) + " has an appointment?\n");

                           // To break the loop and move onto the next customer ID
                           break;
                       }
                   }
                }
            }

            ObservableList<CustomersObj> okay = cList;

            // Token for the program to know that the id has been found
            String yes = "yes";
            ArrayList<String> findIt = new ArrayList<>();

            // Selection box is set to NO
            if (customersBox.getSelectionModel().getSelectedItem().equals("NO")) {
                reportsField.setText("The following customers don't have an appointments scheduled: \n");

                // Loop through customers list
                for (int i = 0; i < cList.size(); i++) {
                    int id = cList.get(i).getCustomerID();

                    // Loop through appointment list
                    for (int j = 0; j < aList.size(); j++) {
                        int aID = aList.get(j).getCustomerID();

                        // Clears list so it doesn't get messy with each loop
                        findIt.clear();

                        // Compares current loop id to the appointment id
                        if (id == aID) {
                          findIt.add(yes);
                            break;
                        }
                    }

                    // If findIt is empty then the ID hasn't been scheduled and it's printed to the report
                    if (findIt.isEmpty()) {
                        reportsField.appendText(okay.get(i).toString() + "\n");
                    }
                }
            }
        }
    }
}
