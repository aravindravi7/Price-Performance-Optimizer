package ui.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;
import model.Business.Business;
import model.ProductManagement.Product;
import model.Supplier.Supplier;
import ui.models.ProductTableModel;
import controller.PricingController;

public class ProductPanel extends JPanel {
    private Business business;
    private PricingController controller;
    private CardLayout cardLayout;
    private JPanel parentPanel;
    private JTable productTable;
    private ProductTableModel tableModel;
    private JLabel supplierNameLabel;
    
    public ProductPanel(Business business, PricingController controller, CardLayout layout, JPanel parent) {
        this.business = business;
        this.controller = controller;
        this.cardLayout = layout;
        this.parentPanel = parent;
        initializePanel();
    }
    
    private void initializePanel() {
        setLayout(new BorderLayout(10, 10));
        
        // Create header panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Create content panel
        JPanel contentPanel = createContentPanel();
        add(contentPanel, BorderLayout.CENTER);
        
        // Initial update
        updateSupplierInfo();
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        // Left side - Back button
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("← Back to Login");
        backButton.addActionListener(e -> confirmAndGoBack());
        navPanel.add(backButton);
        
        // Right side - Supplier info
        JPanel supplierPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        supplierPanel.add(new JLabel("Logged in as: "));
        supplierNameLabel = new JLabel();
        supplierNameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        supplierPanel.add(supplierNameLabel);
        
        headerPanel.add(navPanel, BorderLayout.WEST);
        headerPanel.add(supplierPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create table
        tableModel = new ProductTableModel();
        productTable = new JTable(tableModel);
        setupProductTable();
        JScrollPane scrollPane = new JScrollPane(productTable);
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JButton addBtn = new JButton("Add Product");
        JButton editBtn = new JButton("Edit Product");
        JButton removeBtn = new JButton("Remove Product");
        JButton toggleBtn = new JButton("Toggle Active Status");
        
        addBtn.addActionListener(e -> addProduct());
        editBtn.addActionListener(e -> editProduct());
        removeBtn.addActionListener(e -> removeProduct());
        toggleBtn.addActionListener(e -> toggleProductStatus());
        
        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(removeBtn);
        buttonPanel.add(toggleBtn);
        
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        return contentPanel;
    }
    
    private void setupProductTable() {
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productTable.setRowHeight(25);
        
        // Center align all columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < productTable.getColumnCount(); i++) {
            productTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // Set column widths
        productTable.getColumnModel().getColumn(0).setPreferredWidth(200); // Name
        productTable.getColumnModel().getColumn(1).setPreferredWidth(100); // Price
        productTable.getColumnModel().getColumn(2).setPreferredWidth(80);  // Status
    }
    
    private void updateSupplierInfo() {
        Supplier currentSupplier = controller.getCurrentSupplier();
        System.out.println("ProductPanel - Updating supplier info"); // Debug print
        System.out.println("ProductPanel - Current supplier: " + 
            (currentSupplier != null ? currentSupplier.getName() + " (ID: " + currentSupplier.getId() + ")" : "null"));
        
        if (currentSupplier != null) {
            supplierNameLabel.setText(currentSupplier.getName());
        } else {
            supplierNameLabel.setText("No supplier selected");
        }
        supplierNameLabel.revalidate();
        supplierNameLabel.repaint();
    }
    
    private void addProduct() {
        // First verify we have a supplier
        Supplier currentSupplier = controller.getCurrentSupplier();
        if (currentSupplier == null) {
            JOptionPane.showMessageDialog(this,
                "No supplier selected. Please log in again.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            cardLayout.show(parentPanel, "LOGIN");
            return;
        }

        String name = JOptionPane.showInputDialog(this, "Enter Product Name:");
        if (name != null && !name.trim().isEmpty()) {
            try {
                String priceStr = JOptionPane.showInputDialog(this, "Enter Target Price:");
                if (priceStr != null && !priceStr.trim().isEmpty()) {
                    double price = Double.parseDouble(priceStr);
                    if (price < 0) {
                        throw new NumberFormatException("Price cannot be negative");
                    }
                    
                    // Create and set up the product
                    Product newProduct = business.getProductCatalog().newProduct(name);
                    newProduct.setTargetPrice(price);
                    newProduct.setSupplier(currentSupplier);
                    newProduct.setActive(true);
                    
                    System.out.println("Adding product: " + name); // Debug
                    System.out.println("For supplier: " + currentSupplier.getName()); // Debug
                    
                    // Refresh the table
                    refreshProductTable();
                    
                    JOptionPane.showMessageDialog(this,
                        "Product added successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                    "Invalid price format. Please enter a valid positive number.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void editProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow >= 0) {
            Product product = tableModel.getProductAt(selectedRow);
            
            // Edit name
            String newName = JOptionPane.showInputDialog(this,
                "Enter new name for product:",
                product.getName());
                
            if (newName != null && !newName.trim().isEmpty()) {
                try {
                    // Edit price
                    String newPriceStr = JOptionPane.showInputDialog(this,
                        "Enter new price for product:",
                        product.getTargetPrice());
                        
                    if (newPriceStr != null && !newPriceStr.trim().isEmpty()) {
                        double newPrice = Double.parseDouble(newPriceStr);
                        if (newPrice < 0) {
                            throw new NumberFormatException("Price cannot be negative");
                        }
                        
                        // Update product
                        product.setName(newName);
                        product.setTargetPrice(newPrice);
                        
                        // Refresh table
                        refreshProductTable();
                        
                        // Show success message
                        JOptionPane.showMessageDialog(this,
                            "Product updated successfully!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this,
                        "Invalid price format. Please enter a valid positive number.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "Please select a product to edit",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void removeProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow >= 0) {
            Product product = tableModel.getProductAt(selectedRow);
            
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete " + product.getName() + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
                
            if (confirm == JOptionPane.YES_OPTION) {
                controller.deleteProduct(product);
                refreshProductTable();
                
                JOptionPane.showMessageDialog(this,
                    "Product deleted successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "Please select a product to remove",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void toggleProductStatus() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow >= 0) {
            Product product = tableModel.getProductAt(selectedRow);
            product.setActive(!product.isActive());
            refreshProductTable();
        } else {
            JOptionPane.showMessageDialog(this,
                "Please select a product to toggle status",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void refreshProductTable() {
        Supplier currentSupplier = controller.getCurrentSupplier();
        if (currentSupplier == null) {
            System.out.println("Warning: No supplier selected during refresh"); // Debug
            return;
        }

        System.out.println("Refreshing products for: " + currentSupplier.getName()); // Debug
        
        List<Product> allProducts = business.getProductCatalog().getProductList();
        System.out.println("Total products in catalog: " + allProducts.size()); // Debug
        
        List<Product> supplierProducts = allProducts.stream()
            .filter(p -> p.getSupplier() != null && 
                        p.getSupplier().getId().equals(currentSupplier.getId()))
            .toList();
            
        System.out.println("Filtered products for supplier: " + supplierProducts.size()); // Debug
        
        tableModel.setProducts(supplierProducts);
        tableModel.fireTableDataChanged();
    }
    
    private void confirmAndGoBack() {
        int result = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to log out?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
            
        if (result == JOptionPane.YES_OPTION) {
            controller.setCurrentSupplier(null);
            cardLayout.show(parentPanel, "LOGIN");
        }
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            System.out.println("ProductPanel - Panel becoming visible"); // Debug print
            updateSupplierInfo();
            refreshProductTable();
        }
    }
} 