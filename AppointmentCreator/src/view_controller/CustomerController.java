package view_controller;

import database_access.CountriesAccess;
import database_access.CustomersAccess;
import database_access.FirstLevelAccess;
import database_objects.CountriesObj;
import database_objects.FirstLevelObj;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import utilities.AddUpdate;
import utilities.UtilityMessage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * This class controls the Customer Form.
 */
public class CustomerController implements Initializable {

    public TextField custID;
    public TextField custPostal;
    public TextField custAddress;
    public TextField custName;
    public TextField custPhone;
    public ComboBox<CountriesObj> assignCountry;
    public ComboBox<FirstLevelObj> assignDivision;
    public Label formTitle;
    public Label errorMsg;

    /**
     * Initializes the data of the Customer Form so that the user can update or add a customer object
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        assignCountry.setItems(CountriesAccess.getCountriesList());
//        assignDivision.setItems(FirstLevelAccess.getFirstList());

        // checks if Add or Update was clicked
        switch (AddUpdate.getAddOrUpdate()) {
            case ADD:
                formTitle.setText("ADD");

                break;
            case UPDATE:
                formTitle.setText("UPDATE");
                assignDivision.setItems(FirstLevelAccess.getFirstList());

                String stringID = Integer.toString(CustomersAccess.getCustomersObj().getCustomerID());
                custID.setText(stringID);

                custName.setText(CustomersAccess.getCustomersObj().getName());
                custAddress.setText(CustomersAccess.getCustomersObj().getAddress());
                custPostal.setText(CustomersAccess.getCustomersObj().getPostalCode());
                custPhone.setText(CustomersAccess.getCustomersObj().getPhone());

                assignDivision.getSelectionModel().select(FirstLevelAccess.getFindFirst());
                assignCountry.getSelectionModel().select(CountriesAccess.getCountriesObj());

                break;
        }

    }

    /**
     * Checks if all fields of the Customer form are entered correctly. If fields are correct, the information is
     * passed to the CustomerAccess class and are added to the mySQL database. The user is returned to the Home Page
     * if successful.
     * @param actionEvent the Save button is clicked
     * @throws SQLException prevents app from crashing if incorrect SQL syntax is used
     * @throws IOException prevents app from crashing if the Home Page fxml file isn't found
     */
    public void onCustSave(ActionEvent actionEvent) throws IOException, SQLException {

        UtilityMessage msg = s -> s + " is missing.";

        if (custName.getText().isEmpty()){
            errorMsg.setText(msg.genMsg("Name"));
            return;
        }

        if (custAddress.getText().isEmpty()){
            errorMsg.setText(msg.genMsg("Address"));
            return;
        }

        if (custPostal.getText().isEmpty()){
            errorMsg.setText(msg.genMsg("Postal Code"));
            return;
        }

        if (custPhone.getText().isEmpty()){
            errorMsg.setText(msg.genMsg("Phone Number"));
            return;
        }

        if (assignCountry.getSelectionModel().isEmpty()) {
            errorMsg.setText("Please select a Country");
            return;
        }

        if (assignDivision.getSelectionModel().isEmpty()) {
            errorMsg.setText("Please select a Division");
            return;
        }

        switch (AddUpdate.getAddOrUpdate()) {
            case ADD:
                CustomersAccess.addCustomer(assignDivision.getSelectionModel().getSelectedItem().getDivisionID(), custPostal.getText(), custAddress.getText(),
                        custName.getText(), custPhone.getText());
                break;

            case UPDATE:
                CustomersAccess.updateCustomer(CustomersAccess.getCustomersObj().getCustomerID(),
                        assignDivision.getSelectionModel().getSelectedItem().getDivisionID(), custPostal.getText(), custAddress.getText(),
                        custName.getText(), custPhone.getText());

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
     * Quits the Customer Form and returns the user back to the Home Page.
     * @param actionEvent the Cancel button is clicked
     * @throws IOException prevents the app from crashing if the Home Page fxml file isn't found
     */
    public void onCustCancel(ActionEvent actionEvent) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/view_controller/HomePage.fxml"));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage .setTitle("Home");
        stage.setScene(scene);
        stage.show();

    }

    /**
     * Checks if the Country selection has been made. If it hasn't been made the Division list remains empty. If a country is
     * selected then the associated divisions of the Country are loaded in the FirstLevel <code>ComboBox</code> list.
     * @param mouseEvent the <code>ComboBox</code> for Division is clicked
     */
    public void onDivisionClicked(MouseEvent mouseEvent) {

        if (assignCountry.getSelectionModel().isEmpty()){
            errorMsg.setText("Please select a country first");
            return;
        }

        ObservableList<FirstLevelObj> associatedDivisions = FXCollections.observableArrayList();
        int found = assignCountry.getSelectionModel().getSelectedItem().getId();

        for (int i = 0; i < FirstLevelAccess.getFirstList().size(); i++) {
            if (found == FirstLevelAccess.getFirstList().get(i).getCountryID()) {
                associatedDivisions.add(FirstLevelAccess.getFirstList().get(i));
            }
        }
        assignDivision.setItems(associatedDivisions);

    }

    /**
     * Clears the Division selection so that if another country is selected, the list is prepared to load the new Divisions.
     * @param mouseEvent the Country <code>ComboBox</code> is clicked and resets the Division box.
     */
    public void onCountryClicked(MouseEvent mouseEvent) {

        assignDivision.getSelectionModel().clearSelection();

    }
}
