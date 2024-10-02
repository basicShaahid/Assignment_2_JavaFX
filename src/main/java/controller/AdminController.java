package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Book;

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

    @FXML
    private void initialize() {
        // Initialize the book inventory with some sample data
        bookInventoryList = FXCollections.observableArrayList(
                new Book("Absolute Java", "Savitch", 10, 50, 142),
                new Book("JAVA: How to Program", "Deitel and Deitel", 100, 70, 475),
                new Book("Computing Concepts with JAVA 8 Essentials", "Horstman", 500, 89, 60),
                new Book("Java Software Solutions", "Lewis and Loftus", 500, 99, 12)
        );

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
    private void addBook() {
        // Collect book details from text fields
        String title = titleField.getText();
        String author = authorField.getText();
        double price = Double.parseDouble(priceField.getText());
        int copies = Integer.parseInt(copiesField.getText());
        int soldCopies = Integer.parseInt(soldField.getText());

        // Create a new Book object and add it to the inventory list
        Book newBook = new Book(title, author, copies, price, soldCopies);
        bookInventoryList.add(newBook);
    }

    @FXML
    private void updateBook() {
        // Get the selected book and update its details based on text field values
        Book selectedBook = bookTableView.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            selectedBook.setTitle(titleField.getText());
            selectedBook.setAuthor(authorField.getText());
            selectedBook.setPrice(Double.parseDouble(priceField.getText()));
            selectedBook.setPhysicalCopies(Integer.parseInt(copiesField.getText()));
            selectedBook.setSoldCopies(Integer.parseInt(soldField.getText()));

            // Refresh the TableView to show updated values
            bookTableView.refresh();
        }
    }

    @FXML
    private void deleteBook() {
        // Remove the selected book from the inventory list
        Book selectedBook = bookTableView.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            bookInventoryList.remove(selectedBook);
        }
    }
}
