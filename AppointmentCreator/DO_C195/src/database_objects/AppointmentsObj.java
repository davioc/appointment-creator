package database_objects;

import java.time.LocalDateTime;

/**
 * Creates Appointment objects and provides methods to access attributes of the object.
 */
public class AppointmentsObj {

    private int appointmentID;
    private String title;
    private String location;
    private String type;
    private LocalDateTime start;
    private LocalDateTime end;
    private String description;
    private int customerID;
    private int userID;
    private int contactID;

    /**
     * Creates an Appointment object.
     * @param appointmentID
     * @param title
     * @param location
     * @param type
     * @param start
     * @param end
     * @param description
     * @param customerID
     * @param userID
     * @param contactID
     */
    public AppointmentsObj(int appointmentID, String title, String location, String type, LocalDateTime start, LocalDateTime end, String description, int customerID, int userID, int contactID) {
        this.appointmentID = appointmentID;
        this.title = title;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.description = description;
        this.customerID = customerID;
        this.userID = userID;
        this.contactID = contactID;

    }

    /**
     * Retrieves the appointment ID of the selected appointment object.
     * @return the appointment's ID
     */
    public int getAppointmentID() {
        return appointmentID;
    }

    /**
     * Retrieves the appointment Title of the selected appointment object.
     * @return the appointment's Title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Retrieves the appointment Location of the selected appointment object.
     * @return the appointment's Location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Retrieves the appointment Type of the selected appointment object.
     * @return the appointment's Type
     */
    public String getType() {
        return type;
    }

    /**
     * Retrieves the appointment Start Date of the selected appointment object.
     * @return the appointment's Start Date
     */
    public LocalDateTime getStart() {
        return start;
    }

    /**
     * Retrieves the appointment End Date of the selected appointment object.
     * @return the appointment's End Date
     */
    public LocalDateTime getEnd() {
        return end;
    }

    /**
     * Retrieves the appointment Description of the selected appointment object.
     * @return the appointment's Description Date
     */
    public String getDescription() {
        return description;
    }

    /**
     * Retrieves the appointment Customer ID of the selected appointment object.
     * @return the appointment's customer ID
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * Retrieves the appointment Contact ID of the selected appointment object.
     * @return the appointment's contact ID
     */
    public int getContactID() {
        return contactID;
    }

    /**
     * Returns the information of the appointment object in a human-readable format.
     * @return
     */
    @Override
    public String toString() {
        return ("Appointment ID: [" + appointmentID + "]" + " Title: [" + title + "] " + "Type: [" + type + "] " +
                "Description: [ " + description + " ] " + "Start: [" + start + "] " + "End: [" + end + "] " + " Customer ID: [" + customerID + "]");
    }
}
