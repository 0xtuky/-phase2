package project;

import java.io.*;

public class ECommerceSystem {
    
    private CustomLinkedList<Product> products;
    private CustomLinkedList<Customer> customers;
    private CustomLinkedList<Order> orders;
    private CustomLinkedList<Review> reviews;
    
    public ECommerceSystem() {
        products = new CustomLinkedList<>();
        customers = new CustomLinkedList<>();
        orders = new CustomLinkedList<>();
        reviews = new CustomLinkedList<>();
    }
    
   
    public void loadProductsFromCSV(String filename) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String line = br.readLine(); 
            
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                int productId = Integer.parseInt(data[0]);
                String name = data[1];
                double price = Double.parseDouble(data[2]);
                int stock = Integer.parseInt(data[3]);
                
                Product product = new Product(productId, name, price, stock);
                products.add(product);
            }
            br.close();
            System.out.println("Products loaded.");
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
                customers.add(customer);
            }
            br.close();
            System.out.println("Customers loaded.");
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
                
                String[] productIds = productIdsStr.split(";");
                for (String pid : productIds) {
                    order.addProduct(Integer.parseInt(pid));
                }
                
                orders.add(order);
                
                Customer customer = findCustomerById(customerId);
                if (customer != null) {
                    customer.addOrder(order);
                }
            }
            br.close();
            System.out.println("Orders loaded.");
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
                reviews.add(review);
                
                Product product = findProductById(productId);
                if (product != null) {
                    product.addReview(review);
                }
            }
            br.close();
            System.out.println("Reviews loaded successfully!");
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
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        result.add(current.toString());
        
        String[] arr = new String[result.size()];
        for (int i = 0; i < result.size(); i++) {
            arr[i] = result.get(i);
        }
        return arr;
    }
    
   
    public void addProduct(Product product) {
        products.add(product);
        System.out.println("Product added: " + product.getName());
    }
    
    public boolean removeProduct(int productId) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getProductId() == productId) {
                products.remove(i);
                System.out.println("Product removed.");
                return true;
            }
        }
        System.out.println("Product not found.");
        return false;
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
        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);
            if (p.getProductId() == productId) {
                return p;
            }
        }
        return null;
    }
    
    public Product findProductByName(String name) {
    	int size = products.size();
        for (int i = 0; i < size ; i++) {
            Product p = products.get(i);
            if (p.getName().equalsIgnoreCase(name)) {
                return p;
            }
        }
        return null;
    }
    
    public CustomLinkedList<Product> getOutOfStockProducts() {
        CustomLinkedList<Product> outOfStock = new CustomLinkedList<>();
        int size = products.size();
        for (int i = 0; i < size; i++) {
            Product p = products.get(i);
            if (p.isOutOfStock()) {
                outOfStock.add(p);
            }
        }
        return outOfStock;
    }
   
    public void registerCustomer(Customer customer) {
        customers.add(customer);
        System.out.println("Customer registered: " + customer.getName());
    }
    
    public Customer findCustomerById(int customerId) {
        for (int i = 0; i < customers.size(); i++) {
            Customer c = customers.get(i);
            if (c.getCustomerId() == customerId) {
                return c;
            }
        }
        return null;
    }
    
   
    public boolean placeOrder(Order order) {
        Customer customer = findCustomerById(order.getCustomerId());
        if (customer != null) {
            orders.add(order);
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
            System.out.println("Order status update.!");
            return true;
        }
        System.out.println("Order not found.");
        return false;
    }
    
    public Order findOrderById(int orderId) {
        for (int i = 0; i < orders.size(); i++) {
            Order o = orders.get(i);
            if (o.getOrderId() == orderId) {
                return o;
            }
        }
        return null;
    }
    
  
    public boolean addReview(int productId, int customerId, int rating, String comment) {
        Product product = findProductById(productId);
        if (product != null) {
            int reviewId = reviews.size() + 1; // Simple way to generate a unique ID
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
        Review reviewToEdit = null;
        int size = reviews.size();
        for (int i = 0; i < size; i++) {
            if (reviews.get(i).getReviewId() == reviewId) {
                reviewToEdit = reviews.get(i);
                break;
            }
        }
        
        if (reviewToEdit != null) {
            reviewToEdit.setRating(newRating);
            reviewToEdit.setComment(newComment);
            System.out.println("Review " + reviewId + " updated.");
            return true;
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
        CustomLinkedList<Product> productsWithReviews = new CustomLinkedList<>();
        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);
            if (!p.getReviews().isEmpty()) {
                productsWithReviews.add(p);
            }
        }
        
        for (int i = 0; i < productsWithReviews.size() - 1; i++) {
            for (int j = 0; j < productsWithReviews.size() - i - 1; j++) {
                Product p1 = productsWithReviews.get(j);
                Product p2 = productsWithReviews.get(j + 1);
                if (p1.getAverageRating() < p2.getAverageRating()) {
                    Product temp = productsWithReviews.get(j);
                    productsWithReviews.remove(j);
                    productsWithReviews.add(productsWithReviews.get(j)); 
                    productsWithReviews.remove(j);
                    productsWithReviews.add(temp); 

                }
            }
        }


        CustomLinkedList<Product> sortedList = new CustomLinkedList<>();
        while (!productsWithReviews.isEmpty()) {
            Product maxProduct = productsWithReviews.get(0);
            int maxIndex = 0;
            for (int i = 1; i < productsWithReviews.size(); i++) {
                if (productsWithReviews.get(i).getAverageRating() > maxProduct.getAverageRating()) {
                    maxProduct = productsWithReviews.get(i);
                    maxIndex = i;
                }
            }
            sortedList.add(maxProduct);
            productsWithReviews.remove(maxIndex);
        }
        
        // Now 'sortedList' has the products from best to worst
        CustomLinkedList<Product> top3 = new CustomLinkedList<>();
        int count = Math.min(3, sortedList.size());
        for (int i = 0; i < count; i++) {
            top3.add(sortedList.get(i));
        }
        
        return top3;
    }
    
    
    public CustomLinkedList<Order> getOrdersBetweenDates(String startDate, String endDate) {
        CustomLinkedList<Order> result = new CustomLinkedList<>();
        int size = orders.size();
        for (int i = 0; i < size; i++) {
            Order o = orders.get(i);
            String orderDate = o.getOrderDate();
            if (orderDate.compareTo(startDate) >= 0 && orderDate.compareTo(endDate) <= 0) {
                result.add(o);
            }
        }
        return result;
    }
    
  
    public CustomLinkedList<Product> getCommonHighRatedProducts(int customerId1, int customerId2) {
        CustomLinkedList<Product> commonProducts = new CustomLinkedList<>();
        
        CustomLinkedList<Review> reviews1 = getReviewsByCustomer(customerId1);
        CustomLinkedList<Review> reviews2 = getReviewsByCustomer(customerId2);
        
        for (int i = 0; i < reviews1.size(); i++) {
            Review r1 = reviews1.get(i);
            for (int j = 0; j < reviews2.size(); j++) {
                Review r2 = reviews2.get(j);
                
                if (r1.getProductId() == r2.getProductId()) {
                    Product product = findProductById(r1.getProductId());
                    if (product != null && product.getAverageRating() > 4.0) {
                        boolean alreadyAdded = false;
                        for (int k = 0; k < commonProducts.size(); k++) {
                            if (commonProducts.get(k).getProductId() == product.getProductId()) {
                                alreadyAdded = true;
                                break;
                            }
                        }
                        if (!alreadyAdded) {
                            commonProducts.add(product);
                        }
                    }
                }
            }
        }
        
        return commonProducts;
    }
    
    
    public void displayAllProducts() {
        System.out.println("\n===== ALL PRODUCTS =====");
        for (int i = 0; i < products.size(); i++) {
            System.out.println(products.get(i));
        }
    }
    
    public void displayAllCustomers() {
        System.out.println("\n===== ALL CUSTOMERS =====");
        for (int i = 0; i < customers.size(); i++) {
            System.out.println(customers.get(i));
        }
    }
    
    public void displayAllOrders() {
        System.out.println("\n===== ALL ORDERS =====");
        for (int i = 0; i < orders.size(); i++) {
            System.out.println(orders.get(i));
        }
    }
    
    public CustomLinkedList<Product> getProducts() {
        return products;
    }
    
    public CustomLinkedList<Customer> getCustomers() {
        return customers;
    }
    
    public CustomLinkedList<Order> getOrders() {
        return orders;
    }
    
    public CustomLinkedList<Review> getReviews() {
        return reviews;
    }
}