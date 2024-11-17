package model.OrderManagement;

import java.util.ArrayList;
import java.util.List;
import model.Personnel.Profile;

public class MasterOrderList {
    private ArrayList<Order> orders;
    
    public MasterOrderList() {
        orders = new ArrayList<>();
    }
    
    public Order newOrder() {
        Order order = new Order();
        orders.add(order);
        return order;
    }
    
    public List<Order> getOrders() {
        return orders;
    }
    
    public void removeOrder(Order order) {
        orders.remove(order);
    }
}
