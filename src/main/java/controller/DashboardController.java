package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import model.Book;

public class DashboardController {

    @FXML
    private ListView<Book> bookListView; // ListView to display books

    @FXML
    private TextArea shoppingCartArea; // TextArea to display items in the shopping cart

    // Observable list to hold book data
    private ObservableList<Book> bookList;

    // Observable list to hold items in the shopping cart
    private ObservableList<Book> cartList;

    @FXML
    private void initialize() {
        // Initialize book list with some sample books
        bookList = FXCollections.observableArrayList(
                new Book("Absolute Java", "Savitch", 10, 50, 142),
                new Book("JAVA: How to Program", "Deitel and Deitel", 100, 70, 475),
                new Book("Computing Concepts with JAVA 8 Essentials", "Horstman", 500, 89, 60),
                new Book("Java Software Solutions", "Lewis and Loftus", 500, 99, 12)
        );

        // Set the bookListView with the bookList
        bookListView.setItems(bookList);

        // Initialize shopping cart as an empty observable list
        cartList = FXCollections.observableArrayList();

        // Use a custom cell factory to display the book title and author in the ListView
        bookListView.setCellFactory(param -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Book book, boolean empty) {
                super.updateItem(book, empty);
                setText(empty || book == null ? "" : book.getTitle() + " by " + book.getAuthor() +
                        " | Price: $" + book.getPrice() + " | Copies Available: " + book.getPhysicalCopies());
            }
        });
    }

    @FXML
    private void addToCart() {
        Book selectedBook = bookListView.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            // Add the selected book to the shopping cart
            cartList.add(selectedBook);
            shoppingCartArea.appendText(selectedBook.getTitle() + " - $" + selectedBook.getPrice() + "\n");
        }
    }

    @FXML
    private void checkout() {
        double totalCost = 0;
        for (Book book : cartList) {
            // Calculate total cost
            totalCost += book.getPrice();

            // Update the number of physical copies left
            book.setPhysicalCopies(book.getPhysicalCopies() - 1);
        }

        // Display total cost in the console (or in the UI)
        System.out.println("Total cost: $" + totalCost);

        // Clear shopping cart after checkout
        shoppingCartArea.clear();
        cartList.clear();
        System.out.println("Checkout completed!");
    }
}
