package ui;

import javax.swing.*;
import java.awt.*;
import controller.PricingController;
import model.Business.Business;
import model.OrderManagement.Order;
import model.OrderManagement.OrderItem;
import model.ProductManagement.Product;
import model.Personnel.Profile;
import java.util.HashMap;
import java.util.Map;

public class CustomerPanel extends JPanel {
    private Business business;
    private PricingController controller;
    private Profile currentCustomer;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JTable productTable;
    private ProductTableModel productTableModel;
    private Map<Product, Integer> shoppingCart;
    private JLabel totalLabel;
    
    public CustomerPanel(Business business, PricingController controller) {
        this.business = business;
        this.controller = controller;
        this.shoppingCart = new HashMap<>();
        initializePanel();
    }
    
    private void initializePanel() {
        setLayout(new BorderLayout());
        
        // Create navigation panel
        JPanel navPanel = new JPanel();
        JButton shopBtn = new JButton("Browse Products");
        JButton cartBtn = new JButton("View Cart");
        
        navPanel.add(shopBtn);
        navPanel.add(cartBtn);
        
        // Create card panel
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        
        // Create panels
        JPanel shopPanel = createShopPanel();
        JPanel cartPanel = createCartPanel();
        
        cardPanel.add(shopPanel, "SHOP");
        cardPanel.add(cartPanel, "CART");
        
        // Add action listeners
        shopBtn.addActionListener(e -> cardLayout.show(cardPanel, "SHOP"));
        cartBtn.addActionListener(e -> {
            updateCartDisplay();
            cardLayout.show(cardPanel, "CART");
        });
        
        add(navPanel, BorderLayout.NORTH);
        add(cardPanel, BorderLayout.CENTER);
        
        // Start with shop panel
        cardLayout.show(cardPanel, "SHOP");
    }
    
    private JPanel createShopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Create product table
        productTableModel = new ProductTableModel(business.getProductCatalog().getProductList());
        productTable = new JTable(productTableModel);
        JScrollPane scrollPane = new JScrollPane(productTable);
        
        // Create buttons panel
        JPanel buttonPanel = new JPanel();
        JButton addToCartBtn = new JButton("Add to Cart");
        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        
        buttonPanel.add(new JLabel("Quantity: "));
        buttonPanel.add(quantitySpinner);
        buttonPanel.add(addToCartBtn);
        
        addToCartBtn.addActionListener(e -> {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow >= 0) {
                Product product = productTableModel.getProductAt(selectedRow);
                int quantity = (Integer) quantitySpinner.getValue();
                shoppingCart.merge(product, quantity, Integer::sum);
                JOptionPane.showMessageDialog(this,
                    "Added to cart successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Please select a product",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            }
        });
        
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createCartPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Create cart display
        DefaultListModel<String> cartListModel = new DefaultListModel<>();
        JList<String> cartList = new JList<>(cartListModel);
        JScrollPane scrollPane = new JScrollPane(cartList);
        
        // Create total panel
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalLabel = new JLabel("Total: $0.00");
        totalPanel.add(totalLabel);
        
        // Create buttons panel
        JPanel buttonPanel = new JPanel();
        JButton removeBtn = new JButton("Remove Selected");
        JButton checkoutBtn = new JButton("Checkout");
        
        removeBtn.addActionListener(e -> {
            int selectedIndex = cartList.getSelectedIndex();
            if (selectedIndex >= 0) {
                Product product = (Product) shoppingCart.keySet().toArray()[selectedIndex];
                shoppingCart.remove(product);
                updateCartDisplay();
            }
        });
        
        checkoutBtn.addActionListener(e -> checkout());
        
        buttonPanel.add(removeBtn);
        buttonPanel.add(checkoutBtn);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(totalPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void updateCartDisplay() {
        double total = 0.0;
        DefaultListModel<String> model = (DefaultListModel<String>) 
            ((JList<?>) ((JScrollPane) cardPanel.getComponent(1).getComponent(0)).getViewport().getView()).getModel();
        model.clear();
        
        for (Map.Entry<Product, Integer> entry : shoppingCart.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            double subtotal = product.getTargetPrice() * quantity;
            total += subtotal;
            
            model.addElement(String.format("%s (x%d) - $%.2f",
                product.getName(),
                quantity,
                subtotal));
        }
        
        totalLabel.setText(String.format("Total: $%.2f", total));
    }
    
    private void checkout() {
        if (shoppingCart.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Your cart is empty",
                "Cannot Checkout",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            Order order = business.getMasterOrderList().newOrder();
            
            for (Map.Entry<Product, Integer> entry : shoppingCart.entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();
                order.newOrderItem(product, quantity, (int)product.getTargetPrice());
            }
            
            shoppingCart.clear();
            updateCartDisplay();
            
            JOptionPane.showMessageDialog(this,
                "Order placed successfully!",
                "Order Confirmation",
                JOptionPane.INFORMATION_MESSAGE);
                
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error placing order: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void setCurrentCustomer(Profile customer) {
        this.currentCustomer = customer;
    }
} 