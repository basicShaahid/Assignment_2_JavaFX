package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;
import util.ShoppingCartRepository;
import util.UserRepository;

import java.io.IOException;
import java.util.Optional;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    private User authenticateUser(String username, String password) {
        Optional<User> userOptional = UserRepository.getInstance().validateCredentials(username, password);
        return userOptional.orElse(null); // Return the user if found, otherwise null
    }

    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        User loggedInUser = authenticateUser(username, password);

        if (loggedInUser != null) {
            loadDashboard(loggedInUser, event);
        } else {
            showAlert("Error", "Invalid username or password.");
        }
    }

    private void loadDashboard(User user, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Dashboard.fxml"));
            Parent dashboardRoot = loader.load();

            // Initialize the DashboardController with the logged-in user and shopping cart repository
            DashboardController dashboardController = loader.getController();
            dashboardController.setUser(user);
            dashboardController.setShoppingCartRepository(new ShoppingCartRepository());

            // Switch to the dashboard scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(dashboardRoot));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the dashboard.");
        }
    }

    @FXML
    private void navigateToRegistrationScreen(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Registration.fxml"));
            Parent registrationRoot = loader.load();

            // Set the new scene for registration
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(registrationRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the registration screen.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
