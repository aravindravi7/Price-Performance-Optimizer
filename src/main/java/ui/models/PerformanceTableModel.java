package ui.models;

import javax.swing.table.AbstractTableModel;
import controller.PricingController.PerformanceResult;
import java.util.List;
import java.util.ArrayList;

public class PerformanceTableModel extends AbstractTableModel {
    private List<PerformanceResult> results;
    private final String[] columnNames = {"Product", "Performance"};
    
    public PerformanceTableModel(List<PerformanceResult> results) {
        this.results = new ArrayList<>(results);
    }
    
    public void setResults(List<PerformanceResult> results) {
        this.results = new ArrayList<>(results);
    }
    
    @Override
    public int getRowCount() {
        return results.size();
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
        PerformanceResult result = results.get(row);
        switch (column) {
            case 0: return result.getProduct().getName();
            case 1: return String.format("%.2f%%", result.getPerformance());
            default: return null;
        }
    }
} 