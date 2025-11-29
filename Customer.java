package project;

public class Customer {
    private int customerId;
    private String name;
    private String email;
    private CustomLinkedList<Order> orders;
    
    public Customer(int cId, String n, String e) {
        customerId = cId;
        name = n;
        email = e;
        this.orders = new CustomLinkedList<>();
    }
    
   
    
    public CustomLinkedList<Order> getOrderHistory() {
        return orders;
    }
    
    public void addOrder(Order o) {
        orders.add(o);
    }
    
 
    
    public String getName() {
        return name;
    }
    
    public int getCustomerId() {
        return customerId;
    }
    
    public String getEmail() {
        return email;
    }
    
    public CustomLinkedList<Order> getOrders() {
        return orders;
    }
    
    public void setName(String n) {
        name = n;
    }
    
    public void setEmail(String em) {
        email = em;
    }
    
    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", totalOrders=" + orders.size() +
                '}';
    }
}