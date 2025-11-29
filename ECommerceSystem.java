import java.io.*;

public class ECommerceSystem {
    
    // Phase II: Core data stored in BSTs
    private BST<Product> products;
    private BST<Customer> customers;
    private BST<Order> orders;
    
    // Reviews can remain in a List as they are usually accessed via Product/Customer, 
    // or you could create a BST keyed by reviewId if strictly required.
    private CustomLinkedList<Review> reviews;
    
    public ECommerceSystem() {
        products = new BST<>();
        customers = new BST<>();
        orders = new BST<>();
        reviews = new CustomLinkedList<>();
    }
    
    // ================= LOAD METHODS ================= //
   
    public void loadProductsFromCSV(String filename) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String line = br.readLine(); // skip header
            
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                int productId = Integer.parseInt(data[0]);
                String name = data[1];
                double price = Double.parseDouble(data[2]);
                int stock = Integer.parseInt(data[3]);
                
                Product product = new Product(productId, name, price, stock);
                // Insert into BST (Key: productId)
                products.insert(productId, product);
            }
            br.close();
            System.out.println("Products loaded into BST.");
        } catch (IOException e) {
            System.out.println("Error loading products: " + e.getMessage());
        }
    }
    
    public void loadCustomersFromCSV(String filename) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String line = br.readLine(); 
            
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                int customerId = Integer.parseInt(data[0]);
                String name = data[1];
                String email = data[2];
                
                Customer customer = new Customer(customerId, name, email);
                // Insert into BST (Key: customerId)
                customers.insert(customerId, customer);
            }
            br.close();
            System.out.println("Customers loaded into BST.");
        } catch (IOException e) {
            System.out.println("Error loading customers: " + e.getMessage());
        }
    }
    
    public void loadOrdersFromCSV(String filename) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String line = br.readLine(); 
            
            while ((line = br.readLine()) != null) {
                String[] data = parseCSVLine(line);
                int orderId = Integer.parseInt(data[0]);
                int customerId = Integer.parseInt(data[1]);
                String productIdsStr = data[2].replace("\"", "");
                double totalPrice = Double.parseDouble(data[3]);
                String orderDate = data[4];
                String status = data[5];
                
                Order order = new Order(orderId, customerId, totalPrice, orderDate, status);
                
                if (!productIdsStr.isEmpty()) {
                    String[] productIds = productIdsStr.split(";");
                    for (String pid : productIds) {
                        if (!pid.trim().isEmpty()) {
                            order.addProduct(Integer.parseInt(pid));
                        }
                    }
                }
                
                // Insert into BST (Key: orderId)
                orders.insert(orderId, order);
                
                // Update Customer History
                Customer customer = findCustomerById(customerId);
                if (customer != null) {
                    customer.addOrder(order);
                }
            }
            br.close();
            System.out.println("Orders loaded into BST.");
        } catch (IOException e) {
            System.out.println("Error loading orders: " + e.getMessage());
        }
    }
    
    public void loadReviewsFromCSV(String filename) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String line = br.readLine(); 
            
            while ((line = br.readLine()) != null) {
                String[] data = parseCSVLine(line);
                int reviewId = Integer.parseInt(data[0]);
                int productId = Integer.parseInt(data[1]);
                int customerId = Integer.parseInt(data[2]);
                int rating = Integer.parseInt(data[3]);
                String comment = data[4].replace("\"", "");
                
                Review review = new Review(reviewId, productId, customerId, rating, comment);
                reviews.add(review); // Keeps simple list for reviews
                
                Product product = findProductById(productId);
                if (product != null) {
                    product.addReview(review);
                }
            }
            br.close();
            System.out.println("Reviews loaded.");
        } catch (IOException e) {
            System.out.println("Error loading reviews: " + e.getMessage());
        }
    }
    
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
    
    // ================= SAVE METHODS ================= //
    // To save, we retrieve ALL items from the BST as a list first

    public void saveProductsToCSV(String filename) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
            bw.write("ProductId,Name,Price,Stock");
            bw.newLine();
            
            // Get all products from BST (Sorted by ID)
            CustomLinkedList<Product> allProducts = products.getAll();
            
            for (int i = 0; i < allProducts.size(); i++) {
                Product p = allProducts.get(i);
                String line = p.getProductId() + "," + p.getName() + "," + p.getPrice() + "," + p.getStock();
                bw.write(line);
                bw.newLine();
            }
            bw.close();
            System.out.println("Products saved.");
        } catch (IOException e) { System.out.println("Error saving products: " + e.getMessage()); }
    }

    public void saveCustomersToCSV(String filename) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
            bw.write("CustomerId,Name,Email");
            bw.newLine();
            
            CustomLinkedList<Customer> allCustomers = customers.getAll();
            
            for (int i = 0; i < allCustomers.size(); i++) {
                Customer c = allCustomers.get(i);
                String line = c.getCustomerId() + "," + c.getName() + "," + c.getEmail();
                bw.write(line);
                bw.newLine();
            }
            bw.close();
            System.out.println("Customers saved.");
        } catch (IOException e) { System.out.println("Error saving customers: " + e.getMessage()); }
    }

    public void saveOrdersToCSV(String filename) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
            bw.write("OrderId,CustomerId,ProductIds,TotalPrice,OrderDate,Status");
            bw.newLine();
            
            CustomLinkedList<Order> allOrders = orders.getAll();
            
            for (int i = 0; i < allOrders.size(); i++) {
                Order o = allOrders.get(i);
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
            bw.close();
            System.out.println("Orders saved.");
        } catch (IOException e) { System.out.println("Error saving orders: " + e.getMessage()); }
    }

    public void saveReviewsToCSV(String filename) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
            bw.write("ReviewId,ProductId,CustomerId,Rating,Comment");
            bw.newLine();
            for (int i = 0; i < reviews.size(); i++) {
                Review r = reviews.get(i);
                String comment = "\"" + r.getComment() + "\"";
                String line = r.getReviewId() + "," + r.getProductId() + "," + 
                              r.getCustomerId() + "," + r.getRating() + "," + comment;
                bw.write(line);
                bw.newLine();
            }
            bw.close();
            System.out.println("Reviews saved.");
        } catch (IOException e) { System.out.println("Error saving reviews: " + e.getMessage()); }
    }
    
    // ================= LOGIC METHODS (BST UPDATED) ================= //
   
    public void addProduct(Product product) {
        // O(log n)
        products.insert(product.getProductId(), product);
        System.out.println("Product added: " + product.getName());
    }
    
    public boolean removeProduct(int productId) {
        // O(log n)
        boolean deleted = products.delete(productId);
        if (deleted) System.out.println("Product removed.");
        else System.out.println("Product not found.");
        return deleted;
    }
    
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
    
    public Product findProductById(int productId) {
        // O(log n)
        return products.find(productId);
    }
    
    public Product findProductByName(String name) {
        // Name search requires O(n) traversal since BST is keyed by ID
        CustomLinkedList<Product> all = products.getAll();
        for (int i = 0; i < all.size(); i++) {
            Product p = all.get(i);
            if (p.getName().equalsIgnoreCase(name)) {
                return p;
            }
        }
        return null;
    }
    
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
        // O(log n)
        customers.insert(customer.getCustomerId(), customer);
        System.out.println("Customer registered: " + customer.getName());
    }
    
    public Customer findCustomerById(int customerId) {
        // O(log n)
        return customers.find(customerId);
    }
    
    public boolean placeOrder(Order order) {
        Customer customer = findCustomerById(order.getCustomerId());
        if (customer != null) {
            orders.insert(order.getOrderId(), order);
            customer.addOrder(order);
            System.out.println("Order placed.");
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
        // O(log n)
        return orders.find(orderId);
    }
    
    public boolean addReview(int productId, int customerId, int rating, String comment) {
        Product product = findProductById(productId);
        if (product != null) {
            int reviewId = reviews.size() + 1; 
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
    
    public CustomLinkedList<Product> getTop3ProductsByRating() {
        // 1. Get all products from BST
        CustomLinkedList<Product> all = products.getAll();
        CustomLinkedList<Product> rated = new CustomLinkedList<>();
        
        for (int i = 0; i < all.size(); i++) {
            Product p = all.get(i);
            if (!p.getReviews().isEmpty()) {
                rated.add(p);
            }
        }
        
        // Simple Selection Sort for Rating
        CustomLinkedList<Product> sortedList = new CustomLinkedList<>();
        while (!rated.isEmpty()) {
            Product maxProduct = rated.get(0);
            int maxIndex = 0;
            for (int i = 1; i < rated.size(); i++) {
                if (rated.get(i).getAverageRating() > maxProduct.getAverageRating()) {
                    maxProduct = rated.get(i);
                    maxIndex = i;
                }
            }
            sortedList.add(maxProduct);
            rated.remove(maxIndex);
        }
        
        CustomLinkedList<Product> top3 = new CustomLinkedList<>();
        int count = Math.min(3, sortedList.size());
        for (int i = 0; i < count; i++) {
            top3.add(sortedList.get(i));
        }
        return top3;
    }
    
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
    
    public CustomLinkedList<Product> getCommonHighRatedProducts(int c1, int c2) {
        CustomLinkedList<Product> common = new CustomLinkedList<>();
        CustomLinkedList<Review> r1 = getReviewsByCustomer(c1);
        CustomLinkedList<Review> r2 = getReviewsByCustomer(c2);
        
        for (int i = 0; i < r1.size(); i++) {
            for (int j = 0; j < r2.size(); j++) {
                if (r1.get(i).getProductId() == r2.get(j).getProductId()) {
                    Product p = findProductById(r1.get(i).getProductId());
                    if (p != null && p.getAverageRating() > 4.0) {
                        // Check duplicates
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
    
    // NEW PHASE II: Range Query
    public CustomLinkedList<Product> getProductsInPriceRange(double min, double max) {
        CustomLinkedList<Product> result = new CustomLinkedList<>();
        CustomLinkedList<Product> all = products.getAll();
        for (int i = 0; i < all.size(); i++) {
            Product p = all.get(i);
            if (p.getPrice() >= min && p.getPrice() <= max) {
                result.add(p);
            }
        }
        return result;
    }
    
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
