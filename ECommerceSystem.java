import java.io.*;

/**
 * Core system logic for the E-Commerce platform.
 * * Data Structure Design:
 * - Products, Customers, and Orders are stored in Binary Search Trees (BST) 
 * to ensure O(log n) time complexity for search, insertion, and deletion operations.
 * - Reviews are stored in a Custom Linked List since they are typically accessed 
 * sequentially per product or customer, making linear traversal O(n) acceptable.
 */
public class ECommerceSystem {
    
    // BSTs for scalable data management
    private BST<Product> products;
    private BST<Customer> customers;
    private BST<Order> orders;
    
    // Linked List for secondary data relationships
    private CustomLinkedList<Review> reviews;
    
    public ECommerceSystem() {
        products = new BST<>();
        customers = new BST<>();
        orders = new BST<>();
        reviews = new CustomLinkedList<>();
    }
    
    // ================= DATA LOADING (File I/O) ================= //
   
    /**
     * Loads products from a CSV file into the BST.
     * Re-initializes the tree to prevent duplicates upon reload.
     * Includes error handling to skip specific corrupted lines without crashing.
     */
    public void loadProductsFromCSV(String filename) {
        products = new BST<>(); 
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine(); // Skip CSV header
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                try {
                    String[] data = parseCSVLine(line);
                    int productId = Integer.parseInt(data[0]);
                    String name = data[1].replace("\"", ""); // sanitize quotes
                    double price = Double.parseDouble(data[2]);
                    int stock = Integer.parseInt(data[3]);
                    
                    Product product = new Product(productId, name, price, stock);
                    products.insert(productId, product); // O(log n) insertion
                } catch (Exception e) {
                    System.out.println("Skipping corrupted product line: " + line);
                }
            }
            System.out.println("Products loaded into BST.");
        } catch (IOException e) {
            System.out.println("Error reading product file: " + e.getMessage());
        }
    }
    
    /**
     * Loads customers from CSV into BST.
     */
    public void loadCustomersFromCSV(String filename) {
        customers = new BST<>(); 
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine(); 
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                try {
                    String[] data = parseCSVLine(line);
                    int customerId = Integer.parseInt(data[0]);
                    String name = data[1].replace("\"", "");
                    String email = data[2].replace("\"", "");
                    
                    Customer customer = new Customer(customerId, name, email);
                    customers.insert(customerId, customer);
                } catch (Exception e) {
                    System.out.println("Skipping corrupted customer line: " + line);
                }
            }
            System.out.println("Customers loaded into BST.");
        } catch (IOException e) {
            System.out.println("Error reading customer file: " + e.getMessage());
        }
    }
    
    /**
     * Loads orders from CSV.
     * Parses nested product IDs (semicolon separated) and links the order 
     * to the corresponding Customer object in memory.
     */
    public void loadOrdersFromCSV(String filename) {
        orders = new BST<>(); 
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine(); 
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                try {
                    String[] data = parseCSVLine(line);
                    int orderId = Integer.parseInt(data[0]);
                    int customerId = Integer.parseInt(data[1]);
                    String productIdsStr = data[2].replace("\"", "");
                    double totalPrice = Double.parseDouble(data[3]);
                    String orderDate = data[4];
                    String status = data[5];
                    
                    Order order = new Order(orderId, customerId, totalPrice, orderDate, status);
                    
                    // Parse nested product IDs "101;102;103"
                    if (!productIdsStr.isEmpty()) {
                        String[] productIds = productIdsStr.split(";");
                        for (String pid : productIds) {
                            if (!pid.trim().isEmpty()) {
                                order.addProduct(Integer.parseInt(pid));
                            }
                        }
                    }
                    orders.insert(orderId, order);
                    
                    // Maintain relationship: Add order to Customer's history
                    Customer customer = findCustomerById(customerId);
                    if (customer != null) {
                        customer.addOrder(order);
                    }
                } catch (Exception e) {
                    System.out.println("Skipping corrupted order line: " + line);
                }
            }
            System.out.println("Orders loaded into BST.");
        } catch (IOException e) {
            System.out.println("Error reading order file: " + e.getMessage());
        }
    }
    
    /**
     * Loads reviews and links them to the specific Product object.
     */
    public void loadReviewsFromCSV(String filename) {
        reviews.clear(); 
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine(); 
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                try {
                    String[] data = parseCSVLine(line);
                    int reviewId = Integer.parseInt(data[0]);
                    int productId = Integer.parseInt(data[1]);
                    int customerId = Integer.parseInt(data[2]);
                    int rating = Integer.parseInt(data[3]);
                    String comment = data[4].replace("\"", "");
                    
                    Review review = new Review(reviewId, productId, customerId, rating, comment);
                    reviews.add(review); 
                    
                    // Link review to Product for easier average rating calculation
                    Product product = findProductById(productId);
                    if (product != null) {
                        product.addReview(review);
                    }
                } catch (Exception e) {
                    System.out.println("Skipping corrupted review line: " + line);
                }
            }
            System.out.println("Reviews loaded.");
        } catch (IOException e) {
            System.out.println("Error reading review file: " + e.getMessage());
        }
    }
    
    /**
     * Custom CSV Parser.
     * Handles cases where data fields (like names or comments) contain commas 
     * by checking if they are enclosed in quotes.
     */
    private String[] parseCSVLine(String line) {
        CustomLinkedList<String> result = new CustomLinkedList<>();
        boolean inQuotes = false;
        StringBuilder current = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') { inQuotes = !inQuotes; } 
            else if (c == ',' && !inQuotes) {
                result.add(current.toString());
                current = new StringBuilder();
            } else { current.append(c); }
        }
        result.add(current.toString());
        String[] arr = new String[result.size()];
        for (int i = 0; i < result.size(); i++) { arr[i] = result.get(i); }
        return arr;
    }
    
    // ================= DATA SAVING ================= //

    /**
     * Saves Products to CSV.
     * Uses BST.getAll() which performs an In-Order Traversal to 
     * ensure data is saved sorted by ID.
     */
    public void saveProductsToCSV(String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            bw.write("ProductId,Name,Price,Stock");
            bw.newLine();
            CustomLinkedList<Product> allProducts = products.getAll(); // O(n) traversal
            for (int i = 0; i < allProducts.size(); i++) {
                Product p = allProducts.get(i);
                String safeName = "\"" + p.getName() + "\""; // Escape quotes
                String line = p.getProductId() + "," + safeName + "," + p.getPrice() + "," + p.getStock();
                bw.write(line);
                bw.newLine();
            }
            System.out.println("Products saved.");
        } catch (IOException e) { System.out.println("Error saving products: " + e.getMessage()); }
    }

    public void saveCustomersToCSV(String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            bw.write("CustomerId,Name,Email");
            bw.newLine();
            CustomLinkedList<Customer> allCustomers = customers.getAll();
            for (int i = 0; i < allCustomers.size(); i++) {
                Customer c = allCustomers.get(i);
                String safeName = "\"" + c.getName() + "\"";
                String safeEmail = "\"" + c.getEmail() + "\"";
                String line = c.getCustomerId() + "," + safeName + "," + safeEmail;
                bw.write(line);
                bw.newLine();
            }
            System.out.println("Customers saved.");
        } catch (IOException e) { System.out.println("Error saving customers: " + e.getMessage()); }
    }

    public void saveOrdersToCSV(String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            bw.write("OrderId,CustomerId,ProductIds,TotalPrice,OrderDate,Status");
            bw.newLine();
            CustomLinkedList<Order> allOrders = orders.getAll();
            for (int i = 0; i < allOrders.size(); i++) {
                Order o = allOrders.get(i);
                // Reconstruct product IDs list "1;2;3"
                StringBuilder pIds = new StringBuilder();
                CustomLinkedList<Integer> ids = o.getProductIds();
                for (int j = 0; j < ids.size(); j++) {
                    pIds.append(ids.get(j));
                    if (j < ids.size() - 1) pIds.append(";");
                }
                String pIdsStr = "\"" + pIds.toString() + "\"";
                String line = o.getOrderId() + "," + o.getCustomerId() + "," + pIdsStr + "," + 
                              o.getTotalPrice() + "," + o.getOrderDate() + "," + o.getStatus();
                bw.write(line);
                bw.newLine();
            }
            System.out.println("Orders saved.");
        } catch (IOException e) { System.out.println("Error saving orders: " + e.getMessage()); }
    }

    public void saveReviewsToCSV(String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            bw.write("ReviewId,ProductId,CustomerId,Rating,Comment");
            bw.newLine();
            for (int i = 0; i < reviews.size(); i++) {
                Review r = reviews.get(i);
                String safeComment = "\"" + r.getComment() + "\"";
                String line = r.getReviewId() + "," + r.getProductId() + "," + 
                              r.getCustomerId() + "," + r.getRating() + "," + safeComment;
                bw.write(line);
                bw.newLine();
            }
            System.out.println("Reviews saved.");
        } catch (IOException e) { System.out.println("Error saving reviews: " + e.getMessage()); }
    }
    
    // ================= CORE OPERATIONS ================= //
   
    // Adds a product to the BST. Time Complexity: O(log n)
    public void addProduct(Product product) {
        products.insert(product.getProductId(), product);
        System.out.println("Product added: " + product.getName());
    }
    
    // Removes a product from the BST. Time Complexity: O(log n)
    public boolean removeProduct(int productId) {
        boolean deleted = products.delete(productId);
        if (deleted) System.out.println("Product removed.");
        else System.out.println("Product not found.");
        return deleted;
    }
    
    // Search O(log n) then update details.
    public boolean updateProduct(int productId, String newName, double newPrice, int newStock) {
        Product product = findProductById(productId);
        if (product != null) {
            product.setName(newName);
            product.setPrice(newPrice);
            product.setStock(newStock);
            System.out.println("Product updated.");
            return true;
        }
        System.out.println("Product not found.");
        return false;
    }
    
    // Performs Binary Search on the Tree.
    public Product findProductById(int productId) {
        return products.find(productId);
    }
    
    // Traverses all products to find those with 0 stock.
    public CustomLinkedList<Product> getOutOfStockProducts() {
        CustomLinkedList<Product> outOfStock = new CustomLinkedList<>();
        CustomLinkedList<Product> all = products.getAll();
        for (int i = 0; i < all.size(); i++) {
            Product p = all.get(i);
            if (p.isOutOfStock()) {
                outOfStock.add(p);
            }
        }
        return outOfStock;
    }
   
    public void registerCustomer(Customer customer) {
        customers.insert(customer.getCustomerId(), customer);
        System.out.println("Customer registered: " + customer.getName());
    }
    
    public Customer findCustomerById(int customerId) {
        return customers.find(customerId);
    }
    
    // Places order and calculates total price dynamically based on current product prices.
    public boolean placeOrder(Order order) {
        Customer customer = findCustomerById(order.getCustomerId());
        if (customer != null) {
            double calculatedTotal = 0.0;
            CustomLinkedList<Integer> pIds = order.getProductIds();
            for (int i = 0; i < pIds.size(); i++) {
                int pid = pIds.get(i);
                Product p = findProductById(pid); // O(log n) lookup per product
                if (p != null) {
                    calculatedTotal += p.getPrice();
                }
            }
            order.setTotalPrice(calculatedTotal);
            
            orders.insert(order.getOrderId(), order);
            customer.addOrder(order);
            
            System.out.println("Order placed. Total: $" + calculatedTotal);
            return true;
        }
        System.out.println("Customer not found.");
        return false;
    }
    
    public boolean cancelOrder(int orderId) {
        Order order = findOrderById(orderId);
        if (order != null) {
            order.cancelOrder();
            System.out.println("Order cancelled.");
            return true;
        }
        System.out.println("Order not found.");
        return false;
    }
    
    public boolean updateOrderStatus(int orderId, String newStatus) {
        Order order = findOrderById(orderId);
        if (order != null) {
            order.updateStatus(newStatus);
            System.out.println("Order status updated!");
            return true;
        }
        System.out.println("Order not found.");
        return false;
    }
    
    public Order findOrderById(int orderId) {
        return orders.find(orderId);
    }
    
    // Validates rating range (1-5) before adding.
    public boolean addReview(int productId, int customerId, int rating, String comment) {
        if (rating < 1 || rating > 5) {
            System.out.println("Error: Rating must be between 1 and 5.");
            return false;
        }
        Product product = findProductById(productId);
        if (product != null) {
            int reviewId = reviews.size() + 1; // Simple auto-increment
            Review review = new Review(reviewId, productId, customerId, rating, comment);
            reviews.add(review);
            product.addReview(review);
            System.out.println("Review added.");
            return true;
        }
        System.out.println("Product not found.");
        return false;
    }

    public boolean editReview(int reviewId, int newRating, String newComment) {
        for (int i = 0; i < reviews.size(); i++) {
            if (reviews.get(i).getReviewId() == reviewId) {
                Review r = reviews.get(i);
                r.setRating(newRating);
                r.setComment(newComment);
                System.out.println("Review " + reviewId + " updated.");
                return true;
            }
        }
        System.out.println("Review not found");
        return false;
    }
    
    public CustomLinkedList<Review> getReviewsByCustomer(int customerId) {
        CustomLinkedList<Review> customerReviews = new CustomLinkedList<>();
        for (int i = 0; i < reviews.size(); i++) {
            Review r = reviews.get(i);
            if (r.getCustomerId() == customerId) {
                customerReviews.add(r);
            }
        }
        return customerReviews;
    }
    
    // ================= ADVANCED QUERIES ================= //

    // Linear search O(n) through product list to find items in range.
    public CustomLinkedList<Product> getProductsInPriceRange(double min, double max) {
        CustomLinkedList<Product> result = new CustomLinkedList<>();
        CustomLinkedList<Product> all = products.getAll(); // Flatten tree to list
        for (int i = 0; i < all.size(); i++) {
            Product p = all.get(i);
            if (p.getPrice() >= min && p.getPrice() <= max) {
                result.add(p);
            }
        }
        return result;
    }

    // Implements Selection Sort O(n^2) to sort customers alphabetically by name.
    public CustomLinkedList<Customer> getCustomersSortedByName() {
        CustomLinkedList<Customer> all = customers.getAll();
        CustomLinkedList<Customer> sorted = new CustomLinkedList<>();
        
        while (!all.isEmpty()) {
            Customer minCust = all.get(0);
            int minIndex = 0;
            // Find alphabetically first name
            for (int i = 1; i < all.size(); i++) {
                if (all.get(i).getName().compareToIgnoreCase(minCust.getName()) < 0) {
                    minCust = all.get(i);
                    minIndex = i;
                }
            }
            sorted.add(minCust);
            all.remove(minIndex);
        }
        return sorted;
    }

    // Filters reviews for a product, sorts them by rating (Selection Sort),
    // then retrieves the associated Customer objects.
    public CustomLinkedList<Customer> getCustomersWhoReviewedProduct(int productId) {
        Product p = findProductById(productId);
        CustomLinkedList<Customer> result = new CustomLinkedList<>();
        
        if (p == null) return result;
        
        CustomLinkedList<Review> prodReviews = p.getReviews();
        CustomLinkedList<Review> tempReviews = new CustomLinkedList<>();
        for (int i = 0; i < prodReviews.size(); i++) tempReviews.add(prodReviews.get(i));
        
        // Sort Reviews: High Rating -> Low Rating
        CustomLinkedList<Review> sortedReviews = new CustomLinkedList<>();
        while(!tempReviews.isEmpty()) {
            Review maxRev = tempReviews.get(0);
            int maxIdx = 0;
            for(int i=1; i<tempReviews.size(); i++) {
                if(tempReviews.get(i).getRating() > maxRev.getRating()) {
                    maxRev = tempReviews.get(i);
                    maxIdx = i;
                }
            }
            sortedReviews.add(maxRev);
            tempReviews.remove(maxIdx);
        }
        
        // Fetch Customers corresponding to sorted reviews
        for(int i=0; i<sortedReviews.size(); i++) {
            Customer c = findCustomerById(sortedReviews.get(i).getCustomerId());
            if(c != null) result.add(c);
        }
        return result;
    }

    // Calculates ratings for all products and returns top 3 using Selection Sort.
    public CustomLinkedList<Product> getTop3ProductsByRating() {
        CustomLinkedList<Product> all = products.getAll();
        CustomLinkedList<Product> rated = new CustomLinkedList<>();
        for (int i = 0; i < all.size(); i++) {
            Product p = all.get(i);
            if (!p.getReviews().isEmpty()) rated.add(p);
        }
        
        CustomLinkedList<Product> top3 = new CustomLinkedList<>();
        while (!rated.isEmpty() && top3.size() < 3) {
            Product maxP = rated.get(0);
            int maxIdx = 0;
            for (int i = 1; i < rated.size(); i++) {
                if (rated.get(i).getAverageRating() > maxP.getAverageRating()) {
                    maxP = rated.get(i);
                    maxIdx = i;
                }
            }
            top3.add(maxP);
            rated.remove(maxIdx);
        }
        return top3;
    }
    
    // Filters orders by performing String comparison on YYYY-MM-DD format.
    public CustomLinkedList<Order> getOrdersBetweenDates(String startDate, String endDate) {
        CustomLinkedList<Order> result = new CustomLinkedList<>();
        CustomLinkedList<Order> all = orders.getAll();
        for (int i = 0; i < all.size(); i++) {
            Order o = all.get(i);
            String orderDate = o.getOrderDate();
            if (orderDate.compareTo(startDate) >= 0 && orderDate.compareTo(endDate) <= 0) {
                result.add(o);
            }
        }
        return result;
    }
    
    // Intersection algorithm: Finds products rated > 4.0 by both Customer A and Customer B.
    public CustomLinkedList<Product> getCommonHighRatedProducts(int c1, int c2) {
        CustomLinkedList<Product> common = new CustomLinkedList<>();
        CustomLinkedList<Review> r1 = getReviewsByCustomer(c1);
        CustomLinkedList<Review> r2 = getReviewsByCustomer(c2);
        
        for (int i = 0; i < r1.size(); i++) {
            for (int j = 0; j < r2.size(); j++) {
                if (r1.get(i).getProductId() == r2.get(j).getProductId()) {
                    Product p = findProductById(r1.get(i).getProductId());
                    if (p != null && p.getAverageRating() > 4.0) {
                        boolean exists = false;
                        for(int k=0; k<common.size(); k++) 
                            if(common.get(k).getProductId() == p.getProductId()) exists = true;
                        
                        if(!exists) common.add(p);
                    }
                }
            }
        }
        return common;
    }
    
    // Helper to print entire product list to console.
    public void displayAllProducts() {
        System.out.println("\n===== ALL PRODUCTS (Sorted by ID) =====");
        CustomLinkedList<Product> all = products.getAll();
        for (int i = 0; i < all.size(); i++) {
            System.out.println(all.get(i));
        }
    }
    
    public void displayAllCustomers() {
        System.out.println("\n===== ALL CUSTOMERS (Sorted by ID) =====");
        CustomLinkedList<Customer> all = customers.getAll();
        for (int i = 0; i < all.size(); i++) {
            System.out.println(all.get(i));
        }
    }
    
    public void displayAllOrders() {
        System.out.println("\n===== ALL ORDERS (Sorted by ID) =====");
        CustomLinkedList<Order> all = orders.getAll();
        for (int i = 0; i < all.size(); i++) {
            System.out.println(all.get(i));
        }
    }
}
