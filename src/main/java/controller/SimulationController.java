package controller;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.core.Country;
import model.core.Resource;
import model.simulation.Clock;
import model.simulation.SimulationConfig;
import model.simulation.Simulator;

import java.util.List;

public class SimulationController {
	private Country selectedCountry;
	private Resource selectedResource;
	private Resource selectedResourceNode;
	private String currentSeriesName;

	@FXML
	private Button toggleSimulationButton;
	@FXML
	private TextField daysTextField;
	@FXML
	private Label delayWarningLabel;
	@FXML
	private TextField delayTextField;
	@FXML
	private ComboBox<String> countryComboBox;
	@FXML
	private ComboBox<String> resourceComboBox;
	@FXML
	private ComboBox<String> resourceNodeComboBox;
	@FXML
	private LineChart<String, Number> lineChart;

	@FXML
	private void initialize() {
		delayWarningLabel.setVisible(false);
		delayWarningLabel.setManaged(false);
	}

	public void initialize(List<Resource> resources, List<Country> countries) {
		countryComboBox.getItems().addAll(countries.stream().map(Country::getName).toList());
		resourceComboBox.getItems().addAll(resources.stream().map(Resource::name).toList());

		Simulator simulator = new Simulator(this, resources, countries);
		simulator.runSimulation();
	}

	public void updateData() {
		int currentDay = Clock.getInstance().getTime();
		daysTextField.setText(String.valueOf(currentDay));

		if (currentSeriesName == null) {
			return;
		}

		switch (currentSeriesName) {
			case "Population/Day":
				updatePopulationGraph();
				break;
			case "Money/Day":
				updateMoneyGraph();
				break;
			case "Average Happiness/Day":
				updateAverageHappinessGraph();
				break;
			case "Individual Budget/Day":
				updateIndividualBudgetGraph();
				break;
			case "Quantity/Day":
				updateQuantityGraph();
				break;
			case "Value/Day":
				updateValueGraph();
				break;
			case "Production Cost/Day":
				updateProductionCostGraph();
				break;
			case "Max Capacity/Day":
				updateMaxCapacityGraph();
				break;
			case "Tier/Day":
				updateTierGraph();
				break;
		}
	}

	private void drawLineGraph(String seriesName, List<Integer> xData, List<Number> yData) {
		XYChart.Series<String, Number> series = new XYChart.Series<>();
		series.setName(seriesName);
		currentSeriesName = seriesName;

		for (int i = 0; i < xData.size(); i++) {
			series.getData().add(new XYChart.Data<>(String.valueOf(xData.get(i)), yData.get(i)));
		}

		lineChart.getData().clear();
		lineChart.getData().add(series);
	}

	private void updateLineGraph(String seriesName, List<Integer> xData, List<Number> yData) {
		XYChart.Series<String, Number> series = null;

		// Check if the series already exists
		for (XYChart.Series<String, Number> existingSeries : lineChart.getData()) {
			if (existingSeries.getName().equals(seriesName)) {
				series = existingSeries;
				break;
			}
		}

		// If the series doesn't exist, create a new one
		if (series == null) {
			series = new XYChart.Series<>();
			series.setName(seriesName);
			lineChart.getData().add(series);
		}

		// Update the series with new data
		series.getData().clear();
		for (int i = 0; i < xData.size(); i++) {
			series.getData().add(new XYChart.Data<>(String.valueOf(xData.get(i)), yData.get(i)));
		}
	}

	@FXML
	public void toggleSimulation() {
		if (Clock.getInstance().isPaused()) {
			Clock.getInstance().resume();
			toggleSimulationButton.setText("Pause");
		} else {
			Clock.getInstance().pause();
			toggleSimulationButton.setText("Resume");
		}
	}

	@FXML
	public void updateDelay() {
		try {
			int newDelay = Integer.parseInt(delayTextField.getText());
			SimulationConfig.setSimulationDelay(newDelay);
			delayWarningLabel.setVisible(false);
			delayWarningLabel.setManaged(false);
		} catch (IllegalArgumentException e) {
			delayWarningLabel.setVisible(true);
			delayWarningLabel.setManaged(true);
			delayTextField.setText(String.valueOf(SimulationConfig.getSimulationDelay()));
		}
	}

	@FXML
	private void handlePopulationButton() {
		// TODO: Add database call fetchAll
	}

	private void updatePopulationGraph() {
		// TODO: Add database call fetch for this day only
	}

	@FXML
	private void handleMoneyButton() {
		// TODO: Add database call
	}

	private void updateMoneyGraph() {
		// TODO: Add database call fetch for this day only
	}

	@FXML
	private void handleAverageHappinessButton() {
		// TODO: Add database call
	}

	private void updateAverageHappinessGraph() {
		// TODO: Add database call fetch for this day only
	}

	@FXML
	private void handleIndividualBudgetButton() {
		// TODO: Add database call
	}

	private void updateIndividualBudgetGraph() {
		// TODO: Add database call fetch for this day only
	}

	@FXML
	private void handleQuantityButton() {
		// TODO: Add database call
	}

	private void updateQuantityGraph() {
		// TODO: Add database call fetch for this day only
	}

	@FXML
	private void handleValueButton() {
		// TODO: Add database call
	}

	private void updateValueGraph() {
		// TODO: Add database call fetch for this day only
	}

	@FXML
	private void handleProductionCostButton() {
		// TODO: Add database call
	}

	private void updateProductionCostGraph() {
		// TODO: Add database call fetch for this day only
	}

	@FXML
	private void handleMaxCapacityButton() {
		// TODO: Add database call
	}

	private void updateMaxCapacityGraph() {
		// TODO: Add database call fetch for this day only
	}

	@FXML
	private void handleTierButton() {
		// TODO: Add database call
	}

	private void updateTierGraph() {
		// TODO: Add database call fetch for this day only
	}
}