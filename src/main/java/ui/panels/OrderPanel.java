package ui.panels;

import javax.swing.*;
import java.awt.*;
import controller.PricingController;
import model.OrderManagement.Order;
import model.OrderManagement.OrderItem;
import java.util.List;

public class OrderPanel extends JPanel implements Refreshable {
    private PricingController controller;
    private JTable orderTable;
    private OrderTableModel tableModel;
    private JTextArea detailsArea;
    
    public OrderPanel(PricingController controller) {
        this.controller = controller;
        initializePanel();
    }
    
    private void initializePanel() {
        setLayout(new BorderLayout());
        
        // Create table
        tableModel = new OrderTableModel(controller.getBusiness().getMasterOrderList().getOrders());
        orderTable = new JTable(tableModel);
        
        // Add scroll pane for table
        JScrollPane tableScrollPane = new JScrollPane(orderTable);
        
        // Create details panel
        detailsArea = new JTextArea(10, 40);
        detailsArea.setEditable(false);
        JScrollPane detailsScrollPane = new JScrollPane(detailsArea);
        
        // Add selection listener
        orderTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateDetails();
            }
        });
        
        // Layout components
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                                            tableScrollPane,
                                            detailsScrollPane);
        splitPane.setDividerLocation(400);
        
        add(splitPane, BorderLayout.CENTER);
    }
    
    private void updateDetails() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow >= 0) {
            Order order = tableModel.getOrderAt(selectedRow);
            StringBuilder details = new StringBuilder();
            details.append("Order Details:\n\n");
            details.append("Order ID: ").append(order.getOrderId()).append("\n");
            details.append("Customer: ").append(order.getCustomer().getPerson().getName()).append("\n\n");
            details.append("Items:\n");
            
            for (OrderItem item : order.getOrderItems()) {
                details.append(String.format("- %s (Qty: %d) @ $%.2f\n",
                    item.getProduct().getName(),
                    item.getQuantity(),
                    item.getActualPrice()));
            }
            
            details.append("\nTotal: $").append(String.format("%.2f", order.getOrderTotal()));
            detailsArea.setText(details.toString());
        } else {
            detailsArea.setText("");
        }
    }
    
    @Override
    public void refresh() {
        tableModel.setOrders(controller.getBusiness().getMasterOrderList().getOrders());
        tableModel.fireTableDataChanged();
        updateDetails();
    }
} 