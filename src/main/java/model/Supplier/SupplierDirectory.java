package model.Supplier;

import java.util.ArrayList;
import java.util.List;

public class SupplierDirectory {
    private ArrayList<Supplier> suppliers;
    
    public SupplierDirectory() {
        suppliers = new ArrayList<>();
    }
    
    public Supplier addSupplier(String id, String name) {
        Supplier supplier = new Supplier(id, name);
        suppliers.add(supplier);
        return supplier;
    }
    
    public Supplier findSupplier(String id) {
        for (Supplier supplier : suppliers) {
            if (supplier.getId().equals(id)) {
                return supplier;
            }
        }
        return null;
    }
    
    public List<Supplier> getSupplierList() {
        return suppliers;
    }
    
    public void removeSupplier(Supplier supplier) {
        suppliers.remove(supplier);
    }
}