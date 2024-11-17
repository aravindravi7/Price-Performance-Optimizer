package config;

import java.util.ArrayList;
import java.util.List;

public class BusinessConfiguration {
    private List<SupplierConfig> suppliers;
    private List<ProductConfig> products;
    private List<CustomerConfig> customers;

    public BusinessConfiguration() {
        // Initialize with default data
        this.suppliers = createDefaultSuppliers();
        this.products = createDefaultProducts();
        this.customers = createDefaultCustomers();
    }

    private List<SupplierConfig> createDefaultSuppliers() {
        List<SupplierConfig> defaultSuppliers = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            SupplierConfig supplier = new SupplierConfig();
            supplier.setId("SUP" + i);
            supplier.setName("Supplier " + i);
            defaultSuppliers.add(supplier);
        }
        return defaultSuppliers;
    }

    private List<ProductConfig> createDefaultProducts() {
        List<ProductConfig> defaultProducts = new ArrayList<>();
        for (SupplierConfig supplier : suppliers) {
            for (int i = 1; i <= 10; i++) {
                ProductConfig product = new ProductConfig();
                product.setId("PROD" + supplier.getId() + "_" + i);
                product.setName("Product " + i + " from " + supplier.getName());
                product.setTargetPrice(100.0 + (Math.random() * 900.0));
                product.setCost(product.getTargetPrice() * 0.6);
                product.setSupplierId(supplier.getId());
                defaultProducts.add(product);
            }
        }
        return defaultProducts;
    }

    private List<CustomerConfig> createDefaultCustomers() {
        List<CustomerConfig> defaultCustomers = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            CustomerConfig customer = new CustomerConfig();
            customer.setId("CUST" + i);
            customer.setName("Customer " + i);
            defaultCustomers.add(customer);
        }
        return defaultCustomers;
    }

    // Getters and setters
    public List<SupplierConfig> getSuppliers() { return suppliers; }
    public List<ProductConfig> getProducts() { return products; }
    public List<CustomerConfig> getCustomers() { return customers; }

    public void setSuppliers(List<SupplierConfig> suppliers) { this.suppliers = suppliers; }
    public void setProducts(List<ProductConfig> products) { this.products = products; }
    public void setCustomers(List<CustomerConfig> customers) { this.customers = customers; }

    // Inner config classes with their getters and setters
    public static class SupplierConfig {
        private String id;
        private String name;
        
        public String getId() { return id; }
        public String getName() { return name; }
        public void setId(String id) { this.id = id; }
        public void setName(String name) { this.name = name; }
    }

    public static class ProductConfig {
        private String id;
        private String name;
        private double targetPrice;
        private double cost;
        private String supplierId;
        
        public String getId() { return id; }
        public String getName() { return name; }
        public double getTargetPrice() { return targetPrice; }
        public double getCost() { return cost; }
        public String getSupplierId() { return supplierId; }
        
        public void setId(String id) { this.id = id; }
        public void setName(String name) { this.name = name; }
        public void setTargetPrice(double targetPrice) { this.targetPrice = targetPrice; }
        public void setCost(double cost) { this.cost = cost; }
        public void setSupplierId(String supplierId) { this.supplierId = supplierId; }
    }

    public static class CustomerConfig {
        private String id;
        private String name;
        
        public String getId() { return id; }
        public String getName() { return name; }
        public void setId(String id) { this.id = id; }
        public void setName(String name) { this.name = name; }
    }
} 