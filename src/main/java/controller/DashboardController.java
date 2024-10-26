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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Book;
import model.User;
import model.ShoppingCartItem;
import util.BookRepository;
import util.DatabaseHelper;
import util.UserRepository;
import util.ShoppingCartRepository;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DashboardController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private TableView<Book> topBooksTableView;

    @FXML
    private TableColumn<Book, String> titleColumn;

    @FXML
    private TableColumn<Book, String> authorColumn;

    @FXML
    private TableColumn<Book, Integer> soldCopiesColumn;

    private User currentUser;

    //Do not touch below 2 varibale declarations
    private UserRepository userRepository;
    private BookRepository bookRepository;
    private ShoppingCartRepository shoppingCartRepository;

    public DashboardController() {
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setBookRepository(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void setUser(User user) {
        this.currentUser = user;
        if (currentUser != null) {
            System.out.println("Current user set: " + currentUser.getFirstName());
            if (welcomeLabel != null) {
                welcomeLabel.setText("Welcome, " + currentUser.getFirstName() + "!");
            }
        } else {
            System.out.println("Failed to set current user.");
        }
    }

    @FXML
    public void initialize() {
        setupTable();
        loadTopBooks();
    }

    public void setShoppingCartRepository(ShoppingCartRepository shoppingCartRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
    }

    private void setupTable() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        soldCopiesColumn.setCellValueFactory(new PropertyValueFactory<>("soldCopies"));
    }

    //currently inactive, serves purpose to display top 5 books along with other books
    private List<Book> getTop5Books() {
        List<Book> topBooks = new ArrayList<>();
        String sql = "SELECT * FROM Books ORDER BY soldCopies DESC LIMIT 5";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Book book = new Book(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getInt("physicalCopies"),
                        resultSet.getDouble("price"),
                        resultSet.getInt("soldCopies")
                );
                topBooks.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error retrieving top books from the database.");
        }

        return topBooks;
    }

    private void loadTopBooks() {
        List<Book> books = BookRepository.getInstance().getTop5Books();
        ObservableList<Book> topBooks = FXCollections.observableArrayList(books);
        topBooksTableView.setItems(topBooks);
    }

    @FXML
    private void handleViewCartButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ShoppingCart.fxml"));
            Parent cartRoot = loader.load();

            ShoppingCartController shoppingCartController = loader.getController();
            shoppingCartController.setCurrentUser(currentUser);
            shoppingCartController.setShoppingCartRepository(shoppingCartRepository);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(cartRoot));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to load the shopping cart.");
        }
    }

    @FXML
    private void handleAddToCartButtonAction(ActionEvent event) {
        if (currentUser == null) {
            showAlert("Error", "User not set. Please log in again.");
            return;
        }

        Book selectedBook = topBooksTableView.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            int requestedQuantity = 1;  // Set default quantity to 1

            // Verify stock availability
            boolean hasStock = shoppingCartRepository.verifyStock(selectedBook.getId(), requestedQuantity);
            if (!hasStock) {
                showAlert("Warning", "Insufficient stock available.");
                return;
            }

            // Create a new ShoppingCartItem with the default quantity of 1
            ShoppingCartItem cartItem = new ShoppingCartItem(
                    0, // Placeholder for cart item ID,
                    selectedBook.getTitle(),
                    selectedBook.getAuthor(),
                    selectedBook.getPrice(),
                    requestedQuantity, // Default quantity set to 1
                    selectedBook.getId(),
                    currentUser.getId()  // Use the actual user ID
            );

            shoppingCartRepository.addItem(cartItem);
            showAlert("Success", "Book added to cart with quantity 1.");
        } else {
            showAlert("Warning", "No book selected. Please select a book to add to the cart.");
        }
    }


    @FXML
    public void handleEditProfileButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/EditProfile.fxml"));
            Parent editProfileRoot = loader.load();

            EditProfileController editProfileController = loader.getController();
            editProfileController.setCurrentUser(currentUser);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(editProfileRoot));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to load the Edit Profile screen. Check file path and controller setup.");
        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Unexpected Error", "An error occurred: " + ex.getMessage());
        }
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
