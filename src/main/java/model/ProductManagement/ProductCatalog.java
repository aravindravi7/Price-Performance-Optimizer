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
public class ProductCatalog {

    private ArrayList<Product> products;

    public ProductCatalog() {
        products = new ArrayList<>();
    }

    public Product newProduct(String name) {
        Product product = new Product(name);
        products.add(product);
        return product;
    }

    public List<Product> getProductList() {
        return products;
    }

    public void removeProduct(Product product) {
        products.remove(product);
    }
}
