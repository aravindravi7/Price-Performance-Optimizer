package model.OrderManagement;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author kal bugrara
 */
public class MasterOrderReport {
    private MasterOrderList orderList;
    
    public MasterOrderReport(MasterOrderList orderList) {
        this.orderList = orderList;
    }

    public Map<Product, Double> getRevenueByProduct() {
        return orderList.getOrders().stream()
            .flatMap(order -> order.getOrderItems().stream())
            .collect(Collectors.groupingBy(
                OrderItem::getProduct,
                Collectors.summingDouble(item -> 
                    item.getQuantity() * item.getActualPrice())
            ));
    }

    public Map<String, Double> getRevenueByCategory() {
        return orderList.getOrders().stream()
            .flatMap(order -> order.getOrderItems().stream())
            .collect(Collectors.groupingBy(
                item -> item.getProduct().getCategory(),
                Collectors.summingDouble(item -> 
                    item.getQuantity() * item.getActualPrice())
            ));
    }
}

