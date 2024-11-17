package controller;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import model.Business.Business;
import model.ProductManagement.Product;
import model.OrderManagement.Order;
import model.OrderManagement.OrderItem;
import model.Supplier.Supplier;

public class PricingController {
    private Business business;
    private Supplier currentSupplier;
    
    public PricingController(Business business) {
        this.business = business;
    }
    
    public Business getBusiness() {
        return business;
    }
    
    public void setCurrentSupplier(Supplier supplier) {
        System.out.println("PricingController - Setting current supplier: " + 
            (supplier != null ? supplier.getName() + " (ID: " + supplier.getId() + ")" : "null"));
        this.currentSupplier = supplier;
    }
    
    public Supplier getCurrentSupplier() {
        System.out.println("PricingController - Getting current supplier: " + 
            (currentSupplier != null ? currentSupplier.getName() + " (ID: " + currentSupplier.getId() + ")" : "null"));
        return currentSupplier;
    }
    
    // Product Management Methods
    public void updateProductPrice(Product product, double newPrice) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (newPrice < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        
        product.setTargetPrice(newPrice);
        System.out.println("Updated price for " + product.getName() + " to $" + newPrice);
    }
    
    public Product createProduct(String name, double targetPrice) {
        if (currentSupplier == null) {
            throw new IllegalStateException("No supplier selected");
        }
        
        Product product = business.getProductCatalog().newProduct(name);
        product.setTargetPrice(targetPrice);
        product.setSupplier(currentSupplier);
        System.out.println("Created product: " + name + " for supplier: " + currentSupplier.getName());
        return product;
    }
    
    public void deleteProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        business.getProductCatalog().removeProduct(product);
    }
    
    // Performance Analysis Methods
    public double calculateProductPerformance(Product product) {
        if (product == null) return 0.0;
        
        List<Order> orders = business.getMasterOrderList().getOrders();
        final double[] targetRevenue = {0.0};
        final double[] actualRevenue = {0.0};
        
        orders.forEach(order -> {
            order.getOrderItems().stream()
                .filter(item -> item.getProduct().equals(product))
                .forEach(item -> {
                    targetRevenue[0] += item.getQuantity() * product.getTargetPrice();
                    actualRevenue[0] += item.getQuantity() * item.getActualPrice();
                });
        });
        
        return targetRevenue[0] == 0 ? 0 : ((actualRevenue[0] - targetRevenue[0]) / targetRevenue[0]) * 100;
    }
    
    public List<PerformanceResult> generatePerformanceReport() {
        List<PerformanceResult> results = new ArrayList<>();
        List<Product> products = business.getProductCatalog().getProductList();
        
        for (Product product : products) {
            double performance = calculateProductPerformance(product);
            double revenue = calculateProductRevenue(product);
            int unitsSold = calculateUnitsSold(product);
            results.add(new PerformanceResult(product, performance, revenue, unitsSold));
        }
        
        return results;
    }
    
    private double calculateProductRevenue(Product product) {
        if (product == null) return 0.0;
        
        return business.getMasterOrderList().getOrders().stream()
            .flatMap(order -> order.getOrderItems().stream())
            .filter(item -> item.getProduct().equals(product))
            .mapToDouble(item -> item.getQuantity() * item.getActualPrice())
            .sum();
    }
    
    private int calculateUnitsSold(Product product) {
        if (product == null) return 0;
        
        return business.getMasterOrderList().getOrders().stream()
            .flatMap(order -> order.getOrderItems().stream())
            .filter(item -> item.getProduct().equals(product))
            .mapToInt(OrderItem::getQuantity)
            .sum();
    }
    
    public static class PerformanceResult {
        private Product product;
        private double performance;
        private double revenue;
        private int unitsSold;
        
        public PerformanceResult(Product product, double performance, double revenue, int unitsSold) {
            this.product = product;
            this.performance = performance;
            this.revenue = revenue;
            this.unitsSold = unitsSold;
        }
        
        public Product getProduct() {
            return product;
        }
        
        public double getPerformance() {
            return performance;
        }
        
        public double getRevenue() {
            return revenue;
        }
        
        public int getUnitsSold() {
            return unitsSold;
        }
        
        @Override
        public String toString() {
            return String.format("%s: Performance=%.2f%%, Revenue=$%.2f, Units=%d", 
                product.getName(), performance, revenue, unitsSold);
        }
    }
} 