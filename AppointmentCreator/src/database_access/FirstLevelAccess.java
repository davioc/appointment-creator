package database_access;

import database_objects.FirstLevelObj;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utilities.DatabaseConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class is used to interact with the first_level_divisions table in the MySQL database. It provides other classes and methods
 * access to the FirstLevel objects stored locally in the firsList <code>ObservableList</code>.
 */
public class FirstLevelAccess {

    public static FirstLevelObj findFirst;
    static ObservableList<FirstLevelObj> firstList = FXCollections.observableArrayList();

    /**
     * Uses a <code>SELECT</code> SQL call to retrieve all objects from the first_level_divisions table in the MySQL database and
     * adds them to an <code>ObservableList</code>.
     */
    public static void getAllFirstLevel() {

        try{
            String sql = "SELECT * FROM first_level_divisions";

            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int divisionID = rs.getInt("Division_ID");
                int countryID = rs.getInt("COUNTRY_ID");
                String division = rs.getString("Division");
                FirstLevelObj F = new FirstLevelObj(divisionID, countryID, division);
                firstList.add(F);

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
    /**
     * Retrieves the countries <code>Observable List</code> that were created from the mySQL database.
     * @return
     */
    public static ObservableList<FirstLevelObj> getFirstList() {
        return firstList;
    }

    /**
     * Gets the division ID from the selected customer object to locate the associated FirstLevel object. It then stores
     * the object separately in this class so it can later be used to pre-fill the Appointment form.
     */
    public static void setFindFirst(int divisionID) {

        for (int i = 0; i <= firstList.size(); i++) {
            if (divisionID == firstList.get(i).getDivisionID()) {
                findFirst = firstList.get(i);
                CountriesAccess.setCountriesObjByID(findFirst.getCountryID());
                return;
            }
        }

    }

    /**
     * Returns the temporary FirstLevel object stored in this class. It's used to set the First Level selection in the
     * appointment form to the <code>findFirst</code> object.
     * @return
     */
    public static FirstLevelObj getFindFirst() {
        return findFirst;
    }
}
