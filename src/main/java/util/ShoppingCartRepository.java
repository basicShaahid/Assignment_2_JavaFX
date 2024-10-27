package util;

import model.ShoppingCartItem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCartRepository {

    // Verify available stock for a book before adding to the cart
    public boolean verifyStock(int bookId, int requestedQuantity) {
        String stockCheckSql = "SELECT physicalCopies FROM Books WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stockStatement = conn.prepareStatement(stockCheckSql)) {

            stockStatement.setInt(1, bookId);
            ResultSet resultSet = stockStatement.executeQuery();

            if (resultSet.next()) {
                int availableCopies = resultSet.getInt("physicalCopies");
                return availableCopies >= requestedQuantity;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Add an item to the shopping cart with stock verification
    public void addItem(ShoppingCartItem cartItem) {
        String selectSql = "SELECT quantity FROM ShoppingCart WHERE userId = ? AND bookId = ?";
        String insertSql = "INSERT INTO ShoppingCart (title, author, price, quantity, bookId, userId) VALUES (?, ?, ?, ?, ?, ?)";
        String updateSql = "UPDATE ShoppingCart SET quantity = quantity + ? WHERE userId = ? AND bookId = ?";

        try (Connection conn = DatabaseHelper.getConnection()) {
            PreparedStatement selectStatement = conn.prepareStatement(selectSql);
            selectStatement.setInt(1, cartItem.getUserId());
            selectStatement.setInt(2, cartItem.getBookId());
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                // Update existing item
                try (PreparedStatement updateStatement = conn.prepareStatement(updateSql)) {
                    updateStatement.setInt(1, cartItem.getQuantity());  // Add only the quantity requested
                    updateStatement.setInt(2, cartItem.getUserId());
                    updateStatement.setInt(3, cartItem.getBookId());
                    updateStatement.executeUpdate();
                }
            } else {
                // Insert new item
                try (PreparedStatement insertStatement = conn.prepareStatement(insertSql)) {
                    insertStatement.setString(1, cartItem.getTitle());
                    insertStatement.setString(2, cartItem.getAuthor());
                    insertStatement.setDouble(3, cartItem.getPrice());
                    insertStatement.setInt(4, cartItem.getQuantity());  // Add only the quantity requested
                    insertStatement.setInt(5, cartItem.getBookId());
                    insertStatement.setInt(6, cartItem.getUserId());
                    insertStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Clears cart for user
    public void clearCartForUser(int userId) {
        String sql = "DELETE FROM ShoppingCart WHERE userId = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Retrieves cart items by user ID
    public List<ShoppingCartItem> getCartItemsByUserId(int userId) {
        List<ShoppingCartItem> items = new ArrayList<>();
        String sql = "SELECT * FROM ShoppingCart WHERE userId = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, userId);
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

    // Updates item quantity in cart
    public void updateCartItemQuantity(ShoppingCartItem cartItem) {
        if (!verifyStock(cartItem.getBookId(), cartItem.getQuantity())) {
            System.out.println("Insufficient stock for book ID: " + cartItem.getBookId());
            return;
        }

        String sql = "UPDATE ShoppingCart SET quantity = ? WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, cartItem.getQuantity());
            statement.setInt(2, cartItem.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Removes an item from the cart
    public void removeItem(int itemId) {
        String sql = "DELETE FROM ShoppingCart WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, itemId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Checkout method to process the order
    public boolean checkout(int userId, List<ShoppingCartItem> cartItems) {
        String deleteSql = "DELETE FROM ShoppingCart WHERE userId = ?";
        Connection connection = null;
        try {
            connection = DatabaseHelper.getConnection();
            connection.setAutoCommit(false);  // Begin transaction

            // Simulate order processing (e.g., save order to Orders table)
            for (ShoppingCartItem item : cartItems) {
                // Record each item in Orders or another relevant table if necessary
            }

            // Clear items from the shopping cart after checkout
            try (PreparedStatement statement = connection.prepareStatement(deleteSql)) {
                statement.setInt(1, userId);
                statement.executeUpdate();
            }

            connection.commit();  // Commit transaction
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            if (connection != null) {
                try {
                    connection.rollback();  // Roll back transaction in case of error
                } catch (SQLException rollbackException) {
                    rollbackException.printStackTrace();
                }
            }
            return false;

        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);  // Reset auto-commit
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }



}
