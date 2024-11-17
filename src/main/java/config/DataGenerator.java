import java.time.*;
import java.util.*;
import java.util.stream.*;
import model.Business.*;
import model.ProductManagement.*;
import model.OrderManagement.*;
import model.CustomerManagement.*;

public class DataGenerator {
    private static final Random random = new Random();
    
    public static void generateInitialData(Business business) {
        generateOrders(business);
        generatePriceHistory(business);
    }
    
    private static void generateOrders(Business business) {
        LocalDate startDate = LocalDate.now().minusMonths(6);
        
        for (Product product : business.getProductCatalog().getProducts()) {
            // Generate 10 orders per product
            for (int i = 0; i < 10; i++) {
                CustomerProfile customer = getRandomCustomer(business);
                LocalDate orderDate = startDate.plusDays(random.nextInt(180));
                
                Order order = createOrder(product, customer, orderDate);
                business.getMasterOrderList().addOrder(order);
            }
        }
    }
    
    private static Order createOrder(Product product, CustomerProfile customer, LocalDate date) {
        Order order = new Order();
        order.setDate(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        order.setCustomer(customer);
        
        // Vary actual price around target price
        double priceVariation = (random.nextDouble() - 0.5) * 0.2; // ±10% variation
        double actualPrice = product.getTargetPrice() * (1 + priceVariation);
        
        OrderItem item = new OrderItem();
        item.setProduct(product);
        item.setQuantity(random.nextInt(3) + 1);
        item.setActualPrice(actualPrice);
        
        order.addOrderItem(item);
        return order;
    }
    
    private static void generatePriceHistory(Business business) {
        LocalDate startDate = LocalDate.now().minusMonths(6);
        
        for (Product product : business.getProductCatalog().getProducts()) {
            List<PricePoint> priceHistory = new ArrayList<>();
            double basePrice = product.getTargetPrice();
            
            // Generate price points over time
            for (int i = 0; i < 26; i++) { // Weekly price points
                LocalDate pointDate = startDate.plusWeeks(i);
                double priceVariation = (random.nextDouble() - 0.5) * 0.15; // ±7.5% variation
                double historicalPrice = basePrice * (1 + priceVariation);
                
                priceHistory.add(new PricePoint(pointDate, historicalPrice));
            }
            
            product.setPriceHistory(priceHistory);
        }
    }
} 