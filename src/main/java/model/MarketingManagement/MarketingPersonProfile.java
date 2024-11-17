/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.MarketingManagement;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;

import model.OrderManagement.Order;
import model.Personnel.Person;
import model.Personnel.Profile;
import model.MarketModel.Market;
import model.MarketModel.PriceAdjustment;
import model.ProductManagement.Product;

/**
 *
 * @author kal bugrara
 */
public class MarketingPersonProfile extends Profile {
    private List<Market> managedMarkets;
    private List<PriceAdjustment> priceAdjustmentHistory;

    public MarketingPersonProfile(Person p) {
        super(p);
        this.managedMarkets = new ArrayList<>();
        this.priceAdjustmentHistory = new ArrayList<>();
    }

    public void addSalesOrder(Order o){
        // Implementation for adding sales order
    }

    @Override
    public String getRole(){
        return  "Marketing";
    }

    public void recommendPriceAdjustment(Product product, double adjustment, String justification) {
        PriceAdjustment priceAdjustment = new PriceAdjustment(product, adjustment, justification);
        priceAdjustmentHistory.add(priceAdjustment);
    }

    public List<PriceAdjustment> getRecentAdjustments() {
        return priceAdjustmentHistory.stream()
            .sorted(Comparator.comparing(PriceAdjustment::getDate).reversed())
            .collect(Collectors.toList());
    }
}
