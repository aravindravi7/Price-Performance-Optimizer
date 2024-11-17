package ui.components;

import javax.swing.*;
import java.awt.*;

public class SimulationControlPanel extends JPanel {
    private JSpinner priceAdjustmentSpinner;
    private JButton simulateButton;
    private JButton optimizeButton;
    private JComboBox<String> timeframeComboBox;

    public SimulationControlPanel() {
        initializeComponents();
        layoutComponents();
    }

    private void initializeComponents() {
        // Price adjustment spinner (-50% to +100%)
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(0.0, -0.5, 1.0, 0.01);
        priceAdjustmentSpinner = new JSpinner(spinnerModel);
        JSpinner.NumberEditor editor = new JSpinner.NumberEditor(priceAdjustmentSpinner, "0%");
        priceAdjustmentSpinner.setEditor(editor);

        // Timeframe selection
        timeframeComboBox = new JComboBox<>(new String[]{
            "Next Month", "Next Quarter", "Next Year"
        });

        // Action buttons
        simulateButton = new JButton("Run Simulation");
        optimizeButton = new JButton("Optimize Prices");
    }

    private void layoutComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Price adjustment controls
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Price Adjustment:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        add(priceAdjustmentSpinner, gbc);

        // Timeframe selection
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        add(new JLabel("Timeframe:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        add(timeframeComboBox, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(simulateButton);
        buttonPanel.add(optimizeButton);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        add(buttonPanel, gbc);
    }

    public double getPriceAdjustment() {
        return (Double) priceAdjustmentSpinner.getValue();
    }

    public String getSelectedTimeframe() {
        return (String) timeframeComboBox.getSelectedItem();
    }

    public JButton getSimulateButton() {
        return simulateButton;
    }

    public JButton getOptimizeButton() {
        return optimizeButton;
    }
} 