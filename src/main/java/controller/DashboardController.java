package controller;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

public class DashboardController {

    @FXML
    private ListView<String> bookListView; // List view to display books

    @FXML
    private TextArea shoppingCartArea; // Text area to display items in the shopping cart

    // Method to initialize the dashboard with data (called automatically when the FXML is loaded)
    @FXML
    private void initialize() {
        // For demonstration, adding sample books to the ListView (replace this with actual data)
        bookListView.getItems().addAll("Book 1 - Title", "Book 2 - Title", "Book 3 - Title");
    }

    // Method to handle adding a book to the shopping cart
    @FXML
    private void addToCart() {
        String selectedBook = bookListView.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            shoppingCartArea.appendText(selectedBook + "\n");
        }
    }

    // Method to handle checkout process
    @FXML
    private void checkout() {
        String cartContents = shoppingCartArea.getText();
        if (!cartContents.isEmpty()) {
            // Perform checkout actions (e.g., update database, clear cart)
            shoppingCartArea.clear();
            System.out.println("Checkout completed!");
        } else {
            System.out.println("Shopping cart is empty.");
        }
    }
}
