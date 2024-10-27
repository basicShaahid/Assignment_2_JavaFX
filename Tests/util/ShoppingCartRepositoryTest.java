package util;

import model.ShoppingCartItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingCartRepositoryTest {

    private ShoppingCartRepository shoppingCartRepository;

    @BeforeEach
    void setUp() {
        shoppingCartRepository = new ShoppingCartRepository();
        // Clear any existing items for a test user (e.g., userId = 1) before each test
        shoppingCartRepository.clearCartForUser(1);
    }

    @Test
    void addItem() {
        // Arrange : this test has passesd last tested 10/27/2024
        ShoppingCartItem newItem = new ShoppingCartItem(0, "Sample Book", "Author", 29.99, 1, 101, 1); // Added 0 as a dummy id
        shoppingCartRepository.addItem(newItem);

        // Act
        List<ShoppingCartItem> items = shoppingCartRepository.getCartItemsByUserId(newItem.getUserId());

        // Assert
        assertTrue(items.stream().anyMatch(item -> item.getBookId() == newItem.getBookId()),
                "The item should be added to the cart.");
    }


    @Test
    void clearCartForUser() {
        // Arrange : this test has passesd last tested 10/27/2024
        ShoppingCartItem newItem = new ShoppingCartItem(0, "Clearable Book", "Author", 19.99, 1, 102, 1);
        shoppingCartRepository.addItem(newItem);

        // Act: Clear the cart for the user
        shoppingCartRepository.clearCartForUser(newItem.getUserId());

        // Assert
        List<ShoppingCartItem> items = shoppingCartRepository.getCartItemsByUserId(newItem.getUserId());
        assertTrue(items.isEmpty(), "The cart should be empty after clearing.");
    }


    @Test
    void getCartItemsByUserId() {
        // Arrange : this test has passesd last tested 10/27/2024
        ShoppingCartItem item1 = new ShoppingCartItem(0, "Book 1", "Author 1", 10.0, 1, 103, 1);
        ShoppingCartItem item2 = new ShoppingCartItem(1, "Book 2", "Author 2", 15.0, 1, 104, 1);
        shoppingCartRepository.addItem(item1);
        shoppingCartRepository.addItem(item2);

        // Act
        List<ShoppingCartItem> items = shoppingCartRepository.getCartItemsByUserId(1);

        // Assert
        assertEquals(2, items.size(), "The cart should contain 2 items for the user.");
    }


    @Test
    void checkout() {
        // Arrange : this test has passesd last tested 10/27/2024
        ShoppingCartItem item1 = new ShoppingCartItem(0,"Book 1", "Author 1", 12.99, 1, 106, 1);
        ShoppingCartItem item2 = new ShoppingCartItem(0,"Book 2", "Author 2", 8.99, 1, 107, 1);
        shoppingCartRepository.addItem(item1);
        shoppingCartRepository.addItem(item2);

        List<ShoppingCartItem> cartItems = shoppingCartRepository.getCartItemsByUserId(1);

        // Act: Perform checkout
        boolean checkoutSuccessful = shoppingCartRepository.checkout(1, cartItems);

        // Assert: Ensure checkout clears the cart for the user
        List<ShoppingCartItem> itemsAfterCheckout = shoppingCartRepository.getCartItemsByUserId(1);
        assertTrue(checkoutSuccessful, "Checkout should be successful.");
        assertTrue(itemsAfterCheckout.isEmpty(), "Cart should be empty after checkout.");
    }
}
