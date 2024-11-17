package model.ProductManagement;

import model.simulation.PriceSimulator;
import model.simulation.SimulationResult;
import java.util.*;

public class PriceOptimizer {
    private PriceSimulator simulator;
    private static final double CONVERGENCE_THRESHOLD = 0.001;
    private static final int MAX_ITERATIONS = 100;

    public PriceOptimizer(PriceSimulator simulator) {
        this.simulator = simulator;
    }

    public OptimizationResult optimizeProfitMargins(List<Product> products) {
        OptimizationResult result = new OptimizationResult();
        Map<Product, Double> optimizedPrices = new HashMap<>();
        
        for (Product product : products) {
            double optimalPrice = findOptimalPriceIterative(product);
            optimizedPrices.put(product, optimalPrice);
        }
        
        result.setOptimizedPrices(optimizedPrices);
        calculateOverallImpact(result, products);
        generateRecommendations(result);
        
        return result;
    }

    private double findOptimalPriceIterative(Product product) {
        double currentPrice = product.getTargetPrice();
        double bestPrice = currentPrice;
        double bestProfit = 0;
        double step = currentPrice * 0.1; // 10% initial step size
        
        for (int iteration = 0; iteration < MAX_ITERATIONS; iteration++) {
            boolean improved = false;
            
            // Try prices above and below current best
            for (double multiplier : new double[]{-1, 1}) {
                double testPrice = bestPrice + (step * multiplier);
                if (testPrice < product.getCost() * 1.1) continue; // Ensure minimum margin
                
                SimulationResult simResult = simulator.simulatePrice(product, testPrice);
                if (simResult.getSimulatedProfit() > bestProfit) {
                    bestProfit = simResult.getSimulatedProfit();
                    bestPrice = testPrice;
                    improved = true;
                }
            }
            
            if (!improved) {
                step *= 0.5; // Reduce step size if no improvement
            }
            
            if (step < currentPrice * CONVERGENCE_THRESHOLD) {
                break; // Converged
            }
        }
        
        return bestPrice;
    }
} 