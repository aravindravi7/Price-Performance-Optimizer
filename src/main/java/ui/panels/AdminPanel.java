package ui.panels;

import javax.swing.*;
import java.awt.*;
import controller.PricingController;
import model.Business.Business;
import model.Supplier.Supplier;
import ui.models.SupplierTableModel;
import java.util.List;

public class AdminPanel extends JPanel {
    private Business business;
    private PricingController controller;
    private CardLayout cardLayout;
    private JPanel parentPanel;
    private JTable supplierTable;
    private SupplierTableModel tableModel;
    
    public AdminPanel(Business business, PricingController controller, CardLayout layout, JPanel parent) {
        this.business = business;
        this.controller = controller;
        this.cardLayout = layout;
        this.parentPanel = parent;
        initializePanel();
    }
    
    private void initializePanel() {
        setLayout(new BorderLayout());
        
        // Add navigation panel at the top
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("← Back to Login");
        backButton.addActionListener(e -> cardLayout.show(parentPanel, "LOGIN"));
        navPanel.add(backButton);
        add(navPanel, BorderLayout.NORTH);
        
        // Create supplier panel
        JPanel supplierPanel = createSupplierPanel();
        add(supplierPanel, BorderLayout.CENTER);
    }
    
    private JPanel createSupplierPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Supplier Management"));
        
        // Create supplier table
        tableModel = new SupplierTableModel(business.getSupplierDirectory().getSupplierList());
        supplierTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(supplierTable);
        
        // Create button panel
        JPanel buttonPanel = new JPanel();
        JButton addBtn = new JButton("Add Supplier");
        JButton removeBtn = new JButton("Remove Supplier");
        
        addBtn.addActionListener(e -> addSupplier());
        removeBtn.addActionListener(e -> removeSupplier());
        
        buttonPanel.add(addBtn);
        buttonPanel.add(removeBtn);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void addSupplier() {
        String id = JOptionPane.showInputDialog(this, "Enter Supplier ID:");
        if (id != null && !id.trim().isEmpty()) {
            String name = JOptionPane.showInputDialog(this, "Enter Supplier Name:");
            if (name != null && !name.trim().isEmpty()) {
                business.getSupplierDirectory().addSupplier(id, name);
                refreshSupplierTable();
            }
        }
    }
    
    private void removeSupplier() {
        int selectedRow = supplierTable.getSelectedRow();
        if (selectedRow >= 0) {
            Supplier supplier = tableModel.getSupplierAt(selectedRow);
            business.getSupplierDirectory().removeSupplier(supplier);
            refreshSupplierTable();
        }
    }
    
    private void refreshSupplierTable() {
        List<Supplier> supplierList = business.getSupplierDirectory().getSupplierList();
        tableModel.setSuppliers(supplierList);
        tableModel.fireTableDataChanged();
    }
} 