package model.CustomerManagement;

import java.util.ArrayList;
import java.util.List;

public class CustomerDirectory {
    private ArrayList<CustomerProfile> customers;
    
    public CustomerDirectory() {
        customers = new ArrayList<>();
    }
    
    public CustomerProfile addCustomer(String name) {
        CustomerProfile customer = new CustomerProfile(name);
        customers.add(customer);
        return customer;
    }
    
    public List<CustomerProfile> getCustomerList() {
        return customers;
    }
    
    public void removeCustomer(CustomerProfile customer) {
        customers.remove(customer);
    }
}
