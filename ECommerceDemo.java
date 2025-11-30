
import java.util.Scanner;

public class ECommerceDemo {

    // Main system instance managing BSTs and business logic
    private static ECommerceSystem system = new ECommerceSystem();
    // Scanner for capturing user input from console
    private static Scanner scanner = new Scanner(System.in);

    // Centralized constants for CSV file paths to ensure consistency between load and save operations.
    // Using absolute paths to avoid directory ambiguity during execution.
    private static final String PRODUCT_FILE = "C:\\products.csv";
    private static final String CUSTOMER_FILE = "C:\\customers.csv";
    private static final String ORDER_FILE = "C:\\orders.csv";
    private static final String REVIEW_FILE = "C:\\reviews.csv";

    public static void main(String[] args) {
        System.out.println("Welcome to the E-Commerce Management System!");
        
        // Automatically load existing data into BSTs upon application startup
        loadAllCSVs();

        // Main event loop: Keeps the application running until user selects Exit
        while (true) {
            printMenu();
            try {
                String input = scanner.nextLine();
                if (input.isEmpty()) continue; // Ignore empty inputs (e.g., accidental Enter key)
                int choice = Integer.parseInt(input);
                handleMenuChoice(choice);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
    }

    // Displays the CLI menu options categorized by functionality
    private static void printMenu() {
        System.out.println("\n===== E-Commerce System Menu =====");
        System.out.println("--- Data Management ---");
        System.out.println(" 1. Reload All Data from CSVs");
        System.out.println("--- Core Operations (Auto-Saves) ---");
        System.out.println(" 6. Add a New Product");
        System.out.println(" 7. Register a New Customer");
        System.out.println(" 8. Place a New Order");
        System.out.println(" 9. Add a Review for a Product");
        System.out.println(" 10. Edit an Existing Review");
        System.out.println(" 11. Update an Order Status");
        System.out.println(" 12. Cancel an Order");
        System.out.println("--- Display & Search ---");
        System.out.println(" 13. Display All Products");
        System.out.println(" 14. Display All Customers");
        System.out.println(" 15. Display All Orders");
        System.out.println(" 16. Find Product by ID");
        System.out.println(" 17. Find Customer by ID");
        System.out.println(" 18. Find Order by ID");
        System.out.println("--- Analysis & Reports ---");
        System.out.println(" 19. Get Reviews by Customer ID");
        System.out.println(" 20. Get Top 3 Rated Products");
        System.out.println(" 21. Get Orders Between Two Dates");
        System.out.println(" 22. Get Common High-Rated Products by Two Customers");
        System.out.println(" 23. Get Out-of-Stock Products");
        System.out.println(" 24. Update Product Details");
        System.out.println(" 25. List Products in Price Range");
        System.out.println(" 26. List Customers Alphabetically");
        System.out.println(" 27. List Customers Who Reviewed a Product");
        System.out.println(" 0. Exit");
        System.out.print("Enter your choice: ");
    }

    // Routes user input to specific handler methods
    private static void handleMenuChoice(int choice) {
        switch (choice) {
            // Data Management
            case 1: loadAllCSVs(); break;
            
            // File Loaders (Individual)
            case 2: loadProductsFromCSV(); break;
            case 3: loadCustomersFromCSV(); break;
            case 4: loadOrdersFromCSV(); break;
            case 5: loadReviewsFromCSV(); break;
            
            // Modification Operations (Trigger Auto-Save)
            case 6: handleAddProduct(); break;
            case 7: handleRegisterCustomer(); break;
            case 8: handlePlaceOrder(); break;
            case 9: handleAddReview(); break;
            case 10: handleEditReview(); break;
            case 11: handleUpdateOrderStatus(); break;
            case 12: handleCancelOrder(); break;
            
            // Display Operations (Traversal)
            case 13: system.displayAllProducts(); break;
            case 14: system.displayAllCustomers(); break;
            case 15: system.displayAllOrders(); break;
            
            // Search Operations (Binary Search)
            case 16: handleFindProductById(); break;
            case 17: handleFindCustomerById(); break;
            case 18: handleFindOrderById(); break;
            
            // Analytical Queries
            case 19: handleGetReviewsByCustomer(); break;
            case 20: handleGetTop3Products(); break;
            case 21: handleGetOrdersBetweenDates(); break;
            case 22: handleGetCommonProducts(); break;
            case 23: handleGetOutOfStockProducts(); break;
            
            // Advanced Queries
            case 24: handleUpdateProduct(); break;
            case 25: handleProductPriceRange(); break;
            case 26: handleListCustomersAlpha(); break;
            case 27: handleProductReviewers(); break;
            
            case 0:
                saveAllCSVs(); // Ensure data is persisted before exit
                System.out.println("Exiting system. Goodbye!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    // Persists in-memory BST data to CSV files. 
    // Called automatically after any state-changing operation (Add/Edit/Delete).
    private static void saveAllCSVs() {
        System.out.println(">> Auto-Saving data to CSVs...");
        system.saveProductsToCSV(PRODUCT_FILE);
        system.saveCustomersToCSV(CUSTOMER_FILE);
        system.saveOrdersToCSV(ORDER_FILE);
        system.saveReviewsToCSV(REVIEW_FILE);
        System.out.println(">> Save complete.");
    }

    // Re-initializes BSTs and loads data from CSVs. Used at startup and manual reload.
    private static void loadAllCSVs() {
        System.out.println("Attempting to load all data...");
        system.loadProductsFromCSV(PRODUCT_FILE);
        system.loadCustomersFromCSV(CUSTOMER_FILE);
        system.loadOrdersFromCSV(ORDER_FILE);
        system.loadReviewsFromCSV(REVIEW_FILE);
        System.out.println("Finished loading all CSVs.");
    }

    // Individual file loaders allowing custom filenames if needed (though defaults are set)
    private static void loadProductsFromCSV() {
        System.out.print("Enter product CSV filename: ");
        String filename = scanner.nextLine();
        system.loadProductsFromCSV(filename);
    }

    private static void loadCustomersFromCSV() {
        System.out.print("Enter customer CSV filename: ");
        String filename = scanner.nextLine();
        system.loadCustomersFromCSV(filename);
    }

    private static void loadOrdersFromCSV() {
        System.out.print("Enter order CSV filename: ");
        String filename = scanner.nextLine();
        system.loadOrdersFromCSV(filename);
    }

    private static void loadReviewsFromCSV() {
        System.out.print("Enter review CSV filename: ");
        String filename = scanner.nextLine();
        system.loadReviewsFromCSV(filename);
    }

    // Creates a Product object and inserts it into the BST. Auto-saves on success.
    private static void handleAddProduct() {
        try {
            System.out.print("Enter Product ID: ");
            int id = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter Name: ");
            String name = scanner.nextLine();
            System.out.print("Enter Price: ");
            double price = Double.parseDouble(scanner.nextLine());
            System.out.print("Enter Stock: ");
            int stock = Integer.parseInt(scanner.nextLine());
            
            system.addProduct(new Product(id, name, price, stock));
            saveAllCSVs(); 
        } catch (Exception e) { 
            System.out.println("Invalid input. Please check your data formats."); 
        }
    }

    // Registers a new customer into the system. Auto-saves on success.
    private static void handleRegisterCustomer() {
        try {
            System.out.print("Enter Customer ID: ");
            int id = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter Name: ");
            String name = scanner.nextLine();
            System.out.print("Enter Email: ");
            String email = scanner.nextLine();
            
            system.registerCustomer(new Customer(id, name, email));
            saveAllCSVs(); 
        } catch (Exception e) { 
            System.out.println("Invalid input."); 
        }
    }

    // Handles complex order creation logic:
    // 1. Creates Order object. 2. Adds multiple products via loop. 3. Calculates total price. 4. Saves.
    private static void handlePlaceOrder() {
        try {
            System.out.print("Enter Order ID: ");
            int orderId = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter Customer ID: ");
            int custId = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter Order Date (YYYY-MM-DD): ");
            String date = scanner.nextLine();
            
            // Total price initialized to 0.0; calculateTotalPrice() inside placeOrder will update it.
            Order order = new Order(orderId, custId, 0.0, date, "Pending");
            
            while (true) {
                System.out.print("Enter Product ID to add (or 'done'): ");
                String pid = scanner.nextLine();
                if (pid.equalsIgnoreCase("done")) break;
                try { 
                    order.addProduct(Integer.parseInt(pid)); 
                } catch (NumberFormatException e) { 
                    System.out.println("Invalid ID format."); 
                }
            }
            if(system.placeOrder(order)) {
                saveAllCSVs(); 
            }
        } catch (Exception e) { 
            System.out.println("Invalid input during order placement."); 
        }
    }

    // Adds a review to a product. Auto-saves on success.
    private static void handleAddReview() {
        try {
            System.out.print("Enter Product ID: ");
            int pid = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter Customer ID: ");
            int cid = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter Rating (1-5): ");
            int rating = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter Comment: ");
            String comment = scanner.nextLine();
            
            // System validates rating range (1-5) internally
            if(system.addReview(pid, cid, rating, comment)) {
                saveAllCSVs(); 
            }
        } catch (Exception e) { 
            System.out.println("Invalid input."); 
        }
    }

    // Edits an existing review based on its unique ID.
    private static void handleEditReview() {
        try {
            System.out.print("Enter Review ID to edit: ");
            int rid = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter New Rating (1-5): ");
            int rating = Integer.parseInt(scanner.nextLine());
            
            // Input validation for rating
            if(rating < 1 || rating > 5){
                System.out.println("Invalid rating. Must be 1-5.");
                return;
            }
            
            System.out.print("Enter New Comment: ");
            String comment = scanner.nextLine();
            
            if(system.editReview(rid, rating, comment)) {
                saveAllCSVs(); 
            }
        } catch (Exception e) { 
            System.out.println("Invalid input."); 
        }
    }

    // Updates the status string of an order (e.g., "Pending" -> "Shipped").
    private static void handleUpdateOrderStatus() {
        try {
            System.out.print("Enter Order ID to update: ");
            int oid = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter New Status: ");
            String status = scanner.nextLine();
            
            if(system.updateOrderStatus(oid, status)) {
                saveAllCSVs(); 
            }
        } catch (Exception e) { 
            System.out.println("Invalid input."); 
        }
    }

    // Marks an order as "Cancelled".
    private static void handleCancelOrder() {
        try {
            System.out.print("Enter Order ID to cancel: ");
            int oid = Integer.parseInt(scanner.nextLine());
            if(system.cancelOrder(oid)) {
                saveAllCSVs(); 
            }
        } catch (Exception e) { 
            System.out.println("Invalid input."); 
        }
    }

    // Uses BST search to find a product by ID in O(log n).
    private static void handleFindProductById() {
        try {
            System.out.print("Enter Product ID: ");
            int pid = Integer.parseInt(scanner.nextLine());
            Product p = system.findProductById(pid);
            if (p != null) System.out.println("Found: " + p);
            else System.out.println("Product not found.");
        } catch (Exception e) { 
            System.out.println("Invalid input."); 
        }
    }
    
    // Uses BST search to find a customer by ID.
    private static void handleFindCustomerById() {
        try {
            System.out.print("Enter Customer ID: ");
            int cid = Integer.parseInt(scanner.nextLine());
            Customer c = system.findCustomerById(cid);
            if (c != null) System.out.println("Found: " + c);
            else System.out.println("Customer not found.");
        } catch (Exception e) { 
            System.out.println("Invalid input."); 
        }
    }
    
    // Uses BST search to find an order by ID.
    private static void handleFindOrderById() {
        try {
            System.out.print("Enter Order ID: ");
            int oid = Integer.parseInt(scanner.nextLine());
            Order o = system.findOrderById(oid);
            if (o != null) System.out.println("Found: " + o);
            else System.out.println("Order not found.");
        } catch (Exception e) { 
            System.out.println("Invalid input."); 
        }
    }

    // Iterates through review list to find matches for a customer ID.
    private static void handleGetReviewsByCustomer() {
        try {
            System.out.print("Enter Customer ID: ");
            int cid = Integer.parseInt(scanner.nextLine());
            CustomLinkedList<Review> reviews = system.getReviewsByCustomer(cid);
            if (reviews.isEmpty()) System.out.println("No reviews found for this customer.");
            else {
                System.out.println("Found " + reviews.size() + " reviews:");
                for (int i = 0; i < reviews.size(); i++) {
                    System.out.println(reviews.get(i));
                }
            }
        } catch (Exception e) { 
            System.out.println("Invalid input."); 
        }
    }

    // Calculates ratings and sorts products to find top 3.
    private static void handleGetTop3Products() {
        System.out.println("--- Top 3 Products by Rating ---");
        CustomLinkedList<Product> top3 = system.getTop3ProductsByRating();
        if (top3.isEmpty()) {
            System.out.println("No products with reviews found.");
        } else {
            for (int i = 0; i < top3.size(); i++) {
                System.out.println(top3.get(i));
            }
        }
    }

    // Filters orders based on date string comparison.
    private static void handleGetOrdersBetweenDates() {
        try {
            System.out.print("Enter Start Date (YYYY-MM-DD): ");
            String start = scanner.nextLine();
            System.out.print("Enter End Date (YYYY-MM-DD): ");
            String end = scanner.nextLine();
            
            CustomLinkedList<Order> orders = system.getOrdersBetweenDates(start, end);
            if (orders.isEmpty()) {
                System.out.println("No orders found in this date range.");
            } else {
                System.out.println("Found " + orders.size() + " orders:");
                for (int i = 0; i < orders.size(); i++) {
                    System.out.println(orders.get(i));
                }
            }
        } catch (Exception e) { 
            System.out.println("Invalid input."); 
        }
    }

    // Identifies products highly rated (>4) by both specified customers.
    private static void handleGetCommonProducts() {
        try {
            System.out.print("Enter Customer ID 1: ");
            int c1 = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter Customer ID 2: ");
            int c2 = Integer.parseInt(scanner.nextLine());
            
            CustomLinkedList<Product> common = system.getCommonHighRatedProducts(c1, c2);
            if (common.isEmpty()) {
                System.out.println("No common, high-rated products found.");
            } else {
                System.out.println("Found " + common.size() + " common products:");
                for (int i = 0; i < common.size(); i++) {
                    System.out.println(common.get(i));
                }
            }
        } catch (Exception e) { 
            System.out.println("Invalid input."); 
        }
    }

    // Lists products with 0 stock.
    private static void handleGetOutOfStockProducts() {
        System.out.println("--- Out of Stock Products ---");
        CustomLinkedList<Product> outOfStock = system.getOutOfStockProducts();
        if (outOfStock.isEmpty()) {
            System.out.println("No products are out of stock.");
        } else {
            for (int i = 0; i < outOfStock.size(); i++) {
                System.out.println(outOfStock.get(i));
            }
        }
    }

    // Updates product attributes (Name, Price, Stock) by ID. Auto-saves.
    private static void handleUpdateProduct() {
        try {
            System.out.print("Enter Product ID to Update: ");
            int id = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter New Name: ");
            String name = scanner.nextLine();
            System.out.print("Enter New Price: ");
            double price = Double.parseDouble(scanner.nextLine());
            System.out.print("Enter New Stock: ");
            int stock = Integer.parseInt(scanner.nextLine());
            
            if(system.updateProduct(id, name, price, stock)) {
                saveAllCSVs();
            }
        } catch (Exception e) { 
            System.out.println("Invalid input."); 
        }
    }

    // Performs a range query on product prices.
    private static void handleProductPriceRange() {
        try {
            System.out.print("Enter Min Price: ");
            double min = Double.parseDouble(scanner.nextLine());
            System.out.print("Enter Max Price: ");
            double max = Double.parseDouble(scanner.nextLine());
            
            CustomLinkedList<Product> list = system.getProductsInPriceRange(min, max);
            System.out.println("Found " + list.size() + " products:");
            for (int i = 0; i < list.size(); i++) {
                System.out.println(list.get(i));
            }
        } catch (Exception e) { 
            System.out.println("Invalid input."); 
        }
    }

    // Uses selection sort to display customers alphabetically.
    private static void handleListCustomersAlpha() {
        System.out.println("--- Customers Sorted Alphabetically ---");
        CustomLinkedList<Customer> list = system.getCustomersSortedByName();
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
    }

    // Lists all customers who reviewed a specific product, sorted by rating.
    private static void handleProductReviewers() {
        try {
            System.out.print("Enter Product ID: ");
            int id = Integer.parseInt(scanner.nextLine());
            System.out.println("--- Customers who reviewed this product (Sorted by Rating) ---");
            CustomLinkedList<Customer> list = system.getCustomersWhoReviewedProduct(id);
            if (list.isEmpty()) {
                System.out.println("No reviews found.");
            } else {
                for (int i = 0; i < list.size(); i++) {
                    System.out.println(list.get(i));
                }
            }
        } catch (Exception e) { 
            System.out.println("Invalid input."); 
        }
    }
}
