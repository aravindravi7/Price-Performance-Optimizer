/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.OrderManagement;

import model.ProductManagement.Product;

/**
 *
 * @author kal bugrara
 */
public class OrderItem {

    private Product product;
    private int quantity;
    private double actualPrice;

    public OrderItem(Product product, int quantity, double actualPrice) {
        this.product = product;
        this.quantity = quantity;
        this.actualPrice = actualPrice;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getActualPrice() {
        return actualPrice;
    }

    public double getTotal() {
        return quantity * actualPrice;
    }
}
