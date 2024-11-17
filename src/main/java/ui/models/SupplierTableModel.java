package ui.models;

import javax.swing.table.AbstractTableModel;
import model.Supplier.Supplier;
import java.util.List;
import java.util.ArrayList;

public class SupplierTableModel extends AbstractTableModel {
    private List<Supplier> suppliers;
    private final String[] columnNames = {"ID", "Name"};
    
    public SupplierTableModel(List<Supplier> suppliers) {
        this.suppliers = new ArrayList<>(suppliers);
    }
    
    @Override
    public int getRowCount() {
        return suppliers.size();
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
        Supplier supplier = suppliers.get(rowIndex);
        switch (columnIndex) {
            case 0: return supplier.getId();
            case 1: return supplier.getName();
            default: return null;
        }
    }
    
    public void setSuppliers(List<Supplier> suppliers) {
        this.suppliers = new ArrayList<>(suppliers);
    }
    
    public Supplier getSupplierAt(int row) {
        return suppliers.get(row);
    }
} 