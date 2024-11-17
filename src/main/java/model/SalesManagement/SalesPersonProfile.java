/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.Personnel;

import java.util.ArrayList;
import java.util.List;
import model.OrderManagement.Order;

/**
 *
 * @author kal bugrara
 */
public class SalesPersonProfile extends Profile {
    private List<Order> salesHistory;
    
    public SalesPersonProfile() {
        super();
        this.salesHistory = new ArrayList<>();
    }
    
    public void addSalesOrder(Order order) {
        if (order != null) {
            salesHistory.add(order);
        }
    }
    
    public List<Order> getSalesHistory() {
        return new ArrayList<>(salesHistory);
    }
}
