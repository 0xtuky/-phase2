package project;

public class Review {
    private int reviewId;
    private int productId;
    private int customerId;
    private int rating; 
    private String comment;
    
    public Review(int rId, int pId, int cId, int r, String comment) {
        reviewId = rId;
        productId = pId;
        customerId = cId;
        rating = r;
        this.comment = comment;
    }
    
    
    
    public int getProductId() {
        return productId;
    }
    
    public int getReviewId() {
        return reviewId;
    }
    
    public int getCustomerId() {
        return customerId;
    }
    
    public int getRating() {
        return rating;
    }
    
    public String getComment() {
        return comment;
    }
    
    public void setRating(int r) {
        if (r >= 1 && r <= 5) {
            rating = r;
        }
    }
    
    public void setComment(String c) {
        comment = c;
    }
    
    @Override
    public String toString() {
        return "Review{" +
                "reviewId=" + reviewId +
                ", productId=" + productId +
                ", customerId=" + customerId +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                '}';
    }
}