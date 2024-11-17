/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.MarketingManagement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Business.Business;
import model.MarketModel.Market;
import model.Personnel.Person;

/**
 *
 * @author kal bugrara
 */
public class MarketingPersonDirectory {

    private Map<String, MarketingPersonProfile> marketingPersons;
    private Map<Market, List<MarketingPersonProfile>> marketAssignments;

    public MarketingPersonDirectory() {
        this.marketingPersons = new HashMap<>();
        this.marketAssignments = new HashMap<>();
    }

    public void addMarketingPerson(MarketingPersonProfile profile) {
        marketingPersons.put(profile.getId(), profile);
    }

    public void assignToMarket(MarketingPersonProfile profile, Market market) {
        marketAssignments.computeIfAbsent(market, k -> new ArrayList<>())
                        .add(profile);
        profile.addManagedMarket(market);
    }

    public List<MarketingPersonProfile> getMarketManagers(Market market) {
        return marketAssignments.getOrDefault(market, new ArrayList<>());
    }

    public MarketingPersonProfile findMarketingPerson(String id) {
        return marketingPersons.get(id);
    }

}
