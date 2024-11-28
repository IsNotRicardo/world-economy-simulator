package controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.InitialView;


public class InitialController {
    @FXML
    public TextField simTimeField;
    @FXML
    public TextField delayField;
    @FXML
    public Button minusDelayButton;
    @FXML
    public Button resetDelayButton;
    @FXML
    public Button plusDelayButton;
    @FXML
    public TextField timeElapsedField;
    @FXML
    public Button addCountryButton;
    public TextField countryNameField;
    public TextField populationField;
    public TextField moneyField;
    public Button saveCountryButton;
    public TextField moenyField;

    public void decreaseDelay(){

    }
    public void resetDelay(){

    }
    public void IncrementDelay(){

    }
    public void saveCountry(){}

    public void getMoney(ActionEvent actionEvent) {
    }

    public void getPopulation(ActionEvent actionEvent) {
    }

    public void getCountryName(ActionEvent actionEvent) {
    }

    public void openCountryCreation(ActionEvent actionEvent) {

    }

    public void getMoeny(ActionEvent actionEvent) {
    }
    public void getDelay(){

    }
    public void updateSimulationTime(){
        
    }
}

