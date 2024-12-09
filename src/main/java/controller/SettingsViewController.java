package controller;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.simulation.SimulationConfig;

public class SettingsViewController {
    private static final int TAB_OFFSET_VALUE = 68;
    private static final int TAB_HEIGHT = 48;

    @FXML
    private TabPane tabPane;
    @FXML
    private VBox simulationSettingsVBox;
    @FXML
    private TextField simulationTimeField;
    @FXML
    private Label simulationTimeErrorLabel;
    @FXML
    private TextField simulationDelayField;
    @FXML
    private Label simulationDelayErrorLabel;
    @FXML
    private TextField countrySupplySampleField;
    @FXML
    private Label countrySupplySampleErrorLabel;
    @FXML
    private TextField populationSegmentSizeField;
    @FXML
    private Label populationSegmentSizeErrorLabel;

    @FXML
    public void initialize() {
        tabPane.tabMinWidthProperty().bind(Bindings.createDoubleBinding(
                () -> (tabPane.getWidth() - TAB_OFFSET_VALUE) / tabPane.getTabs().size(),
                tabPane.widthProperty(), tabPane.getTabs()
        ));
        tabPane.tabMaxWidthProperty().bind(Bindings.createDoubleBinding(
                () -> (tabPane.getWidth() - TAB_OFFSET_VALUE) / tabPane.getTabs().size(),
                tabPane.widthProperty(), tabPane.getTabs()
        ));
        tabPane.tabMinHeightProperty().set(TAB_HEIGHT);
        tabPane.tabMaxHeightProperty().set(TAB_HEIGHT);

        Insets margin = new Insets(30, 30, 0, 30);
        simulationSettingsVBox.getChildren().forEach(child -> {
            VBox.setMargin(child, margin);
        });

        simulationTimeField.setText(Integer.toString(SimulationConfig.getSimulationTime()));
        simulationDelayField.setText(Integer.toString(SimulationConfig.getSimulationDelay()));
        countrySupplySampleField.setText(Integer.toString(SimulationConfig.getSupplyArchiveTime()));
        populationSegmentSizeField.setText(Integer.toString(SimulationConfig.getPopulationSegmentSize()));

        simulationTimeField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                validateField(simulationTimeField, simulationTimeErrorLabel, SimulationConfig.getSimulationTime(), 1, "Simulation time must be positive");
            }
        });
        simulationDelayField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                validateField(simulationDelayField, simulationDelayErrorLabel, SimulationConfig.getSimulationDelay(), 1, "Simulation delay must be positive");
            }
        });
        countrySupplySampleField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                validateField(countrySupplySampleField, countrySupplySampleErrorLabel, SimulationConfig.getSupplyArchiveTime(), 2, "Supply archive sample must be greater than 1");
            }
        });
        populationSegmentSizeField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                validateField(populationSegmentSizeField, populationSegmentSizeErrorLabel, SimulationConfig.getPopulationSegmentSize(), 1, "Population segment size must be positive");
            }
        });
    }

    private void validateField(TextField textField, Label errorLabel, int defaultValue, int minValue, String errorMessage) {
        try {
            int value = Integer.parseInt(textField.getText());

            if (value < minValue) {
                textField.setText(Integer.toString(defaultValue));
                errorLabel.setText(errorMessage);
            } else {
                errorLabel.setText("");
            }
        } catch (NumberFormatException e) {
            textField.setText(Integer.toString(defaultValue));
            errorLabel.setText("Input must be a number");
        }
    }
}