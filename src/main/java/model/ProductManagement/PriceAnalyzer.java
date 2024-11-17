package model.ProductManagement;

import model.Supplier.Supplier;
import model.Supplier.SupplierDirectory;
import model.OrderManagement.OrderAnalyzer;
import java.util.*;

public class PriceAnalyzer {
    private SupplierDirectory supplierDirectory;
    private OrderAnalyzer orderAnalyzer;
    private ProductCatalog productCatalog;

    public PriceAnalyzer(SupplierDirectory supplierDirectory, 
                        OrderAnalyzer orderAnalyzer, 
                        ProductCatalog productCatalog) {
        this.supplierDirectory = supplierDirectory;
        this.orderAnalyzer = orderAnalyzer;
        this.productCatalog = productCatalog;
    }

    public Map<Supplier, List<ProductPerformanceMetrics>> analyzeSupplierCatalogPerformance() {
        Map<Supplier, List<ProductPerformanceMetrics>> supplierPerformance = new HashMap<>();
        
        for (Supplier supplier : supplierDirectory.getSupplierList()) {
            List<Product> supplierProducts = productCatalog.getProductsBySupplier(supplier.getId());
            List<ProductPerformanceMetrics> productMetrics = new ArrayList<>();
            
            for (Product product : supplierProducts) {
                ProductPerformanceMetrics metrics = analyzeProductPerformance(product);
                productMetrics.add(metrics);
            }
            
            supplierPerformance.put(supplier, productMetrics);
        }
        
        return supplierPerformance;
    }

    private ProductPerformanceMetrics analyzeProductPerformance(Product product) {
        ProductPerformanceMetrics metrics = new ProductPerformanceMetrics(product);
        
        // Get sales data
        double averageSellingPrice = orderAnalyzer.getAverageSellingPrice(product);
        int totalSales = orderAnalyzer.getTotalSales(product);
        int salesBelowTarget = orderAnalyzer.getSalesBelowTarget(product);
        int salesAboveTarget = orderAnalyzer.getSalesAboveTarget(product);
        
        // Calculate performance indicators
        double targetPrice = product.getTargetPrice();
        double priceVariance = (averageSellingPrice - targetPrice) / targetPrice;
        double revenue = averageSellingPrice * totalSales;
        double cost = product.getCost() * totalSales;
        double profitMargin = (revenue - cost) / revenue;
        
        // Update metrics
        metrics.updateMetrics(
            averageSellingPrice,
            totalSales,
            salesBelowTarget,
            salesAboveTarget,
            priceVariance,
            revenue,
            profitMargin
        );
        
        return metrics;
    }
} 