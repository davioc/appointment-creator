package database_access;

import database_objects.CustomersObj;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utilities.DatabaseConnection;
import utilities.DatabaseQuery;
import java.sql.*;

/**
 * This class is used to interact with the customers table in the MySQL database. It provides other classes and methods
 * access to the Customer objects stored locally in the customerList <code>ObservableList</code>. It can add, update
 * and delete customer objects.
 */
public class CustomersAccess {

    static CustomersObj customersObj;
//    static int customerID;
    static ObservableList<CustomersObj> customerlist = FXCollections.observableArrayList();

    /**
     * Uses a <code>SELECT</code> SQL call to retrieve all objects from the customers table in the MySQL database and
     * adds them to an <code>ObservableList</code>.
     */
    public static void getAllCustomers() {

        try {
            String sql = "SELECT * FROM customers";

            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int customerID = rs.getInt("Customer_ID");
                String name = rs.getString("Customer_Name");
                String address = rs.getString("Address");
                String postalCode = rs.getString("Postal_Code");
                String phone = rs.getString("Phone");
                int divisionID = rs.getInt("Division_ID");

                CustomersObj C = new CustomersObj(customerID, divisionID, postalCode, address, name, phone);
                customerlist.add(C);
//                System.out.println("Added customer: " + customerID + " " + name + " "
//                        + address + " " + postalCode + " " + phone + " " + divisionID + "." +
//                        ".");

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

    /**
     * Returns a copy of the <code>ObservableList</code> "customerList" so that it can be viewed in the UI and modified
     * using CRUD techniques.
     * @return a copy of the <code>ObservableList</code>
     */
    public static ObservableList<CustomersObj> getCustomerlist() {

        return customerlist;
    }

    /**
     * Takes in input from the Add Customer form, turns it into a <code>INSERT</code> SQL statement, and adds it to the
     * customers table in the mySQL database.
     * @param divisionID the first level division ID
     * @param postal the postal code of the customer
     * @param address the address of the customer
     * @param name the name of the customer
     * @param phone the phone number of the customer
     * @throws SQLException prevents app from crashing when incorrect SQL syntax is used
     */
    public static void addCustomer(int divisionID, String postal, String address, String name, String phone) throws SQLException {

        Connection conn = DatabaseConnection.getConnection();

        DatabaseQuery.setStatement(conn);

        Statement statement = DatabaseQuery.getStatement();

        String insertSQL = "INSERT INTO customers(Customer_Name, Address, Postal_Code, Phone, Division_ID) " +
                "VALUES('" + name + "', '" +
                address + "', '" +
                postal + "', '" +
                phone + "', '" +
                divisionID + "')";

        // EXECUTE statement

        statement.execute(insertSQL);

        // Confirm rows affected
        if(statement.getUpdateCount() > 0) {
            System.out.println(statement.getUpdateCount() + " rows(s) affected!");
            customerlist.clear();
            getAllCustomers();

        }
        else
            System.out.println("Nothing changed..");

    }

    /**
     * Updates the selected Customer in the database.
     * @param customerID the customer's ID
     * @param divisionID the first level division ID
     * @param postal the postal code of the customer
     * @param address the address of the customer
     * @param name the name of the customer
     * @param phone the phone number of the customer
     * @throws SQLException prevents app from crashing when incorrect SQL syntax is used
     */
    public static void updateCustomer(int customerID, int divisionID, String postal, String address, String name, String phone) throws SQLException {

        Connection conn = DatabaseConnection.getConnection();

        DatabaseQuery.setStatement(conn);

        Statement statement = DatabaseQuery.getStatement();

        // Update SQL statement for the appointments table
        String updateSQL = "UPDATE customers " +
                "SET Customer_Name = '" + name + "', " +
                "Address = '" + address + "', " +
                "Postal_Code = '" + postal + "', " +
                "Phone = '" + phone + "', " +
                "Division_ID = '" + divisionID + "' " +
                "WHERE Customer_ID = " + customerID;

        // EXECUTE statement

        statement.execute(updateSQL);

        // Confirm rows affected
        if(statement.getUpdateCount() > 0) {
            System.out.println(statement.getUpdateCount() + " rows(s) affected!");
            customerlist.clear();
            getAllCustomers();

        }
        else
            System.out.println("Nothing changed..");
    }

    /**
     * Used to create a temporary static placeholder of a customer object that is selected to be or was modified. Used
     * in the HomeController class method - <code>onUpdateCus</code>.
     * @param customersObj the selected customer object
     */
    public static void setCustomersObj(CustomersObj customersObj) {
        CustomersAccess.customersObj = customersObj;
    }

    /**
     * Retrieves a copy of the customer object stored in this class. It's used as a placeholder to pre-load the customer
     * object as a selection in the Appointment form. Also used in the CustomerController class when updating a customer.
     * @return appointment object to be loaded in new <code>scene</code>
     */
    public static CustomersObj getCustomersObj() {
        return customersObj;
    }

    /**
     * Finds the Contact object by the contact ID from the Customer Tableview on the home page.
     * @param id is the ID number of the contact object.
     */
    public static void findCustomer(int id) {

        for (int i = 0; i <= customerlist.size(); i++) {
            if (id == customerlist.get(i).getCustomerID()) {
                customersObj = customerlist.get(i);
                return;
            }
        }
    }

    /**
     * Uses a <code>DELETE</code> SQL call to delete selected object from the customers table in the MySQL database by
     * customer ID. If rows are updated in the MySQL database then the current customerList <code>ObservableList</code> is cleared and an updated
     * one is retrieved from the database.
     * @param customer the selected customer object to be deleted
     * @throws SQLException prevents crashes if incorrect SQL syntax is used
     */
    public static void deleteCustomer(CustomersObj customer) throws SQLException {

        int found = customer.getCustomerID();

        Connection conn = DatabaseConnection.getConnection();

        DatabaseQuery.setStatement(conn);

        Statement statement = DatabaseQuery.getStatement();

        String deleteSQL = "DELETE FROM customers WHERE Customer_ID = " + found;

        statement.execute(deleteSQL);

        // Confirm rows affected
        if(statement.getUpdateCount() > 0) {
            System.out.println(statement.getUpdateCount() + " rows(s) affected!");
            customerlist.clear();
            getAllCustomers();

        }
        else
            System.out.println("Nothing changed..");

    }

//    public static void isCustomerScheduled(int cusID) {
//
//    }

}
