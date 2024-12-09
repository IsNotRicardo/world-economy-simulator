package controller;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import model.core.Resource;
import model.simulation.SimulationConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SettingsViewController {
    private static final int TAB_OFFSET_VALUE = 68;
    private static final int TAB_HEIGHT = 48;

    private List<Resource> resourceList = new ArrayList<>();
    private boolean isResourceEditingMode;
    private int currentResourceIndex;

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
    private ListView<String> resourceListView;
    @FXML
    private VBox resourceSettingsVBox;
    @FXML
    private TextField resourceNameField;
    @FXML
    private TextField resourcePriorityField;
    @FXML
    private TextField resourceBaseCapacityField;
    @FXML
    private TextField resourceProductionCostField;
    @FXML
    private Button addResourceButton;
    @FXML
    private Button saveResourceButton;
    @FXML
    private Region resourceOptionalRegion;
    @FXML
    private Button deleteResourceButton;

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

        resourceSettingsVBox.getChildren().forEach(child -> {
            VBox.setMargin(child, margin);
        });
        changeResourceButtonVisibility(false);
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

    @FXML
    public void newResource() {
        if (resolveIsResourceNotSaved()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("New Resource");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to create a new resource?\n" +
                    "Any unsaved changes will be lost.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isEmpty() || result.get() != ButtonType.OK) {
                return;
            }
        }
        clearResourceFields();
        changeResourceButtonVisibility(false);
        resourceListView.getSelectionModel().clearSelection();
    }

    @FXML
    public void addResource() {

    }

    @FXML
    public void saveResource() {

    }

    @FXML
    public void deleteResource() {

    }

    private void changeResourceButtonVisibility(boolean editingMode) {
        // In editing mode, the add button is not visible, and the edit and delete buttons are visible
        isResourceEditingMode = editingMode;

        addResourceButton.setVisible(!editingMode);
        addResourceButton.setManaged(!editingMode);

        saveResourceButton.setVisible(editingMode);
        saveResourceButton.setManaged(editingMode);

        resourceOptionalRegion.setVisible(editingMode);
        resourceOptionalRegion.setManaged(editingMode);

        deleteResourceButton.setVisible(editingMode);
        deleteResourceButton.setManaged(editingMode);
    }

    private boolean resolveIsResourceNotSaved() {
        if (isResourceEditingMode) {
            String savedName = resourceList.get(currentResourceIndex).name();
            String savedPriority = String.valueOf(resourceList.get(currentResourceIndex).priority());
            String savedBaseCapacity = String.valueOf(resourceList.get(currentResourceIndex).baseCapacity());
            String savedProductionCost = String.valueOf(resourceList.get(currentResourceIndex).productionCost());

            return !resourceNameField.getText().equals(savedName) || !resourcePriorityField.getText().equals(savedPriority) ||
                    !resourceBaseCapacityField.getText().equals(savedBaseCapacity) || !resourceProductionCostField.getText().equals(savedProductionCost);
        } else {
            return !resourceNameField.getText().isEmpty() || !resourcePriorityField.getText().isEmpty() ||
                    !resourceBaseCapacityField.getText().isEmpty() || !resourceProductionCostField.getText().isEmpty();
        }
    }

    private void clearResourceFields() {
        resourceNameField.setText("");
        resourcePriorityField.setText("");
        resourceBaseCapacityField.setText("");
        resourceProductionCostField.setText("");
    }
}