package ui.reports;

import model.ProductManagement.*;
import model.OrderManagement.*;
import model.Supplier.Supplier;
import java.time.*;
import java.util.*;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;
import model.Business.Business;
import config.BusinessConfiguration;
import config.ConfigurationLoader;
import model.Business.ConfigureABusiness;
import controller.PricingController;
import ui.MainPricingFrame;

public class ProductPerformanceReport {
    private LocalDateTime generationTime;
    private Map<Supplier, List<ProductPerformanceEntry>> supplierEntries;
    private PerformanceSummary summary;

    public ProductPerformanceReport() {
        this.generationTime = LocalDateTime.now();
        this.supplierEntries = new HashMap<>();
        this.summary = new PerformanceSummary();
    }

    public void addSupplierData(Supplier supplier, List<Product> products, 
                               OrderAnalyzer orderAnalyzer) {
        List<ProductPerformanceEntry> entries = new ArrayList<>();
        
        for (Product product : products) {
            ProductPerformanceEntry entry = new ProductPerformanceEntry(
                product,
                product.getTargetPrice(),
                orderAnalyzer.getAverageSellingPrice(product),
                orderAnalyzer.getSalesBelowTarget(product),
                orderAnalyzer.getSalesAboveTarget(product),
                orderAnalyzer.getTotalRevenue(product)
            );
            
            entries.add(entry);
            summary.updateMetrics(entry);
        }
        
        supplierEntries.put(supplier, entries);
    }

    public String generateReport() {
        StringBuilder report = new StringBuilder();
        report.append("Product Performance Report\n");
        report.append("Generated: ").append(generationTime).append("\n\n");

        // Overall Summary
        report.append(summary.toString()).append("\n\n");

        // Supplier-wise breakdown
        for (Map.Entry<Supplier, List<ProductPerformanceEntry>> entry : 
             supplierEntries.entrySet()) {
            report.append("Supplier: ").append(entry.getKey().getName()).append("\n");
            report.append("----------------------------------------\n");
            
            for (ProductPerformanceEntry product : entry.getValue()) {
                report.append(product.toString()).append("\n");
            }
            report.append("\n");
        }

        return report.toString();
    }

    private static class ProductPerformanceEntry {
        private Product product;
        private double targetPrice;
        private double averageActualPrice;
        private int salesBelowTarget;
        private int salesAboveTarget;
        private double totalRevenue;

        public ProductPerformanceEntry(Product product, double targetPrice, 
                                     double averageActualPrice, int salesBelowTarget, 
                                     int salesAboveTarget, double totalRevenue) {
            this.product = product;
            this.targetPrice = targetPrice;
            this.averageActualPrice = averageActualPrice;
            this.salesBelowTarget = salesBelowTarget;
            this.salesAboveTarget = salesAboveTarget;
            this.totalRevenue = totalRevenue;
        }

        @Override
        public String toString() {
            return String.format(
                "Product: %s\n" +
                "  Target Price: $%.2f\n" +
                "  Average Actual Price: $%.2f\n" +
                "  Sales Below Target: %d\n" +
                "  Sales Above Target: %d\n" +
                "  Total Revenue: $%.2f\n",
                product.getName(),
                targetPrice,
                averageActualPrice,
                salesBelowTarget,
                salesAboveTarget,
                totalRevenue
            );
        }
    }

    private static class PerformanceSummary {
        private int totalProducts;
        private double totalRevenue;
        private int totalSalesBelowTarget;
        private int totalSalesAboveTarget;
        private double averageMargin;

        public void updateMetrics(ProductPerformanceEntry entry) {
            totalProducts++;
            totalRevenue += entry.totalRevenue;
            totalSalesBelowTarget += entry.salesBelowTarget;
            totalSalesAboveTarget += entry.salesAboveTarget;
            averageMargin = (totalRevenue - (totalSalesBelowTarget * entry.targetPrice)) / totalRevenue;
        }

        @Override
        public String toString() {
            return String.format(
                "Total Products: %d\n" +
                "Total Revenue: $%,.2f\n" +
                "Total Sales Below Target: %d\n" +
                "Total Sales Above Target: %d\n" +
                "Average Margin: %.1f%%\n",
                totalProducts,
                totalRevenue,
                totalSalesBelowTarget,
                totalSalesAboveTarget,
                averageMargin * 100
            );
        }
    }
} 