import javax.swing.*;
import model.Business.Business;
import controller.PricingController;
import ui.MainPricingFrame;

public class PricingApplication {
    private Business business;
    private PricingController controller;
    private MainPricingFrame mainFrame;
    
    public PricingApplication() {
        initializeApplication();
    }
    
    private void initializeApplication() {
        business = new Business();
        business.setName("Pricing Management System");
        controller = new PricingController(business);
        mainFrame = new MainPricingFrame(business, controller);
        mainFrame.setVisible(true);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PricingApplication());
    }
} 