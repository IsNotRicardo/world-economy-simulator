package view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.util.Objects;

public class SimulationView extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/menu-layout.fxml"));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/menu-styles.css")).toExternalForm());

        stage.setScene(scene);
        stage.setTitle("World Economy Simulator");
        stage.setFullScreen(true);
        stage.setWidth(900);
        stage.setHeight(600);
        stage.show();
    }
}
