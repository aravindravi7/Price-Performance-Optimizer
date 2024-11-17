/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.OrderManagement;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author kal bugrara
 */
public class OrderSummary {
    private Order order;
    private double totalRevenue;
    private double totalProfit;
    private Map<String, Double> categoryRevenue;
    
    public OrderSummary(Order order) {
        this.order = order;
        this.categoryRevenue = new HashMap<>();
        calculateMetrics();
    }
    
    private void calculateMetrics() {
        for (OrderItem item : order.getOrderItems()) {
            Product product = item.getProduct();
            double itemRevenue = item.getQuantity() * item.getActualPrice();
            double itemProfit = itemRevenue - (item.getQuantity() * product.getCost());
            
            totalRevenue += itemRevenue;
            totalProfit += itemProfit;
            
            // Aggregate by category
            categoryRevenue.merge(product.getCategory(), itemRevenue, Double::sum);
        }
    }
    
    public double getProfitMargin() {
        return totalRevenue > 0 ? (totalProfit / totalRevenue) * 100 : 0;
    }
}
