package project;

public class Product {
    private int productId;
    private String name;
    private double price;
    private int stock;
    private CustomLinkedList<Review> reviews;
    
    public Product(int productId, String name, double price, int stock) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.reviews = new CustomLinkedList<>();
    }
    
    public void addReview(Review review) {
        reviews.add(review);
    }
    
    public double getAverageRating() {
        if (reviews.isEmpty()) {
            return 0.0;
        }
        
        double sum = 0;
        for (int i = 0; i < reviews.size(); i++) {
            Review r = reviews.get(i);
            sum += r.getRating();
        }
        return sum / reviews.size();
    }
    
    public boolean isOutOfStock() {
        return stock == 0;
    }
    
    public void updateStock(int newStock) {
        this.stock = newStock;
    }
    
    public boolean reduceStock(int quantity) {
        if (stock >= quantity) {
            stock -= quantity;
            return true;
        }
        return false;
    }
    
    public int getProductId() {
        return productId;
    }
    
    public String getName() {
        return name;
    }
    
    public double getPrice() {
        return price;
    }
    
    public int getStock() {
        return stock;
    }
    
    public CustomLinkedList<Review> getReviews() {
        return reviews;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public void setStock(int stock) {
        this.stock = stock;
    }
    
    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", avgRating=" + String.format("%.2f", getAverageRating()) +
                '}';
    }
}
