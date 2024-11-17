/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.MarketModel;

import java.util.*;

/**
 *
 * @author kal bugrara
 */
public class Channel {
    private String id;
    private String name;
    private double marginMultiplier;
    private ChannelType type;
    private Map<String, Double> customPriceMultipliers;
    private ChannelMetrics metrics;

    public Channel(String name, ChannelType type, double marginMultiplier) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.type = type;
        this.marginMultiplier = marginMultiplier;
        this.customPriceMultipliers = new HashMap<>();
        this.metrics = new ChannelMetrics();
    }

    public void setCustomPriceMultiplier(String productId, double multiplier) {
        customPriceMultipliers.put(productId, multiplier);
    }

    public double getPriceMultiplier(String productId) {
        return customPriceMultipliers.getOrDefault(productId, marginMultiplier);
    }

    // Getters and Setters
    public String getId() { return id; }
    public String getName() { return name; }
    public ChannelType getType() { return type; }
    public double getMarginMultiplier() { return marginMultiplier; }
    public void setMarginMultiplier(double marginMultiplier) { 
        this.marginMultiplier = marginMultiplier; 
    }
    public ChannelMetrics getMetrics() { return metrics; }
}

public enum ChannelType {
    DIRECT_SALES,
    DISTRIBUTOR,
    ONLINE,
    RETAIL
}
