/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.MarketModel;

import java.util.*;
import model.ProductManagement.Product;

/**
 *
 * @author kal bugrara
 */
public class Market {
    private String id;
    private String name;
    private String description;
    private List<Channel> channels;
    private Map<String, MarketPriceStrategy> priceStrategies;
    private MarketMetrics metrics;
    
    public Market(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.channels = new ArrayList<>();
        this.priceStrategies = new HashMap<>();
        this.metrics = new MarketMetrics();
    }
    
    public void addChannel(Channel channel) {
        channels.add(channel);
    }
    
    public void setPriceStrategy(Product product, MarketPriceStrategy strategy) {
        priceStrategies.put(product.getId(), strategy);
    }
    
    public double calculateMarketPrice(Product product, Channel channel) {
        MarketPriceStrategy strategy = priceStrategies.get(product.getId());
        if (strategy != null) {
            return strategy.calculatePrice(product, channel);
        }
        return product.getTargetPrice() * channel.getMarginMultiplier();
    }
    
    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public List<Channel> getChannels() { return new ArrayList<>(channels); }
    public MarketMetrics getMetrics() { return metrics; }
}
