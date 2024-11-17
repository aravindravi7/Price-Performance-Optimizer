package model.simulation;

import model.ProductManagement.Product;
import java.util.*;

public class SimulationResult {
    private Product product;
    private double currentPrice;
    private double simulatedPrice;
    private double currentRevenue;
    private double simulatedRevenue;
    private double currentProfit;
    private double simulatedProfit;
    private int estimatedSalesVolume;
    private double confidenceLevel;

    // Constructor and getters
    
    public double getRevenueChange() {
        return ((simulatedRevenue - currentRevenue) / currentRevenue) * 100;
    }

    public double getProfitChange() {
        return ((simulatedProfit - currentProfit) / currentProfit) * 100;
    }
} 