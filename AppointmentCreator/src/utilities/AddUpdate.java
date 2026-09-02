package utilities;

/**
 * This class holds the ADD or UPDATE status. It's used by controllers to determine if the actions taken are to add or update
 * an object.
 */
public class AddUpdate {
    public static AddOrUpdate addOrUpdate;

    /**
     * Retrieves the AddOrUpdate object of this class
     * @return the <code>ENUM</code> value of ADD or UPDATE
     */
    public static AddOrUpdate getAddOrUpdate() {
        return addOrUpdate;
    }

    /**
     * Sets the AddOrUpdate object in this class to the ADD or UPDATE <code>ENUM</code> value.
     * @param addupdate the <code>ENUM - AddOrUpdate</code> value, "ADD" or "UPDATE"
     */
    public static void setAddOrUpdate(AddOrUpdate addupdate){
        addOrUpdate = addupdate;
    }
}
