package controller;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class AdminController {

    @FXML
    private ListView<String> bookInventoryListView; // List view to display book inventory

    @FXML
    private TextField stockUpdateField; // Text field to enter new stock value

    // Method to initialize the admin dashboard with data (called automatically when FXML is loaded)
    @FXML
    private void initialize() {
        // For demonstration, adding sample books to the ListView (replace this with actual data)
        bookInventoryListView.getItems().addAll("Book 1 - 5 copies", "Book 2 - 3 copies", "Book 3 - 10 copies");
    }

    // Method to handle updating the stock of a selected book
    @FXML
    private void updateStock() {
        String selectedBook = bookInventoryListView.getSelectionModel().getSelectedItem();
        String newStockValue = stockUpdateField.getText();

        if (selectedBook != null && !newStockValue.isEmpty()) {
            try {
                int newStock = Integer.parseInt(newStockValue);
                // For demonstration, printing the new stock (replace with actual update logic)
                System.out.println("Updated " + selectedBook + " to " + newStock + " copies.");
                stockUpdateField.clear(); // Clear the text field after updating
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number for stock.");
            }
        } else {
            System.out.println("Please select a book and enter a valid stock value.");
        }
    }
}
