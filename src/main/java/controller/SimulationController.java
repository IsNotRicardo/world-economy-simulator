package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
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

	@FXML
	private Button toggleSimulationButton;
	@FXML
	private TextField daysTextField;
	@FXML
	private Label delayWarningLabel;
	@FXML
	private TextField delayTextField;

	@FXML
	private void initialize() {
		delayWarningLabel.setVisible(false);
		delayWarningLabel.setManaged(false);
	}

	public void initialize(List<Resource> resources, List<Country> countries) {
		Simulator simulator = new Simulator(this, resources, countries);
		simulator.runSimulation();
	}

	public void updateData() {
		int currentDay = Clock.getInstance().getTime();
		daysTextField.setText(String.valueOf(currentDay));
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
}