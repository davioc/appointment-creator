package database_objects;

/**
 * Creates Country objects and provides methods to access attributes of the object.
 */
public class CountriesObj {
    private int id;
    private String name;

    /**
     * Creates a country object.
     * @param id the id of the country object
     * @param name the name of the country object
     */
    public CountriesObj(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Retrieves the ID of the selected country object. Used in the CountriesAccess class to find the First level division
     * associated with it.
     * @return the country object's ID
     */
    public int getId() {return id;}

    /**
     * Overrides the <code>toString()</code> method so that it is presented in human-readable. It's used in the selection
     * list of the appointment form.
     * @return
     */
    @Override
    public String toString() {
        return ("[ ID: " + id + " ] - " + name);
    }
}
