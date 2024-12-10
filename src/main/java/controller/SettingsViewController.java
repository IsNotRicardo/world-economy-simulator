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
    private Label resourceItemsLabel;
    @FXML
    private TextField resourceNameField;
    @FXML
    private TextField resourcePriorityField;
    @FXML
    private TextField resourceBaseCapacityField;
    @FXML
    private TextField resourceProductionCostField;
    @FXML
    private Label resourceNameErrorLabel;
    @FXML
    private Label resourcePriorityErrorLabel;
    @FXML
    private Label resourceBaseCapacityErrorLabel;
    @FXML
    private Label resourceProductionCostErrorLabel;
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
        simulationSettingsVBox.getChildren().forEach(child -> VBox.setMargin(child, margin));

        simulationTimeField.setText(Integer.toString(SimulationConfig.getSimulationTime()));
        simulationDelayField.setText(Integer.toString(SimulationConfig.getSimulationDelay()));
        countrySupplySampleField.setText(Integer.toString(SimulationConfig.getSupplyArchiveTime()));
        populationSegmentSizeField.setText(Integer.toString(SimulationConfig.getPopulationSegmentSize()));

        simulationTimeField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                validateGeneralField(simulationTimeField, simulationTimeErrorLabel, SimulationConfig.getSimulationTime(), 1, "Simulation time must be positive");
            }
        });
        simulationDelayField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                validateGeneralField(simulationDelayField, simulationDelayErrorLabel, SimulationConfig.getSimulationDelay(), 1, "Simulation delay must be positive");
            }
        });
        countrySupplySampleField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                validateGeneralField(countrySupplySampleField, countrySupplySampleErrorLabel, SimulationConfig.getSupplyArchiveTime(), 2, "Supply archive sample must be greater than 1");
            }
        });
        populationSegmentSizeField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                validateGeneralField(populationSegmentSizeField, populationSegmentSizeErrorLabel, SimulationConfig.getPopulationSegmentSize(), 1, "Population segment size must be positive");
            }
        });

        Insets titleMargin = new Insets(0, 30, 0, 30);
        resourceSettingsVBox.getChildren().forEach(child -> {
            if (child instanceof Label) {
                VBox.setMargin(child, titleMargin);
            } else {
                VBox.setMargin(child, margin);
            }
        });
        changeResourceButtonVisibility(false);
    }

    private void validateGeneralField(TextField textField, Label errorLabel, int defaultValue, int minValue, String errorMessage) {
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
            errorLabel.setText("Input must be an integer");
        }
    }

    private boolean validateIntField(TextField textField, Label errorLabel, int minValue, String errorMessage) {
        try {
            int value = Integer.parseInt(textField.getText());
            if (value < minValue) {
                errorLabel.setText(errorMessage);
                return false;
            } else {
                errorLabel.setText("");
                return true;
            }
        } catch (NumberFormatException e) {
            errorLabel.setText("Input must be an integer");
            return false;
        }
    }

    private boolean validateDoubleField(TextField textField, Label errorLabel, double minValue, double maxValue, String errorMessage) {
        try {
            double value = Double.parseDouble(textField.getText());
            if (value < minValue || value > maxValue) {
                errorLabel.setText(errorMessage);
                return false;
            } else {
                errorLabel.setText("");
                return true;
            }
        } catch (NumberFormatException e) {
            errorLabel.setText("Input must be a number");
            return false;
        }
    }

    private boolean validateStringField(TextField textField, Label errorLabel, String errorMessage) {
        if (textField.getText() == null || textField.getText().isEmpty()) {
            errorLabel.setText(errorMessage);
            return false;
        } else {
            errorLabel.setText("");
            return true;
        }
    }

    private boolean isResourceValid() {
        boolean isNameValid = validateStringField(resourceNameField, resourceNameErrorLabel,
                "Name must not be empty");
        boolean isPriorityValid = validateDoubleField(resourcePriorityField, resourcePriorityErrorLabel,
                0.0, 1.0, "Priority must be between 0 and 1");
        boolean isBaseCapacityValid = validateIntField(resourceBaseCapacityField, resourceBaseCapacityErrorLabel,
                1, "Base capacity must be positive");
        boolean isProductionCostValid = validateDoubleField(resourceProductionCostField, resourceProductionCostErrorLabel,
                0.0, Double.MAX_VALUE, "Production cost cannot be negative");

        return isNameValid && isPriorityValid && isBaseCapacityValid && isProductionCostValid;
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
        if (isResourceValid()) {
            String name = resourceNameField.getText();
            double priority = Double.parseDouble(resourcePriorityField.getText());
            int baseCapacity = Integer.parseInt(resourceBaseCapacityField.getText());
            double productionCost = Double.parseDouble(resourceProductionCostField.getText());

            // Category needs to be removed later
            Resource newResource = new Resource(name, null, priority, baseCapacity, productionCost);
            resourceList.add(newResource);
            resourceListView.getItems().add(name);
            clearResourceFields();
        }
    }

    @FXML
    public void handleResourceSelection() {
        if (resolveIsResourceNotSaved()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Select Resource");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to change resources?\n" +
                    "Any unsaved changes will be lost.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isEmpty() || result.get() != ButtonType.OK) {
                resourceListView.getSelectionModel().select(currentResourceIndex);
                return;
            }
        }
        int selectedIndex = resourceListView.getSelectionModel().getSelectedIndex();

        if (selectedIndex != -1) {
            resourceNameField.setText(resourceList.get(selectedIndex).name());
            resourcePriorityField.setText(String.valueOf(resourceList.get(selectedIndex).priority()));
            resourceBaseCapacityField.setText(String.valueOf(resourceList.get(selectedIndex).baseCapacity()));
            resourceProductionCostField.setText(String.valueOf(resourceList.get(selectedIndex).productionCost()));
            changeResourceButtonVisibility(true);
            currentResourceIndex = selectedIndex;
        }
    }

    @FXML
    public void saveResource() {
        int selectedIndex = resourceListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            if (isResourceValid()) {
                String name = resourceNameField.getText();
                double priority = Double.parseDouble(resourcePriorityField.getText());
                int baseCapacity = Integer.parseInt(resourceBaseCapacityField.getText());
                double productionCost = Double.parseDouble(resourceProductionCostField.getText());

                // Category needs to be removed later
                Resource updatedResource = new Resource(name, null, priority, baseCapacity, productionCost);
                resourceList.set(selectedIndex, updatedResource);
                resourceListView.getItems().set(selectedIndex, name);
                resourceListView.getSelectionModel().select(selectedIndex);
            }
        }
    }

    @FXML
    public void deleteResource() {
        int selectedIndex = resourceListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Resource");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete this resource?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                resourceList.remove(selectedIndex);
                resourceListView.getItems().remove(selectedIndex);
                resourceListView.getSelectionModel().clearSelection();

                clearResourceFields();
                changeResourceButtonVisibility(false);
            }
        }
    }

    private void changeResourceButtonVisibility(boolean editingMode) {
        // In editing mode, the add button is not visible, and the edit and delete buttons are visible
        isResourceEditingMode = editingMode;

        if (editingMode) {
            resourceItemsLabel.setText("Modify Resource");
        } else {
            resourceItemsLabel.setText("Insert Resource");
        }

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

        resourceNameErrorLabel.setText("");
        resourcePriorityErrorLabel.setText("");
        resourceBaseCapacityErrorLabel.setText("");
        resourceProductionCostErrorLabel.setText("");
    }
}