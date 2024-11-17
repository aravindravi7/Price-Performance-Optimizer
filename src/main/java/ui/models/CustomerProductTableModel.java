package ui.models;

import javax.swing.table.AbstractTableModel;
import model.ProductManagement.Product;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class CustomerProductTableModel extends AbstractTableModel {
    private List<Product> allProducts;
    private List<Product> filteredProducts;
    private final String[] columnNames = {"Product Name", "Price", "Supplier"};
    
    public CustomerProductTableModel() {
        this.allProducts = new ArrayList<>();
        this.filteredProducts = new ArrayList<>();
    }
    
    @Override
    public int getRowCount() {
        return filteredProducts.size();
    }
    
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }
    
    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Product product = filteredProducts.get(rowIndex);
        switch (columnIndex) {
            case 0: return product.getName();
            case 1: return String.format("$%.2f", product.getTargetPrice());
            case 2: return product.getSupplier() != null ? product.getSupplier().getName() : "Unknown";
            default: return null;
        }
    }
    
    public void setProducts(List<Product> products) {
        this.allProducts = new ArrayList<>(products != null ? products : new ArrayList<>());
        this.filteredProducts = new ArrayList<>(this.allProducts);
        fireTableDataChanged();
        
        // Debug print
        System.out.println("CustomerProductTableModel - Updated with " + 
            this.allProducts.size() + " products");
    }
    
    public Product getProductAt(int row) {
        return row >= 0 && row < filteredProducts.size() ? filteredProducts.get(row) : null;
    }
    
    public void filterProducts(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            filteredProducts = new ArrayList<>(allProducts);
        } else {
            searchText = searchText.toLowerCase().trim();
            final String searchTerm = searchText;
            
            filteredProducts = allProducts.stream()
                .filter(p -> p.getName().toLowerCase().contains(searchTerm) ||
                           (p.getSupplier() != null && 
                            p.getSupplier().getName().toLowerCase().contains(searchTerm)))
                .collect(Collectors.toList());
        }
        fireTableDataChanged();
        
        // Debug print
        System.out.println("CustomerProductTableModel - Filtered to " + 
            filteredProducts.size() + " products");
    }
} 