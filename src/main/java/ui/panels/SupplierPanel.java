package ui.panels;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import controller.PricingController;
import model.Business.Business;
import model.CustomerManagement.Customer;
import model.OrderManagement.Order;
import model.OrderManagement.OrderItem;
import model.ProductManagement.Product;

public class CustomerPanel extends JPanel {
    private Business business;
    private PricingController controller;
    private Customer currentCustomer;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JTable productTable;
    private ProductTableModel productTableModel;
    private JTable cartTable;
    private CartTableModel cartTableModel;
    private JTable orderHistoryTable;
    private OrderHistoryTableModel orderHistoryTableModel;
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
        JButton shopBtn = new JButton("Shop Products");
        JButton cartBtn = new JButton("Shopping Cart");
        JButton ordersBtn = new JButton("Order History");
        JButton logoutBtn = new JButton("Logout");
        
        navPanel.add(shopBtn);
        navPanel.add(cartBtn);
        navPanel.add(ordersBtn);
        navPanel.add(logoutBtn);
        
        // Create card panel
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        
        // Create panels
        JPanel shopPanel = createShopPanel();
        JPanel cartPanel = createCartPanel();
        JPanel orderHistoryPanel = createOrderHistoryPanel();
        
        cardPanel.add(shopPanel, "SHOP");
        cardPanel.add(cartPanel, "CART");
        cardPanel.add(orderHistoryPanel, "ORDERS");
        
        // Add action listeners
        shopBtn.addActionListener(e -> cardLayout.show(cardPanel, "SHOP"));
        cartBtn.addActionListener(e -> {
            updateCartPanel();
            cardLayout.show(cardPanel, "CART");
        });
        ordersBtn.addActionListener(e -> {
            updateOrderHistoryPanel();
            cardLayout.show(cardPanel, "ORDERS");
        });
        logoutBtn.addActionListener(e -> handleLogout());
        
