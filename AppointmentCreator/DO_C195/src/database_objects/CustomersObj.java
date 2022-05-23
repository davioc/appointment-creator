package database_objects;

/**
 * Creates Customer objects and provides methods to access attributes of the object.
 */
public class CustomersObj {
    private int customerID;
    private int divisionID; // for country
    private String postalCode;
    private String address;
    private String name;
    private String phone;

    /**
     * Creates a Customer object.
     * @param customerID the ID of the customer
     * @param divisionID the first level division ID
     * @param postalCode the postal code of the customer
     * @param address the address of the customer
     * @param name the name of the customer
     * @param phone the phone number of the customer
     */
    public CustomersObj(int customerID, int divisionID, String postalCode, String address, String name, String phone) {
        this.customerID = customerID;
        this.divisionID = divisionID;
        this.postalCode = postalCode;
        this.address = address;
        this.name = name;
        this.phone = phone;
    }

    /**
     * Retrieves the customer object's ID
     * @return the customer's ID
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * Retrieves the customer object's first level division ID.
     * @return the customer's first level division ID
     */
    public int getDivisionID() { return divisionID;}

    /**
     * Retrieves the customer object's Postal Code.
     * @return the customer's postal code
     */
    public String getPostalCode() { return postalCode;}

    /**
     * Retrieves the customer object's address.
     * @return the customer's address
     */
    public  String getAddress() { return address;}

    /**
     * Retrieves the customer object's name
     * @return the customer's name
     */
    public String getName() { return name;}

    /**
     * Retrieves the customer object's phone number
     * @return the customer's phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Overrides the <code>toString()</code> method so that the customer object is presented in human-readable form. It's
     * used in the appointment form when selecting a customer and in the reports section of the Home Page.
     * @return the ID and name of the customer
     */
    @Override
    public String toString() {
        return ("[ ID: " + customerID + " ] - " + name);
    }

}
