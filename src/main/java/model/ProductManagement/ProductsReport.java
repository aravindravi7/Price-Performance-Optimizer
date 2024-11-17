/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.ProductManagement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author kal bugrara
 */
public class ProductsReport {

    private List<ProductSummary> productSummaries;
    private Date reportDate;

    public ProductsReport() {
        this.productSummaries = new ArrayList<>();
        this.reportDate = new Date();
    }

    public void generateReport(ProductCatalog catalog, MasterOrderList orderList) {
        for (Product product : catalog.getProducts()) {
            ProductSummary summary = new ProductSummary(product);
            summary.analyzePerformance(orderList);
            productSummaries.add(summary);
        }
    }

    public String getReportContent() {
        StringBuilder report = new StringBuilder();
        report.append("Product Performance Report - ").append(reportDate).append("\n\n");
        
        for (ProductSummary summary : productSummaries) {
            report.append(formatProductSummary(summary)).append("\n");
        }
        
        return report.toString();
    }

    public ProductSummary getTopProductAboveTarget() {
        ProductSummary currenttopproduct = null;

        for (ProductSummary ps : productSummaries) {
            if (currenttopproduct == null) {
                currenttopproduct = ps; // initial step 
            } else if (ps.getNumberAboveTarget() > currenttopproduct.getNumberAboveTarget()) {
                currenttopproduct = ps; //we have a new higher total
            }

        }
        return currenttopproduct;
    }

    public ArrayList<ProductSummary> getProductsAlwaysAboveTarget() {
        ArrayList<ProductSummary> productsalwaysabovetarget = new ArrayList(); //temp array list

        for (ProductSummary ps : productSummaries) {
            if (ps.isProductAlwaysAboveTarget() == true) {
                productsalwaysabovetarget.add(ps);
            }
        }

        return productsalwaysabovetarget;
    }

}
