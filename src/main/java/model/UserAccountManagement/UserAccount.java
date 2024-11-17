/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.UserAccountManagement;

import java.util.*;
import java.time.LocalDateTime;

/**
 *
 * @author kal bugrara
 */
public class UserAccount {
    
    private String username;
    private String passwordHash;
    private Set<PricingAction> permissions;
    private List<PriceAdjustment> adjustmentHistory;
    private boolean active;
    private LocalDateTime lastLogin;
    
    public UserAccount(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.permissions = new HashSet<>();
        this.adjustmentHistory = new ArrayList<>();
        this.active = true;
    }
    
    public boolean hasPermission(PricingAction action) {
        return permissions.contains(action);
    }
    
    public void grantPermission(PricingAction action) {
        permissions.add(action);
    }
    
    public void revokePermission(PricingAction action) {
        permissions.remove(action);
    }
    
    public void recordPriceAdjustment(PriceAdjustment adjustment) {
        adjustmentHistory.add(adjustment);
    }
    
    public void recordLogin() {
        this.lastLogin = LocalDateTime.now();
    }
    
    public void deactivate() {
        this.active = false;
    }
    
    public void activate() {
        this.active = true;
    }
    
    // Getters
    public String getUsername() { return username; }
    public boolean isActive() { return active; }
    public LocalDateTime getLastLogin() { return lastLogin; }
    public Set<PricingAction> getPermissions() { 
        return new HashSet<>(permissions); 
    }
    public List<PriceAdjustment> getAdjustmentHistory() { 
        return new ArrayList<>(adjustmentHistory); 
    }
}
