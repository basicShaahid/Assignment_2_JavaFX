module ReadingRoomGUI {
    requires javafx.controls;
    requires javafx.fxml;

    // Open and export the controller package to JavaFX
    opens controller to javafx.fxml;
    exports controller;
}
