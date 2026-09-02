package database_access;

import database_objects.UsersObj;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utilities.DatabaseConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class is used to interact with the users table in the MySQL database and verify user login credentials.
 */
public class UsersAccess {

    // Placeholder of the current signed in user so that when an appointment is added the ID number can be accessed
    static UsersObj usersObj;

    // Store user table data as an observable list to verify login
    static ObservableList<UsersObj> ulist = FXCollections.observableArrayList();

    /**
     * This method uses a SELECT statement to view the users table in the database. User objects from the database are
     * added to a local <code>ObservableList</code> so other methods verify the login information from Login Form.
     */
    public static void getAllUsers() {

        try{
            String sql = "SELECT * FROM users";

            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int idNum = rs.getInt("User_ID");
                String name = rs.getString("User_Name");
                String pass = rs.getString("Password");
                UsersObj U = new UsersObj(idNum, name, pass);
                ulist.add(U);
//                System.out.println(name + " " + pass + " was added to ulist");

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    /**
     * Compares the username and password with the values stored in ulist. If found, user can proceed to log into the
     * application. The user is also saved as a variable in this class so that its ID can be accessed when adding an
     * appointment.
     * @param username takes in the information entered from the Username field
     * @param password takes in the information entered from the Password field
     * @return <code>boolean</code> value if user info is/isn't found
     */
    public static boolean verifyUserLogin(String username, String password) {

        int max = ulist.size();

        try {
            for (int i = 0; i <= max; i++) {
                if (ulist.get(i).getName().equals(username) & ulist.get(i).getPassword().equals(password)) {
                    // Sets the user object variable in this class to the one that's found
                    usersObj = ulist.get(i);
                    return true;
                }
            }
        } catch (Exception e) {
        }
                return false;
            }

}



