package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import model.User;
import util.UserRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class RegistrationController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private ChoiceBox<String> roleChoiceBox;

    @FXML
    private void initialize() {
        // Populate the roleChoiceBox with "User" and "Admin" options
        roleChoiceBox.setItems(FXCollections.observableArrayList("User", "Admin"));
        roleChoiceBox.setValue("User");  // Set a default value
    }

    @FXML
    private void handleRegisterButtonAction() {
        // Get input from the form fields
        String username = usernameField.getText();
        String password = passwordField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String role = roleChoiceBox.getValue();  // Get the selected role

        // Validate that all fields are filled
        if (username.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
            showAlert("Error", "All fields must be filled.");
            return;
        }

        // Check if the username already exists in the UserRepository
        if (UserRepository.getInstance().getUserByUsername(username).isPresent()) {
            showAlert("Error", "Username is already taken. Please choose a different one.");
            return;
        }

        // Create a new user object
        User newUser = new User(username, password, firstName, lastName, role);

        // Add the user to the repository (or database)
        UserRepository.getInstance().addUser(newUser);

        // Show success message
        showAlert("Success", "User registered successfully!");

        // Navigate to the login screen
        navigateToLoginScreen();
    }

    // Method to navigate back to the login screen
    @FXML
    private void navigateToLoginScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
            Parent loginRoot = loader.load();

            // Get the current stage and switch scenes
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(loginRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Utility method to show alert messages
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
