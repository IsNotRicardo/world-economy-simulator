package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;

public class TestController {
    @FXML
    public void handleStartSimulation() {

    }

    @FXML
    public void handleQuitSimulation() {
        Platform.exit();
    }
}
