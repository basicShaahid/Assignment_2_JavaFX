package model;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private int orderId;
    private LocalDateTime orderDate;
    private double totalPrice;
    private List<ShoppingCartItem> items;

    public Order(int orderId, LocalDateTime orderDate, double totalPrice, List<ShoppingCartItem> items) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.items = items;
    }

    // Getters and Setters
    public int getOrderId() { return orderId; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public double getTotalPrice() { return totalPrice; }
    public List<ShoppingCartItem> getItems() { return items; }
}
