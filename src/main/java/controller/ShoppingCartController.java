package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.ShoppingCartItem;
import model.User;
import util.DatabaseHelper;
import util.ShoppingCartRepository;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ShoppingCartController {

    @FXML
    private TableView<ShoppingCartItem> shoppingCartTableView;

    @FXML
    private TableColumn<ShoppingCartItem, String> titleColumn;

    @FXML
    private TableColumn<ShoppingCartItem, String> authorColumn;

    @FXML
    private TableColumn<ShoppingCartItem, Double> priceColumn;

    @FXML
    private TableColumn<ShoppingCartItem, Integer> quantityColumn;

    @FXML
    private TableView<ShoppingCartItem> cartTableView;


    @FXML
    private TextField quantityTextField;

    @FXML
    private TextField totalAmountField;

    @FXML
    private Button checkoutButton;

    private User currentUser;
    private ShoppingCartRepository shoppingCartRepository;
    private ObservableList<ShoppingCartItem> cartItems;

    public void setCurrentUser(User user) {
        this.currentUser = user;
        System.out.println("Current user set in ShoppingCartController: " + currentUser.getFirstName());
        initializeCart();  // Call initializeCart() instead of loadCartItems() directly
    }

    public void setShoppingCartRepository(ShoppingCartRepository shoppingCartRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
        System.out.println("ShoppingCartRepository set in ShoppingCartController.");
        initializeCart();  // Call initializeCart() instead of loadCartItems() directly
    }

    private void initializeCart() {
        // Only load cart items if both currentUser and shoppingCartRepository are set
        if (shoppingCartRepository != null && currentUser != null) {
            System.out.println("Both currentUser and shoppingCartRepository are set. Loading cart items.");
            loadCartItems();
        } else {
            System.out.println("Cannot initialize cart because one or both dependencies are null.");
        }
    }

    // Initialize method called when FXML is loaded
    @FXML
    public void initialize() {
        setupTable();
    }

    // Set up table columns
    private void setupTable() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    }


    private void loadCartItems() {
        try {
            List<ShoppingCartItem> items = shoppingCartRepository.getCartItemsByUserId(currentUser.getId());
            if (items == null) {
                System.out.println("Error: Retrieved null list from getCartItemsByUserId.");
                return;
            }

            cartItems = FXCollections.observableArrayList(items);
            cartTableView.setItems(cartItems);
            System.out.println("Cart items loaded successfully. Number of items: " + cartItems.size());

            updateTotalAmount();  // Call to calculate the total price of items in cart

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception occurred in loadCartItems: " + e.getMessage());
        }
    }

    private void updateTotalAmount() {
        try {
            double totalAmount = cartItems.stream()
                    .mapToDouble(ShoppingCartItem::getTotalPrice)
                    .sum();
            totalAmountField.setText(String.format("%.2f", totalAmount));
            System.out.println("Total amount updated to: " + totalAmount);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception occurred in updateTotalAmount: " + e.getMessage());
        }
    }


    // Event handler for the "Add to Cart" button
    @FXML
    private void handleAddToCartButtonAction(ActionEvent event) {
        showAlert("Information", "Add to Cart functionality is not implemented in this view.");
    }

    // Event handler for the "Update Quantity" button
    @FXML
    private void handleUpdateQuantityButtonAction(ActionEvent event) {
        // Get the selected item from the cart
        ShoppingCartItem selectedItem = cartTableView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("Warning", "Please select an item to update.");
            return;
        }

        try {
            // Parse the new quantity from the text field
            int newQuantity = Integer.parseInt(quantityTextField.getText());

            // Ensure the quantity is positive
            if (newQuantity <= 0) {
                showAlert("Error", "Quantity must be a positive number.");
                return;
            }

            // Update the quantity in the selected item and in the database
            selectedItem.setQuantity(newQuantity);
            shoppingCartRepository.updateCartItemQuantity(selectedItem);

            // Refresh the cart view to show the updated quantity and total
            loadCartItems();
            updateTotalAmount();

            // Clear the quantity text field after updating
            quantityTextField.clear();

            showAlert("Success", "Quantity updated successfully.");

        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid number for quantity.");
        }
    }


    // Event handler for the "Remove Item" button
    @FXML
    private void handleRemoveItemButtonAction(ActionEvent event) {
        // Get the selected item from the cart
        ShoppingCartItem selectedItem = cartTableView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("Warning", "Please select an item to remove.");
            return;
        }

        // Confirm removal (Optional)
        boolean confirm = showConfirmation("Remove Item", "Are you sure you want to remove this item from the cart?");
        if (!confirm) return;

        // Remove the item from the database
        shoppingCartRepository.removeItem(selectedItem.getId());

        // Remove the item from the TableView and update the total
        cartItems.remove(selectedItem);
        updateTotalAmount();

        showAlert("Success", "Item removed from cart.");
    }


    // Event handler for the "Checkout" button
    @FXML
    private void handleCheckoutButtonAction(ActionEvent event) {
        if (cartItems.isEmpty()) {
            showAlert("Checkout Error", "Your cart is empty. Add items before checking out.");
            return;
        }

        // Display a summary in a confirmation dialog
        StringBuilder receipt = new StringBuilder("Receipt:\n");
        double totalAmount = 0;

        for (ShoppingCartItem item : cartItems) {
            receipt.append(item.getTitle())
                    .append(" x ")
                    .append(item.getQuantity())
                    .append(" - $")
                    .append(item.getTotalPrice())
                    .append("\n");
            totalAmount += item.getTotalPrice();
        }
        receipt.append("\nTotal Amount: $").append(totalAmount);

        // Show confirmation dialog
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Checkout");
        confirmationAlert.setHeaderText("Proceed with Checkout?");
        confirmationAlert.setContentText(receipt.toString());

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Prompt for payment details
            if (collectPaymentDetails()) {
                // Process checkout
                boolean isCheckoutSuccessful = shoppingCartRepository.checkout(currentUser.getId(), cartItems);

                if (isCheckoutSuccessful) {
                    showAlert("Checkout Complete", "Thank you! Your order has been placed.");
                    cartItems.clear();  // Clear the local cart items
                    updateTotalAmount();  // Update the total amount to reflect empty cart
                    cartTableView.setItems(FXCollections.observableArrayList(cartItems));  // Refresh the table
                } else {
                    showAlert("Checkout Error", "There was an issue processing your checkout. Please try again.");
                }
            }
        }
    }


    @FXML
    private void handleBackToDashboardAction(ActionEvent event) {
        try {
            // Load the Dashboard FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Dashboard.fxml"));
            Parent dashboardRoot = loader.load();

            // Set the user in the DashboardController
            DashboardController dashboardController = loader.getController();
            dashboardController.setUser(currentUser);
            dashboardController.setShoppingCartRepository(shoppingCartRepository);

            // Get the current stage and switch scenes
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(dashboardRoot));
            stage.setTitle("The Reading Room - Dashboard");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to load the dashboard.");
        }
    }

    private boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        return alert.showAndWait().orElse(null) == ButtonType.OK;
    }

    private boolean collectPaymentDetails() {
        TextInputDialog cardDialog = new TextInputDialog();
        cardDialog.setTitle("Payment Information");
        cardDialog.setHeaderText("Enter Credit Card Information");

        // Card Number
        TextField cardNumberField = new TextField();
        cardNumberField.setPromptText("Card Number (16 digits)");

        // Expiry Date
        TextField expiryDateField = new TextField();
        expiryDateField.setPromptText("Expiry Date (MM/YY)");

        // CVV
        TextField cvvField = new TextField();
        cvvField.setPromptText("CVV (3 digits)");

        VBox dialogContent = new VBox();
        dialogContent.getChildren().addAll(
                new Label("Card Number:"), cardNumberField,
                new Label("Expiry Date:"), expiryDateField,
                new Label("CVV:"), cvvField
        );
        cardDialog.getDialogPane().setContent(dialogContent);

        Optional<String> result = cardDialog.showAndWait();
        if (result.isPresent()) {
            String cardNumber = cardNumberField.getText();
            String expiryDate = expiryDateField.getText();
            String cvv = cvvField.getText();

            // Validate Card Information
            if (validateCardDetails(cardNumber, expiryDate, cvv)) {
                return true;
            } else {
                showAlert("Payment Error", "Invalid payment details. Please check and try again.");
                return false;
            }
        }
        return false;
    }

    private boolean validateCardDetails(String cardNumber, String expiryDate, String cvv) {
        // Validate card number (16 digits)
        if (!cardNumber.matches("\\d{16}")) {
            return false;
        }
        // Validate expiry date (MM/YY format)
        if (!expiryDate.matches("(0[1-9]|1[0-2])/\\d{2}")) {
            return false;
        }
        // Validate CVV (3 digits)
        if (!cvv.matches("\\d{3}")) {
            return false;
        }
        return true;
    }


    // Show an alert message
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
