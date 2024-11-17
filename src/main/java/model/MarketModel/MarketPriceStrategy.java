package model.MarketModel;

import model.ProductManagement.Product;

public interface MarketPriceStrategy {
    double calculatePrice(Product product, Channel channel);
} 