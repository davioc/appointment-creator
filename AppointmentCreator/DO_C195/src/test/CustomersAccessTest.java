package test;

import database_access.CustomersAccess;
import database_objects.CustomersObj;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utilities.DatabaseConnection;

import java.sql.SQLException;

import static java.lang.String.valueOf;

public class CustomersAccessTest {

    @BeforeEach
    public void setUp() {
        // Set up any necessary test data or test environment
        // This method will be executed before each test method
        // You can initialize database connections, mock objects, etc.

        // Connection to Database
        DatabaseConnection.startConnection();
    }

    @Test
    public void testGetAllCustomers() {
        // Test if getAllCustomers method retrieves customers successfully
        CustomersAccess.getAllCustomers();
        Assertions.assertFalse(CustomersAccess.getCustomerlist().isEmpty(), "Customer list is empty");
    }

    @Test
    public void testAddCustomer() throws SQLException {
        // Test if a new customer is added successfully
        CustomersAccess.getAllCustomers();
        int initialSize = CustomersAccess.getCustomerlist().size();

        // Add a new customer
        CustomersAccess.addCustomer(1, "12345", "Test Address", "Test Customer #" + valueOf(initialSize), "1234567890");

        // Verify if the customer list size has increased
        Assertions.assertEquals(initialSize + 1, CustomersAccess.getCustomerlist().size(),
                "Customer was not added successfully");
    }

    @Test
    public void testUpdateCustomer() throws SQLException {

        CustomersAccess.getAllCustomers();
        // Test if a customer is updated successfully
        int customerId = CustomersAccess.getCustomerlist().size()-1; // Specify an existing customer ID to update

        System.out.println(valueOf(customerId));
        // Update the customer
        CustomersAccess.updateCustomer(customerId, 2, "54321", "Updated Address", "Updated Customer", "0987654321");
        System.out.println(valueOf(customerId));
        //CustomersAccess.getAllCustomers();

        // Retrieve the updated customer object
        CustomersObj updatedCustomer = CustomersAccess.getCustomerlist().get(customerId-2);

        System.out.println(updatedCustomer.getCustomerID());

        // Verify if the customer details are updated correctly
        Assertions.assertEquals("Updated Address", updatedCustomer.getAddress(),
                "Customer address was not updated successfully");
        Assertions.assertEquals("Updated Customer", updatedCustomer.getName(),
                "Customer name was not updated successfully");
    }

    @Test
    public void testDeleteCustomer() throws SQLException {
        // Test if a customer is deleted successfully
        int initialSize = CustomersAccess.getCustomerlist().size();

        // Look for existing customers
        int customerId = 0;
        for (CustomersObj obj : CustomersAccess.getCustomerlist()){
            customerId = obj.getCustomerID();
            return;
        }

        // Choose a customer to delete (replace with an existing customer object)
        CustomersObj customerToDelete = new CustomersObj(customerId, 1, "12345", "Test Address", "Test Customer", "1234567890");

        // Delete the customer
        CustomersAccess.deleteCustomer(customerToDelete);

        // Verify if the customer list size has decreased
        Assertions.assertEquals(initialSize - 1, CustomersAccess.getCustomerlist().size(),
                "Customer was not deleted successfully");
    }
}
