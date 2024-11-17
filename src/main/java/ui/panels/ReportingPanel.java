package ui.panels;

import controller.PricingController;
import model.OrderManagement.OrderAnalyzer;
import model.ProductManagement.ProductsReport;
import ui.reports.PerformanceReportPanel;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ReportingPanel extends JPanel implements Refreshable {
    private PricingController controller;
    private JComboBox<String> reportTypeComboBox;
    private JComboBox<String> timeframeComboBox;
    private JButton generateButton;
    private JButton exportButton;
    private PerformanceReportPanel reportPanel;
    private ProductsReport currentReport;

    public ReportingPanel(PricingController controller) {
        this.controller = controller;
        initializeComponents();
        layoutComponents();
        setupActions();
    }

    private void initializeComponents() {
        reportTypeComboBox = new JComboBox<>(new String[]{
            "Product Performance Report",
            "Price Adjustment Impact Report",
            "Market Segment Analysis",
            "Revenue Trend Analysis"
        });

        timeframeComboBox = new JComboBox<>(new String[]{
            "Last Month",
            "Last Quarter",
            "Last Year",
            "Year to Date"
        });

        generateButton = new JButton("Generate Report");
        exportButton = new JButton("Export Report");
        exportButton.setEnabled(false);

        reportPanel = new PerformanceReportPanel();
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Control Panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        controlPanel.add(new JLabel("Report Type:"));
        controlPanel.add(reportTypeComboBox);
        controlPanel.add(new JLabel("Timeframe:"));
        controlPanel.add(timeframeComboBox);
        controlPanel.add(generateButton);
        controlPanel.add(exportButton);

        add(controlPanel, BorderLayout.NORTH);
        add(new JScrollPane(reportPanel), BorderLayout.CENTER);
    }

    private void setupActions() {
        generateButton.addActionListener(e -> generateReport());
        exportButton.addActionListener(e -> exportReport());
        
        reportTypeComboBox.addActionListener(e -> updateTimeframeOptions());
    }

    private void generateReport() {
        String reportType = (String) reportTypeComboBox.getSelectedItem();
        String timeframe = (String) timeframeComboBox.getSelectedItem();
        
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            
            // Get date range based on timeframe
            LocalDate[] dateRange = getDateRange(timeframe);
            
            switch (reportType) {
                case "Product Performance Report":
                    currentReport = controller.generateProductPerformanceReport(
                        dateRange[0], dateRange[1]);
                    break;
                case "Price Adjustment Impact Report":
                    currentReport = controller.generatePriceAdjustmentReport(
                        dateRange[0], dateRange[1]);
                    break;
                case "Market Segment Analysis":
                    currentReport = controller.generateMarketSegmentReport(
                        dateRange[0], dateRange[1]);
                    break;
                case "Revenue Trend Analysis":
                    currentReport = controller.generateRevenueTrendReport(
                        dateRange[0], dateRange[1]);
                    break;
            }
            
            reportPanel.displayReport(currentReport);
            exportButton.setEnabled(true);
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error generating report: " + ex.getMessage(),
                "Report Generation Error",
                JOptionPane.ERROR_MESSAGE);
        } finally {
            setCursor(Cursor.getDefaultCursor());
        }
    }

    private void exportReport() {
        if (currentReport == null) {
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export Report");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!filePath.toLowerCase().endsWith(".pdf")) {
                    filePath += ".pdf";
                }
                
                currentReport.exportToPDF(filePath);
                
                JOptionPane.showMessageDialog(this,
                    "Report exported successfully!",
                    "Export Complete",
                    JOptionPane.INFORMATION_MESSAGE);
                    
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Error exporting report: " + ex.getMessage(),
                    "Export Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private LocalDate[] getDateRange(String timeframe) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate;
        
        switch (timeframe) {
            case "Last Month":
                startDate = endDate.minus(1, ChronoUnit.MONTHS);
                break;
            case "Last Quarter":
                startDate = endDate.minus(3, ChronoUnit.MONTHS);
                break;
            case "Last Year":
                startDate = endDate.minus(1, ChronoUnit.YEARS);
                break;
            case "Year to Date":
                startDate = LocalDate.of(endDate.getYear(), 1, 1);
                break;
            default:
                startDate = endDate.minus(1, ChronoUnit.MONTHS);
        }
        
        return new LocalDate[]{startDate, endDate};
    }

    private void updateTimeframeOptions() {
        String reportType = (String) reportTypeComboBox.getSelectedItem();
        timeframeComboBox.removeAllItems();
        
        if ("Revenue Trend Analysis".equals(reportType)) {
            timeframeComboBox.addItem("Last Quarter");
            timeframeComboBox.addItem("Last Year");
            timeframeComboBox.addItem("Year to Date");
        } else {
            timeframeComboBox.addItem("Last Month");
            timeframeComboBox.addItem("Last Quarter");
            timeframeComboBox.addItem("Last Year");
            timeframeComboBox.addItem("Year to Date");
        }
    }

    @Override
    public void refreshData() {
        if (currentReport != null) {
            generateReport();
        }
    }
} 