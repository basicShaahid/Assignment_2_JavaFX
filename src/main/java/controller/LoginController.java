package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;
import util.UserRepository;

import java.io.IOException;
import java.util.Optional;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorMessageLabel;

    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Validate credentials using the UserRepository
        Optional<User> userOptional = UserRepository.getInstance().validateCredentials(username, password);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Check if the user is an Admin (you can adjust this based on your role management)
            if (user.getUsername().equals("admin")) {
                navigateTo("AdminDashboard.fxml", event); // Navigate to Admin Dashboard
            } else {
                navigateTo("Dashboard.fxml", event); // Navigate to Customer Dashboard
            }
        } else {
            // Show error message if login failed
            errorMessageLabel.setText("Invalid username or password. Please try again.");
        }
    }

    // Method to navigate to the specified FXML file
    private void navigateTo(String fxmlFile, ActionEvent event) {
        try {
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/view/" + fxmlFile));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to navigate to registration screen if the user does not have an account
    @FXML
    private void navigateToRegistrationScreen(ActionEvent event) {
        try {
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/view/Registration.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
