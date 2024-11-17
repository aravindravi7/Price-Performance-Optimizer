/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.CustomerManagement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author kal bugrara
 */
public class CustomersReport {
    private List<CustomerSummary> customerSummaries;
    private Date reportDate;
    
    public CustomersReport() {
        this.customerSummaries = new ArrayList<>();
        this.reportDate = new Date();
    }
    
    public void generateReport(CustomerDirectory directory) {
        for (CustomerProfile customer : directory.getCustomerList()) {
            CustomerSummary summary = new CustomerSummary(customer);
            customerSummaries.add(summary);
        }
        
        analyzeCustomerSegments();
    }
    
    private void analyzeCustomerSegments() {
        Map<String, List<CustomerSummary>> segments = customerSummaries.stream()
            .collect(Collectors.groupingBy(this::determineSegment));
            
        // Calculate segment metrics
        segments.forEach(this::calculateSegmentMetrics);
    }
    
    private String determineSegment(CustomerSummary summary) {
        double avgOrderValue = summary.getCustomer().getAverageOrderValue();
        if (avgOrderValue > 75000) return "Premium";
        if (avgOrderValue > 40000) return "Mid-tier";
        return "Standard";
    }
    
    private void calculateSegmentMetrics(String segment, List<CustomerSummary> customers) {
        double totalRevenue = customers.stream()
            .mapToDouble(cs -> cs.getCustomer().getTotalSpending())
            .sum();
            
        // Add segment metrics to report
    }
}
