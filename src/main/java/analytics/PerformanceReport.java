package analytics;

import java.util.HashMap;
import java.util.Map;
import model.ProductManagement.Product;

public class PerformanceReport {
    private Map<Product, ProductPerformance> productPerformances;
    
    public PerformanceReport() {
        this.productPerformances = new HashMap<>();
    }
    
    public void addProductPerformance(Product product, double performance, 
                                    double actualRevenue, double targetRevenue) {
        productPerformances.put(product, 
            new ProductPerformance(performance, actualRevenue, targetRevenue));
    }
    
    public Map<Product, ProductPerformance> getProductPerformances() {
        return new HashMap<>(productPerformances);
    }
    
    public static class ProductPerformance {
        private double performance;
        private double actualRevenue;
        private double targetRevenue;
        
        public ProductPerformance(double performance, double actualRevenue, double targetRevenue) {
            this.performance = performance;
            this.actualRevenue = actualRevenue;
            this.targetRevenue = targetRevenue;
        }
        
        public double getPerformance() { return performance; }
        public double getActualRevenue() { return actualRevenue; }
        public double getTargetRevenue() { return targetRevenue; }
    }
} 