        add(navPanel, BorderLayout.NORTH);
        add(cardPanel, BorderLayout.CENTER);
    }
    
    private JPanel createShopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Create search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField searchField = new JTextField(20);
        JButton searchBtn = new JButton("Search");
        searchPanel.add(new JLabel("Search: "));
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        
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
        
        // Add action listeners
        searchBtn.addActionListener(e -> searchProducts(searchField.getText()));
        addToCartBtn.addActionListener(e -> addToCart(
            (Integer)quantitySpinner.getValue()
        ));
        
        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createCartPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Create cart table
        cartTableModel = new CartTableModel(shoppingCart);
        cartTable = new JTable(cartTableModel);
        JScrollPane scrollPane = new JScrollPane(cartTable);
        
        // Create total panel
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalLabel = new JLabel("Total: $0.00");
        totalPanel.add(totalLabel);
        
        // Create buttons panel
        JPanel buttonPanel = new JPanel();
        JButton updateBtn = new JButton("Update Quantity");
        JButton removeBtn = new JButton("Remove Item");
        JButton checkoutBtn = new JButton("Checkout");
        buttonPanel.add(updateBtn);
        buttonPanel.add(removeBtn);
        buttonPanel.add(checkoutBtn);
        
        // Add action listeners
        updateBtn.addActionListener(e -> updateCartItem());
        removeBtn.addActionListener(e -> removeFromCart());
        checkoutBtn.addActionListener(e -> checkout());
        
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(totalPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createOrderHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Create order history table
        orderHistoryTableModel = new OrderHistoryTableModel(new ArrayList<>());
        orderHistoryTable = new JTable(orderHistoryTableModel);
        JScrollPane scrollPane = new JScrollPane(orderHistoryTable);
        
        // Create details panel
        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Order Details"));
        JTextArea detailsArea = new JTextArea(10, 40);
        detailsArea.setEditable(false);
        detailsPanel.add(new JScrollPane(detailsArea), BorderLayout.CENTER);
        
        // Add selection listener
        orderHistoryTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateOrderDetails(detailsArea);
            }
        });
        
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(detailsPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void searchProducts(String query) {
        List<Product> allProducts = business.getProductCatalog().getProductList();
        List<Product> filteredProducts = new ArrayList<>();
        
        for (Product product : allProducts) {
            if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredProducts.add(product);
            }
        }
        
        productTableModel.setProducts(filteredProducts);
        productTableModel.fireTableDataChanged();
    }
    
    private void addToCart(int quantity) {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow >= 0) {
            Product product = productTableModel.getProductAt(selectedRow);
            shoppingCart.merge(product, quantity, Integer::sum);
            updateCartPanel();
        }
    }
    
    private void updateCartItem() {
        int selectedRow = cartTable.getSelectedRow();
        if (selectedRow >= 0) {
            Product product = cartTableModel.getProductAt(selectedRow);
            String input = JOptionPane.showInputDialog(this,
                "Enter new quantity:",
                "Update Quantity",
                JOptionPane.PLAIN_MESSAGE);
                
            try {
                int quantity = Integer.parseInt(input);
                if (quantity > 0) {
                    shoppingCart.put(product, quantity);
                } else {
                    shoppingCart.remove(product);
                }
                updateCartPanel();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                    "Please enter a valid number",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void removeFromCart() {
        int selectedRow = cartTable.getSelectedRow();
        if (selectedRow >= 0) {
            Product product = cartTableModel.getProductAt(selectedRow);
            shoppingCart.remove(product);
            updateCartPanel();
        }
    }
    
    private void checkout() {
        if (shoppingCart.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Your cart is empty",
                "Cannot Checkout",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Order order = business.getMasterOrderList().newOrder(currentCustomer);
        
        for (Map.Entry<Product, Integer> entry : shoppingCart.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            OrderItem orderItem = order.newOrderItem(product, quantity, product.getTargetPrice());
        }
        
        shoppingCart.clear();
        updateCartPanel();
        
        JOptionPane.showMessageDialog(this,
            "Order placed successfully!\nOrder ID: " + order.getOrderId(),
            "Order Confirmation",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void updateCartPanel() {
        cartTableModel.setCartItems(shoppingCart);
        cartTableModel.fireTableDataChanged();
        
        double total = calculateTotal();
        totalLabel.setText(String.format("Total: $%.2f", total));
    }
    
    private void updateOrderHistoryPanel() {
        List<Order> orders = business.getMasterOrderList().getOrdersByCustomer(currentCustomer);
        orderHistoryTableModel.setOrders(orders);
        orderHistoryTableModel.fireTableDataChanged();
    }
    
    private void updateOrderDetails(JTextArea detailsArea) {
        int selectedRow = orderHistoryTable.getSelectedRow();
        if (selectedRow >= 0) {
            Order order = orderHistoryTableModel.getOrderAt(selectedRow);
            StringBuilder details = new StringBuilder();
            details.append("Order ID: ").append(order.getOrderId()).append("\n");
            details.append("Date: ").append(order.getOrderDate()).append("\n\n");
            details.append("Items:\n");
            
            for (OrderItem item : order.getOrderItems()) {
                details.append(String.format("- %s (Qty: %d) @ $%.2f = $%.2f\n",
                    item.getProduct().getName(),
                    item.getQuantity(),
                    item.getActualPrice(),
                    item.getQuantity() * item.getActualPrice()));
            }
            
            details.append("\nTotal: $").append(String.format("%.2f", order.getOrderTotal()));
            detailsArea.setText(details.toString());
        } else {
            detailsArea.setText("");
        }
    }
    
    private double calculateTotal() {
        return shoppingCart.entrySet().stream()
            .mapToDouble(entry -> entry.getKey().getTargetPrice() * entry.getValue())
            .sum();
    }
    
    private void handleLogout() {
        shoppingCart.clear();
        Container parent = getParent();
        while (parent != null && !(parent.getLayout() instanceof CardLayout)) {
            parent = parent.getParent();
        }
        
        if (parent != null) {
            CardLayout layout = (CardLayout) parent.getLayout();
            layout.show(parent, "LOGIN");
        }
    }
    
    public void setCurrentCustomer(Customer customer) {
        this.currentCustomer = customer;
        updateOrderHistoryPanel();
    }
} 