package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.User;
import util.UserRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class EditProfileController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private PasswordField passwordField;

    private User currentUser;
    private UserRepository userRepository;

    public EditProfileController() {
        this.userRepository = UserRepository.getInstance();
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        loadUserData();
    }

    private void loadUserData() {
        if (currentUser != null) {
            usernameField.setText(currentUser.getUsername());
            firstNameField.setText(currentUser.getFirstName());
            lastNameField.setText(currentUser.getLastName());
            passwordField.setText(currentUser.getPassword());
        }
    }

    @FXML
    private void handleSaveChanges(ActionEvent event) {
        String newFirstName = firstNameField.getText();
        String newLastName = lastNameField.getText();
        String newPassword = passwordField.getText();

        if (newFirstName.isEmpty() || newLastName.isEmpty() || newPassword.isEmpty()) {
            showAlert("Error", "All fields must be filled out.");
            return;
        }

        // Update the currentUser object
        currentUser.setFirstName(newFirstName);
        currentUser.setLastName(newLastName);
        currentUser.setPassword(newPassword);

        // Update the user in the database
        userRepository.updateUserProfile(currentUser);

        showAlert("Success", "Profile updated successfully!");
    }

    @FXML
    private void handleBackToDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Dashboard.fxml"));
            Parent dashboardRoot = loader.load();
            DashboardController dashboardController = loader.getController();
            dashboardController.setUser(currentUser);

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(dashboardRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
