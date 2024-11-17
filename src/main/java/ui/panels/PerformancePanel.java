package ui.panels;

import javax.swing.*;
import java.awt.*;
import controller.PricingController;
import model.Business.Business;
import ui.models.PerformanceTableModel;
import java.util.List;

public class PerformancePanel extends JPanel {
    private Business business;
    private PricingController controller;
    private JTable performanceTable;
    private PerformanceTableModel tableModel;
    private JPanel chartPanel;
    private JComboBox<String> timeRangeSelector;
    
    public PerformancePanel(Business business, PricingController controller) {
        this.business = business;
        this.controller = controller;
        initializePanel();
    }
    
    private void initializePanel() {
        setLayout(new BorderLayout(10, 10));
        
        // Create control panel
        JPanel controlPanel = createControlPanel();
        add(controlPanel, BorderLayout.NORTH);
        
        // Create main content panel
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(0.6);
        
        // Add table panel
        JPanel tablePanel = createTablePanel();
        splitPane.setLeftComponent(tablePanel);
        
        // Add chart panel
        chartPanel = createChartPanel();
        splitPane.setRightComponent(chartPanel);
        
        add(splitPane, BorderLayout.CENTER);
        
        // Initial data load
        refreshData();
    }
    
    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        String[] timeRanges = {"Last 7 Days", "Last 30 Days", "Last 90 Days", "Year to Date"};
        timeRangeSelector = new JComboBox<>(timeRanges);
        timeRangeSelector.addActionListener(e -> refreshData());
        
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshData());
        
        JButton exportButton = new JButton("Export Report");
        exportButton.addActionListener(e -> exportReport());
        
        panel.add(new JLabel("Time Range: "));
        panel.add(timeRangeSelector);
        panel.add(refreshButton);
        panel.add(exportButton);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        tableModel = new PerformanceTableModel();
        performanceTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(performanceTable);
        
        panel.add(new JLabel("Performance Metrics"), BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createChartPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Performance Charts"));
        
        // Add chart components here
        
        return panel;
    }
    
    private void refreshData() {
        // Update table data
        List<PricingController.PerformanceResult> results = 
            controller.generatePerformanceReport();
        tableModel.setPerformanceData(results);
        
        // Update charts
        updateCharts();
    }
    
    private void updateCharts() {
        // Update chart visualizations
    }
    
    private void exportReport() {
        // Handle report export
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            // Export logic here
        }
    }
} 