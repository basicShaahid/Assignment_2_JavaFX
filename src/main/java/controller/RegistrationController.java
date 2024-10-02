package controller;

import javafx.application.Platform;
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

public class RegistrationController {

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    @FXML
    private void handleRegisterButtonAction(ActionEvent event) {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Check if username already exists in the repository
        if (UserRepository.getInstance().usernameExists(username)) {
            messageLabel.setText("Username already exists. Please choose a different one.");
            return;
        }

        // Create a new user and add it to the repository
        User newUser = new User(username, password, firstName, lastName);
        UserRepository.getInstance().addUser(newUser);

        // Show success message
        messageLabel.setText("Registration successful! You can now log in.");

        // Use Platform.runLater() to ensure the UI update happens on the JavaFX Application thread
        new Thread(() -> {
            try {
                Thread.sleep(2000); // Sleep for 2 seconds to show the message

                // Update the UI using Platform.runLater
                Platform.runLater(() -> navigateToLoginScreen(event));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    // Method to navigate back to the login screen
    private void navigateToLoginScreen(ActionEvent event) {
        try {
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
