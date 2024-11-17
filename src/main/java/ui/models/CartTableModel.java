package ui.models;

import javax.swing.table.AbstractTableModel;
import model.ProductManagement.Product;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CartTableModel extends AbstractTableModel {
    private List<Product> products;
    private List<Integer> quantities;
    private final String[] columnNames = {"Product", "Price", "Quantity", "Subtotal"};
    
    public CartTableModel(Map<Product, Integer> cartItems) {
        setCartItems(cartItems);
    }
    
    public void setCartItems(Map<Product, Integer> cartItems) {
        products = new ArrayList<>(cartItems.keySet());
        quantities = new ArrayList<>(cartItems.values());
    }
    
    public Product getProductAt(int row) {
        return products.get(row);
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
    public Object getValueAt(int row, int column) {
        Product product = products.get(row);
        int quantity = quantities.get(row);
        
        switch (column) {
            case 0: return product.getName();
            case 1: return String.format("$%.2f", product.getTargetPrice());
            case 2: return quantity;
            case 3: return String.format("$%.2f", product.getTargetPrice() * quantity);
            default: return null;
        }
    }
} 