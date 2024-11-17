package ui.models;

import javax.swing.table.AbstractTableModel;
import model.OrderManagement.Order;
import java.util.List;
import java.util.ArrayList;

public class OrderHistoryTableModel extends AbstractTableModel {
    private List<Order> orders;
    private final String[] columnNames = {"Order ID", "Date", "Items", "Total"};
    
    public OrderHistoryTableModel(List<Order> orders) {
        this.orders = new ArrayList<>(orders);
    }
    
    public void setOrders(List<Order> orders) {
        this.orders = new ArrayList<>(orders);
    }
    
    public Order getOrderAt(int row) {
        return orders.get(row);
    }
    
    @Override
    public int getRowCount() {
        return orders.size();
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
        Order order = orders.get(row);
        
        switch (column) {
            case 0: return order.getOrderId();
            case 1: return order.getOrderDate();
            case 2: return order.getOrderItems().size();
            case 3: return String.format("$%.2f", order.getOrderTotal());
            default: return null;
        }
    }
} 