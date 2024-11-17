package model.simulation;

import model.ProductManagement.Product;
import java.util.*;

public class OptimizationResult {
    private Map<Product, Double> optimizedPrices;
    private double totalRevenueImprovement;
    private double totalProfitImprovement;
    private double averageConfidenceLevel;
    private List<String> recommendations;

    public OptimizationResult() {
        this.optimizedPrices = new HashMap<>();
        this.recommendations = new ArrayList<>();
    }

    public void setOptimizedPrices(Map<Product, Double> prices) {
        this.optimizedPrices = new HashMap<>(prices);
    }

    public void addRecommendation(String recommendation) {
        recommendations.add(recommendation);
    }

    public void calculateImprovements(Map<Product, SimulationResult> simResults) {
        double totalCurrentRevenue = 0;
        double totalSimulatedRevenue = 0;
        double totalCurrentProfit = 0;
        double totalSimulatedProfit = 0;
        double confidenceSum = 0;

        for (SimulationResult result : simResults.values()) {
            totalCurrentRevenue += result.getCurrentRevenue();
            totalSimulatedRevenue += result.getSimulatedRevenue();
            totalCurrentProfit += result.getCurrentProfit();
            totalSimulatedProfit += result.getSimulatedProfit();
            confidenceSum += result.getConfidenceLevel();
        }

        this.totalRevenueImprovement = ((totalSimulatedRevenue - totalCurrentRevenue) / totalCurrentRevenue) * 100;
        this.totalProfitImprovement = ((totalSimulatedProfit - totalCurrentProfit) / totalCurrentProfit) * 100;
        this.averageConfidenceLevel = confidenceSum / simResults.size();
    }

    // Getters
    public Map<Product, Double> getOptimizedPrices() {
        return new HashMap<>(optimizedPrices);
    }

    public double getTotalRevenueImprovement() {
        return totalRevenueImprovement;
    }

    public double getTotalProfitImprovement() {
        return totalProfitImprovement;
    }

    public double getAverageConfidenceLevel() {
        return averageConfidenceLevel;
    }

    public List<String> getRecommendations() {
        return new ArrayList<>(recommendations);
    }
} 