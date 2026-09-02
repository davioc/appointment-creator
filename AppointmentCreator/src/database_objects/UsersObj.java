package database_objects;

/**
 * Creates User objects and provides methods to access attributes of the object.
 */
public class UsersObj {

    private int id;
    private String name;
    private String password;

    /**
     * Creates a user object.
     * @param id
     * @param name the name of the user
     * @param password the password of the user
     */
    public UsersObj(int id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;

    }

    public int getId() { return id; }

    /**
     * Retrieves the name of the user object
     * @return the name of the user
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the password of the user object
     * @return the password of the user
     */
    public String getPassword() {
        return password;
    }
}
