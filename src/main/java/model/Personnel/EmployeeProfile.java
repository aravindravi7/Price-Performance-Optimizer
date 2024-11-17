/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.Personnel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author kal bugrara
 */
public class EmployeeProfile extends Profile {
    private Person person;
    private List<PriceAction> priceActions;
    private Set<String> managedCategories;
    
    public EmployeeProfile(Person person) {
        this.person = person;
        this.priceActions = new ArrayList<>();
        this.managedCategories = new HashSet<>();
    }
    
    public void addPriceAction(Product product, double oldPrice, double newPrice) {
        PriceAction action = new PriceAction(product, oldPrice, newPrice);
        priceActions.add(action);
    }
    
    public List<PriceAction> getPriceActionHistory() {
        return new ArrayList<>(priceActions);
    }
    
    public boolean canManageCategory(String category) {
        return managedCategories.contains(category);
    }
    
    @Override
    public String getRole(){
        return  "Admin";
    }

}