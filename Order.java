package project;

public class Order {
    private int orderId;
    private int customerId;
    private CustomLinkedList<Integer> productIds; 
    private double totalPrice;
    private String orderDate;
    private String status; 
    
    public Order(int oId, int cId, double totalPrice, String oDate, String status) {
        orderId = oId;
        customerId = cId;
        this.productIds = new CustomLinkedList<>();
        this.totalPrice = totalPrice;
        orderDate = oDate;
        this.status = status;
    }
    
   
    
    public void updateStatus(String nStatus) {
        this.status = nStatus;
    }
    
   
    
    public void addProduct(int pId) {
        productIds.add(pId);
    }
    
    
    public void cancelOrder() {
        this.status = "Cancelled";
    }
    
    public int getOrderId() {
        return orderId;
    }
    
    public int getCustomerId() {
        return customerId;
    }
    
    public CustomLinkedList<Integer> getProductIds() {
        return productIds;
    }
    
    public double getTotalPrice() {
        return totalPrice;
    }
    
    public String getOrderDate() {
        return orderDate;
    }
    
    public String getStatus() {
        return status;
    }
    
   
    
    public void setOrderDate(String oDate) {
        this.orderDate = oDate;
    }
    
    public void setTotalPrice(double tPrice) {
        this.totalPrice = tPrice;
    }
    
    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", customerId=" + customerId +
                ", totalPrice=" + totalPrice +
                ", orderDate='" + orderDate + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}