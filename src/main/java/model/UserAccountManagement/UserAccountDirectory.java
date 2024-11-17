/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.UserAccountManagement;

import java.util.*;

/**
 *
 * @author kal bugrara
 */
public class UserAccountDirectory {
    private Map<String, UserAccount> accounts;
    private Map<String, Set<PricingAction>> rolePermissions;

    public UserAccountDirectory() {
        this.accounts = new HashMap<>();
        this.rolePermissions = new HashMap<>();
        initializeRoles();
    }

    private void initializeRoles() {
        // Initialize Marketing Manager role
        Set<PricingAction> marketingManagerPermissions = new HashSet<>(Arrays.asList(
            PricingAction.VIEW_PRICES,
            PricingAction.ADJUST_PRICE,
            PricingAction.APPROVE_CHANGE,
            PricingAction.VIEW_METRICS,
            PricingAction.SIMULATE_PRICES,
            PricingAction.GENERATE_REPORTS
        ));
        rolePermissions.put("MARKETING_MANAGER", marketingManagerPermissions);

        // Initialize Sales Person role
        Set<PricingAction> salesPersonPermissions = new HashSet<>(Arrays.asList(
            PricingAction.VIEW_PRICES,
            PricingAction.VIEW_METRICS
        ));
        rolePermissions.put("SALES_PERSON", salesPersonPermissions);
    }

    public void createAccount(String username, String password, String role) {
        String passwordHash = hashPassword(password);
        UserAccount account = new UserAccount(username, passwordHash);
        
        // Assign role permissions
        Set<PricingAction> permissions = rolePermissions.get(role);
        if (permissions != null) {
            permissions.forEach(account::grantPermission);
        }
        
        accounts.put(username, account);
    }

    public boolean hasPermission(UserAccount account, PricingAction action) {
        return account != null && account.isActive() && account.hasPermission(action);
    }

    public UserAccount authenticate(String username, String password) {
        UserAccount account = accounts.get(username);
        if (account != null && account.isActive() && 
            verifyPassword(password, account.getPasswordHash())) {
            account.recordLogin();
            return account;
        }
        return null;
    }

    private String hashPassword(String password) {
        // Implementation of password hashing
        // In a real application, use a proper hashing algorithm like BCrypt
        return password; // Simplified for demonstration
    }

    private boolean verifyPassword(String password, String hash) {
        // Implementation of password verification
        return password.equals(hash); // Simplified for demonstration
    }
}
