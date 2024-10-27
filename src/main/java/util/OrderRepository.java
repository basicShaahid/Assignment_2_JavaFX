package util;

import model.Order;
import model.ShoppingCartItem;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository {

    public List<Order> getOrdersByUserId(int userId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM Orders WHERE userId = ? ORDER BY orderDate DESC";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int orderId = resultSet.getInt("orderId");
                LocalDateTime orderDate = resultSet.getTimestamp("orderDate").toLocalDateTime();
                double totalPrice = resultSet.getDouble("totalPrice");

                // Retrieve items for each order
                List<ShoppingCartItem> items = getOrderItems(orderId);
                Order order = new Order(orderId, orderDate, totalPrice, items);
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    private List<ShoppingCartItem> getOrderItems(int orderId) {
        List<ShoppingCartItem> items = new ArrayList<>();
        String sql = "SELECT * FROM OrderItems WHERE orderId = ?";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, orderId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                ShoppingCartItem item = new ShoppingCartItem(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getDouble("price"),
                        resultSet.getInt("quantity"),
                        resultSet.getInt("bookId"),
                        resultSet.getInt("userId")
                );
                items.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }
}
