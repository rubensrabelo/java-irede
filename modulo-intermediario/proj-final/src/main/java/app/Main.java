package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/view/main-view.fxml"));
            Parent root = loader.load();
            
            primaryStage.setTitle("FinTrack - Gestão Financeira");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (Exception e) {
            System.out.println("Erro crítico ao carregar a interface gráfica:");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Ignora problemas causados por drivers de vídeo nativos no ambiente Linux
        System.setProperty("prism.order", "sw");
        launch(args);
    }
}
