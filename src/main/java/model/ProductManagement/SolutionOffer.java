/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.ProductManagement;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kal bugrara
 */
public class SolutionOffer {
    private String name;
    private List<Product> products;
    private double bundleDiscount;
    
    public SolutionOffer(String name) {
        this.name = name;
        this.products = new ArrayList<>();
    }
    
    public void addProduct(Product product) {
        products.add(product);
    }
    
    public double calculateBundlePrice() {
        double totalPrice = products.stream()
            .mapToDouble(Product::getTargetPrice)
            .sum();
        return totalPrice * (1 - bundleDiscount);
    }
    
    public void setBundleDiscount(double discount) {
        this.bundleDiscount = Math.max(0, Math.min(1, discount));
    }
}
