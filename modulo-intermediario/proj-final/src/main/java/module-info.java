module app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.sql; 
    requires io.github.cdimascio.dotenv.java;

    opens app to javafx.graphics, javafx.fxml;
    opens app.application.controller to javafx.fxml;
    opens app.application.dto to javafx.base;
    opens app.domain to javafx.base;
    
    exports app;
    exports app.shared.factory;
    exports app.shared.exceptions;
    exports app.application.mapper;
}
