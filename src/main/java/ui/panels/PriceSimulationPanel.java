package ui.panels;

import controller.PricingController;
import model.ProductManagement.Product;
import model.simulation.*;
import ui.components.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.text.DecimalFormat;

public class PriceSimulationPanel extends JPanel implements Refreshable {
    private PricingController controller;
    private ProductSelectionTable productTable;
    private SimulationControlPanel controlPanel;
    private SimulationResultPanel resultPanel;
    private OptimizationPanel optimizationPanel;
    
    private static final DecimalFormat CURRENCY_FORMAT = new DecimalFormat("$#,##0.00");
    private static final DecimalFormat PERCENT_FORMAT = new DecimalFormat("#0.0%");

    public PriceSimulationPanel(PricingController controller) {
        this.controller = controller;
        initializeComponents();
        layoutComponents();
        setupActions();
    }

    private void initializeComponents() {
        productTable = new ProductSelectionTable(controller);
        controlPanel = new SimulationControlPanel();
        resultPanel = new SimulationResultPanel();
        optimizationPanel = new OptimizationPanel();
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Left side - Product selection and controls
        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
        leftPanel.add(new JScrollPane(productTable), BorderLayout.CENTER);
        leftPanel.add(controlPanel, BorderLayout.SOUTH);

        // Right side - Results and optimization
        JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
        rightPanel.add(resultPanel, BorderLayout.CENTER);
        rightPanel.add(optimizationPanel, BorderLayout.SOUTH);

        // Add to split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(500);
        add(splitPane, BorderLayout.CENTER);
    }

    private void setupActions() {
        controlPanel.getSimulateButton().addActionListener(e -> runSimulation());
        controlPanel.getOptimizeButton().addActionListener(e -> runOptimization());
        productTable.getSelectionModel().addListSelectionListener(e -> updateSimulationControls());
    }

    private void runSimulation() {
        List<Product> selectedProducts = productTable.getSelectedProducts();
        if (selectedProducts.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please select at least one product to simulate.",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double priceAdjustment = controlPanel.getPriceAdjustment();
            Map<Product, SimulationResult> results = new HashMap<>();

            for (Product product : selectedProducts) {
                double newPrice = product.getTargetPrice() * (1 + priceAdjustment);
                SimulationResult result = controller.simulatePriceImpact(product.getId(), newPrice);
                results.put(product, result);
            }

            resultPanel.displayResults(results);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error running simulation: " + ex.getMessage(),
                "Simulation Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void runOptimization() {
        List<Product> selectedProducts = productTable.getSelectedProducts();
        if (selectedProducts.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please select products to optimize.",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            List<String> productIds = selectedProducts.stream()
                .map(Product::getId)
                .collect(java.util.stream.Collectors.toList());

            OptimizationResult result = controller.optimizePrices(productIds);
            optimizationPanel.displayResults(result);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error running optimization: " + ex.getMessage(),
                "Optimization Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateSimulationControls() {
        boolean hasSelection = !productTable.getSelectedProducts().isEmpty();
        controlPanel.getSimulateButton().setEnabled(hasSelection);
        controlPanel.getOptimizeButton().setEnabled(hasSelection);
    }

    @Override
    public void refreshData() {
        productTable.refreshData();
        resultPanel.clear();
        optimizationPanel.clear();
    }
} 