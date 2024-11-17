package model.ProductManagement;

import model.Supplier.Supplier;

public class Product {
    private String name;
    private double targetPrice;
    private boolean active;
    private Supplier supplier;
    private int id;
    private static int counter = 1;
    
    public Product(String name) {
        this.name = name;
        this.active = true;
        this.id = counter++;
    }
    
    // Getters and Setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public double getTargetPrice() {
        return targetPrice;
    }
    
    public void setTargetPrice(double targetPrice) {
        this.targetPrice = targetPrice;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    public Supplier getSupplier() {
        return supplier;
    }
    
    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }
    
    public int getId() {
        return id;
    }
    
    @Override
    public String toString() {
        return name;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Product other = (Product) obj;
        return id == other.id;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
