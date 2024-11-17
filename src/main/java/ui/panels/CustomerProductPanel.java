package ui.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import model.Business.Business;
import model.ProductManagement.Product;
import model.OrderManagement.Order;
import model.OrderManagement.OrderItem;
import ui.models.CustomerProductTableModel;
import controller.PricingController;

public class CustomerProductPanel extends JPanel {
    private Business business;
    private PricingController controller;
    private CardLayout cardLayout;
    private JPanel parentPanel;
    private JTable productTable;
    private CustomerProductTableModel tableModel;
    private List<OrderItem> cart;
    private JLabel totalLabel;
    private DefaultListModel<String> cartListModel;
    
    public CustomerProductPanel(Business business, PricingController controller, CardLayout layout, JPanel parent) {
        this.business = business;
        this.controller = controller;
        this.cardLayout = layout;
        this.parentPanel = parent;
        this.cart = new ArrayList<>();
        initializePanel();
    }
    
    private void initializePanel() {
        setLayout(new BorderLayout(10, 10));
        
        // Add header
        add(createHeaderPanel(), BorderLayout.NORTH);
        
        // Create split pane for products and cart
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(createProductPanel());
        splitPane.setRightComponent(createCartPanel());
        splitPane.setDividerLocation(600);
        
        add(splitPane, BorderLayout.CENTER);
        
        // Initial product load
        refreshProductTable();
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        // Back button
        JButton backButton = new JButton("← Back to Login");
        backButton.addActionListener(e -> confirmAndGoBack());
        
        // Welcome message
        JLabel welcomeLabel = new JLabel("Welcome Customer! Browse Our Products", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        headerPanel.add(backButton, BorderLayout.WEST);
        headerPanel.add(welcomeLabel, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    private JPanel createProductPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Available Products"));
        
        // Create product table
        tableModel = new CustomerProductTableModel();
        productTable = new JTable(tableModel);
        setupProductTable();
        
        // Add search panel at the top
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search Products: "));
        JTextField searchField = new JTextField(20);
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { search(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { search(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { search(); }
            
            private void search() {
                String text = searchField.getText();
                tableModel.filterProducts(text);
            }
        });
        searchPanel.add(searchField);
        
        // Add quantity spinner and add to cart button
        JPanel quantityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        quantityPanel.add(new JLabel("Quantity:"));
        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        quantityPanel.add(quantitySpinner);
        
        JButton addToCartButton = new JButton("Add to Cart");
        addToCartButton.addActionListener(e -> addToCart((Integer)quantitySpinner.getValue()));
        quantityPanel.add(addToCartButton);
        
        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(productTable), BorderLayout.CENTER);
        panel.add(quantityPanel, BorderLayout.SOUTH);
        
        return panel;
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
    }
    
    private JPanel createCartPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Shopping Cart"));
        
        // Cart items list
        cartListModel = new DefaultListModel<>();
        JList<String> cartList = new JList<>(cartListModel);
        cartList.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Total label
        totalLabel = new JLabel("Total: $0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton clearButton = new JButton("Clear Cart");
        JButton checkoutButton = new JButton("Checkout");
        
        clearButton.addActionListener(e -> clearCart());
        checkoutButton.addActionListener(e -> checkout());
        
        buttonPanel.add(clearButton);
        buttonPanel.add(checkoutButton);
        
        panel.add(new JScrollPane(cartList), BorderLayout.CENTER);
        panel.add(totalLabel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void refreshProductTable() {
        // Get all active products from all suppliers
        List<Product> availableProducts = business.getProductCatalog().getProductList().stream()
            .filter(Product::isActive)  // Only show active products
            .toList();
        
        // Debug prints
        System.out.println("CustomerPanel - Refreshing product table");
        System.out.println("Total products in catalog: " + business.getProductCatalog().getProductList().size());
        System.out.println("Active products available: " + availableProducts.size());
        
        // Print each available product for debugging
        availableProducts.forEach(p -> 
            System.out.println("Product: " + p.getName() + 
                             ", Price: $" + p.getTargetPrice() + 
                             ", Supplier: " + (p.getSupplier() != null ? p.getSupplier().getName() : "None")));
        
        tableModel.setProducts(availableProducts);
        tableModel.fireTableDataChanged();
    }
    
    private void addToCart(int quantity) {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow >= 0) {
            Product product = tableModel.getProductAt(selectedRow);
            
            // Debug print
            System.out.println("Adding to cart: " + product.getName() + " x" + quantity);
            
            OrderItem item = new OrderItem(product, quantity, product.getTargetPrice());
            cart.add(item);
            updateCartDisplay();
            
            JOptionPane.showMessageDialog(this,
                product.getName() + " (x" + quantity + ") added to cart",
                "Added to Cart",
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                "Please select a product to add to cart",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void updateCartDisplay() {
        cartListModel.clear();
        double total = 0.0;
        
        System.out.println("Updating cart display with " + cart.size() + " items"); // Debug
        
        for (OrderItem item : cart) {
            double itemTotal = item.getQuantity() * item.getActualPrice();
            String cartItem = String.format("%s (x%d) - $%.2f",
                item.getProduct().getName(),
                item.getQuantity(),
                itemTotal);
            cartListModel.addElement(cartItem);
            total += itemTotal;
            
            // Debug print
            System.out.println("Cart item: " + cartItem);
        }
        
        totalLabel.setText(String.format("Total: $%.2f", total));
        System.out.println("Cart total updated: $" + String.format("%.2f", total));
    }
    
    private void clearCart() {
        if (!cart.isEmpty()) {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to clear your cart?",
                "Confirm Clear Cart",
                JOptionPane.YES_NO_OPTION);
                
            if (confirm == JOptionPane.YES_OPTION) {
                cart.clear();
                updateCartDisplay();
                System.out.println("Cart cleared");
            }
        }
    }
    
    private void checkout() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Your cart is empty",
                "Cannot Checkout",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // Create a new order
            Order order = business.getMasterOrderList().newOrder();
            
            // Debug print
            System.out.println("Creating new order: " + order.getOrderId());
            System.out.println("Cart items: " + cart.size());
            
            // Add all items from cart to order
            for (OrderItem item : cart) {
                order.addOrderItem(item);
                System.out.println("Added to order: " + item.getProduct().getName() + 
                                 " x" + item.getQuantity());
            }
            
            // Calculate total
            double total = cart.stream()
                .mapToDouble(item -> item.getQuantity() * item.getActualPrice())
                .sum();
            
            // Show success message with order details
            StringBuilder message = new StringBuilder();
            message.append("Order placed successfully!\n\n");
            message.append("Order ID: ").append(order.getOrderId()).append("\n");
            message.append("Items:\n");
            
            for (OrderItem item : cart) {
                message.append("- ")
                       .append(item.getProduct().getName())
                       .append(" (x").append(item.getQuantity()).append(")")
                       .append(" $").append(String.format("%.2f", item.getQuantity() * item.getActualPrice()))
                       .append("\n");
            }
            
            message.append("\nTotal: $").append(String.format("%.2f", total));
            
            JOptionPane.showMessageDialog(this,
                message.toString(),
                "Order Confirmation",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Clear the cart after successful checkout
            cart.clear();
            updateCartDisplay();
            
            System.out.println("Checkout completed successfully");
            
        } catch (Exception e) {
            System.err.println("Error during checkout: " + e.getMessage());
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(this,
                "An error occurred while processing your order.\nPlease try again.",
                "Checkout Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void confirmAndGoBack() {
        if (!cart.isEmpty()) {
            int result = JOptionPane.showConfirmDialog(this,
                "You have items in your cart. Are you sure you want to leave?",
                "Confirm Navigation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
                
            if (result != JOptionPane.YES_OPTION) {
                return;
            }
        }
        cardLayout.show(parentPanel, "LOGIN");
    }
    
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            System.out.println("CustomerPanel - Panel becoming visible"); // Debug print
            refreshProductTable();
        }
    }
    
    @Override
    public void addNotify() {
        super.addNotify();
        System.out.println("CustomerPanel - Panel being added to container"); // Debug print
        refreshProductTable();
    }
}
