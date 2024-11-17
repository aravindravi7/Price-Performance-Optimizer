/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.CustomerManagement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.MarketModel.Market;
import model.OrderManagement.Order;
import model.OrderManagement.OrderItem;
import model.Personnel.Person;
import model.Personnel.Profile;
import model.ProductManagement.Product;

/**
 *
 * @author kal bugrara
 */
public class CustomerProfile extends Profile {
    private List<Order> orderHistory;
    private Map<String, Double> categoryPreferences;
    private double totalSpending;
    
    public CustomerProfile() {
        super();
        this.orderHistory = new ArrayList<>();
        this.categoryPreferences = new HashMap<>();
    }
    
    public CustomerProfile(String id, Person person) {
        this();
        this.id = id;
        this.person = person;
    }
    
    // ID getters and setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    // Person getters and setters
    public Person getPerson() {
        return person;
    }
    
    public void setPerson(String name) {
        this.person = new Person(name, "PERS" + System.currentTimeMillis());
    }
    
    public void setPerson(Person person) {
        this.person = person;
    }
    
    // Order management
    public void addCustomerOrder(Order order) {
        if (order != null) {
            orderHistory.add(order);
            updateMetrics(order);
        }
    }
    
    // Alias for backward compatibility
    public void addOrder(Order order) {
        addCustomerOrder(order);
    }
    
    public List<Order> getOrderHistory() {
        return new ArrayList<>(orderHistory);
    }
    
    // Metrics and preferences
    private void updateMetrics(Order order) {
        if (order != null && order.getOrderItems() != null) {
            for (OrderItem item : order.getOrderItems()) {
                if (item != null) {
                    Product product = item.getProduct();
                    if (product != null) {
                        double itemTotal = item.getQuantity() * item.getActualPrice();
                        totalSpending += itemTotal;
                        categoryPreferences.merge(
                            product.getCategory(), 
                            itemTotal, 
                            Double::sum
                        );
                    }
                }
            }
        }
    }
    
    public Map<String, Double> getCategoryPreferences() {
        return new HashMap<>(categoryPreferences);
    }
    
    public double getTotalSpending() {
        return totalSpending;
    }
    
    public double getAverageOrderValue() {
        return orderHistory.isEmpty() ? 0 : totalSpending / orderHistory.size();
    }
    
    @Override
    public String toString() {
        return person != null ? person.getName() : "Unknown Customer";
    }
}
