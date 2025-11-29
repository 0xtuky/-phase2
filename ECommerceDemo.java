import java.util.Scanner;

public class ECommerceDemo {

    private static ECommerceSystem system = new ECommerceSystem();
    private static Scanner scanner = new Scanner(System.in);

    // CENTRALIZED FILE PATHS
    private static final String PRODUCT_FILE = "C:\\Users\\lvxpq\\OneDrive\\سطح المكتب\\prodcuts.csv";
    private static final String CUSTOMER_FILE = "C:\\Users\\lvxpq\\OneDrive\\سطح المكتب\\customers.csv";
    private static final String ORDER_FILE = "C:\\Users\\lvxpq\\OneDrive\\سطح المكتب\\orders.csv";
    private static final String REVIEW_FILE = "C:\\Users\\lvxpq\\OneDrive\\سطح المكتب\\reviews.csv";

    public static void main(String[] args) {
        System.out.println("Welcome to the E-Commerce Management System!");
        loadAllCSVs();

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
        System.out.println(" 22. Get Common High-Rated Products");
        System.out.println(" 23. Get Out-of-Stock Products");
        System.out.println(" 24. Update Product Details");
        System.out.println(" 25. List Products in Price Range");
        System.out.println(" 26. List Customers Alphabetically");
        System.out.println(" 27. List Customers Who Reviewed a Product");
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
            case 24: handleUpdateProduct(); break;
            case 25: handleProductPriceRange(); break;
            case 26: handleListCustomersAlpha(); break;
            case 27: handleProductReviewers(); break;
            case 0:
                saveAllCSVs(); 
                System.out.println("Exiting system. Goodbye!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void saveAllCSVs() {
        System.out.println(">> Auto-Saving data to CSVs...");
        system.saveProductsToCSV(PRODUCT_FILE);
        system.saveCustomersToCSV(CUSTOMER_FILE);
        system.saveOrdersToCSV(ORDER_FILE);
        system.saveReviewsToCSV(REVIEW_FILE);
        System.out.println(">> Save complete.");
    }

    private static void loadAllCSVs() {
        System.out.println("Attempting to load all data...");
        system.loadProductsFromCSV(PRODUCT_FILE);
        system.loadCustomersFromCSV(CUSTOMER_FILE);
        system.loadOrdersFromCSV(ORDER_FILE);
        system.loadReviewsFromCSV(REVIEW_FILE);
        System.out.println("Finished loading all CSVs.");
    }

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
        } catch (Exception e) { System.out.println("Invalid input."); }
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
            saveAllCSVs(); 
        } catch (Exception e) { System.out.println("Invalid input."); }
    }

    private static void handlePlaceOrder() {
        try {
            System.out.print("Enter Order ID: ");
            int orderId = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter Customer ID: ");
            int custId = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter Order Date (YYYY-MM-DD): ");
            String date = scanner.nextLine();
            Order order = new Order(orderId, custId, 0.0, date, "Pending");
            while (true) {
                System.out.print("Enter Product ID to add (or 'done'): ");
                String pid = scanner.nextLine();
                if (pid.equalsIgnoreCase("done")) break;
                try { order.addProduct(Integer.parseInt(pid)); } 
                catch (NumberFormatException e) { System.out.println("Invalid ID."); }
            }
            if(system.placeOrder(order)) saveAllCSVs(); 
        } catch (Exception e) { System.out.println("Invalid input."); }
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
            if(system.addReview(pid, cid, rating, comment)) saveAllCSVs(); 
        } catch (Exception e) { System.out.println("Invalid input."); }
    }

    private static void handleEditReview() {
        try {
            System.out.print("Enter Review ID: ");
            int rid = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter New Rating (1-5): ");
            int rating = Integer.parseInt(scanner.nextLine());
            if(rating<1 || rating>5){
                System.out.println("invalid rating");
                return;
            }
            System.out.print("Enter New Comment: ");
            String comment = scanner.nextLine();
            if(system.editReview(rid, rating, comment)) saveAllCSVs(); 
        } catch (Exception e) { System.out.println("Invalid input."); }
    }

    private static void handleUpdateOrderStatus() {
        try {
            System.out.print("Enter Order ID: ");
            int oid = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter New Status: ");
            String status = scanner.nextLine();
            if(system.updateOrderStatus(oid, status)) saveAllCSVs(); 
        } catch (Exception e) { System.out.println("Invalid input."); }
    }

    private static void handleCancelOrder() {
        try {
            System.out.print("Enter Order ID: ");
            int oid = Integer.parseInt(scanner.nextLine());
            if(system.cancelOrder(oid)) saveAllCSVs(); 
        } catch (Exception e) { System.out.println("Invalid input."); }
    }

    private static void handleFindProductById() {
        try {
            System.out.print("Enter Product ID: ");
            int pid = Integer.parseInt(scanner.nextLine());
            Product p = system.findProductById(pid);
            if (p != null) System.out.println(p);
            else System.out.println("Not found.");
        } catch (Exception e) { System.out.println("Invalid input."); }
    }
    
    private static void handleFindCustomerById() {
        try {
            System.out.print("Enter Customer ID: ");
            int cid = Integer.parseInt(scanner.nextLine());
            Customer c = system.findCustomerById(cid);
            if (c != null) System.out.println(c);
            else System.out.println("Not found.");
        } catch (Exception e) { System.out.println("Invalid input."); }
    }
    
    private static void handleFindOrderById() {
        try {
            System.out.print("Enter Order ID: ");
            int oid = Integer.parseInt(scanner.nextLine());
            Order o = system.findOrderById(oid);
            if (o != null) System.out.println(o);
            else System.out.println("Not found.");
        } catch (Exception e) { System.out.println("Invalid input."); }
    }

    private static void handleGetReviewsByCustomer() {
        try {
            System.out.print("Enter Customer ID: ");
            int cid = Integer.parseInt(scanner.nextLine());
            CustomLinkedList<Review> reviews = system.getReviewsByCustomer(cid);
            if (reviews.isEmpty()) System.out.println("No reviews found.");
            else for (int i = 0; i < reviews.size(); i++) System.out.println(reviews.get(i));
        } catch (Exception e) { System.out.println("Invalid input."); }
    }

    private static void handleGetTop3Products() {
        System.out.println("--- Top 3 Products ---");
        CustomLinkedList<Product> top3 = system.getTop3ProductsByRating();
        for (int i = 0; i < top3.size(); i++) System.out.println(top3.get(i));
    }

    private static void handleGetOrdersBetweenDates() {
        System.out.print("Start Date: ");
        String start = scanner.nextLine();
        System.out.print("End Date: ");
        String end = scanner.nextLine();
        CustomLinkedList<Order> orders = system.getOrdersBetweenDates(start, end);
        for (int i = 0; i < orders.size(); i++) System.out.println(orders.get(i));
    }

    private static void handleGetCommonProducts() {
        try {
            System.out.print("Cust ID 1: ");
            int c1 = Integer.parseInt(scanner.nextLine());
            System.out.print("Cust ID 2: ");
            int c2 = Integer.parseInt(scanner.nextLine());
            CustomLinkedList<Product> common = system.getCommonHighRatedProducts(c1, c2);
            for (int i = 0; i < common.size(); i++) System.out.println(common.get(i));
        } catch (Exception e) { System.out.println("Invalid input."); }
    }

    private static void handleGetOutOfStockProducts() {
        System.out.println("--- Out of Stock ---");
        CustomLinkedList<Product> list = system.getOutOfStockProducts();
        for (int i = 0; i < list.size(); i++) System.out.println(list.get(i));
    }

    // === NEW HANDLERS FOR PHASE II ===

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
            if(system.updateProduct(id, name, price, stock)) saveAllCSVs();
        } catch (Exception e) { System.out.println("Invalid input."); }
    }

    private static void handleProductPriceRange() {
        try {
            System.out.print("Enter Min Price: ");
            double min = Double.parseDouble(scanner.nextLine());
            System.out.print("Enter Max Price: ");
            double max = Double.parseDouble(scanner.nextLine());
            CustomLinkedList<Product> list = system.getProductsInPriceRange(min, max);
            System.out.println("Found " + list.size() + " products:");
            for (int i = 0; i < list.size(); i++) System.out.println(list.get(i));
        } catch (Exception e) { System.out.println("Invalid input."); }
    }

    private static void handleListCustomersAlpha() {
        System.out.println("--- Customers Sorted Alphabetically ---");
        CustomLinkedList<Customer> list = system.getCustomersSortedByName();
        for (int i = 0; i < list.size(); i++) System.out.println(list.get(i));
    }

    private static void handleProductReviewers() {
        try {
            System.out.print("Enter Product ID: ");
            int id = Integer.parseInt(scanner.nextLine());
            System.out.println("--- Customers who reviewed this product (Sorted by Rating) ---");
            CustomLinkedList<Customer> list = system.getCustomersWhoReviewedProduct(id);
            if (list.isEmpty()) System.out.println("No reviews found.");
            else for (int i = 0; i < list.size(); i++) System.out.println(list.get(i));
        } catch (Exception e) { System.out.println("Invalid input."); }
    }
}
