package database_access;

import database_objects.CountriesObj;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utilities.DatabaseConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.sql.Timestamp;

/**
 * This class is used to interact with the countries table in the MySQL database. It provides other classes and methods
 * access to the Countries objects stored locally in the countriesList <code>ObservableList</code>.
 */
public class CountriesAccess {

    private static CountriesObj countriesObj;
//    private static int countryID;
    private static ObservableList<CountriesObj> countriesList = FXCollections.observableArrayList();

    /**
     * Uses a <code>SELECT</code> SQL call to retrieve all objects from the countries table in the MySQL database and
     * adds them to an <code>ObservableList</code>.
     */
    public  static void getAllCountries(){

        try{
            String sql = "SELECT * from countries";

            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int countryID = rs.getInt("Country_ID");
                String countryName = rs.getString("Country");
                CountriesObj C = new CountriesObj(countryID, countryName);
                countriesList.add(C);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
//        return countriesList;
    }

    /**
     * Returns a copy of the <code>ObservableList</code> "countriesList" so that it can be viewed in the UI and modified
     * using CRUD techniques.
     * @return a copy of the <code>ObservableList</code>
     */
    public static ObservableList<CountriesObj> getCountriesList() {
        return countriesList;
    }

    /**
     * Finds a countries object by ID number. if found, a temporary placeholder of the object is stored as a
     * static variable in this class. It's to find the object's ID that is associated with the FirstLevelAccess class.
     * @param countryID the <code>int</code> value entered by the user
     */
    public static void setCountriesObjByID(int countryID) {

        for (int i = 0; i <= countriesList.size(); i++) {
            if (countryID ==  countriesList.get(i).getId()) {
                countriesObj = countriesList.get(i);
                return;
            }
        }
    }


    /**
     * Returns a copy of the countries object stored in this class. It's used to return a countries object that's
     * associated with the FirstLevel object.
     * @return
     */
    public static CountriesObj getCountriesObj() {
        return countriesObj;
    }
}
