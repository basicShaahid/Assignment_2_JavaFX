package controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.User;
import util.ShoppingCartRepository;

public class Main extends Application {

    private static User currentUser;
    private ShoppingCartRepository shoppingCartRepository;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initialize the ShoppingCartRepository
        shoppingCartRepository = new ShoppingCartRepository();

        // Load the Login screen
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle("The Reading Room - Login");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Clear the user's cart on application startup
        primaryStage.setOnShown(event -> {
            if (currentUser != null && shoppingCartRepository != null) {
                shoppingCartRepository.clearCartForUser(currentUser.getId());
                System.out.println("Cart cleared for user: " + currentUser.getId());
            }
        });

        // Set up shutdown hook to clear cart on application exit
        primaryStage.setOnCloseRequest(event -> {
            if (currentUser != null && shoppingCartRepository != null) {
                shoppingCartRepository.clearCartForUser(currentUser.getId());
                System.out.println("Cart cleared for user: " + currentUser.getId());
            }
        });
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
