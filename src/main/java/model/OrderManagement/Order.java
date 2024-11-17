/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.OrderManagement;

import java.util.*;
import java.time.LocalDate;

import model.CustomerManagement.CustomerProfile;
import model.MarketModel.MarketChannelAssignment;
import model.ProductManagement.Product;
import model.SalesManagement.SalesPersonProfile;

/**
 *
 * @author kal bugrara
 */
public class Order {

    private String orderId;
    private List<OrderItem> orderItems;
    private static int counter = 1;
    
    public Order() {
        this.orderId = "ORD" + counter++;
        this.orderItems = new ArrayList<>();
    }
    
    public String getOrderId() {
        return orderId;
    }
    
    public void addOrderItem(OrderItem item) {
        if (item != null) {
            orderItems.add(item);
        }
    }
    
    public List<OrderItem> getOrderItems() {
        return new ArrayList<>(orderItems);
    }
    
    public double getTotal() {
        return orderItems.stream()
            .mapToDouble(item -> item.getQuantity() * item.getActualPrice())
            .sum();
    }
}
