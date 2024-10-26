package util;

import model.ShoppingCartItem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCartRepository {

    // Add an item to the shopping cart
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
                int currentQuantity = resultSet.getInt("quantity");
                try (PreparedStatement updateStatement = conn.prepareStatement(updateSql)) {
                    updateStatement.setInt(1, cartItem.getQuantity());
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
                    insertStatement.setInt(4, cartItem.getQuantity());
                    insertStatement.setInt(5, cartItem.getBookId());
                    insertStatement.setInt(6, cartItem.getUserId());
                    insertStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


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




    // Get all cart items for a specific user
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

    public void updateCartItemQuantity(ShoppingCartItem cartItem) {
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


    // Checkout method to process the order for the user
    public boolean checkout(int userId, List<ShoppingCartItem> cartItems) {
        String deleteSql = "DELETE FROM ShoppingCart WHERE userId = ?";
        try (Connection connection = DatabaseHelper.getConnection()) {
            connection.setAutoCommit(false);  // Begin transaction

            // Simulate order processing (e.g., save order to Orders table)
            for (ShoppingCartItem item : cartItems) {
                // This is where you'd implement saving to Orders table if required
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
            try (Connection connection = DatabaseHelper.getConnection()) {
                connection.rollback();  // Roll back transaction in case of error
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }
            return false;
        } finally {
            try (Connection connection = DatabaseHelper.getConnection()) {
                connection.setAutoCommit(true);  // Reset auto-commit
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
