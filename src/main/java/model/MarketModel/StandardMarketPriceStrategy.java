package model.MarketModel;

import model.ProductManagement.Product;

public class StandardMarketPriceStrategy implements MarketPriceStrategy {
    private double marketMultiplier;
    
    public StandardMarketPriceStrategy(double marketMultiplier) {
        this.marketMultiplier = marketMultiplier;
    }
    
    @Override
    public double calculatePrice(Product product, Channel channel) {
        double basePrice = product.getTargetPrice();
        double channelAdjustment = channel.getMarginMultiplier();
        return basePrice * marketMultiplier * channelAdjustment;
    }
} 