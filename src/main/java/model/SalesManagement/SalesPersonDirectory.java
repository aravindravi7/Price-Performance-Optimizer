/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.SalesManagement;

import java.util.*;

/**
 *
 * @author kal bugrara
 */
public class SalesPersonDirectory {

    private Map<String, SalesPersonProfile> salesPersons;

    public SalesPersonDirectory() {
        salesPersons = new HashMap<>();
    }

    public void addSalesPerson(SalesPersonProfile salesPerson) {
        salesPersons.put(salesPerson.getId(), salesPerson);
    }

    public SalesPersonProfile findSalesPerson(String id) {
        return salesPersons.get(id);
    }

    public List<SalesPersonProfile> getSalesPersons() {
        return new ArrayList<>(salesPersons.values());
    }
}
