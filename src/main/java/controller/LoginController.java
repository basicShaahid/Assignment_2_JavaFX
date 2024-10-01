package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorMessageLabel;

    @FXML
    private void handleLoginButtonAction(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.equals("admin") && password.equals("admin123")) {
            // If admin, navigate to AdminDashboard.fxml
            navigateTo("AdminDashboard.fxml", event);
        } else if (username.equals("user") && password.equals("user123")) {
            // If normal user, navigate to Dashboard.fxml
            navigateTo("Dashboard.fxml", event);
        } else {
            errorMessageLabel.setText("Invalid username or password. Please try again.");
        }
    }

    private void navigateTo(String fxmlFile, ActionEvent event) throws IOException {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/view/" + fxmlFile));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
