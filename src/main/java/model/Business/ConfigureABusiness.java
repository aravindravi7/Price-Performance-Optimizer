package model.Business;

import model.CustomerManagement.CustomerProfile;
import model.CustomerManagement.CustomerDirectory;
import model.ProductManagement.ProductCatalog;
import model.Supplier.SupplierDirectory;
import model.OrderManagement.MasterOrderList;
import model.OrderManagement.Order;
import model.ProductManagement.Product;
import model.Supplier.Supplier;

public class ConfigureABusiness {
    
    public static Business initialize() {
        try {
            Business business = new Business("Pricing Management System");
            
            // Create suppliers
            createSuppliers(business);
            
            // Create products
            createProducts(business);
            
            // Create customers
            createCustomers(business);
            
            // Create orders
            createOrders(business);
            
            return business;
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize business: " + e.getMessage());
        }
    }

    private static void createSuppliers(Business business) {
        SupplierDirectory supplierDirectory = business.getSupplierDirectory();
        for (int i = 1; i <= 5; i++) {
            supplierDirectory.addSupplier("SUP" + i, "Supplier " + i);
        }
    }

    private static void createProducts(Business business) {
        ProductCatalog productCatalog = business.getProductCatalog();
        SupplierDirectory supplierDirectory = business.getSupplierDirectory();
        
        for (Supplier supplier : supplierDirectory.getSupplierList()) {
            for (int i = 1; i <= 10; i++) {
                double targetPrice = 100.0 + (Math.random() * 900.0);
                productCatalog.addProduct(
                    "PROD" + supplier.getId() + "_" + i,
                    "Product " + i + " from " + supplier.getName(),
                    targetPrice,
                    targetPrice * 0.6,
                    supplier
                );
            }
        }
    }

    private static void createCustomers(Business business) {
        CustomerDirectory customerDirectory = business.getCustomerDirectory();
        for (int i = 1; i <= 10; i++) {
            CustomerProfile profile = new CustomerProfile();
            profile.setId("CUST" + i);
            profile.setPerson("Customer " + i);
            customerDirectory.addCustomerProfile(profile);
        }
    }

    private static void createOrders(Business business) {
        ProductCatalog productCatalog = business.getProductCatalog();
        for (Product product : productCatalog.getProductList()) {
            createOrdersForProduct(business, product);
        }
    }

    private static void createOrdersForProduct(Business business, Product product) {
        MasterOrderList masterOrderList = business.getMasterOrderList();
        CustomerDirectory customerDirectory = business.getCustomerDirectory();
        
        for (int i = 0; i < 10; i++) {
            CustomerProfile customer = customerDirectory.getRandomCustomerProfile();
            if (customer != null) {
                double actualPrice = generateRandomActualPrice(product);
                int quantity = generateRandomQuantity();
                
                Order order = masterOrderList.createOrder(product, customer, quantity, actualPrice);
                if (order != null) {
                    customer.addOrder(order);
                }
            }
        }
    }

    private static int generateRandomQuantity() {
        return 1 + (int)(Math.random() * 10);
    }

    private static double generateRandomActualPrice(Product product) {
        double targetPrice = product.getTargetPrice();
        return targetPrice * (0.8 + (Math.random() * 0.4));
    }
}
