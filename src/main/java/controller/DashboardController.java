package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Book;
import util.BookRepository; // Ensure this points to the correct package

public class DashboardController {

    @FXML
    private TableView<Book> bookTableView;

    @FXML
    private Button logoutButton;

    private BookRepository bookRepository;

    @FXML
    public void initialize() {
        bookRepository = BookRepository.getInstance();
        loadBooks();
    }

    private void loadBooks() {
        TableColumn<Book, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Book, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

        TableColumn<Book, Integer> copiesColumn = new TableColumn<>("Copies");
        copiesColumn.setCellValueFactory(new PropertyValueFactory<>("physicalCopies"));

        TableColumn<Book, Double> priceColumn = new TableColumn<>("Price (AUD)");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        bookTableView.getColumns().addAll(titleColumn, authorColumn, copiesColumn, priceColumn);

        bookTableView.getItems().setAll(bookRepository.getAllBooks());
    }

    @FXML
    private void handleLogoutButtonAction() {
        System.out.println("Logout button clicked");
        // Add logout functionality here
    }
}
