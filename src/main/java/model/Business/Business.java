package model.Business;

import model.CustomerManagement.CustomerDirectory;
import model.OrderManagement.MasterOrderList;
import model.ProductManagement.ProductCatalog;
import model.Supplier.SupplierDirectory;
import model.Personnel.PersonDirectory;

public class Business {
    private String name;
    private ProductCatalog productCatalog;
    private SupplierDirectory supplierDirectory;
    private CustomerDirectory customerDirectory;
    private MasterOrderList masterOrderList;
    private PersonDirectory personDirectory;
    
    public Business() {
        productCatalog = new ProductCatalog();
        supplierDirectory = new SupplierDirectory();
        customerDirectory = new CustomerDirectory();
        masterOrderList = new MasterOrderList();
        personDirectory = new PersonDirectory();
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public ProductCatalog getProductCatalog() {
        return productCatalog;
    }
    
    public SupplierDirectory getSupplierDirectory() {
        return supplierDirectory;
    }
    
    public CustomerDirectory getCustomerDirectory() {
        return customerDirectory;
    }
    
    public MasterOrderList getMasterOrderList() {
        return masterOrderList;
    }
    
    public PersonDirectory getPersonDirectory() {
        return personDirectory;
    }
}
