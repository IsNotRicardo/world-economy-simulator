package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class InitialController {

	@FXML
	private TextField simTimeField;
	@FXML
	private TextField delayField;
	@FXML
	private Button minusDelayButton;
	@FXML
	private Button resetDelayButton;
	@FXML
	private Button plusDelayButton;
	@FXML
	private ProgressBar Progress;
	@FXML
	private TextField ResourceNameField;
	@FXML
	private TextField ResourceCategoryField;
	@FXML
	private TextField baseCapacityField;
	@FXML
	private TextField productionCostField;
	@FXML
	private Slider prioritySlider;
	@FXML
	private Button saveResource;
	@FXML
	private TextField countryNameField;
	@FXML
	private TextField populationField;
	@FXML
	private TextField moneyField;
	@FXML
	private Button addCountryField;
	@FXML
	private TextField starterResourcesField;
	@FXML
	private TextField ownedResourcesField;

	@FXML
	public void decreaseDelay(ActionEvent actionEvent) {

	}

	@FXML
	public void resetDelay(ActionEvent actionEvent) {
	}

	@FXML
	public void IncrementDelay(ActionEvent actionEvent) {
	}

	@FXML
	public void setPriority(MouseEvent mouseEvent) {
	}

	@FXML
	public void addResource(ActionEvent actionEvent) {
	}

	@FXML
	public void addCountry(ActionEvent actionEvent) {
	}
}