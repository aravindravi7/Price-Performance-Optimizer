/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.MarketModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.ProductManagement.Product;
import model.OrderManagement.Order;
import model.CustomerManagement.CustomerProfile;

/**
 *
 * @author kal bugrara
 */
public class MarketChannelAssignment {
    
    private Market market;
    private Channel channel;
    private Map<Product, ChannelPerformance> performanceMetrics;
    
    public MarketChannelAssignment(Market market, Channel channel) {
        this.market = market;
        this.channel = channel;
        this.performanceMetrics = new HashMap<>();
    }
    
    public void analyzePerformance(MasterOrderList orderList) {
        orderList.getOrders().stream()
            .filter(order -> order.getChannel().equals(channel))
            .forEach(this::processOrder);
    }
    
    private void processOrder(Order order) {
        for (OrderItem item : order.getOrderItems()) {
            Product product = item.getProduct();
            performanceMetrics.computeIfAbsent(product, k -> new ChannelPerformance())
                            .addSale(item.getQuantity(), item.getActualPrice());
        }
    }
    
    private class ChannelPerformance {
        private int totalQuantity;
        private double totalRevenue;
        private List<Double> prices;
        
        public ChannelPerformance() {
            this.prices = new ArrayList<>();
        }
        
        public void addSale(int quantity, double price) {
            totalQuantity += quantity;
            totalRevenue += quantity * price;
            prices.add(price);
        }
    }
    
}
