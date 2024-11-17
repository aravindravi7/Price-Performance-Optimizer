import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import model.ProductManagement.Product;
import org.jfree.chart.*;
import org.jfree.data.category.DefaultCategoryDataset;
import java.text.DecimalFormat;
import java.util.function.Consumer;

public class PriceAdjustmentDialog extends JDialog {
    private Product product;
    private boolean confirmed = false;
    private JTextField priceField;
    private JLabel marginLabel;
    private JLabel revenueImpactLabel;
    private JPanel previewChart;
    
    public PriceAdjustmentDialog(Frame owner, Product product) {
        super(owner, "Adjust Price - " + product.getName(), true);
        this.product = product;
        
        setupDialog();
        createComponents();
        pack();
        setLocationRelativeTo(owner);
    }
    
    private void setupDialog() {
        setMinimumSize(new Dimension(500, 400));
        setResizable(false);
        setLayout(new BorderLayout(10, 10));
        getRootPane().setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }
    
    private void createComponents() {
        // Current price info
        add(createCurrentPricePanel(), BorderLayout.NORTH);
        
        // Adjustment panel
        add(createAdjustmentPanel(), BorderLayout.CENTER);
        
        // Buttons
        add(createButtonPanel(), BorderLayout.SOUTH);
    }
    
    private JPanel createCurrentPricePanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Current Price Information"));
        
        panel.add(new JLabel("Current Target Price:"));
        panel.add(new JLabel(String.format("$%.2f", product.getTargetPrice())));
        
        panel.add(new JLabel("Cost:"));
        panel.add(new JLabel(String.format("$%.2f", product.getCost())));
        
        panel.add(new JLabel("Current Margin:"));
        double currentMargin = (product.getTargetPrice() - product.getCost()) / product.getTargetPrice();
        panel.add(new JLabel(String.format("%.1f%%", currentMargin * 100)));
        
        panel.add(new JLabel("Sales Performance:"));
        String performance = calculateSalesPerformance();
        panel.add(new JLabel(performance));
        
        return panel;
    }
    
    private JPanel createAdjustmentPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Price Adjustment"));
        
        // Price input
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputPanel.add(new JLabel("New Price:"));
        priceField = new JTextField(10);
        priceField.setText(String.format("%.2f", product.getTargetPrice()));
        inputPanel.add(priceField);
        
        // Impact preview
        JPanel impactPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        marginLabel = new JLabel("New Margin: ");
        revenueImpactLabel = new JLabel("Revenue Impact: ");
        impactPanel.add(marginLabel);
        impactPanel.add(revenueImpactLabel);
        
        // Preview chart
        previewChart = createPreviewChart();
        
        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(impactPanel, BorderLayout.CENTER);
        panel.add(previewChart, BorderLayout.SOUTH);
        
        // Add listener for real-time updates
        priceField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { updatePreview(); }
            public void removeUpdate(DocumentEvent e) { updatePreview(); }
            public void insertUpdate(DocumentEvent e) { updatePreview(); }
        });
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton cancelButton = new JButton("Cancel");
        JButton applyButton = new JButton("Apply");
        
        cancelButton.addActionListener(e -> dispose());
        applyButton.addActionListener(e -> {
            if (validateAndApplyChange()) {
                confirmed = true;
                dispose();
            }
        });
        
        panel.add(cancelButton);
        panel.add(applyButton);
        
        return panel;
    }
    
    private void updatePreview() {
        try {
            double newPrice = Double.parseDouble(priceField.getText());
            double newMargin = (newPrice - product.getCost()) / newPrice;
            double revenueImpact = calculateRevenueImpact(newPrice);
            
            marginLabel.setText(String.format("New Margin: %.1f%%", newMargin * 100));
            revenueImpactLabel.setText(String.format("Revenue Impact: %.1f%%", revenueImpact * 100));
            
            updatePreviewChart(newPrice);
        } catch (NumberFormatException e) {
            marginLabel.setText("New Margin: Invalid input");
            revenueImpactLabel.setText("Revenue Impact: Invalid input");
        }
    }
} 