package ui.models;

import javax.swing.table.AbstractTableModel;
import model.ProductManagement.Product;
import java.util.List;
import java.util.ArrayList;

public class ProductTableModel extends AbstractTableModel {
    private List<Product> products;
    private final String[] columnNames = {"Name", "Target Price", "Status"};
    
    public ProductTableModel() {
        this.products = new ArrayList<>();
    }
    
    public ProductTableModel(List<Product> products) {
        this.products = new ArrayList<>(products != null ? products : new ArrayList<>());
    }
    
    @Override
    public int getRowCount() {
        return products.size();
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
        if (rowIndex >= products.size()) {
            return null;
        }
        
        Product product = products.get(rowIndex);
        switch (columnIndex) {
            case 0: return product.getName();
            case 1: return String.format("$%.2f", product.getTargetPrice());
            case 2: return product.isActive() ? "Active" : "Inactive";
            default: return null;
        }
    }
    
    public void setProducts(List<Product> products) {
        this.products = new ArrayList<>(products != null ? products : new ArrayList<>());
        fireTableDataChanged();
    }
    
    public Product getProductAt(int row) {
        return row >= 0 && row < products.size() ? products.get(row) : null;
    }
    
    public void addProduct(Product product) {
        if (product != null) {
            products.add(product);
            fireTableRowsInserted(products.size() - 1, products.size() - 1);
        }
    }
} 