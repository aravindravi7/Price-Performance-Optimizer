package ui;

import javax.swing.*;
import java.awt.*;
import controller.PricingController;
import model.Business.Business;
import ui.panels.LoginPanel;
import ui.panels.ProductPanel;
import ui.panels.AdminPanel;
import ui.panels.CustomerProductPanel;

public class MainPricingFrame extends JFrame {
    private Business business;
    private PricingController controller;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private LoginPanel loginPanel;
    private ProductPanel productPanel;
    private AdminPanel adminPanel;
    private CustomerProductPanel customerProductPanel;
    
    public MainPricingFrame(Business business, PricingController controller) {
        this.business = business;
        this.controller = controller;
        setupFrame();
        createComponents();
    }
    
    private void setupFrame() {
        setTitle("Pricing Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
    }
    
    private void createComponents() {
        // Create card layout and main panel
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        // Create panels
        loginPanel = new LoginPanel(cardLayout, mainPanel, business, controller);
        productPanel = new ProductPanel(business, controller, cardLayout, mainPanel);
        adminPanel = new AdminPanel(business, controller, cardLayout, mainPanel);
        customerProductPanel = new CustomerProductPanel(business, controller, cardLayout, mainPanel);
        
        // Add panels to card layout
        mainPanel.add(loginPanel, "LOGIN");
        mainPanel.add(productPanel, "PRODUCT");
        mainPanel.add(adminPanel, "ADMIN");
        mainPanel.add(customerProductPanel, "CUSTOMER_PRODUCT");
        
        // Start with login panel
        cardLayout.show(mainPanel, "LOGIN");
        
        // Add main panel to frame
        add(mainPanel);
    }
    
    public void showLoginPanel() {
        cardLayout.show(mainPanel, "LOGIN");
    }
    
    public void showProductPanel() {
        cardLayout.show(mainPanel, "PRODUCT");
    }
    
    public void showCustomerProductPanel() {
        cardLayout.show(mainPanel, "CUSTOMER_PRODUCT");
    }
    
    public void showAdminPanel() {
        cardLayout.show(mainPanel, "ADMIN");
    }
} 