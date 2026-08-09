package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import app.shared.exceptions.InitializationException;
import app.shared.factory.ControllerFactory;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/app/view/main-view.fxml"));
            loader.setControllerFactory(new ControllerFactory());
            
            Parent root = loader.load();
            primaryStage.setTitle("FinTrack - Gestão Financeira");
            primaryStage.setScene(new Scene(root, 750, 500));
            primaryStage.show();
        } catch (Exception e) {
            System.err.println("[LOG CRÍTICO BOOTSTRAP]: " + e.getMessage());
            throw new InitializationException("Falha catastrófica ao inicializar o carregamento da janela principal da aplicação.", e);
        }
    }

    public static void main(String[] args) {
        System.setProperty("prism.order", "sw");
        launch(args);
    }
}
