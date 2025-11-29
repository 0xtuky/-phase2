import java.util.Scanner;

public class ECommerceDemo {

    private static ECommerceSystem system = new ECommerceSystem();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Welcome to the E-Commerce Management System!");
        
        while (true) {
            printMenu();
            try {
                String input = scanner.nextLine();
                if (input.isEmpty()) continue; 
                int choice = Integer.parseInt(input);
                handleMenuChoice(choice);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n===== E-Commerce System Menu =====");
        System.out.println("--- Data Management ---");
        System.out.println(" 1. Load All Data from CSVs");
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
        // Option 24 removed as requested
        System.out.println(" 0. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void handleMenuChoice(int choice) {
        switch (choice) {
            case 1: loadAllCSVs(); break;
            case 2: loadProductsFromCSV(); break;
            case 3: loadCustomersFromCSV(); break;
            case 4: loadOrdersFromCSV(); break;
            case 5: loadReviewsFromCSV(); break;
            case 6: handleAddProduct(); break;
            case 7: handleRegisterCustomer(); break;
            case 8: handlePlaceOrder(); break;
            case 9: handleAddReview(); break;
            case 10: handleEditReview(); break;
            case 11: handleUpdateOrderStatus(); break;
            case 12: handleCancelOrder(); break;
            case 13: system.displayAllProducts(); break;
            case 14: system.displayAllCustomers(); break;
            case 15: system.displayAllOrders(); break;
            case 16: handleFindProductById(); break;
            case 17: handleFindCustomerById(); break;
            case 18: handleFindOrderById(); break;
            case 19: handleGetReviewsByCustomer(); break;
            case 20: handleGetTop3Products(); break;
            case 21: handleGetOrdersBetweenDates(); break;
            case 22: handleGetCommonProducts(); break;
            case 23: handleGetOutOfStockProducts(); break;
            case 0:
                saveAllCSVs(); // Still auto-saves on exit
                System.out.println("Exiting system. Goodbye!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    // Helper method used by other operations to auto-save
    private static void saveAllCSVs() {
        System.out.println(">> Auto-Saving data to CSVs...");
        system.saveProductsToCSV("products.csv");
        system.saveCustomersToCSV("customers.csv");
        system.saveOrdersToCSV("orders.csv");
        system.saveReviewsToCSV("reviews.csv");
        System.out.println(">> Save complete.");
    }

    private static void loadAllCSVs() {
        System.out.println("Attempting to load all data from default CSV files...");
        system.loadProductsFromCSV("products.csv");
        system.loadCustomersFromCSV("customers.csv");
        system.loadOrdersFromCSV("orders.csv");
        system.loadReviewsFromCSV("reviews.csv");
        System.out.println("Finished loading all CSVs.");
    }

    private static void loadProductsFromCSV() {
        System.out.print("Enter product CSV filename (e.g., products.csv): ");
        String filename = scanner.nextLine();
        system.loadProductsFromCSV(filename);
    }

    private static void loadCustomersFromCSV() {
        System.out.print("Enter customer CSV filename (e.g., customers.csv): ");
        String filename = scanner.nextLine();
        system.loadCustomersFromCSV(filename);
    }

    private static void loadOrdersFromCSV() {
        System.out.print("Enter order CSV filename (e.g., orders.csv): ");
        String filename = scanner.nextLine();
        system.loadOrdersFromCSV(filename);
    }

    private static void loadReviewsFromCSV() {
        System.out.print("Enter review CSV filename (e.g., reviews.csv): ");
        String filename = scanner.nextLine();
        system.loadReviewsFromCSV(filename);
    }

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
            saveAllCSVs(); // AUTO SAVE
        } catch (Exception e) {
            System.out.println("Invalid input. " + e.getMessage());
        }
    }

    private static void handleRegisterCustomer() {
        try {
            System.out.print("Enter Customer ID: ");
            int id = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter Name: ");
            String name = scanner.nextLine();
            System.out.print("Enter Email: ");
            String email = scanner.nextLine();
            
            system.registerCustomer(new Customer(id, name, email));
            saveAllCSVs(); // AUTO SAVE
        } catch (Exception e) {
            System.out.println("Invalid input. " + e.getMessage());
        }
    }

    private static void handlePlaceOrder() {
        try {
            System.out.print("Enter Order ID: ");
            int orderId = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter Customer ID: ");
            int custId = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter Total Price: ");
            double price = Double.parseDouble(scanner.nextLine());
            System.out.print("Enter Order Date (YYYY-MM-DD): ");
            String date = scanner.nextLine();
            
            Order order = new Order(orderId, custId, price, date, "Pending");
            
            while (true) {
                System.out.print("Enter a Product ID to add (or 'done' to finish): ");
                String pid = scanner.nextLine();
                if (pid.equalsIgnoreCase("done")) break;
                try {
                    order.addProduct(Integer.parseInt(pid));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid Product ID.");
                }
            }
            if(system.placeOrder(order)) {
                saveAllCSVs(); // AUTO SAVE
            }
        } catch (Exception e) {
            System.out.println("Invalid input. " + e.getMessage());
        }
    }

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
            
            if(system.addReview(pid, cid, rating, comment)) {
                saveAllCSVs(); // AUTO SAVE
            }
        } catch (Exception e) {
            System.out.println("Invalid input. " + e.getMessage());
        }
    }

    private static void handleEditReview() {
        try {
            System.out.print("Enter Review ID to edit: ");
            int rid = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter New Rating (1-5): ");
            int rating = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter New Comment: ");
            String comment = scanner.nextLine();
            
            if(system.editReview(rid, rating, comment)) {
                saveAllCSVs(); // AUTO SAVE
            }
        } catch (Exception e) {
            System.out.println("Invalid input. " + e.getMessage());
        }
    }

    private static void handleUpdateOrderStatus() {
        try {
            System.out.print("Enter Order ID to update: ");
            int oid = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter New Status (e.g., Shipped, Delivered): ");
            String status = scanner.nextLine();
            
            if(system.updateOrderStatus(oid, status)) {
                saveAllCSVs(); // AUTO SAVE
            }
        } catch (Exception e) {
            System.out.println("Invalid input. " + e.getMessage());
        }
    }

    private static void handleCancelOrder() {
        try {
            System.out.print("Enter Order ID to cancel: ");
            int oid = Integer.parseInt(scanner.nextLine());
            if(system.cancelOrder(oid)) {
                saveAllCSVs(); // AUTO SAVE
            }
        } catch (Exception e) {
            System.out.println("Invalid input. " + e.getMessage());
        }
    }

    private static void handleFindProductById() {
        try {
            System.out.print("Enter Product ID: ");
            int pid = Integer.parseInt(scanner.nextLine());
            Product p = system.findProductById(pid);
            if (p != null) {
                System.out.println("Found: " + p);
            } else {
                System.out.println("Product not found.");
            }
        } catch (Exception e) {
            System.out.println("Invalid input. " + e.getMessage());
        }
    }
    
    private static void handleFindCustomerById() {
        try {
            System.out.print("Enter Customer ID: ");
            int cid = Integer.parseInt(scanner.nextLine());
            Customer c = system.findCustomerById(cid);
            if (c != null) {
                System.out.println("Found: " + c);
            } else {
                System.out.println("Customer not found.");
            }
        } catch (Exception e) {
            System.out.println("Invalid input. " + e.getMessage());
        }
    }
    
    private static void handleFindOrderById() {
        try {
            System.out.print("Enter Order ID: ");
            int oid = Integer.parseInt(scanner.nextLine());
            Order o = system.findOrderById(oid);
            if (o != null) {
                System.out.println("Found: " + o);
            } else {
                System.out.println("Order not found.");
            }
        } catch (Exception e) {
            System.out.println("Invalid input. " + e.getMessage());
        }
    }

    private static void handleGetReviewsByCustomer() {
        try {
            System.out.print("Enter Customer ID: ");
            int cid = Integer.parseInt(scanner.nextLine());
            CustomLinkedList<Review> reviews = system.getReviewsByCustomer(cid);
            if (reviews.isEmpty()) {
                System.out.println("No reviews found for this customer.");
            } else {
                System.out.println("Found " + reviews.size() + " reviews:");
                for (int i = 0; i < reviews.size(); i++) {
                    System.out.println(reviews.get(i));
                }
            }
        } catch (Exception e) {
            System.out.println("Invalid input. " + e.getMessage());
        }
    }

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
            System.out.println("Invalid input. " + e.getMessage());
        }
    }

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
                System.out.println("Found " + common.size() + " common, high-rated products:");
                for (int i = 0; i < common.size(); i++) {
                    System.out.println(common.get(i));
                }
            }
        } catch (Exception e) {
            System.out.println("Invalid input. " + e.getMessage());
        }
    }

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
}
