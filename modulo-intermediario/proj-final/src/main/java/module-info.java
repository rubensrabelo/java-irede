module app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    
    opens app to javafx.graphics, javafx.fxml;
    opens app.controller to javafx.fxml;
    opens app.model to javafx.base;
    
    exports app;
}
