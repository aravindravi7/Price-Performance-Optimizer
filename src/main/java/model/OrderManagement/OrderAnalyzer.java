public class OrderAnalyzer {
    private MasterOrderList masterOrderList;
    
    public OrderAnalyzer(MasterOrderList masterOrderList) {
        this.masterOrderList = masterOrderList;
    }
    
    public Map<Product, ProductPerformanceMetrics> analyzeProductPerformance() {
        Map<Product, ProductPerformanceMetrics> performance = new HashMap<>();
        
        for (Order order : masterOrderList.getOrders()) {
            for (OrderItem item : order.getOrderItems()) {
                Product product = item.getProduct();
                performance.computeIfAbsent(product, k -> new ProductPerformanceMetrics())
                          .addSale(item.getQuantity(), item.getActualPrice());
            }
        }
        
        return performance;
    }
    
    public List<PriceTrend> analyzePriceTrends(Product product) {
        List<PriceTrend> trends = new ArrayList<>();
        List<Order> relevantOrders = findOrdersForProduct(product);
        
        // Sort orders by date
        relevantOrders.sort(Comparator.comparing(Order::getDate));
        
        // Calculate moving averages and trends
        double movingAverage = 0;
        int windowSize = 5;
        
        for (int i = 0; i < relevantOrders.size(); i++) {
            if (i >= windowSize) {
                double sum = 0;
                for (int j = i - windowSize; j < i; j++) {
                    sum += getProductPrice(relevantOrders.get(j), product);
                }
                movingAverage = sum / windowSize;
                
                trends.add(new PriceTrend(
                    relevantOrders.get(i).getDate(),
                    getProductPrice(relevantOrders.get(i), product),
                    movingAverage
                ));
            }
        }
        
        return trends;
    }
    
    private List<Order> findOrdersForProduct(Product product) {
        return masterOrderList.getOrders().stream()
            .filter(order -> order.getOrderItems().stream()
                .anyMatch(item -> item.getProduct().equals(product)))
            .collect(Collectors.toList());
    }
    
    private double getProductPrice(Order order, Product product) {
        return order.getOrderItems().stream()
            .filter(item -> item.getProduct().equals(product))
            .findFirst()
            .map(OrderItem::getActualPrice)
            .orElse(0.0);
    }
    
    public static class ProductPerformanceMetrics {
        private int totalQuantity = 0;
        private double totalRevenue = 0;
        private double highestPrice = Double.MIN_VALUE;
        private double lowestPrice = Double.MAX_VALUE;
        private List<Double> prices = new ArrayList<>();
        
        public void addSale(int quantity, double price) {
            totalQuantity += quantity;
            totalRevenue += quantity * price;
            highestPrice = Math.max(highestPrice, price);
            lowestPrice = Math.min(lowestPrice, price);
            prices.add(price);
        }
        
        public double getAveragePrice() {
            return prices.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        }
        
        // Getters
        public int getTotalQuantity() { return totalQuantity; }
        public double getTotalRevenue() { return totalRevenue; }
        public double getHighestPrice() { return highestPrice; }
        public double getLowestPrice() { return lowestPrice; }
    }
    
    public static class PriceTrend {
        private Date date;
        private double actualPrice;
        private double movingAverage;
        
        public PriceTrend(Date date, double actualPrice, double movingAverage) {
            this.date = date;
            this.actualPrice = actualPrice;
            this.movingAverage = movingAverage;
        }
        
        // Getters
        public Date getDate() { return date; }
        public double getActualPrice() { return actualPrice; }
        public double getMovingAverage() { return movingAverage; }
    }
} 