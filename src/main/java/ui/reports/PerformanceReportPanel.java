import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import model.Business.Business;
import model.ProductManagement.*;
import model.OrderManagement.*;
import org.jfree.chart.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.util.*;
import java.time.*;

public class PerformanceReportPanel extends JPanel {
    private Business business;
    private JTable reportTable;
    private ReportTableModel tableModel;
    private JPanel chartsPanel;
    private JComboBox<String> timeRangeCombo;
    private JComboBox<String> categoryFilterCombo;
    
    public PerformanceReportPanel(Business business) {
        this.business = business;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(240, 242, 245));
        
        createComponents();
        loadData();
    }
    
    private void createComponents() {
        // Top controls
        add(createControlPanel(), BorderLayout.NORTH);
        
        // Main content split
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(createReportTable());
        splitPane.setBottomComponent(createChartsPanel());
        splitPane.setDividerLocation(400);
        
        add(splitPane, BorderLayout.CENTER);
        
        // Export button
        add(createExportPanel(), BorderLayout.SOUTH);
    }
    
    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        
        // Time range selector
        timeRangeCombo = new JComboBox<>(new String[]{
            "Last 30 Days", "Last Quarter", "Last Year", "All Time"
        });
        
        // Category filter
        categoryFilterCombo = new JComboBox<>();
        categoryFilterCombo.addItem("All Categories");
        business.getProductCatalog().getCategories().forEach(categoryFilterCombo::addItem);
        
        // Refresh button
        JButton refreshButton = new JButton("Refresh Data");
        refreshButton.addActionListener(e -> refreshReport());
        
        panel.add(new JLabel("Time Range:"));
        panel.add(timeRangeCombo);
        panel.add(new JLabel("Category:"));
        panel.add(categoryFilterCombo);
        panel.add(refreshButton);
        
        return panel;
    }
    
    private JScrollPane createReportTable() {
        tableModel = new ReportTableModel();
        reportTable = new JTable(tableModel);
        
        // Style the table
        reportTable.setShowGrid(false);
        reportTable.setRowHeight(40);
        reportTable.getTableHeader().setPreferredSize(new Dimension(0, 40));
        
        // Add sorting capability
        TableRowSorter<ReportTableModel> sorter = new TableRowSorter<>(tableModel);
        reportTable.setRowSorter(sorter);
        
        return new JScrollPane(reportTable);
    }
    
    private JPanel createChartsPanel() {
        chartsPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        chartsPanel.setBackground(Color.WHITE);
        chartsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        chartsPanel.add(createPricePerformanceChart());
        chartsPanel.add(createCategoryPerformanceChart());
        chartsPanel.add(createTrendChart());
        
        return chartsPanel;
    }
    
    private JPanel createExportPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton exportPdfButton = new JButton("Export PDF");
        JButton exportExcelButton = new JButton("Export Excel");
        
        exportPdfButton.addActionListener(e -> exportReport("pdf"));
        exportExcelButton.addActionListener(e -> exportReport("excel"));
        
        panel.add(exportPdfButton);
        panel.add(exportExcelButton);
        
        return panel;
    }
    
    private void refreshReport() {
        String timeRange = (String) timeRangeCombo.getSelectedItem();
        String category = (String) categoryFilterCombo.getSelectedItem();
        
        List<ProductPerformanceData> performanceData = 
            generatePerformanceData(timeRange, category);
        
        tableModel.setData(performanceData);
        updateCharts(performanceData);
    }
    
    private void exportReport(String format) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export Report");
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                if ("pdf".equals(format)) {
                    exportToPdf(file);
                } else {
                    exportToExcel(file);
                }
                JOptionPane.showMessageDialog(this, 
                    "Report exported successfully!", 
                    "Export Complete", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Error exporting report: " + e.getMessage(),
                    "Export Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
} 