package analytics;

import java.util.List;
import model.ProductManagement.Product;
import model.OrderManagement.Order;

public class PerformanceAnalyzer {
    public PerformanceReport analyzeProductPerformance(List<Product> products, List<Order> orders) {
        PerformanceReport report = new PerformanceReport();
        
        for (Product product : products) {
            double targetRevenue = calculateTargetRevenue(product, orders);
            double actualRevenue = calculateActualRevenue(product, orders);
            double performance = calculatePerformance(actualRevenue, targetRevenue);
            
            report.addProductPerformance(product, performance, actualRevenue, targetRevenue);
        }
        
        return report;
    }
    
    private double calculateTargetRevenue(Product product, List<Order> orders) {
        return orders.stream()
            .flatMap(order -> order.getOrderItems().stream())
            .filter(item -> item.getProduct().equals(product))
            .mapToDouble(item -> item.getQuantity() * product.getTargetPrice())
            .sum();
    }
    
    private double calculateActualRevenue(Product product, List<Order> orders) {
        return orders.stream()
            .flatMap(order -> order.getOrderItems().stream())
            .filter(item -> item.getProduct().equals(product))
            .mapToDouble(item -> item.getQuantity() * item.getActualPrice())
            .sum();
    }
    
    private double calculatePerformance(double actual, double target) {
        return target == 0 ? 0 : ((actual - target) / target) * 100;
    }
} 