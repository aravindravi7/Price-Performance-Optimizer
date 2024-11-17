package ui.panels;

import javax.swing.*;
import java.awt.*;
import controller.PricingController;
import model.Business.Business;
import model.Supplier.Supplier;
import java.util.List;

public class LoginPanel extends JPanel {
    private Business business;
    private PricingController controller;
    private CardLayout cardLayout;
    private JPanel parentPanel;
    private JComboBox<String> roleSelector;
    private JComboBox<Supplier> supplierComboBox;
    private JPanel supplierSelectionPanel;
    
    public LoginPanel(CardLayout layout, JPanel parent, Business business, PricingController controller) {
        this.cardLayout = layout;
        this.parentPanel = parent;
        this.business = business;
        this.controller = controller;
        initializePanel();
    }
    
    private void initializePanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Create welcome label
        JLabel welcomeLabel = new JLabel("Welcome to Pricing Management System");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        
        // Create role selector
        String[] roles = {"Select Role", "Customer", "Supplier", "Admin"};
        roleSelector = new JComboBox<>(roles);
        roleSelector.setPreferredSize(new Dimension(200, 30));
        roleSelector.addActionListener(e -> handleRoleSelection());
        
        // Create supplier selection panel (initially invisible)
        supplierSelectionPanel = createSupplierSelectionPanel();
        supplierSelectionPanel.setVisible(false);
        
        // Create login button
        JButton loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(200, 40));
        loginButton.addActionListener(e -> handleLogin());
        
        // Layout components
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(welcomeLabel, gbc);
        
        gbc.gridy = 1;
        gbc.insets = new Insets(30, 10, 10, 10);
        add(roleSelector, gbc);
        
        gbc.gridy = 2;
        add(supplierSelectionPanel, gbc);
        
        gbc.gridy = 3;
        gbc.insets = new Insets(10, 10, 30, 10);
        add(loginButton, gbc);
    }
    
    private JPanel createSupplierSelectionPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        // Create supplier combo box
        supplierComboBox = new JComboBox<>();
        supplierComboBox.setPreferredSize(new Dimension(200, 30));
        
        // Add a default "Select Supplier" item
        supplierComboBox.addItem(null); // Will be rendered as "Select Supplier"
        
        // Custom renderer to show "Select Supplier" for null value
        supplierComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                if (value == null) {
                    value = "Select Supplier";
                } else {
                    value = ((Supplier) value).getName();
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });
        
        panel.add(new JLabel("Select Supplier: "));
        panel.add(supplierComboBox);
        
        return panel;
    }
    
    private void handleRoleSelection() {
        String selectedRole = (String) roleSelector.getSelectedItem();
        if ("Supplier".equals(selectedRole)) {
            updateSupplierList();
            supplierSelectionPanel.setVisible(true);
        } else {
            supplierSelectionPanel.setVisible(false);
        }
        revalidate();
        repaint();
    }
    
    private void updateSupplierList() {
        supplierComboBox.removeAllItems();
        List<Supplier> suppliers = business.getSupplierDirectory().getSupplierList();
        System.out.println("Available suppliers:"); // Debug print
        for (Supplier supplier : suppliers) {
            supplierComboBox.addItem(supplier);
            System.out.println(" - " + supplier.getName() + " (ID: " + supplier.getId() + ")");
        }
    }
    
    private void handleLogin() {
        String selectedRole = (String) roleSelector.getSelectedItem();
        
        if ("Select Role".equals(selectedRole)) {
            showError("Please select a role");
            return;
        }
        
        switch (selectedRole) {
            case "Customer":
                cardLayout.show(parentPanel, "CUSTOMER_PRODUCT");
                break;
            case "Supplier":
                Supplier selectedSupplier = (Supplier) supplierComboBox.getSelectedItem();
                if (selectedSupplier == null) {
                    showError("Please select a supplier");
                    return;
                }
                
                // Debug prints
                System.out.println("LoginPanel - Selected supplier: " + selectedSupplier.getName());
                System.out.println("LoginPanel - Supplier ID: " + selectedSupplier.getId());
                
                // Set the current supplier in the controller
                controller.setCurrentSupplier(selectedSupplier);
                
                // Verify the supplier was set
                Supplier verifySupplier = controller.getCurrentSupplier();
                System.out.println("LoginPanel - Verified supplier after setting: " + 
                    (verifySupplier != null ? verifySupplier.getName() : "null"));
                
                cardLayout.show(parentPanel, "PRODUCT");
                break;
            case "Admin":
                cardLayout.show(parentPanel, "ADMIN");
                break;
        }
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this,
            message,
            "Input Required",
            JOptionPane.WARNING_MESSAGE);
    }
} 