package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Book;
import util.BookRepository;

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
    private void addBook() {
        // Collect book details from text fields
        String title = titleField.getText();
        String author = authorField.getText();
        double price = Double.parseDouble(priceField.getText());
        int copies = Integer.parseInt(copiesField.getText());
        int soldCopies = Integer.parseInt(soldField.getText());

        // Create a new Book object and add it to the inventory list
        Book newBook = new Book(0, title, author, copies, price, soldCopies); // ID is 0 as it will be auto-generated
        bookInventoryList.add(newBook);

        // Add the book to the database
        BookRepository.getInstance().addBook(newBook);
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

            // Update the book in the database
            BookRepository.getInstance().updateBook(selectedBook);

            // Refresh the TableView to show updated values
            bookTableView.refresh();
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
        }
    }
}
