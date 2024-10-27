package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Book;
import model.User;
import util.BookRepository;

import java.io.IOException;

public class AdminController {

    @FXML
    private TableView<Book> bookTableView;

    @FXML
    private TableColumn<Book, String> titleColumn;

    @FXML
    private TableColumn<Book, String> authorColumn;

    @FXML
    private TableColumn<Book, Double> priceColumn;

    @FXML
    private TableColumn<Book, Integer> copiesColumn;

    @FXML
    private TableColumn<Book, Integer> soldColumn;

    @FXML
    private TextField titleField;

    @FXML
    private TextField authorField;

    @FXML
    private TextField priceField;

    @FXML
    private TextField copiesField;

    @FXML
    private TextField soldField;

    private ObservableList<Book> bookInventoryList;

    private User currentUser; // Add this field to store the admin user information

    public void setUser(User user) {
        this.currentUser = user;
        System.out.println("Admin user set: " + user.getUsername());
    }

    @FXML
    private void initialize() {
        // Initialize the book inventory by fetching from the repository
        bookInventoryList = FXCollections.observableArrayList(BookRepository.getInstance().getAllBooks());

        // Set up the table columns
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        copiesColumn.setCellValueFactory(new PropertyValueFactory<>("physicalCopies"));
        soldColumn.setCellValueFactory(new PropertyValueFactory<>("soldCopies"));

        // Set the book inventory list to the TableView
        bookTableView.setItems(bookInventoryList);
    }

    @FXML
    private void handleAddBookButtonAction() {
        // Validate inputs
        if (titleField.getText().isEmpty() || authorField.getText().isEmpty() ||
                priceField.getText().isEmpty() || copiesField.getText().isEmpty() || soldField.getText().isEmpty()) {
            showAlert("Error", "All fields are required to add a book.");
            return;
        }

        try {
            // Collect book details from text fields
            String title = titleField.getText();
            String author = authorField.getText();
            double price = Double.parseDouble(priceField.getText());
            int copies = Integer.parseInt(copiesField.getText());
            int soldCopies = Integer.parseInt(soldField.getText());

            // Create a new Book object
            Book newBook = new Book(0, title, author, copies, price, soldCopies);

            // Add the book to the database and list
            BookRepository.getInstance().addBook(newBook);
            bookInventoryList.add(newBook);

            // Clear input fields after adding
            clearInputFields();

        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter valid numbers for Price, Copies, and Sold Copies.");
        }
    }


    @FXML
    private void updateBook() {
        // Get the selected book
        Book selectedBook = bookTableView.getSelectionModel().getSelectedItem();

        // Validate inputs and selection
        if (selectedBook == null) {
            showAlert("Warning", "No book selected. Please select a book to update.");
            return;
        }

        if (titleField.getText().isEmpty() || authorField.getText().isEmpty() ||
                priceField.getText().isEmpty() || copiesField.getText().isEmpty() || soldField.getText().isEmpty()) {
            showAlert("Error", "All fields are required to update a book.");
            return;
        }

        try {
            // Update book details based on text field values
            selectedBook.setTitle(titleField.getText());
            selectedBook.setAuthor(authorField.getText());
            selectedBook.setPrice(Double.parseDouble(priceField.getText()));
            selectedBook.setPhysicalCopies(Integer.parseInt(copiesField.getText()));
            selectedBook.setSoldCopies(Integer.parseInt(soldField.getText()));

            // Update the book in the database
            BookRepository.getInstance().updateBook(selectedBook);

            // Refresh the TableView to show updated values
            bookTableView.refresh();

            // Optionally, clear the fields after updating
            clearInputFields();

            // Notify user of successful update
            showAlert("Success", "Book details updated successfully.");

        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter valid numbers for Price, Copies, and Sold Copies.");
        }
    }


    @FXML
    private void deleteBook() {
        // Get the selected book from the TableView
        Book selectedBook = bookTableView.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            // Remove from the observable list
            bookInventoryList.remove(selectedBook);

            // Delete from the database by book ID
            BookRepository.getInstance().deleteBook(selectedBook.getId());
        } else {
            showAlert("Warning", "No book selected. Please select a book to delete.");
        }
    }

    private void clearInputFields() {
        titleField.clear();
        authorField.clear();
        priceField.clear();
        copiesField.clear();
        soldField.clear();
    }
    @FXML
    private void handleLogoutButtonAction(ActionEvent event) {
        try {
            // Load the Login FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
            Parent loginRoot = loader.load();

            // Get the current stage and set the login scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(loginRoot));
            stage.setTitle("The Reading Room - Login");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to load the login screen.");
        }
    }


    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
