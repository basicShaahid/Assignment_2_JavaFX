package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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
    private Label errorLabel;

    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Validate credentials
        Optional<User> userOptional = UserRepository.getInstance().validateCredentials(username, password);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Navigate based on the user's role
            if (user.getRole().equalsIgnoreCase("admin")) {
                navigateToAdminDashboard(event);
            } else {
                navigateToUserDashboard(event);
            }
        } else {
            // Show error if credentials are invalid
            errorLabel.setText("Invalid username or password.");
        }
    }

    // Method to navigate to the Admin Dashboard
    private void navigateToAdminDashboard(ActionEvent event) {
        try {
            // Load the FXML for the admin dashboard
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AdminDashboard.fxml"));
            Parent adminDashboardRoot = loader.load();

            // Set up the scene
            Scene adminDashboardScene = new Scene(adminDashboardRoot);

            // Get the current stage and switch the scene
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(adminDashboardScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to navigate to the User Dashboard
    private void navigateToUserDashboard(ActionEvent event) {
        try {
            // Load the FXML for the user dashboard
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Dashboard.fxml"));
            Parent userDashboardRoot = loader.load();

            // Set up the scene
            Scene userDashboardScene = new Scene(userDashboardRoot);

            // Get the current stage and switch the scene
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(userDashboardScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void navigateToRegistrationScreen(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Registration.fxml"));
            Parent registrationRoot = loader.load();

            // Set the new scene for the registration
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(registrationRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
