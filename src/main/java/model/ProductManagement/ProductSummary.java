/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.ProductManagement;

import model.OrderManagement.MasterOrderList;
import model.OrderManagement.Order;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author kal bugrara
 */

//this class will extract summary data from the product
public class ProductSummary {
    private Product product;
    private List<Order> recentOrders;
    private static final double PRICE_ADJUSTMENT_THRESHOLD = 0.15; // 15%
    private static final int ANALYSIS_PERIOD_DAYS = 30;
    
    public ProductSummary(Product product, MasterOrderList orderList) {
        this.product = product;
        this.recentOrders = orderList.getOrdersForProduct(product).stream()
            .filter(this::isWithinAnalysisPeriod)
            .collect(Collectors.toList());
    }
    
    public boolean needsPriceAdjustment() {
        if (recentOrders.isEmpty()) return false;
        
        double avgSalePrice = calculateAverageSalePrice();
        double targetPrice = product.getTargetPrice();
        double priceDifference = Math.abs(avgSalePrice - targetPrice) / targetPrice;
        
        return priceDifference > PRICE_ADJUSTMENT_THRESHOLD;
    }
    
    public double getRecommendedPrice() {
        if (recentOrders.isEmpty()) return product.getTargetPrice();
        
        double avgSalePrice = calculateAverageSalePrice();
        double targetPrice = product.getTargetPrice();
        
        // If sales are consistently below target, recommend lower price
        if (avgSalePrice < targetPrice) {
            return Math.max(
                avgSalePrice * 1.05, // 5% above average sale price
                product.getCost() * 1.15  // Minimum 15% margin
            );
        }
        
        // If sales are consistently above target, recommend higher price
        return Math.min(
            avgSalePrice * 0.95, // 5% below average sale price
            targetPrice * 1.20   // Maximum 20% increase
        );
    }
    
    private boolean isWithinAnalysisPeriod(Order order) {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(ANALYSIS_PERIOD_DAYS);
        return order.getDate().toInstant()
            .atZone(java.time.ZoneId.systemDefault())
            .toLocalDateTime()
            .isAfter(cutoff);
    }
    
    private double calculateAverageSalePrice() {
        return recentOrders.stream()
            .flatMap(order -> order.getOrderItems().stream())
            .filter(item -> item.getProduct().equals(product))
            .mapToDouble(item -> item.getActualPrice())
            .average()
            .orElse(product.getTargetPrice());
    }
    
    // Additional metrics
    public double getSalesVolume() {
        return recentOrders.stream()
            .flatMap(order -> order.getOrderItems().stream())
            .filter(item -> item.getProduct().equals(product))
            .mapToInt(item -> item.getQuantity())
            .sum();
    }
    
    public double getRevenue() {
        return recentOrders.stream()
            .flatMap(order -> order.getOrderItems().stream())
            .filter(item -> item.getProduct().equals(product))
            .mapToDouble(item -> item.getQuantity() * item.getActualPrice())
            .sum();
    }
    
    public double getProfit() {
        return getRevenue() - (getSalesVolume() * product.getCost());
    }
}
