package model.UserAccountManagement;

import model.ProductManagement.Product;
import java.time.LocalDateTime;
import java.util.UUID;

public class PriceAdjustment {
    private String id;
    private Product product;
    private double oldPrice;
    private double newPrice;
    private UserAccount requestor;
    private LocalDateTime requestTime;
    private boolean approved;
    private UserAccount approver;
    private LocalDateTime approvalTime;
    private String justification;

    public PriceAdjustment(Product product, double newPrice, UserAccount requestor) {
        this.id = UUID.randomUUID().toString();
        this.product = product;
        this.oldPrice = product.getTargetPrice();
        this.newPrice = newPrice;
        this.requestor = requestor;
        this.requestTime = LocalDateTime.now();
        this.approved = false;
    }

    public void approve(UserAccount approver) {
        this.approved = true;
        this.approver = approver;
        this.approvalTime = LocalDateTime.now();
    }

    // Getters and setters
    public String getId() { return id; }
    public Product getProduct() { return product; }
    public double getOldPrice() { return oldPrice; }
    public double getNewPrice() { return newPrice; }
    public UserAccount getRequestor() { return requestor; }
    public LocalDateTime getRequestTime() { return requestTime; }
    public boolean isApproved() { return approved; }
    public UserAccount getApprover() { return approver; }
    public LocalDateTime getApprovalTime() { return approvalTime; }
    public String getJustification() { return justification; }
    public void setJustification(String justification) { this.justification = justification; }
} 