package model.Personnel;

import model.UserAccountManagement.UserAccount;
import model.MarketModel.Market;
import model.ProductManagement.Product;
import model.ProductManagement.ProductsReport;
import model.UserAccountManagement.PriceAdjustment;
import java.util.*;

public class MarketingPersonProfile extends EmployeeProfile {
    private List<Market> managedMarkets;
    private List<Product> managedProducts;
    private List<PriceAdjustment> pendingAdjustments;
    private Map<String, ProductsReport> marketReports;

    public MarketingPersonProfile(String id, String name, UserAccount account) {
        super(id, name, account);
        this.managedMarkets = new ArrayList<>();
        this.managedProducts = new ArrayList<>();
        this.pendingAdjustments = new ArrayList<>();
        this.marketReports = new HashMap<>();
    }

    public void addManagedMarket(Market market) {
        if (!managedMarkets.contains(market)) {
            managedMarkets.add(market);
        }
    }

    public void addManagedProduct(Product product) {
        if (!managedProducts.contains(product)) {
            managedProducts.add(product);
        }
    }

    public void reviewPriceAdjustment(PriceAdjustment adjustment) {
        if (canReviewAdjustment(adjustment)) {
            pendingAdjustments.add(adjustment);
        }
    }

    public void approvePriceAdjustment(PriceAdjustment adjustment, String justification) {
        if (pendingAdjustments.contains(adjustment)) {
            adjustment.setJustification(justification);
            adjustment.approve(this.getUserAccount());
            pendingAdjustments.remove(adjustment);
            
            // Update product price
            Product product = adjustment.getProduct();
            product.updateTargetPrice(adjustment.getNewPrice());
        }
    }

    public void rejectPriceAdjustment(PriceAdjustment adjustment, String reason) {
        if (pendingAdjustments.contains(adjustment)) {
            adjustment.setJustification(reason);
            pendingAdjustments.remove(adjustment);
        }
    }

    public void reviewMarketReport(ProductsReport report) {
        String marketId = report.getMarketId();
        marketReports.put(marketId, report);
        
        // Analyze report and take necessary actions
        analyzeAndActOnReport(report);
    }

    private boolean canReviewAdjustment(PriceAdjustment adjustment) {
        Product product = adjustment.getProduct();
        return managedProducts.contains(product) || 
               managedMarkets.contains(product.getMarket());
    }

    private void analyzeAndActOnReport(ProductsReport report) {
        report.getProductPerformanceMetrics().forEach((product, metrics) -> {
            if (metrics.isUnderperforming()) {
                // Create price adjustment if needed
                double recommendedPrice = metrics.getRecommendedPrice();
                if (Math.abs(recommendedPrice - product.getTargetPrice()) > 0.05) {
                    PriceAdjustment adjustment = new PriceAdjustment(
                        product, 
                        recommendedPrice,
                        this.getUserAccount()
                    );
                    reviewPriceAdjustment(adjustment);
                }
            }
        });
    }

    // Getters
    public List<Market> getManagedMarkets() {
        return new ArrayList<>(managedMarkets);
    }

    public List<Product> getManagedProducts() {
        return new ArrayList<>(managedProducts);
    }

    public List<PriceAdjustment> getPendingAdjustments() {
        return new ArrayList<>(pendingAdjustments);
    }
} 