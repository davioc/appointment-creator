package database_objects;

/**
 * Creates FirstLevel (first level division) objects and provides methods to access attributes of the object.
 */
public class FirstLevelObj {

    private int divisionID;
    private int countryID;
    private String division;

    /**
     * Creates a FirstLevel object.
     * @param divisionID the first level division ID
     * @param countryID the country id associated with the first level division
     * @param division the name of the first level division
     */
    public FirstLevelObj(int divisionID, int countryID, String division) {
        this.divisionID = divisionID;
        this.countryID = countryID;
        this.division = division;
    }

    /**
     * Retrieves the FirstLevel object division ID.
     * @return the first level division ID
     */
    public int getDivisionID() {
        return divisionID;
    }

    /**
     * Retrieves the FirstLevel object country ID.
     * @return the country ID
     */
    public int getCountryID() {
        return countryID;
    }

    /**
     * Retrieves the FirstLevel object name.
     * @return the division name
     */
    public String getDivision() {
        return division;
    }

    /**
     * Overrides the <code>toString()</code> method so that the first level division object is presented in human-readable form. It's
     * used in the appointment form when selecting a first level division.
     * @return the first level division ID and name
     */
    @Override
    public String toString() {
        return ("[ ID: " + divisionID + " ] - " + division);
    }
}
