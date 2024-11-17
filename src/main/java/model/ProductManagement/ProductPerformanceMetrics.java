package model.ProductManagement;

import model.OrderManagement.OrderItem;
import java.time.*;
import java.util.*;

public class ProductPerformanceMetrics {
    private Product product;
    private Map<Period, PerformanceSnapshot> performanceHistory;
    private PerformanceSnapshot currentPeriodMetrics;
    private LocalDateTime lastUpdated;

    public ProductPerformanceMetrics(Product product) {
        this.product = product;
        this.performanceHistory = new HashMap<>();
        this.currentPeriodMetrics = new PerformanceSnapshot();
        this.lastUpdated = LocalDateTime.now();
    }

    public void updateMetrics(OrderItem item) {
        currentPeriodMetrics.updateMetrics(item);
        lastUpdated = LocalDateTime.now();
        
        // Archive metrics if period has changed
        archiveMetricsIfNeeded();
    }

    private void archiveMetricsIfNeeded() {
        Period currentPeriod = getCurrentPeriod();
        if (!performanceHistory.containsKey(currentPeriod)) {
            performanceHistory.put(currentPeriod, currentPeriodMetrics);
            currentPeriodMetrics = new PerformanceSnapshot();
        }
    }

    public boolean needsPriceAdjustment() {
        double targetMargin = 0.3; // 30% target margin
        return currentPeriodMetrics.getAverageMargin() < targetMargin ||
               currentPeriodMetrics.getSalesVolume() < getExpectedSalesVolume();
    }

    private int getExpectedSalesVolume() {
        // Implementation of sales volume prediction
        return 100; // Simplified for example
    }

    private Period getCurrentPeriod() {
        LocalDateTime now = LocalDateTime.now();
        return Period.between(now.toLocalDate().withDayOfMonth(1), 
                            now.toLocalDate());
    }

    // Inner class for storing performance snapshots
    private static class PerformanceSnapshot {
        private int salesVolume;
        private double totalRevenue;
        private double averagePrice;
        private double averageMargin;
        private Map<String, Double> channelPerformance;

        public PerformanceSnapshot() {
            this.channelPerformance = new HashMap<>();
        }

        public void updateMetrics(OrderItem item) {
            salesVolume += item.getQuantity();
            double itemRevenue = item.getQuantity() * item.getActualPrice();
            totalRevenue += itemRevenue;
            averagePrice = totalRevenue / salesVolume;
            
            // Update channel performance
            String channelId = item.getOrder().getChannel().getId();
            channelPerformance.merge(channelId, itemRevenue, Double::sum);
            
            // Update margin
            double cost = item.getProduct().getCost();
            averageMargin = (averagePrice - cost) / averagePrice;
        }

        // Getters
        public int getSalesVolume() { return salesVolume; }
        public double getTotalRevenue() { return totalRevenue; }
        public double getAveragePrice() { return averagePrice; }
        public double getAverageMargin() { return averageMargin; }
        public Map<String, Double> getChannelPerformance() { 
            return new HashMap<>(channelPerformance); 
        }
    }
} 