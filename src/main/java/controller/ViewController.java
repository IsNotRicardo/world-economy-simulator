package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import model.simulation.Clock;

import java.util.HashMap;
import java.util.Map;

public class ViewController {

	private Map<Integer, ResourceData> resourceMap = new HashMap<>();
	private Map<Integer, CountryData> countryMap = new HashMap<>();

	@FXML
	public Label simTimeWarningLabel;
	@FXML
	public Label delayWarningLabel;
	@FXML
	private TextField simTimeField;
	@FXML
	private TextField delayField;
	@FXML
	private ProgressBar Progress;
	@FXML
	private TextField resourceNameField;
	@FXML
	private TextField baseCapacityField;
	@FXML
	private TextField productionCostField;
	@FXML
	private TextField priorityField;
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
	public void initialize() {
		// Initialize delayField with the current delay value from Clock
		delayField.setText(
				String.valueOf(Clock.getInstance().getDelayPerDay() / 1000)); // Convert milliseconds to seconds

		// Add listener to update delay when user changes it
		delayField.textProperty().addListener((observable, oldValue, newValue) -> {
			try {
				int newDelay = Integer.parseInt(newValue) * 1000; // Convert seconds to milliseconds
				Clock.getInstance().setDelayPerDay(newDelay);
			} catch (NumberFormatException e) {
				// Handle invalid input
				delayWarningLabel.setText("Invalid input");
				delayField.setText(oldValue); // Revert to old value
			}
		});
	}

	private int generateUniqueId(int mapSize) {
		return mapSize + 1;
	}

	@FXML
	public void addResource(ActionEvent actionEvent) {
		int id = generateUniqueId(resourceMap.size());
		String name = resourceNameField.getText();
		int baseCapacity = Integer.parseInt(baseCapacityField.getText());
		double productionCost = Double.parseDouble(productionCostField.getText());
		int priority = Integer.parseInt(priorityField.getText());

		ResourceData resourceData = new ResourceData(name, baseCapacity, productionCost, priority);
		addResourceToMap(id, resourceData);
	}

	public void addResourceToMap(int id, ResourceData resourceData) {
		resourceMap.put(id, resourceData);
	}


	@FXML
	public void addCountry(ActionEvent actionEvent) {
		int id = generateUniqueId(countryMap.size());
		String name = countryNameField.getText();
		long population = Long.parseLong(populationField.getText());
		double money = Double.parseDouble(moneyField.getText());
		int starterResources = Integer.parseInt(starterResourcesField.getText());
		int ownedResources = Integer.parseInt(ownedResourcesField.getText());

		CountryData countryData = new CountryData(name, population, money, starterResources, ownedResources);
		addCountryToMap(id, countryData);
	}

	public void addCountryToMap(int id, CountryData countryData) {
		countryMap.put(id, countryData);
	}
}