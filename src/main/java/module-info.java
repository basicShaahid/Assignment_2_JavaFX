module com.example.readingroomgui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;

    opens controller to javafx.fxml;  // Open 'controller' package
    opens model to javafx.fxml;       // Open 'model' package, if needed
    exports controller;               // Export 'controller' package
    exports model;                    // Export 'model' package
}
