/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.Personnel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Business.Business;

/**
 *
 * @author kal bugrara
 */
public class EmployeeDirectory {

    private Map<String, EmployeeProfile> employees;
    private Map<String, List<EmployeeProfile>> categoryManagers;

    public EmployeeDirectory() {
        this.employees = new HashMap<>();
        this.categoryManagers = new HashMap<>();
    }

    public void addEmployee(EmployeeProfile profile) {
        employees.put(profile.getId(), profile);
        
        // Update category managers
        profile.getManagedCategories().forEach(category -> 
            categoryManagers.computeIfAbsent(category, k -> new ArrayList<>())
                          .add(profile));
    }

    public EmployeeProfile findEmployee(String id) {
        return employees.get(id);
    }

    public List<EmployeeProfile> getManagersForCategory(String category) {
        return categoryManagers.getOrDefault(category, new ArrayList<>());
    }
}
