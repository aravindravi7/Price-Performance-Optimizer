public class ConfigurationLoader {
    private static final CarProduct[] INITIAL_PRODUCTS = {
        // Luxury Segment
        new CarProduct("Mercedes-Benz S-Class", 94250.00, 89999.00, "Luxury"),
        new CarProduct("BMW 7 Series", 86800.00, 83500.00, "Luxury"),
        new CarProduct("Audi A8", 86500.00, 82900.00, "Luxury"),
        new CarProduct("Lexus LS", 76000.00, 73500.00, "Luxury"),
        new CarProduct("Porsche Panamera", 88400.00, 85000.00, "Luxury"),

        // Mid-Range Luxury
        new CarProduct("Mercedes-Benz E-Class", 54950.00, 52500.00, "Mid-Luxury"),
        new CarProduct("BMW 5 Series", 54200.00, 51900.00, "Mid-Luxury"),
        new CarProduct("Audi A6", 55900.00, 53500.00, "Mid-Luxury"),
        new CarProduct("Lexus ES", 40800.00, 38900.00, "Mid-Luxury"),
        new CarProduct("Genesis G80", 48000.00, 45900.00, "Mid-Luxury"),

        // Entry-Level Luxury
        new CarProduct("Mercedes-Benz C-Class", 43550.00, 41900.00, "Entry-Luxury"),
        new CarProduct("BMW 3 Series", 41450.00, 39900.00, "Entry-Luxury"),
        new CarProduct("Audi A4", 39100.00, 37500.00, "Entry-Luxury"),
        new CarProduct("Lexus IS", 39050.00, 37400.00, "Entry-Luxury"),
        new CarProduct("Volvo S60", 38950.00, 37200.00, "Entry-Luxury"),

        // Mainstream
        new CarProduct("Toyota Camry", 25945.00, 24500.00, "Mainstream"),
        new CarProduct("Honda Accord", 26520.00, 25000.00, "Mainstream"),
        new CarProduct("Hyundai Sonata", 24150.00, 22900.00, "Mainstream"),
        new CarProduct("Nissan Altima", 24550.00, 23200.00, "Mainstream"),
        new CarProduct("Mazda6", 24475.00, 23100.00, "Mainstream"),

        // SUV Segment
        new CarProduct("Tesla Model Y", 65990.00, 62900.00, "SUV"),
        new CarProduct("BMW X5", 61600.00, 59000.00, "SUV"),
        new CarProduct("Mercedes-Benz GLE", 56750.00, 54500.00, "SUV"),
        new CarProduct("Lexus RX", 46995.00, 44900.00, "SUV"),
        new CarProduct("Audi Q7", 57500.00, 55000.00, "SUV")
    };

    public static void loadInitialData(Business business) {
        ProductCatalog catalog = business.getProductCatalog();
        
        for (CarProduct carProduct : INITIAL_PRODUCTS) {
            Product product = new Product();
            product.setName(carProduct.name);
            product.setTargetPrice(carProduct.targetPrice);
            product.setCost(carProduct.cost);
            product.setCategory(carProduct.category);
            
            // Generate some historical price data
            generateHistoricalPrices(product);
            
            catalog.addProduct(product);
        }
    }

    private static void generateHistoricalPrices(Product product) {
        Random random = new Random();
        double targetPrice = product.getTargetPrice();
        
        // Generate 10 historical prices within ±5% of target price
        for (int i = 0; i < 10; i++) {
            double variation = 0.95 + (random.nextDouble() * 0.10); // -5% to +5%
            product.addHistoricalPrice(targetPrice * variation);
        }
    }

    private static class CarProduct {
        String name;
        double targetPrice;
        double cost;
        String category;

        CarProduct(String name, double targetPrice, double cost, String category) {
            this.name = name;
            this.targetPrice = targetPrice;
            this.cost = cost;
            this.category = category;
        }
    }
} 