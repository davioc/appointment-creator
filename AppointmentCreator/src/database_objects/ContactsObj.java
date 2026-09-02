package database_objects;

/**
 * Creates Contact objects and provides methods to access attributes of the object.
 */
public class ContactsObj {

    private int contactID;
    private String name;
    private String email;

    /**
     * Creates a contacts object. Used in the Appointment form to assign a contact to an appointment and used for locating
     * appointments assigned to the selected contact.
     * @param contactID the contact's ID
     * @param name the contact's name
     * @param email the contact's email address
     */
    public ContactsObj(int contactID, String name, String email) {
        this.contactID = contactID;
        this.name = name;
        this.email = email;
    }

    /**
     * Retrieves the contact object's ID.
     * @return the contact's ID
     */
    public int getContactID() {
        return contactID;
    }

    /**
     * Retrieves the contact object's name.
     * @return the contact's name
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the contact object's email address.
     * @return the contact's email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the contact object's ID.
     * @param contactID the <code>int</code> value of the contact's ID
     */
    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    /**
     * This method overrides the <code>toString()</code> method so only the name of the contact is returned in the Combo box for Appointments
     * @return the ID and name of the contact in human-readable form.
     */
    @Override
    public String toString() {
        return ("[ID: " + contactID + " ] - " + name);
    }

}
