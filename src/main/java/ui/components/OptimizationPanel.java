package ui.components;

import model.ProductManagement.Product;
import model.simulation.OptimizationResult;
import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public class OptimizationPanel extends JPanel {
    private JTextArea optimizationResultArea;
    private static final DecimalFormat CURRENCY_FORMAT = new DecimalFormat("$#,##0.00");
    private static final DecimalFormat PERCENT_FORMAT = new DecimalFormat("#0.0%");

    public OptimizationPanel() {
        initializeComponents();
        layoutComponents();
    }

    private void initializeComponents() {
        optimizationResultArea = new JTextArea();
        optimizationResultArea.setEditable(false);
        optimizationResultArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Price Optimization Results"));
        add(new JScrollPane(optimizationResultArea), BorderLayout.CENTER);
    }

    public void displayResults(OptimizationResult result) {
        StringBuilder display = new StringBuilder();
        display.append("=== Price Optimization Results ===\n\n");
        
        display.append(String.format("Total Revenue Improvement: %s\n", 
            PERCENT_FORMAT.format(result.getTotalRevenueImprovement())));
        display.append(String.format("Total Profit Improvement: %s\n", 
            PERCENT_FORMAT.format(result.getTotalProfitImprovement())));
        display.append(String.format("Average Confidence Level: %s\n\n", 
            PERCENT_FORMAT.format(result.getAverageConfidenceLevel())));

        display.append("Optimized Prices:\n");
        for (Map.Entry<Product, Double> entry : result.getOptimizedPrices().entrySet()) {
            Product product = entry.getKey();
            Double newPrice = entry.getValue();
            display.append(String.format("- %s: %s (from %s)\n",
                product.getName(),
                CURRENCY_FORMAT.format(newPrice),
                CURRENCY_FORMAT.format(product.getTargetPrice())));
        }

        display.append("\nRecommendations:\n");
        for (String recommendation : result.getRecommendations()) {
            display.append("- ").append(recommendation).append("\n");
        }

        optimizationResultArea.setText(display.toString());
        optimizationResultArea.setCaretPosition(0);
    }

    public void clear() {
        optimizationResultArea.setText("");
    }
} 