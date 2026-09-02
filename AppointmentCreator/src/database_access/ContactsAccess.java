package database_access;

import database_objects.ContactsObj;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utilities.DatabaseConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class is used to interact with the contact table in the MySQL database. It provides other classes and methods
 * access to the Contacts objects stored locally in the contact's <code>ObservableList</code>.
 */
public class ContactsAccess {

    static ContactsObj contactsObj;
    static ObservableList<ContactsObj> contactsList = FXCollections.observableArrayList();

    /**
     * Uses a <code>SELECT</code> SQL call to retrieve all objects from the contacts table in the MySQL database and
     * adds them to an <code>ObservableList</code>.
     */
    public static void getAllContacts() {

        try {
            String sql = "SELECT * FROM contacts";

            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("Contact_ID");
                String name = rs.getString("Contact_Name");
                String email = rs.getString("Email");
                ContactsObj C = new ContactsObj(id, name, email);
                contactsList.add(C);

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

    /**
     * Returns a copy of the <code>ObservableList</code> "contactsList" so that it can be viewed in the UI and modified
     * using CRUD techniques.
     * @return a copy of the <code>ObservableList</code>
     */
    public static ObservableList<ContactsObj> getContactsList() {

        return contactsList;
    }


    /**
     * Retrieves a temporary Contact Object to be displayed in the user UI when changing scenes. It's also used for
     * loading form information of the selected contact object.
     * @return a selected contact object
     */
    public static ContactsObj getContactsObj() {
        return contactsObj;
    }


    /**
     * Finds a contact object by the contact ID from the contacts <code>ObservableList</code> of this class.
     * @param id is the ID number of the selected contact object
     */
    public static void findContact(int id) {

        for (int i = 0; i <= contactsList.size(); i++) {
            if (id == contactsList.get(i).getContactID()) {
                contactsObj = contactsList.get(i);
                return;
            }
        }
    }
}